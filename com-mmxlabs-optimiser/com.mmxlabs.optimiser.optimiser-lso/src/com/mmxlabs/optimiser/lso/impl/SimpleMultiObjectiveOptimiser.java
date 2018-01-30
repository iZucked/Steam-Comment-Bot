/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IReducingConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess.Phase;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.IMultiObjectiveFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.INullMove;
import com.mmxlabs.optimiser.lso.SimilarityFitnessMode;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.MultiObjectiveUtils;

/**
 * A sub-class of {@link DefaultLocalSearchOptimiser} implementing a multi-objective algorithm.
 * 
 * @author achurchill
 * 
 */
public class SimpleMultiObjectiveOptimiser extends DefaultLocalSearchOptimiser {

	@Inject
	Injector injector;

	@Inject
	protected IFitnessHelper fitnessHelper;

	protected List<NonDominatedSolution> archive = new ArrayList<>();

	IMultiObjectiveFitnessEvaluator multiObjectiveFitnessEvaluator;
	List<IFitnessComponent> fitnessComponents;

	private final Random r;

	@Inject
	@Named(LocalSearchOptimiserModule.OPTIMISER_DEBUG_MODE)
	protected boolean DEBUG;

	@Inject
	@Named(LocalSearchOptimiserModule.SIMILARITY_SETTING)
	SimilarityFitnessMode similarityFitnessMode;

	enum eQuartile {
		FIRST, SECOND, THIRD
	}

	public SimpleMultiObjectiveOptimiser(List<IFitnessComponent> fitnessComponents, Random random) {
		this.fitnessComponents = fitnessComponents;
		r = random;
	}

	@Override
	public IAnnotatedSolution start(@NonNull IOptimisationContext optimiserContext, @NonNull ISequences initialRawSequences, @NonNull ISequences inputRawSequences) {
		archive = new ArrayList<>();
		return super.start(optimiserContext, initialRawSequences, inputRawSequences);
	}

	@Override
	public void init() {
		super.init();
	}

	public IMultiObjectiveFitnessEvaluator getMultiObjectiveFitnessEvaluator() {
		return multiObjectiveFitnessEvaluator;
	}

	public void setMultiObjectiveFitnessEvaluator(IMultiObjectiveFitnessEvaluator multiObjectiveFitnessEvaluator) {
		this.multiObjectiveFitnessEvaluator = multiObjectiveFitnessEvaluator;
	}

	protected int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences) {

		final int iterationsThisStep = Math.min(Math.max(1, (getNumberOfIterations() * percentage) / 100), getNumberOfIterations() - getNumberOfIterationsCompleted());
		MAIN_LOOP: for (int i = 0; i < iterationsThisStep; i++) {
			initNextIteration();

			// choose a solution from the archive
			ISequences nonDominatedSolution = (ISequences) archive.get(r.nextInt(archive.size())).getSequences();
			updateSequences(nonDominatedSolution, pinnedPotentialRawSequences, nonDominatedSolution.getResources());
			updateSequencesLookup(pinnedPotentialRawSequences, nonDominatedSolution.getResources());

			// Generate a new move
			final IMove move = generateNewMove();

			// Make sure the generator was able to generate a move
			if (move == null || move instanceof INullMove) {
				if (loggingDataStore != null) {
					loggingDataStore.logNullMove(move);
				}
				continue;
			}

			// Test move is valid against data.
			if (!move.validate(pinnedPotentialRawSequences)) {
				++numberOfFailedToValidate;
				if (loggingDataStore != null) {
					loggingDataStore.logFailedToValidateMove(move);
				}
				continue;
			}

			// Update potential sequences
			applyNewMove(pinnedPotentialRawSequences, move);

			// Apply sequence manipulators
			final IModifiableSequences potentialFullSequences = getSequenceManipulator().createManipulatedSequences(pinnedPotentialRawSequences);

			// Apply hard constraint checkers
			if (!applyHardConstraints(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences)) {
				continue MAIN_LOOP;
			}

			if (loggingDataStore != null) {
				if (DO_SEQUENCE_LOGGING) {
					loggingDataStore.logSequence(pinnedPotentialRawSequences);
				}
			}

			final IEvaluationState evaluationState = getEvaluationState(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences);
			if (evaluationState == null) {
				continue MAIN_LOOP;
			}

			// Apply hard constraint checkers
			if (!applyHardEvaluatedConstraintCheckers(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences, evaluationState)) {
				continue MAIN_LOOP;
			}

			if (!evaluateOnEvaluationProcesses(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences, evaluationState)) {
				continue MAIN_LOOP;
			}

			// Test whether new sequence should be added to the archive
			long[] fitnesses = addSequenceToArchiveIfNonDominated(pinnedPotentialRawSequences, move, potentialFullSequences, evaluationState);
		}
		if (DEBUG) {
			System.out.println("-------------");
			List<NonDominatedSolution> sortedValues = getSortedArchive(archive, 1);
			sortedValues.forEach(v -> System.out.println(String.format("[%s-start,%s],", v.getFitnesses()[0] * -1, v.getFitnesses()[1])));
		}
		setNumberOfIterationsCompleted(numberOfMovesTried);

		updateProgressLogs();
		return iterationsThisStep;
	}

	protected List<NonDominatedSolution> getSortedArchive(List<NonDominatedSolution> archive, int objectiveIndex) {
		List<NonDominatedSolution> sortedValues = archive.stream().sorted((a, b) -> Long.compare(a.getFitnesses()[objectiveIndex], b.getFitnesses()[objectiveIndex])).collect(Collectors.toList());
		return MultiObjectiveUtils.filterArchive(archive, objectiveIndex, new long[] {200_000, 0});
	}

	long[] addSequenceToArchiveIfNonDominated(final ModifiableSequences pinnedPotentialRawSequences, final IMove move, final IModifiableSequences potentialFullSequences,
			final IEvaluationState evaluationState) {
		long[] fitnesses = getMultiObjectiveFitnessEvaluator().getCombinedFitnessAndObjectiveValuesForComponentClasses(pinnedPotentialRawSequences, potentialFullSequences, evaluationState,
				move.getAffectedResources(), fitnessComponents);
		if (fitnesses != null) {
			addSolutionToNonDominatedArchive(pinnedPotentialRawSequences, fitnesses);
		}
		return fitnesses;
	}

	protected boolean addSolutionToNonDominatedArchive(final ISequences pinnedPotentialRawSequences, long[] fitnesses) {
		boolean nonDominated = isDominated(archive, fitnesses);
		if (nonDominated) {
			archive.add(new NonDominatedSolution(new Sequences(pinnedPotentialRawSequences), fitnesses, null));
		}
		return nonDominated;
	}

	protected boolean isDominated(List<NonDominatedSolution> archive, long[] thisFitness) {
		List<ISequences> dominated = new LinkedList<>();
		boolean add = true;
		for (NonDominatedSolution other : archive) {
			long[] otherFitness = other.getFitnesses();
			if (!atLeastOneObjectiveLower(thisFitness, otherFitness)) {
				// this solution is dominated
				add = false;
				break;
			}
			if (allObjectivesLower(thisFitness, otherFitness)) {
				// new solution dominates
				dominated.add(other.getSequences());
			} else if (allObjectivesLowerOrEqual(thisFitness, otherFitness)) {
				dominated.add(other.getSequences());
			}
		}
		for (ISequences a : dominated) {
			archive.removeIf(b -> b.getSequences().equals(a));
		}
		return add;
	}

	private boolean allObjectivesLower(long[] thisFitness, long[] otherFitness) {
		for (int i = 0; i < thisFitness.length; i++) {
			if (otherFitness[i] <= thisFitness[i]) {
				return false;
			}
		}
		return true;
	}

	private boolean allObjectivesLowerOrEqual(long[] thisFitness, long[] otherFitness) {
		for (int i = 0; i < thisFitness.length; i++) {
			if (thisFitness[i] > otherFitness[i]) {
				return false;
			}
		}
		return true;
	}

	private boolean atLeastOneObjectiveLower(long[] thisFitness, long[] otherFitness) {
		for (int i = 0; i < thisFitness.length; i++) {
			if (thisFitness[i] < otherFitness[i]) {
				return true;
			}
		}
		return false;
	}

	private boolean allObjectivesHigherOrEqual(long[] thisFitness, long[] otherFitness) {
		for (int i = 0; i < thisFitness.length; i++) {
			if (thisFitness[i] < otherFitness[i]) {
				return false;
			}
		}
		return true;
	}

	protected void evaluateInputSequences(@NonNull final ISequences currentRawSequences) {
		// Apply sequence manipulators
		final IModifiableSequences fullSequences = new ModifiableSequences(currentRawSequences);
		getSequenceManipulator().manipulate(fullSequences);

		// Prime IReducingConstraintCheckers with initial state
		for (final IReducingConstraintChecker checker : getReducingConstraintCheckers()) {
			checker.sequencesAccepted(fullSequences);
		}

		// Prime IInitialSequencesConstraintCheckers with initial state
		for (final IInitialSequencesConstraintChecker checker : getInitialSequencesConstraintCheckers()) {
			checker.sequencesAccepted(currentRawSequences, fullSequences);
		}

		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
			evaluationProcess.evaluate(fullSequences, evaluationState);
		}

		for (final IEvaluatedStateConstraintChecker checker : getEvaluatedStateConstraintCheckers()) {
			checker.checkConstraints(currentRawSequences, fullSequences, evaluationState);
		}

		// Prime fitness cores with initial sequences
		getFitnessEvaluator().setInitialSequences(currentRawSequences, fullSequences, evaluationState);
		long[] fitnesses = getFitnessesFromSequences(currentRawSequences, fullSequences, evaluationState);

		// add to archive
		if (archive.size() == 0) {
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
            lookupManager.createLookup(currentRawSequences);
			archive.add(new NonDominatedSolution(currentRawSequences, fitnesses, lookupManager));
		}
	}

	private long[] getFitnessesFromSequences(final ISequences currentRawSequences, final IModifiableSequences fullSequences, final IEvaluationState evaluationState) {
		long[] fitnesses = getMultiObjectiveFitnessEvaluator().getCombinedFitnessAndObjectiveValuesForComponentClasses(currentRawSequences, fullSequences, evaluationState, null, fitnessComponents);
		return fitnesses;
	}

	@Override
	public ISequences getBestRawSequences() {
		return getBestRawSequences(archive, 4, 0);
	}
	
	public List<NonDominatedSolution> getSortedArchive() {
		return getSortedArchive(archive, 0);
	}

	public ISequences getBestRawSequences(List<NonDominatedSolution> unsortedArchive, int noGroups, int objectiveIndex) {
		return findSolutionWhichReachesQuartile(archive, objectiveIndex, mapSimilarityModeToQuartile(similarityFitnessMode), true).getSequences();
	}

	private eQuartile mapSimilarityModeToQuartile(SimilarityFitnessMode similarityFitnessMode) {
		switch (similarityFitnessMode) {
		case HIGH:
			return eQuartile.FIRST;
		case MEDIUM:
			return eQuartile.SECOND;
		case LOW:
		case OFF:
		default:
			return eQuartile.THIRD;
		}
	}

	private ISequences chooseSimilaritySolutionFromWholeArchive(List<NonDominatedSolution> sortedArchive, SimilarityFitnessMode similarityFitnessMode) {
		switch (similarityFitnessMode) {
		case HIGH:
			if (sortedArchive.size() > 1) {
				return sortedArchive.get(1).getSequences();
			} else {
				return sortedArchive.get(0).getSequences();
			}
		case MEDIUM:
			if (sortedArchive.size() > 2) {
				return sortedArchive.get(sortedArchive.size() - 1).getSequences();
			} else if (sortedArchive.size() > 1) {
				return sortedArchive.get(1).getSequences();
			} else {
				return sortedArchive.get(0).getSequences();
			}
		case LOW:
		case OFF:
		default:
			return sortedArchive.get(sortedArchive.size() - 1).getSequences();
		}
	}

	private ISequences chooseSimilaritySolutionFromDividedSolutions(List<List<NonDominatedSolution>> spatiallyDividedSolutions, SimilarityFitnessMode similarityFitnessMode, long base) {
		int index;
		switch (similarityFitnessMode) {
		case LOW:
			index = 2;
			break;
		case MEDIUM:
			index = 1;
			break;
		case HIGH:
			index = 0;
			break;
		case OFF:
		default:
			index = 3;
			break;
		}
		List<NonDominatedSolution> solutions = spatiallyDividedSolutions.get(index);
		ISequences bestSolution;
		boolean movedUp = false;
		// if no solutions in bracket move up
		while (solutions.size() == 0) {
			index++;
			solutions = spatiallyDividedSolutions.get(index);
			movedUp = true;
		}
		if (!movedUp) {
			bestSolution = findBestFitnessRatio(solutions, 0, 1, base).getSequences();
		} else {
			bestSolution = solutions.get(0).getSequences();
		}
		return bestSolution;
	}

	private NonDominatedSolution findBestFitnessRatio(List<NonDominatedSolution> solutions, int objectiveA, int objectiveB, long base) {
		long bestRatio = Long.MIN_VALUE;
		int bestIndex = 0;
		int index = 0;
		for (NonDominatedSolution solution : solutions) {
			long[] fitnesses = solution.getFitnesses();
			long ratio = (fitnesses[objectiveA] - base) / fitnesses[objectiveB];
			if (ratio > bestRatio) {
				bestRatio = ratio;
				bestIndex = index;
			}
			index++;
		}
		return solutions.get(bestIndex);
	}

	List<List<NonDominatedSolution>> getSpatiallyDividedSolutions(List<NonDominatedSolution> sortedArchive, int noGroups, int objectiveIndex) {
		long[] xDivisions = getXDivisions(noGroups, sortedArchive.get(0).getFitnesses()[objectiveIndex], sortedArchive.get(sortedArchive.size() - 1).getFitnesses()[objectiveIndex]);
		if (xDivisions == null) {
			return null;
		}
		List<List<NonDominatedSolution>> divides = new ArrayList<>(noGroups);
		int lastIndex = 0;
		// divide
		for (int i = 0; i < xDivisions.length - 1; i++) {
			long exclusiveStart = xDivisions[i];
			long inclusiveEnd = xDivisions[i + 1];
			List<NonDominatedSolution> groupList = new ArrayList<>();
			for (int j = lastIndex; j < sortedArchive.size(); j++) {
				NonDominatedSolution entry = sortedArchive.get(j);
				long[] fitnesses = entry.getFitnesses();
				if (fitnesses[objectiveIndex] > exclusiveStart && fitnesses[objectiveIndex] <= inclusiveEnd) {
					groupList.add(entry);
				} else {
					if (i > 0 || (i == 0 && j > 0)) {
						lastIndex = j;
						break;
					}
				}
			}
			divides.add(groupList);
		}
		return divides;
	}

	public static long[] getXDivisions(int x, long lowestFitnessValue, long highestFitnessValue) {
		if (highestFitnessValue / x == 0) {
			return null;
		}
		long[] divisions = new long[x + 1];
		long width = (highestFitnessValue + highestFitnessValue % x) / x;
		long division = lowestFitnessValue;
		int index = 0;
		while (division < highestFitnessValue) {
			divisions[index] = division;
			division += width;
			index++;
		}
		divisions[index] = highestFitnessValue;
		return divisions;
	}

	NonDominatedSolution findSolutionWhichReachesQuartile(List<NonDominatedSolution> unsortedArchive, int objectiveIndex, eQuartile quartile, boolean minimise) {
		List<NonDominatedSolution> sortedArchive = getSortedArchive(unsortedArchive, objectiveIndex);
		long bestFitness, worstFitness;
		// error checks
		if (sortedArchive.size() == 0) {
			return null;
		} else if (sortedArchive.size() == 1) {
			return sortedArchive.get(0);
		}
		// create iterator
		if (minimise) {
			bestFitness = sortedArchive.get(0).getFitnesses()[objectiveIndex];
			worstFitness = sortedArchive.get(sortedArchive.size() - 1).getFitnesses()[objectiveIndex];
		} else {
			bestFitness = sortedArchive.get(sortedArchive.size() - 1).getFitnesses()[objectiveIndex];
			worstFitness = sortedArchive.get(0).getFitnesses()[objectiveIndex];
		}
		long oneQuartile = (worstFitness - bestFitness) / 4;
		if (minimise) {
			oneQuartile *= -1;
		}
		long desiredQuartile;
		switch (quartile) {
		case FIRST:
			desiredQuartile = worstFitness + oneQuartile;
			break;
		case SECOND:
			desiredQuartile = worstFitness + 2 * oneQuartile;
			break;
		case THIRD:
			desiredQuartile = worstFitness + 3 * oneQuartile;
			break;
		default:
			desiredQuartile = worstFitness + 3 * oneQuartile;
			break;
		}
		ListIterator<NonDominatedSolution> iterator = getIterator(minimise, sortedArchive);
		NonDominatedSolution solution = chooseSolution(objectiveIndex, minimise, desiredQuartile, desiredQuartile + oneQuartile, iterator);
		if (solution == null && sortedArchive.size() > 0) {
			iterator = getIterator(minimise, sortedArchive);
			solution = chooseClosestToDesiredQuartile(objectiveIndex, minimise, desiredQuartile, desiredQuartile + oneQuartile, iterator);
			if (solution == null) {
				solution = sortedArchive.get(0);
			}
			return solution;
		} else {
			return solution;
		}
	}

	private ListIterator<NonDominatedSolution> getIterator(boolean minimise, List<NonDominatedSolution> sortedArchive) {
		ListIterator<NonDominatedSolution> iterator;
		if (minimise) {
			iterator = sortedArchive.listIterator(sortedArchive.size());
		} else {
			iterator = sortedArchive.listIterator(0);
		}
		return iterator;
	}

	private NonDominatedSolution chooseSolution(int objectiveIndex, boolean minimise, long desiredQuartile, long endOfDesiredQuarter, ListIterator<NonDominatedSolution> iterator) {
		return chooseMiddleInQuartile(objectiveIndex, minimise, desiredQuartile, endOfDesiredQuarter, iterator);
	}

	private NonDominatedSolution chooseFirstInQuartile(int objectiveIndex, boolean minimise, long desiredQuartile, long endOfDesiredQuarter, ListIterator<NonDominatedSolution> iterator) {
		while (quartileIteratorHasNext(iterator, minimise)) {
			NonDominatedSolution entry = iterateQuartileIterator(iterator, minimise);
			long[] fitnesses = entry.getFitnesses();
			if (minimise) {
				if (fitnesses[objectiveIndex] < desiredQuartile) {
					return entry;
				}
			} else {
				if (fitnesses[objectiveIndex] > desiredQuartile) {
					return entry;
				}
			}
		}
		return null;
	}

	private NonDominatedSolution chooseMiddleInQuartile(int objectiveIndex, boolean minimise, long desiredQuartile, long endOfDesiredQuarter, ListIterator<NonDominatedSolution> iterator) {
		List<NonDominatedSolution> candidates = new ArrayList<>();
		while (quartileIteratorHasNext(iterator, minimise)) {
			NonDominatedSolution entry = iterateQuartileIterator(iterator, minimise);
			long[] fitnesses = entry.getFitnesses();
			if (minimise) {
				if (fitnesses[objectiveIndex] < desiredQuartile && fitnesses[objectiveIndex] > endOfDesiredQuarter) {
					candidates.add(entry);
				}
			} else {
				if (fitnesses[objectiveIndex] > desiredQuartile && fitnesses[objectiveIndex] < endOfDesiredQuarter) {
					candidates.add(entry);
				}
			}
		}
		if (candidates.size() > 0) {
			if (DEBUG) {
				System.out.println("choosing:" + Arrays.toString(candidates.get(((candidates.size() + 1) / 2) - 1).getFitnesses()));
			}
			return candidates.get(((candidates.size() + 1) / 2) - 1);
		} else {
			return null;
		}
	}
	
	private NonDominatedSolution chooseClosestToDesiredQuartile(int objectiveIndex, boolean minimise, long desiredQuartile, long endOfDesiredQuarter, ListIterator<NonDominatedSolution> iterator) {
		while (quartileIteratorHasNext(iterator, minimise)) {
			NonDominatedSolution entry = iterateQuartileIterator(iterator, minimise);
			long[] fitnesses = entry.getFitnesses();
			if (minimise) {
				if (fitnesses[objectiveIndex] < desiredQuartile) {
					return entry;
				}
			} else {
				if (fitnesses[objectiveIndex] > desiredQuartile && fitnesses[objectiveIndex] < endOfDesiredQuarter) {
					return entry;
				}
			}
		}
		return null;
	}

	private NonDominatedSolution iterateQuartileIterator(ListIterator<NonDominatedSolution> iterator, boolean minimise) {
		if (minimise) {
			return iterator.previous();
		} else {
			return iterator.next();
		}
	}

	private boolean quartileIteratorHasNext(ListIterator<NonDominatedSolution> iterator, boolean minimise) {
		if (minimise) {
			return iterator.hasPrevious();
		} else {
			return iterator.hasNext();
		}
	}

	@Override
	public final IAnnotatedSolution getBestSolution() {
		ISequences bestRawSequences = getBestRawSequences();
		// evaluate solution
		final IModifiableSequences fullSequences = new ModifiableSequences(bestRawSequences);
		getSequenceManipulator().manipulate(fullSequences);

		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
			if (!evaluationProcess.evaluate(Phase.Checked_Evaluation, fullSequences, evaluationState)) {
				// Problem evaluating, reject move
				return null;
			}
		}

		for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
			if (!evaluationProcess.evaluate(Phase.Final_Evaluation, fullSequences, evaluationState)) {
				// Problem evaluating, reject move
				return null;
			}
		}

		Map<String, Long> fitnesses = getFitnessesFromFitnessComponents();

		final IAnnotatedSolution annotatedSolution = getBestAnnotatedSolution(fullSequences, evaluationState, fitnesses);
		if (annotatedSolution == null) {
			return null;
		}

		final long clock = System.currentTimeMillis() - getStartTime();

		annotatedSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, getNumberOfIterationsCompleted());
		annotatedSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, clock);

		if (bestRawSequences != null) {
			getMultiObjectiveFitnessEvaluator().updateBest(bestRawSequences, fullSequences, evaluationState);
		}
		return annotatedSolution;
	}

	private Map<String, Long> getFitnessesFromFitnessComponents() {
		Map<String, Long> fitnesses = new HashMap<>();
		for (final IFitnessComponent component : fitnessComponents) {
			fitnesses.put(component.getName(), component.getFitness());
		}
		return fitnesses;
	}

	private IAnnotatedSolution getBestAnnotatedSolution(ISequences fullSequences, final IEvaluationState evaluationState, Map<String, Long> fitnesses) {
		assert fullSequences != null;
		assert evaluationState != null;
		final IAnnotatedSolution result = fitnessHelper.buildAnnotatedSolution(fullSequences, evaluationState, getMultiObjectiveFitnessEvaluator().getFitnessComponents(), getEvaluationProcesses());
		result.setGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, fitnesses);

		return result;
	}

	@Override
	protected void initNextIteration() {
		super.initNextIteration();
		if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
			// this sets best fitness for the best solution	
			getBestSolution();
		}
	}
}
