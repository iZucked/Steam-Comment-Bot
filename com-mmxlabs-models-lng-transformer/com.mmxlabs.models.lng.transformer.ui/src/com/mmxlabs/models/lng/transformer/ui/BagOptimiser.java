/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.impl.MultiStateResult;
import com.mmxlabs.models.lng.transformer.stochasticactionsets.StochasticActionSetUtils;
import com.mmxlabs.models.lng.transformer.ui.breakdown.BagMover;
import com.mmxlabs.models.lng.transformer.ui.breakdown.Change;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeChecker;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSet;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSetFinderJob;
import com.mmxlabs.models.lng.transformer.ui.breakdown.Difference;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobState;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStateMode;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStore;
import com.mmxlabs.models.lng.transformer.ui.breakdown.MetricType;
import com.mmxlabs.models.lng.transformer.ui.breakdown.MyFuture;
import com.mmxlabs.models.lng.transformer.ui.breakdown.SimilarityState;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.ActionSetIndependenceChecking;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;

/**
 * An "optimiser" to generate the sequence of steps required by a user to go from one {@link ISequences} state to another one. I.e. from a pre-optimised state to an optimised state.
 * 
 * This process generates changes and groups them into change sets. A change is a single change to move closer to the target state. This may be a single slot swap or moving a cargo pair onto the
 * correct vessel. A change set is the minimal set of changes required to pass the constraint checkers and other criteria (such as positive P&L change). A single change may create invalid solutions.
 * Overall we aim to generate a list of change sets to get between the initial and target solutions.
 * 
 * The process is a constructive stochastic search.
 * 
 * @author achurchill/simon
 */
public class BagOptimiser {

	private static final Logger LOG = LoggerFactory.getLogger(BagOptimiser.class);

	private static final boolean DEBUG = false;

	private static final boolean BUILD_DEPENDANCY_GRAPH = false;

	@Inject
	private ISequencesManipulator sequencesManipulator;

	@Inject
	@NonNull
	private List<IFitnessComponent> fitnessComponents;

	@Inject
	@NonNull
	private IFitnessHelper fitnessHelper;
	@Inject
	@NonNull
	private IFitnessCombiner fitnessCombiner;

	@Inject
	private List<IEvaluationProcess> evaluationProcesses;

	@Inject
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	@NonNull
	private ISequences initialRawSequences;

	@Inject
	private Injector injector;

	@NonNull
	@Inject
	protected BagMover bagMover;
	private final Random rdm = new Random(0);

	private final List<IMultiStateResult> bestSolutions = new LinkedList<>();

	private final int initialPopulationSize = 10;
	private final int initialSearchSize = 20_000;
	private final int normalSearchSize = 2_000;
	private final int retrySearchSize = 2_000;

	private final int maxEvaluations = 15_000_000; // DO NOT COMMIT
	private final int maxEvaluationsInRun = 5_500_000; // DO NOT COMMIT
	private int maxLeafs = 0;
	private IProgressMonitor progressMonitor = null;
	private ProgressCounter progressCounter = new ProgressCounter();
	private ActionSetOptimisationData actionSetOptimisationData = new ActionSetOptimisationData();
	private boolean ignoreTerminationConditions = false;

	private void init(final IProgressMonitor progressMonitor, final int maxLeafs) {
		setProgressCounter(new ProgressCounter());
		setProgressMonitor(progressMonitor);
		setMaxLeafs(maxLeafs);
		setIgnoreTerminationConditions(false);
		setActionSetOptimisationData(new ActionSetOptimisationData());
	}

	/**
	 * Performs a constructive stochastic search. Algorithm works as follows: Sample a large number of changesets. Store valid changesets as P For each, p in P While (population of nodes does not
	 * contain a leaf mode) Add new randomly constructed changesets to end of node Check if nodes are valid Keep the top n valid nodes
	 * 
	 * @param targetRawSequences
	 * @param progressMonitor
	 * @param maxLeafs
	 * @return
	 */
	public IMultiStateResult optimise(@NonNull final ISequences targetRawSequences, @NonNull final IProgressMonitor progressMonitor, final int maxLeafs) {

		init(progressMonitor, maxLeafs);

		final long time1 = System.currentTimeMillis();

		// Prep Fitness cores based on original initial scenario - for lateness/similarity etc.
		evaluateSolution(initialRawSequences);
		final SimilarityState targetSimilarityState = injector.getInstance(SimilarityState.class);

		try {
			// Prepare initial solution state
			final IModifiableSequences initialFullSequences = sequencesManipulator.createManipulatedSequences(initialRawSequences);

			// Debugging -- get initial change count
			final int changesCount = bagMover.getChangedElements(targetSimilarityState, initialRawSequences).size();
			if (DEBUG) {
				System.out.println("Initial changes " + changesCount);
			}

			if (changesCount == 0) {
				final List<NonNullPair<ISequences, Map<String, Object>>> processedSolution = new LinkedList<>();

				// Evaluate initial state.
				// Return an evaluated state for ITS
				processedSolution.add(evaluateSolution(initialRawSequences));

				final IMultiStateResult r = new MultiStateResult(processedSolution.get(processedSolution.size() - 1), processedSolution);
				return r;
			}

			final ScheduledSequences initialScheduledSequences = evaluateInitialStateAndGetScheduledSequences(initialFullSequences);
			assert initialScheduledSequences != null;

			final long initialUnusedCompulsarySlot = bagMover.calculateUnusedCompulsarySlot(initialRawSequences);
			final long initialLateness = bagMover.calculateScheduleLateness(initialFullSequences, initialScheduledSequences);
			final long initialCapacity = bagMover.calculateScheduleCapacity(initialFullSequences, initialScheduledSequences);
			final long initialPNL = bagMover.calculateSchedulePNL(initialFullSequences, initialScheduledSequences);

			// Generate the initial set of changes, one level deep
			final List<ChangeSet> changeSets = new LinkedList<>();
			final List<Change> changes = new LinkedList<>();
			final long time2 = System.currentTimeMillis();

			targetSimilarityState.getBaseMetrics()[MetricType.LATENESS.ordinal()] = initialLateness;
			targetSimilarityState.getBaseMetrics()[MetricType.CAPACITY.ordinal()] = initialCapacity;
			targetSimilarityState.getBaseMetrics()[MetricType.PNL.ordinal()] = initialPNL;
			targetSimilarityState.getBaseMetrics()[MetricType.COMPULSARY_SLOT.ordinal()] = initialUnusedCompulsarySlot;

			final long bestFitness;

			// Generate the similarity data structures to the target solution
			bestFitness = initSimiliarityTarget(targetRawSequences, targetSimilarityState);

			final ChangeChecker changeChecker = injector.getInstance(ChangeChecker.class);
			changeChecker.init(null, targetSimilarityState, initialRawSequences);
			final List<JobState> currentSearchStates = getInitialSearchStates(initialRawSequences, initialUnusedCompulsarySlot, initialLateness, initialCapacity, initialPNL, changeSets, changes,
					changeChecker);

			final List<JobState> finalPopulation = new LinkedList<>();
			final List<JobState> allLimitedStates = new LinkedList<>();
			progressMonitor.beginTask("Generate Action Sets", 100);
			int initialDepthLimit = 3;
			try {
				while (!shouldTerminate(actionSetOptimisationData)) {
					bagMover.setDepthRange(0, initialDepthLimit);
					actionSetOptimisationData.startNewRun();
					final List<JobState> promisingLimitedStates = new LinkedList<>();
					final List<List<JobState>> savedPopulations = new LinkedList<>();

					// Find initial change set roots
					final List<JobState> initialPopulation = addChangeSetLevel(targetSimilarityState, currentSearchStates, initialPopulationSize);
					allLimitedStates.addAll(initialPopulation);
					if (initialPopulation.isEmpty()) {
						return null;
					}

					if (DEBUG) {
						System.out.println("initial pop");
						printJobStates(initialPopulation);
						System.out.println("evaluations [after initialPopulation] : " + actionSetOptimisationData.getTotalEvaluations());
						System.out.println("constraint failed evaluations [after initialPopulation] : " + actionSetOptimisationData.actualConstraintEvaluations);
						System.out.println("pnl failed evaluations [after initialPopulation] : " + actionSetOptimisationData.actualPNLEvaluations);
					}

					for (final JobState root : initialPopulation) {
						checkIfCancelled(progressMonitor);
						if (shouldTerminate(actionSetOptimisationData)) {
							break;
						}
						actionSetOptimisationData.startNewRun();
						// main loop - search from a change set root. Will add LEAFs to finalPopulation and others to limitedStates
						searchChangeSetRoot(progressMonitor, maxLeafs, targetSimilarityState, finalPopulation, promisingLimitedStates, allLimitedStates, savedPopulations, root);
						if (DEBUG) {
							System.out.println("evaluations [after rootNode] : " + actionSetOptimisationData.getTotalEvaluations());
							System.out.println("constraint failed evaluations [after rootNode] : " + actionSetOptimisationData.actualConstraintEvaluations);
							System.out.println("pnl failed evaluations [after rootNode] : " + actionSetOptimisationData.actualPNLEvaluations);
						}
						if (finalPopulation.size() > maxLeafs) { // Do not commit - move up
							break;
						}
					}

					checkIfCancelled(progressMonitor);
					if (!shouldTerminate(actionSetOptimisationData)) {
						for (final JobState promising : promisingLimitedStates) {
							if (shouldTerminate(actionSetOptimisationData)) {
								break;
							}
							actionSetOptimisationData.startNewRun();
							finalPopulation.addAll(expandNode(promising, 250, targetSimilarityState));
							actionSetOptimisationData.numberOfLeafs = finalPopulation.size();
						}
					}
					if (DEBUG) {
						System.out.println("evaluations [after expanding promising] : " + actionSetOptimisationData.getTotalEvaluations());
						System.out.println("constraint failed evaluations [after expanding promising] : " + actionSetOptimisationData.actualConstraintEvaluations);
						System.out.println("pnl failed evaluations [after expanding promising] : " + actionSetOptimisationData.actualPNLEvaluations);
						System.out.println("LEAFS [after expanding promising] : " + actionSetOptimisationData.numberOfLeafs);
					}
					actionSetOptimisationData.startNewRun();
					while (savedPopulations.size() > 0 && !shouldTerminate(actionSetOptimisationData) && !shouldTerminateInRun(actionSetOptimisationData)) {
						checkIfCancelled(progressMonitor);
						// main loop - search from a change set root. Will add LEAFs to finalPopulation and others to limitedStates
						searchChangeSetPopulation(progressMonitor, maxLeafs, targetSimilarityState, finalPopulation, promisingLimitedStates, allLimitedStates, savedPopulations,
								savedPopulations.remove(0));
						if (DEBUG) {
							System.out.println("evaluations [after savedPopulation] : " + actionSetOptimisationData.getTotalEvaluations());
							System.out.println("constraint failed evaluations [after savedPopulation] : " + actionSetOptimisationData.actualConstraintEvaluations);
							System.out.println("pnl failed evaluations [after savedPopulation] : " + actionSetOptimisationData.actualPNLEvaluations);
						}
						if (finalPopulation.size() > maxLeafs) { // Do not commit - move up
							break;
						}
					}
					initialDepthLimit++;
				}
				logLimiteds(allLimitedStates);
				logFinalPopulation(finalPopulation);
				/**
				 * Add everything else if we couldn't finish
				 */
				if (finalPopulation.size() == 0 && allLimitedStates.size() > 0) {
					final JobState bestSolution = reduceAndSortStatesTotalPNL(allLimitedStates).get(0);
					final ChangeSet changeSet = new ChangeSet(new LinkedList<Change>());
					changeSet.setRawSequences(targetRawSequences);
					final List<ChangeSet> finalSolutionChangeSets = new LinkedList<>(bestSolution.changeSetsAsList);
					finalSolutionChangeSets.add(changeSet);
					final JobState finalSolution = new JobState(targetRawSequences, finalSolutionChangeSets, Collections.<Change> emptyList(), new LinkedList<Difference>());
					finalSolution.mode = JobStateMode.LEAF;
					finalPopulation.add(finalSolution);
					actionSetOptimisationData.numberOfLeafs = finalPopulation.size();
				}

				// TODO: Sort by changset P&L and group size.
				final List<JobState> sortedChangeStates = getSortedLeafStates(finalPopulation);
				if (DEBUG) {
					printPopulationInfo(sortedChangeStates);
				}

				if (!sortedChangeStates.isEmpty()) {
					if (BUILD_DEPENDANCY_GRAPH) {
						buildDependencyGraph(targetSimilarityState, initialRawSequences, sortedChangeStates);
					}
					processAndStoreBreakdownSolution(sortedChangeStates.get(0), initialRawSequences, bestFitness);
				}
				if (sortedChangeStates.isEmpty()) {
					LOG.error("Unable to find action sets");
				}

			} catch (final InterruptedException | ExecutionException e) {
				LOG.error(e.getMessage(), e);
			}
			final long time3 = System.currentTimeMillis();

			if (DEBUG) {
				System.out.printf("Setup time %d -- Search time %d\n", (time2 - time1) / 1000L, (time3 - time2) / 1000L);
			}
			return getBestSolution();
		} finally {
			progressMonitor.done();
		}
	}

	@NonNull
	private NonNullPair<ISequences, Map<String, Object>> evaluateSolution(final @NonNull ISequences rawSequences) {
		final IModifiableSequences currentFullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

		final IEvaluationState changeSetEvaluationState = bagMover.evaluateSequence(currentFullSequences);
		fitnessHelper.evaluateSequencesFromComponents(currentFullSequences, changeSetEvaluationState, fitnessComponents, null);

		final Map<String, Long> currentFitnesses = new HashMap<>();
		for (final IFitnessComponent fitnessComponent : fitnessComponents) {
			currentFitnesses.put(fitnessComponent.getName(), fitnessComponent.getFitness());
		}

		final Map<String, Object> extraAnnotations = new HashMap<>();
		extraAnnotations.put(OptimiserConstants.G_AI_fitnessComponents, currentFitnesses);

		return new NonNullPair<ISequences, Map<String, Object>>(rawSequences, extraAnnotations);
	}

	private void buildDependencyGraph(final SimilarityState targetSimilarityState, final ISequences initialRawSequences, final List<JobState> sortedChangeStates) {
		final ActionSetIndependenceChecking actionSetIndependenceChecking = injector.getInstance(ActionSetIndependenceChecking.class);
		final List<ChangeSet> bestChangeSets = new LinkedList<>();
		for (final ChangeSet cs : sortedChangeStates.get(0).changeSetsAsList) {
			bestChangeSets.add(cs);
		}
		final Map<ChangeSet, Set<List<ChangeSet>>> independenceSets = actionSetIndependenceChecking.getChangeSetIndependence(bestChangeSets, initialRawSequences, targetSimilarityState,
				targetSimilarityState.getBaseMetrics());
	}

	protected long initSimiliarityTarget(final ISequences targetRawSequences, final SimilarityState targetSimilarityState) {
		final long bestFitness;
		final IModifiableSequences potentialFullSequences = sequencesManipulator.createManipulatedSequences(targetRawSequences);

		final IEvaluationState evaluationState = evaluateAndGetIEvaluationState(potentialFullSequences);
		targetSimilarityState.init(potentialFullSequences);

		fitnessHelper.evaluateSequencesFromComponents(potentialFullSequences, evaluationState, fitnessComponents, null);
		bestFitness = fitnessCombiner.calculateFitness(fitnessComponents);
		return bestFitness;
	}

	private List<JobState> getInitialSearchStates(final ISequences initialRawSequences, final long initialUnusedCompulsarySlot, final long initialLateness, final long initialCapacity,
			final long initialPNL, final List<ChangeSet> changeSets, final List<Change> changes, final ChangeChecker changeChecker) {
		final List<JobState> currentSearchStates = new LinkedList<>();
		for (int i = 0; i < initialSearchSize; i++) {
			final JobState job = new JobState(new Sequences(initialRawSequences), changeSets, changes, changeChecker.getFullDifferences());
			job.setMetric(MetricType.PNL, initialPNL, 0, 0);
			job.setMetric(MetricType.LATENESS, initialLateness, 0, 0);
			job.setMetric(MetricType.CAPACITY, initialCapacity, 0, 0);
			job.setMetric(MetricType.COMPULSARY_SLOT, initialUnusedCompulsarySlot, 0, 0);
			currentSearchStates.add(job);
		}
		return currentSearchStates;
	}

	private void searchChangeSetRoot(final IProgressMonitor progressMonitor, final int maxLeafs, final SimilarityState targetSimilarityState, final Collection<JobState> finalPopulation,
			final Collection<JobState> promisingLimitedStates, final Collection<JobState> allLimitedStates, final List<List<JobState>> savedPopulations, final JobState root)
					throws InterruptedException, ExecutionException {
		if (root.mode == JobStateMode.LEAF) {
			finalPopulation.add(root);
			actionSetOptimisationData.numberOfLeafs = finalPopulation.size();
		} else {
			int rootIndex = 0;
			List<JobState> states = new LinkedList<JobState>();
			for (int i = 0; i < normalSearchSize; i++) {
				states.add(new JobState(root));
			}
			while (!foundLeaf(states) && states.size() != 0) {
				checkIfCancelled(progressMonitor);
				if (DEBUG) {
					System.out.println("--------------------------- root ----------------- " + (rootIndex++));
				}
				final List<JobState> returnedPopulation = addChangeSetLevel(targetSimilarityState, states, 10);
				if (returnedPopulation.size() == 0) {
					savedPopulations.add(states);
					bestPopulation.addAll(states);
				}
				states = returnedPopulation;
				allLimitedStates.addAll(states);
			}

			// TODO: make more efficient - currently adding all branches but could choose based on some distance metric
			if (foundLeaf(states)) {
				promisingLimitedStates.addAll(getPromisingBranches(states));
				bestPopulation.clear();
				bestPopulation.addAll(getLeafs(states));
			} else if (states.size() != 0) {
				bestPopulation.addAll(states);
			}

			finalPopulation.addAll(getLeafs(states));
			actionSetOptimisationData.numberOfLeafs = finalPopulation.size();
		}
		long endTime = System.currentTimeMillis();
		int endConstraintEvals = actionSetOptimisationData.actualConstraintEvaluations;
		int endPNLEvals = actionSetOptimisationData.actualPNLEvaluations;
		logRootRun(root, bestPopulation, endTime - startTime, endPNLEvals - startPNLEvals, endConstraintEvals - startConstraintEvals);
	}

	private void logRootRun(JobState root, List<JobState> bestPopulation, long runTime, int pnlEvals, int constraintEvals) {
		if (actionSetLogger != null) {
			int leafs = getLeafs(bestPopulation).size();
			JobState best = getBestSolutionForLogger(bestPopulation);
			List<Difference> diffs = Collections.<Difference>emptyList();
			if (best != null) {
				Collections.sort(bestPopulation, new Comparator<JobState>() {
					@Override
					public int compare(JobState o1, JobState o2) {
						return Integer.compare(o1.getDifferencesList().size(), o2.getDifferencesList().size());
					}
				});
				diffs = bestPopulation.get(0).getDifferencesList();
			}
			actionSetLogger.logRootActionSet(root, best, leafs, pnlEvals, constraintEvals, diffs, runTime);
		}
	}

	private JobState getBestSolutionForLogger(List<JobState> states) {
		if (states.size() > 0) {
			Collections.sort(states, new Comparator<JobState>() {
				@Override
				public int compare(JobState o1, JobState o2) {
					return metric(o1, o2);
				}
			});
			return states.get(0);
		} else {
			return null;
		}
	}
	
//	private int metric(JobState o1, JobState o2) {
//		if (o1.mode == JobStateMode.LEAF && o2.mode != JobStateMode.LEAF) {
//			return -1;
//		} else if (o1.mode != JobStateMode.LEAF && o2.mode == JobStateMode.LEAF) {
//			return 1;
//		}
//		double prctile = 0.8;
//		while (prctile > 0.1 && StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(o1.changeSetsAsList, prctile) == StochasticActionSetUtils
//				.getTotalPNLPerChangeForPercentile(o2.changeSetsAsList, prctile)) {
//			prctile = prctile - 0.1;
//		}
//		double a = StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(o1.changeSetsAsList, prctile);
//		double b = StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(o2.changeSetsAsList, prctile);
//		int compare = Double.compare(a, b);
//		return compare * -1;
//	}
	
	private int metric(JobState o1, JobState o2) {
		return proshunMetric(o1, o2);
	}
	
	private int proshunMetric(JobState o1, JobState o2) {
		return Long.compare(StochasticActionSetUtils.calculatePNLPerCumulativeChangeWithNegativeHandling(o1), StochasticActionSetUtils.calculatePNLPerCumulativeChangeWithNegativeHandling(o2))*-1;
	}

	private void searchChangeSetPopulation(final IProgressMonitor progressMonitor, final int maxLeafs, final SimilarityState targetSimilarityState, final Collection<JobState> finalPopulation,
			final Collection<JobState> promisingLimitedStates, final Collection<JobState> allLimitedStates, final List<List<JobState>> savedPopulations, final List<JobState> population)
					throws InterruptedException, ExecutionException {
		int rootIndex = 0;
		List<JobState> states = new LinkedList<JobState>(population);
		while (!foundLeaf(states) && states.size() != 0) {
			checkIfCancelled(progressMonitor);
			if (DEBUG) {
				System.out.println("--------------------------- root ----------------- " + (rootIndex++));
			}
			final List<JobState> returnedPopulation = addChangeSetLevel(targetSimilarityState, states, 10);
			if (returnedPopulation.size() == 0) {
				savedPopulations.add(states);
			}
			states = returnedPopulation;
			allLimitedStates.addAll(states);
		}

		// TODO: make more efficient - currently adding all branches but could choose based on some distance metric
		if (foundLeaf(states)) {
			promisingLimitedStates.addAll(getPromisingBranches(states));
		}

		finalPopulation.addAll(getLeafs(states));
		actionSetOptimisationData.numberOfLeafs = finalPopulation.size();
	}

	List<JobState> addChangeSetLevel(final SimilarityState targetSimilarityState, final List<JobState> currentStates, final int maxStates) throws InterruptedException, ExecutionException {
		List<JobState> states = new LinkedList<>(currentStates);
		if (states.isEmpty()) {
			return states;
		}
		List<JobState> fullChangesSets = findChangeSets(targetSimilarityState, states, maxStates);
		int retryCount = 0;
		while (fullChangesSets.size() == 0) {
			states = new LinkedList<>();
			for (int i = 0; i < retrySearchSize; i++) {
				states.add(new JobState(currentStates.get(rdm.nextInt(currentStates.size()))));
			}
			fullChangesSets = findChangeSets(targetSimilarityState, states, maxStates);
			++retryCount;
			if (DEBUG) {
				System.out.println("retry:" + (retryCount));
				System.out.println("evaluations [after retry] : " + actionSetOptimisationData.getTotalEvaluations());
				System.out.println("constraint failed evaluations [after retry] : " + actionSetOptimisationData.actualConstraintEvaluations);
				System.out.println("pnl failed evaluations [after retry] : " + actionSetOptimisationData.actualPNLEvaluations);
			}
			if (retryCount > 1) {
				bagMover.setDepthRange(3, 4);
			}
			if (retryCount > 2) {
				bagMover.setDepthRange(3, 5);
			}
			if (retryCount > 4) {
				bagMover.setDepthRange(4, 5);
			}
			if (retryCount > 6) {
				bagMover.setDepthRange(5, 15);
			}
			if (retryCount > 8 && fullChangesSets.isEmpty()) {
				System.out.println("failed to break down:");
				System.out.println("******");
				for (Difference d : currentStates.get(0).getDifferencesList()) {
					System.out.println(d);
				}
				System.out.println("******");
				bagMover.setDepthRange(0, 2);
				return fullChangesSets;
			}
		}
		bagMover.setDepthRange(0, 2);
		if (DEBUG) {
			System.out.printf("addChangeSetLevel ==> Found %d results\n", fullChangesSets.size());
		}
		int stateIndex = 0;
		for (final JobState s : fullChangesSets) {
			if (DEBUG) {
				System.out.println("########" + (stateIndex++) + "########");
				System.out.println("m:" + s.mode);
				System.out.println("pnl:" + s.metricDelta[MetricType.PNL.ordinal()]);
				System.out.println("late:" + s.metricDelta[MetricType.LATENESS.ordinal()]);
				System.out.println(s.changeSetsAsList.get(0).changesList.size());
				System.out.println("diffs:" + s.getDifferencesList().size());
				if (s.mode == JobStateMode.LEAF || s.metricDeltaToBase[MetricType.LATENESS.ordinal()] <= 0) {
					for (final ChangeSet cs : s.changeSetsAsList) {
						System.out.println(String.format("#### CS %s ####", cs.metricDelta[MetricType.PNL.ordinal()]));
						for (final Change c : cs.changesList) {
							System.out.println(c.description);
						}
					}
					System.out.println("-----------------");
				}
			}
		}
		return fullChangesSets;
	}

	protected List<JobState> getSortedLeafStates(final Collection<JobState> finalPopulation) {
		final List<JobState> sortedChangeStates = new ArrayList<>(finalPopulation);
		Collections.sort(sortedChangeStates, new Comparator<JobState>() {

			@Override
			public int compare(final JobState o1, final JobState o2) {
				if (o1.mode == JobStateMode.LEAF && o2.mode != JobStateMode.LEAF) {
					return -1;
				} else if (o1.mode != JobStateMode.LEAF && o2.mode == JobStateMode.LEAF) {
					return 1;
				}
				double prctile = 0.8;
				while (prctile > 0.1 && StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(o1.changeSetsAsList, prctile) == StochasticActionSetUtils
						.getTotalPNLPerChangeForPercentile(o2.changeSetsAsList, prctile)) {
					prctile = prctile - 0.1;
				}
				final double a = StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(o1.changeSetsAsList, prctile);
				final double b = StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(o2.changeSetsAsList, prctile);
				final int compare = Double.compare(a, b);
				return compare * -1;
			}
		});
		return sortedChangeStates;
	}

	private Collection<JobState> getPromisingBranches(final Collection<JobState> fullChangesSets) {
		final Collection<JobState> promising = new LinkedList<>();
		for (final JobState state : fullChangesSets) {
			if (state.mode == JobStateMode.BRANCH) {
				promising.add(state);
			}
		}
		return promising;
	}

	private Collection<JobState> expandNode(final JobState node, final int initialIterations, final SimilarityState targetSimilarityState) {
		List<JobState> states = new LinkedList<JobState>();
		for (int i = 0; i < initialIterations; i++) {
			states.add(new JobState(node));
		}
		try {
			while (!foundLeaf(states) && states.size() != 0) {
				states = addChangeSetLevel(targetSimilarityState, states, 10);
			}
		} catch (final Exception e) {
			assert false;
		}
		return getLeafs(states);
	}

	private List<JobState> getLeafs(final Collection<JobState> states) {
		final List<JobState> leafs = new LinkedList<>();
		for (final JobState job : states) {
			if (job.mode == JobStateMode.LEAF)
				leafs.add(job);
		}
		return leafs;
	}

	private ScheduledSequences evaluateInitialStateAndGetScheduledSequences(final @NonNull ISequences initialFullSequences) {
		final IEvaluationState evaluationState = evaluateAndGetIEvaluationState(initialFullSequences);
		final ScheduledSequences initialScheduledSequences = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);

		// Calculate fitness -- needed to prep initial state correctly.
		fitnessHelper.evaluateSequencesFromComponents(initialFullSequences, evaluationState, fitnessComponents, null);
		fitnessCombiner.calculateFitness(fitnessComponents);

		return initialScheduledSequences;
	}

	private IEvaluationState evaluateAndGetIEvaluationState(final @NonNull ISequences initialFullSequences) {
		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(initialFullSequences, evaluationState)) {
				// We expect the initial solution to be valid....
				assert false;
			}
		}
		return evaluationState;
	}

	protected void printPopulationInfo(final List<JobState> sortedChangeStates) {
		int popIndex = 1;
		for (final JobState s : sortedChangeStates) {
			System.out.println("######## Final " + (popIndex++) + "########");
			System.out.println("m:" + s.mode);
			System.out.println("pnl:" + s.metricDelta[MetricType.PNL.ordinal()]);
			System.out.println("late:" + s.metricDelta[MetricType.LATENESS.ordinal()]);
			System.out.println(s.changeSetsAsList.get(0).changesList.size());
			System.out.println("diffs:" + s.getDifferencesList().size());
			if (s.mode == JobStateMode.LEAF && s.metricDeltaToBase[MetricType.LATENESS.ordinal()] <= 0) {
				for (final ChangeSet cs : s.changeSetsAsList) {
					System.out.println(String.format("#### CS %s ####", cs.metricDelta[MetricType.PNL.ordinal()]));
					for (final Change c : cs.changesList) {
						System.out.println(c.description);
					}
				}
				System.out.println("pnl per change:" + StochasticActionSetUtils.getTotalPNLPerChange(s.changeSetsAsList));
				System.out.println("pnl per change (0.8):" + StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(s.changeSetsAsList, 0.8));
				System.out.println("-----------------");
			}
		}
	}

	private void checkIfCancelled(final IProgressMonitor progressMonitor) {
		if (progressMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}

	private void printJobStates(final Collection<JobState> states) {
		int zz = 0;
		for (final JobState s : states) {
			System.out.println("######## state " + (zz++) + "########");
			System.out.println("m:" + s.mode);
			System.out.println("pnl:" + s.metricDelta[MetricType.PNL.ordinal()]);
			System.out.println("late:" + s.metricDelta[MetricType.LATENESS.ordinal()]);
			System.out.println(s.changeSetsAsList.get(0).changesList.size());
			System.out.println("diffs:" + s.getDifferencesList().size());
			for (final ChangeSet cs : s.changeSetsAsList) {
				System.out.println(String.format("#### CS %s ####", cs.metricDelta[MetricType.PNL.ordinal()]));
				for (final Change c : cs.changesList) {
					System.out.println(c.description);
				}
			}
			System.out.println("-----------------");
		}

	}

	private boolean foundLeaf(final Collection<JobState> states) {
		for (final JobState js : states) {
			if (js.mode == JobStateMode.LEAF) {
				return true;
			}
		}
		return false;
	}

	@NonNull
	List<JobState> findChangeSets(@NonNull final SimilarityState similarityState, final Collection<JobState> currentStates, final int maxStates) throws InterruptedException, ExecutionException {
		final Collection<JobState> states = runJobs(similarityState, currentStates, null);
		List<JobState> reducedStates = reduceAndSortStatesPerChange(states);
		if (DEBUG) {
			int order = 0;
			for (final JobState js : reducedStates) {
				long pnl = 0L;
				long changes = 0L;
				for (final ChangeSet cs : js.changeSetsAsList) {
					pnl += cs.metricDelta[MetricType.PNL.ordinal()];
					changes += cs.changesList.size();
				}
				// System.out.println(String.format("##%s## [%s] / [%s] = %s", ++order, pnl, changes, pnl / changes));
			}
		}
		reducedStates = reducedStates.subList(0, Math.min(reducedStates.size(), maxStates));
		if (DEBUG) {
			System.out.println("Chosen state:");
			for (final JobState js : reducedStates) {
				long pnl = 0;
				long changes = 0;
				for (final ChangeSet cs : js.changeSetsAsList) {
					System.out.println("pnl - " + cs.metricDelta[MetricType.PNL.ordinal()]);
					System.out.println("changes - " + cs.changesList.size());
					pnl += cs.metricDelta[MetricType.PNL.ordinal()];
					changes += cs.changesList.size();
				}
				// System.out.println(String.format("##%s## [%s] / [%s] = %s", 0, pnl, changes, pnl / changes));
			}
		}
		return reducedStates;
	}

	private List<JobState> reduceStates(final Collection<JobState> states) {
		final List<JobState> reducedStates = new LinkedList<>();
		final HashSet<Pair<Long, Long>> seenList = new HashSet<Pair<Long, Long>>();
		for (final JobState js : states) {
			long pnl = 0L;
			long changes = 0L;
			for (final ChangeSet cs : js.changeSetsAsList) {
				pnl += cs.metricDelta[MetricType.PNL.ordinal()];
				changes += cs.changesList.size();
			}
			if (!seenList.contains(new Pair<Long, Long>(pnl, changes))) {
				reducedStates.add(js);
				seenList.add(new Pair<Long, Long>(pnl, changes));
			}
		}
		return reducedStates;
	}

	protected void sortJobStates(@NonNull final Collection<JobState> states, @NonNull final Collection<JobState> leafStates, @Nullable final Collection<JobState> branchStates) {
		for (final JobState state : states) {
			if (state.mode == JobStateMode.LEAF) {
				leafStates.add(state);
			} else if (state.mode == JobStateMode.LIMITED) {
				assert false;
			} else if (state.mode == JobStateMode.BRANCH) {
				if (branchStates != null) {
					branchStates.add(state);
				} else {
					assert false;
				}
			} else {
			}
		}
	}

	List<JobState> reduceAndSortStatesMetric(final Collection<JobState> currentStates) {
		List<JobState> sortedJobStates = reduceStates(currentStates);
		Collections.sort(sortedJobStates, new Comparator<JobState>() {
			@Override
			public int compare(final JobState o1, final JobState o2) {
				return metric(o1, o2);
			}
		});
		return sortedJobStates;
	}

	List<JobState> reduceAndSortStatesPerChange(final Collection<JobState> currentStates) {
		final List<JobState> sortedJobStates = reduceStates(currentStates);
		Collections.sort(sortedJobStates, new Comparator<JobState>() {
			@Override
			public int compare(final JobState o1, final JobState o2) {
				return Double.compare(StochasticActionSetUtils.getTotalPNLPerChange(o1.changeSetsAsList), StochasticActionSetUtils.getTotalPNLPerChange(o2.changeSetsAsList)) * -1;
			}
		});
		return sortedJobStates;
	}

	List<JobState> reduceAndSortStatesTotalPNL(final Collection<JobState> currentStates) {
		final List<JobState> sortedJobStates = reduceStates(currentStates);
		Collections.sort(sortedJobStates, new Comparator<JobState>() {
			@Override
			public int compare(final JobState o1, final JobState o2) {
				return Long.compare(StochasticActionSetUtils.getTotalPNL(o1.changeSetsAsList), StochasticActionSetUtils.getTotalPNL(o2.changeSetsAsList)) * -1;
			}
		});
		return sortedJobStates;
	}

	@NonNull
	protected Collection<JobState> runJobs(@NonNull final SimilarityState similarityState, final Collection<JobState> sortedJobStates, final JobStore jobStore) throws InterruptedException {
		final List<Future<Collection<JobState>>> futures = new LinkedList<>();

		final Iterator<JobState> itr = sortedJobStates.iterator();
		while (itr.hasNext()) {
			final JobState state = itr.next();
			itr.remove();

			// Submit a new change set search to the executor
			futures.add(new MyFuture(new ChangeSetFinderJob(bagMover, state, similarityState, jobStore)));
		}

		final List<JobState> states = new LinkedList<>();
		// Collect all results
		for (final Future<Collection<JobState>> f : futures) {
			try {
				if (!shouldTerminateInRun(actionSetOptimisationData)) {
					final Collection<JobState> futureStates = f.get();
					actionSetOptimisationData.logEvaluations(futureStates.size());
					for (final JobState js : futureStates) {
						if (js.breakdownSearchStatistics != null) {
							actionSetOptimisationData.logConstraintEvaluations(js.breakdownSearchStatistics.getEvaluationsFailedConstraints());
							actionSetOptimisationData.logPNLEvaluations(js.breakdownSearchStatistics.getEvaluationsFailedPNL());
							actionSetOptimisationData.logPNLEvaluations(js.breakdownSearchStatistics.getEvaluationsPassed());
							break;
						}
					}
					states.addAll(removeLimitedStates(futureStates));
					updateProgress(actionSetOptimisationData, maxEvaluations, progressMonitor);
				}
			} catch (final ExecutionException e) {
				throw new RuntimeException(e);
			}
		}

		return states;
	}

	protected void processAndStoreBreakdownSolution(final JobState solution, final ISequences initialRawSequences, final long bestSolutionFitness) {

		List<NonNullPair<ISequences, Map<String, Object>>> processedSolution = new LinkedList<>();
		// Alway add in the original solution
		processedSolution.add(evaluateSolution(initialRawSequences));

		long fitness = Long.MAX_VALUE;
		long lastFitness = Long.MAX_VALUE;
		int bestIdx = -1;
		int idx = 1;
		for (final ChangeSet cs : solution.changeSetsAsList) {
			final NonNullPair<ISequences, Map<String, Object>> ps = evaluateSolution(cs.getRawSequences());
			// Ensure this is called directly after evaluate solution so fitnessComponents are in the correct state
			final long currentFitness = fitnessCombiner.calculateFitness(fitnessComponents);

			if (currentFitness == lastFitness) {
				continue;
			}
			if (currentFitness < fitness) {
				fitness = currentFitness;
				bestIdx = idx;
			}
			lastFitness = currentFitness;

			processedSolution.add(ps);
			idx++;
		}

		// Have we found a better solution than the given input?
		if (fitness < bestSolutionFitness) {
			// Found better solution, skip remaining action sets which reduce overall fitness
			processedSolution = processedSolution.subList(0, bestIdx + 1);
		}
		final IMultiStateResult r = new MultiStateResult(processedSolution.get(processedSolution.size() - 1), processedSolution);
		bestSolutions.add(r);
	}

	private IMultiStateResult getBestSolution() {
		if (bestSolutions.size() > 0) {
			return bestSolutions.get(0);
		} else {
			return null;
		}
	}

	private void updateProgress(final ActionSetOptimisationData actionSetOptimisationData, final int maxEvaluations, final IProgressMonitor progressMonitor) {
		// The percent of weighted actual pnl evaluations + constraint failed evaluations that have completed, based on a given total
		final int progress = (((actionSetOptimisationData.actualPNLEvaluations * 100 + actionSetOptimisationData.actualConstraintEvaluations) * 100) / maxEvaluations);
		while (progress > progressCounter.getTicks()) {
			progressCounter.increment(progressMonitor);
		}
	}

	private boolean shouldTerminate(final ActionSetOptimisationData actionSetOptimisationData) {
		if (actionSetOptimisationData.actualPNLEvaluations * 100 + actionSetOptimisationData.actualConstraintEvaluations > maxEvaluations) {
			return true;
		}
		if (actionSetOptimisationData.getNumberOfLeafs() > maxLeafs) {
			return true;
		}
		return false;
	}

	private boolean shouldTerminateInRun(final ActionSetOptimisationData actionSetOptimisationData) {
		if (ignoreTerminationConditions()) {
			return false;
		}
		if (actionSetOptimisationData.currentRunPNLEvaluations * 100 + actionSetOptimisationData.currentRunConstraintEvaluations > maxEvaluationsInRun) {
			return true;
		}
		if (actionSetOptimisationData.actualPNLEvaluations * 100 + actionSetOptimisationData.actualConstraintEvaluations > maxEvaluations) {
			return true;
		}
		return false;
	}

	private boolean ignoreTerminationConditions() {
		return ignoreTerminationConditions;
	}

	public void setIgnoreTerminationConditions(final boolean ignoreTerminationConditions) {
		this.ignoreTerminationConditions = ignoreTerminationConditions;
	}

	public void setProgressCounter(final ProgressCounter progressCounter) {
		this.progressCounter = progressCounter;
	}

	public void setProgressMonitor(final IProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	public void setActionSetOptimisationData(final ActionSetOptimisationData actionSetOptimisationData) {
		this.actionSetOptimisationData = actionSetOptimisationData;
	}

	public void setMaxLeafs(final int maxLeafs) {
		this.maxLeafs = maxLeafs;
	}

	private Collection<JobState> removeLimitedStates(final Collection<JobState> futureStates) {
		final List<JobState> nonLimitedStates = new LinkedList<>();
		for (final JobState js : futureStates) {
			if (js.mode != JobStateMode.LIMITED) {
				nonLimitedStates.add(js);
			}
		}
		return nonLimitedStates;
	}

	private class ProgressCounter {
		int ticks = 0;

		public void increment(final IProgressMonitor progressMonitor) {
			ticks++;
			progressMonitor.worked(1);
		}

		public int getTicks() {
			return ticks;
		}
	}

	public class ActionSetOptimisationData {
		int totalEvaluations = 0;
		int actualPNLEvaluations = 0;
		int actualConstraintEvaluations = 0;
		int currentRunPNLEvaluations = 0;
		int currentRunConstraintEvaluations = 0;
		int currentRunEvaluations = 0;
		int numberOfLeafs = 0;
		List<Integer> runEvaluations = null;

		public void startNewRun() {
			if (runEvaluations != null) {
				runEvaluations.add(currentRunEvaluations);
				currentRunEvaluations = 0;
				currentRunPNLEvaluations = 0;
				currentRunConstraintEvaluations = 0;
			} else {
				runEvaluations = new LinkedList<>();
			}
		}

		public void logEvaluations(final int evaluations) {
			totalEvaluations += evaluations;
			currentRunEvaluations += evaluations;
			// System.out.println("totalEvaluations:"+totalEvaluations);
			// System.out.println("currentRunEvaluations:"+currentRunEvaluations);
		}

		public void logLeafs(final Collection<JobState> leafs) {
			numberOfLeafs += leafs.size();
		}

		public void logConstraintEvaluations(final int evals) {
			actualConstraintEvaluations += evals;
		}

		public void logPNLEvaluations(final int evals) {
			actualPNLEvaluations += evals;
		}

		public int getTotalEvaluations() {
			return totalEvaluations;
		}

		public int getCurrentRunEvaluations() {
			return currentRunEvaluations;
		}

		public int getNumberOfLeafs() {
			return numberOfLeafs;
		}
	}
}
