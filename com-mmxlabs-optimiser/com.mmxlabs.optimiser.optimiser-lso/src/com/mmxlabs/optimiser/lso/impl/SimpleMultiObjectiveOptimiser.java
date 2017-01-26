/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
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
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.INullMove;
import com.mmxlabs.optimiser.lso.SimilarityFitnessMode;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;

/**
 * A sub-class of {@link DefaultLocalSearchOptimiser} implementing a multi-objective algorithm.
 * 
 * @author achurchill
 * 
 */
public class SimpleMultiObjectiveOptimiser extends DefaultLocalSearchOptimiser {

	private static final boolean DEBUG = false;

	@Inject
	protected IFitnessHelper fitnessHelper;

	protected List<Pair<ISequences, long[]>> archive = new ArrayList<>();
	
	IMultiObjectiveFitnessEvaluator multiObjectiveFitnessEvaluator;
	List<IFitnessComponent> fitnessComponents;
	
	private final Random r;

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
	public IAnnotatedSolution start(@NonNull IOptimisationContext optimiserContext,
			@NonNull ISequences initialRawSequences, @NonNull ISequences inputRawSequences) {
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
//			System.out.println("i:"+i);
			
			// choose a solution from the archive
			ISequences nonDominatedSolution = (ISequences) archive.get(r.nextInt(archive.size())).getFirst();
			updateSequences(nonDominatedSolution, pinnedPotentialRawSequences, nonDominatedSolution.getResources());
			getMoveGenerator().setSequences(pinnedPotentialRawSequences);
			
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

			final IEvaluationState evaluationState = getEvaluationState(pinnedPotentialRawSequences,
					pinnedCurrentRawSequences, move, potentialFullSequences);
			if (evaluationState == null) {
				continue MAIN_LOOP;
			}
			
			// Apply hard constraint checkers
			if (!applyHardEvaluatedConstraintCheckers(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move,
					potentialFullSequences, evaluationState)) {
				continue MAIN_LOOP;
			}

			if (!evaluateOnEvaluationProcesses(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move,
					potentialFullSequences, evaluationState)) {
				continue MAIN_LOOP;
			}

			// Test whether new sequence should be added to the archive
			long[] fitnesses = addSequenceToArchiveIfNonDominated(pinnedPotentialRawSequences, move,
					potentialFullSequences, evaluationState);
		}
		if (DEBUG) {
			System.out.println("-------------");
			List<Pair<ISequences, long[]>> sortedValues = getSortedArchive(archive, 1);
			sortedValues.forEach(v -> System.out.println(String.format("[%s-start,%s],", v.getSecond()[0]*-1, v.getSecond()[1])));
		}
		setNumberOfIterationsCompleted(numberOfMovesTried);

		updateProgressLogs();
		return iterationsThisStep;
	}

	List<Pair<ISequences, long[]>> getSortedArchive(List<Pair<ISequences, long[]>> archive, int objectiveIndex) {
		List<Pair<ISequences, long[]>> sortedValues = archive.stream().sorted((a, b) -> Long.compare(a.getSecond()[objectiveIndex], b.getSecond()[objectiveIndex])).collect(Collectors.toList());
		return sortedValues;
	}

	long[] addSequenceToArchiveIfNonDominated(final ModifiableSequences pinnedPotentialRawSequences,
			final IMove move, final IModifiableSequences potentialFullSequences,
			final IEvaluationState evaluationState) {
		long[] fitnesses = getMultiObjectiveFitnessEvaluator().getCombinedFitnessAndObjectiveValuesForComponentClasses(pinnedPotentialRawSequences, potentialFullSequences, evaluationState, move.getAffectedResources(), fitnessComponents);
		if (fitnesses != null) {
			boolean nonDominated = isDominated(archive, fitnesses);
			if (nonDominated) {
				archive.add(new Pair<>(new Sequences(pinnedPotentialRawSequences), fitnesses));
			}
		}
		return fitnesses;
	}

	private boolean isDominated(List<Pair<ISequences, long[]>> archive, long[] thisFitness) {
		List<ISequences> dominated = new LinkedList<>();
		boolean add = true;
		for (Pair<ISequences, long[]> other : archive) {
			long[] otherFitness = other.getSecond();
			if (!atLeastOneObjectiveLower(thisFitness, otherFitness)) {
				// this solution is dominated
				add = false;
				break;
			} if (allObjectivesLower(thisFitness, otherFitness)) {
				// new solution dominates
				dominated.add(other.getFirst());
			} else if (allObjectivesLowerOrEqual(thisFitness, otherFitness)) {
				dominated.add(other.getFirst());
			}
		}
		for (ISequences a : dominated) {
			archive.removeIf(b -> b.getFirst().equals(a));
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
			archive.add(new Pair<>(currentRawSequences, fitnesses));
		}
	}

	private long[] getFitnessesFromSequences(final ISequences currentRawSequences,
			final IModifiableSequences fullSequences, final IEvaluationState evaluationState) {
		long[] fitnesses = getMultiObjectiveFitnessEvaluator().getCombinedFitnessAndObjectiveValuesForComponentClasses(currentRawSequences, fullSequences, evaluationState, null, fitnessComponents);
		return fitnesses;
	}
	
	@Override
	public ISequences getBestRawSequences() {
		return getBestRawSequences(archive, 4, 0);
	}

	public ISequences getBestRawSequences(List<Pair<ISequences, long[]>> unsortedArchive, int noGroups, int objectiveIndex) {
		List<Pair<ISequences, long[]>> sortedArchive = getSortedArchive(unsortedArchive, objectiveIndex);
		// go through archive, divide into 4 groups
//		List<List<Pair<ISequences,long[]>>> spatiallyDividedSolutions = getSpatiallyDividedSolutions(sortedArchive, noGroups, objectiveIndex);
//		long base = sortedArchive.get(0).getSecond()[0]; // this is used to get score for uplift
//		if (spatiallyDividedSolutions != null) {
//			return chooseSimilaritySolutionFromDividedSolutions(spatiallyDividedSolutions, similarityFitnessMode, base);
//		} else {
//			return chooseSimilaritySolutionFromWholeArchive(sortedArchive, similarityFitnessMode);
//		}
		return findSolutionWhichReachesQuartile(archive, objectiveIndex, mapSimilarityModeToQuartile(similarityFitnessMode), true).getFirst();
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

	private ISequences chooseSimilaritySolutionFromWholeArchive(List<Pair<ISequences, long[]>> sortedArchive,
			SimilarityFitnessMode similarityFitnessMode) {
		switch (similarityFitnessMode) {
		case HIGH:
			if (sortedArchive.size() > 1) {
				return sortedArchive.get(1).getFirst();
			} else {
				return sortedArchive.get(0).getFirst();
			}
		case MEDIUM:
			if (sortedArchive.size() > 2) {
				return sortedArchive.get(sortedArchive.size() - 1).getFirst();
			} else if (sortedArchive.size() > 1) {
				return sortedArchive.get(1).getFirst();
			} else {
				return sortedArchive.get(0).getFirst();
			}
		case LOW:
		case OFF:
		default:
			return sortedArchive.get(sortedArchive.size()-1).getFirst();
		}
	}

	private ISequences chooseSimilaritySolutionFromDividedSolutions(List<List<Pair<ISequences, long[]>>> spatiallyDividedSolutions,
			SimilarityFitnessMode similarityFitnessMode, long base) {
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
		List<Pair<ISequences, long[]>> solutions = spatiallyDividedSolutions.get(index);
		ISequences bestSolution;
		boolean movedUp = false;
		// if no solutions in bracket move up
		while (solutions.size() == 0) {
			index++;
			solutions = spatiallyDividedSolutions.get(index);
			movedUp = true;
		}
		if (!movedUp) {
			bestSolution = findBestFitnessRatio(solutions, 0, 1, base).getFirst();
		} else {
			bestSolution = solutions.get(0).getFirst();
		}
		return bestSolution;
	}
	
	private Pair<ISequences, long[]> findBestFitnessRatio(List<Pair<ISequences, long[]>> solutions, int objectiveA, int objectiveB, long base) {
		long bestRatio = Long.MIN_VALUE;
		int bestIndex = 0;
		int index = 0;
		for (Pair<ISequences, long[]> solution : solutions) {
			long[] fitnesses = solution.getSecond();
			long ratio = (fitnesses[objectiveA] - base) / fitnesses[objectiveB];
			if (ratio > bestRatio) {
				bestRatio = ratio;
				bestIndex = index;
			}
			index++;
		}
		return solutions.get(bestIndex);
	}

	List<List<Pair<ISequences,long[]>>> getSpatiallyDividedSolutions(List<Pair<ISequences, long[]>> sortedArchive, int noGroups, int objectiveIndex) {
		long[] xDivisions = getXDivisions(noGroups, sortedArchive.get(0).getSecond()[objectiveIndex], sortedArchive.get(sortedArchive.size()-1).getSecond()[objectiveIndex]);
		if (xDivisions == null) {
			return null;
		}
		List<List<Pair<ISequences, long[]>>> divides = new ArrayList<>(noGroups);
		int lastIndex = 0;
		// divide
		for (int i = 0; i < xDivisions.length - 1; i++) {
			long exclusiveStart = xDivisions[i];
			long inclusiveEnd = xDivisions[i+1];
			List<Pair<ISequences, long[]>> groupList = new ArrayList<>();
			for (int j = lastIndex; j < sortedArchive.size(); j++) {
				Pair<ISequences, long[]> entry = sortedArchive.get(j);
				long[] fitnesses = entry.getSecond();
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
		long[] divisions = new long[x+1];
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
	
	Pair<ISequences,long[]> findSolutionWhichReachesQuartile(List<Pair<ISequences,long[]>> unsortedArchive, int objectiveIndex, eQuartile quartile, boolean minimise) {
		List<Pair<ISequences, long[]>> sortedArchive = getSortedArchive(unsortedArchive, objectiveIndex);
		long bestFitness,worstFitness;
		// error checks
		if (sortedArchive.size() == 0) {
			return null;
		} else if (sortedArchive.size() == 1) {
			return sortedArchive.get(0);
		}
		// create iterator
		if (minimise) {
			bestFitness = sortedArchive.get(0).getSecond()[objectiveIndex];
			worstFitness = sortedArchive.get(sortedArchive.size() - 1).getSecond()[objectiveIndex];
		} else {
			bestFitness = sortedArchive.get(sortedArchive.size() - 1).getSecond()[objectiveIndex];
			worstFitness = sortedArchive.get(0).getSecond()[objectiveIndex];
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
			desiredQuartile = worstFitness + 2*oneQuartile;
			break;
		case THIRD:
			desiredQuartile = worstFitness + 3*oneQuartile;
			break;
		default:
			desiredQuartile = worstFitness + 3*oneQuartile;
			break;
		}
		ListIterator<Pair<ISequences, long[]>> iterator;
		if (minimise) {
			iterator = sortedArchive.listIterator(sortedArchive.size());
		} else {
			iterator = sortedArchive.listIterator(0);
		}
		Pair<ISequences,long[]> solution = chooseSolution(objectiveIndex, minimise, desiredQuartile, desiredQuartile + oneQuartile, iterator);
		if (solution == null && sortedArchive.size() > 0) {
			return sortedArchive.get(0);
		} else {
			return solution;
		}
	}

	private Pair<ISequences,long[]> chooseSolution(int objectiveIndex, boolean minimise, long desiredQuartile, long endOfDesiredQuarter, ListIterator<Pair<ISequences, long[]>> iterator) {
		return chooseMiddleInQuartile(objectiveIndex, minimise, desiredQuartile, endOfDesiredQuarter, iterator);
	}

	private Pair<ISequences, long[]> chooseFirstInQuartile(int objectiveIndex, boolean minimise, long desiredQuartile, long endOfDesiredQuarter, ListIterator<Pair<ISequences, long[]>> iterator) {
		while (quartileIteratorHasNext(iterator, minimise)) {
			Pair<ISequences, long[]> entry = iterateQuartileIterator(iterator, minimise);
			long[] fitnesses = entry.getSecond();
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
	
	private Pair<ISequences, long[]> chooseMiddleInQuartile(int objectiveIndex, boolean minimise, long desiredQuartile, long endOfDesiredQuarter, ListIterator<Pair<ISequences, long[]>> iterator) {
		List<Pair<ISequences, long[]>> candidates = new ArrayList<Pair<ISequences,long[]>>();
		while (quartileIteratorHasNext(iterator, minimise)) {
			Pair<ISequences, long[]> entry = iterateQuartileIterator(iterator, minimise);
			long[] fitnesses = entry.getSecond();
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
				System.out.println("choosing:"+Arrays.toString(candidates.get(((candidates.size()+1)/2)-1).getSecond()));
			}
			return candidates.get(((candidates.size()+1)/2)-1);
		} else {
			return null;
		}
	}

	private Pair<ISequences, long[]> iterateQuartileIterator(ListIterator<Pair<ISequences, long[]>> iterator, boolean minimise) {
		if (minimise) {
			return iterator.previous();
		} else {
			return iterator.next();
		}
	}

	private boolean quartileIteratorHasNext(ListIterator<Pair<ISequences, long[]>> iterator, boolean minimise) {
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

}
