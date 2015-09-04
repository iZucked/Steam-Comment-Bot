/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.stochasticactionsets.StochasticActionSetUtils;
import com.mmxlabs.models.lng.transformer.ui.breakdown.BagMover;
import com.mmxlabs.models.lng.transformer.ui.breakdown.Change;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeChecker;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSet;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSetFinderJob;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobState;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStateMode;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStore;
import com.mmxlabs.models.lng.transformer.ui.breakdown.MetricType;
import com.mmxlabs.models.lng.transformer.ui.breakdown.MyFuture;
import com.mmxlabs.models.lng.transformer.ui.breakdown.SimilarityState;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;

/**
 * An "optimiser" to generate the sequence of steps required by a user to go from one {@link ISequences} state to another one. I.e. from a pre-optimised state to an optimised state.
 * 
 * This process generates changes and groups them into change sets. A change is a single change to move closer to the target state. This may be a single slot swap or moving a cargo pair onto the
 * correct vessel. A change set is the minimal set of changes required to pass the constraint checkers and other criteria (such as positive P&L change). A single change may create invalid solutions.
 * Overall we aim to generate a list of change sets to get between the initial and target solutions.
 * 
 * The process is a breadth first search. Given the initial state, create the full set of possible initial changes and generate the first change set. We then recurse to generate the successive change
 * sets until we get to the target state. At each level we prune out undesirable solutions to avoid a fully exhaustive search. Undesirable solutions may be those which require large (and unactionable)
 * change sets to get to a valid solution (note we retain these incase we are not able to find desirable solutions continue searching them by permitting large change sets). It may be that some initial
 * change sets generate less P&L than others. Typically we want the "best" changes sets first, and the least useful change sets last.
 * 
 * @author achurchill/simon
 */
public class BagOptimiser {

	@Inject
	private ISequencesManipulator sequencesManipulator;

	@Inject
	private IOptimisationContext optimisationContext;

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
	private Injector injector;

	@NonNull
	@Inject
	private BagMover bagMover;

	private static final Logger LOG = LoggerFactory.getLogger(BagOptimiser.class);

	private static final boolean DEBUG = false;

	private final Random rdm = new Random(0);

	private List<List<Pair<ISequences, IEvaluationState>>> bestSolutions = new LinkedList<>();

	private int initialPopulationSize = 10;
	private int initialSearchSize = 20_000;
	private int normalSearchSize = 2_000;
	private int retrySearchSize = 2_000;

	/**
	 * Main entry point, taking a target state, optimised over the injected initial state (from the optimiser context). Generate (in c:\temp\1 -- remember to make the dir!) various instructions for
	 * getting from a to b.
	 * 
	 * TODO: Return a data structure for the best set of instructions and then convert to EMF.
	 * 
	 * @param bestRawSequences
	 */
	public boolean optimise(@NonNull final ISequences bestRawSequences, final IProgressMonitor progressMonitor, int maxLeafs) {

		final long time1 = System.currentTimeMillis();

		final SimilarityState targetSimilarityState = injector.getInstance(SimilarityState.class);
		final long bestFitness;

		// Generate the similarity data structures to the target solution
		{
			final IModifiableSequences potentialFullSequences = new ModifiableSequences(bestRawSequences);
			sequencesManipulator.manipulate(potentialFullSequences);

			final IEvaluationState evaluationState = new EvaluationState();
			for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
				if (!evaluationProcess.evaluate(potentialFullSequences, evaluationState)) {
					// We expect the input solution to be valid....
					assert false;
				}
			}
			targetSimilarityState.init(potentialFullSequences);

			fitnessHelper.evaluateSequencesFromComponents(potentialFullSequences, evaluationState, fitnessComponents, null);
			bestFitness = fitnessCombiner.calculateFitness(fitnessComponents);

		}

		try {
			// Prepare initial solution state
			final ISequences initialRawSequences = new ModifiableSequences(optimisationContext.getInitialSequences());
			final IModifiableSequences initialFullSequences = new ModifiableSequences(initialRawSequences);
			sequencesManipulator.manipulate(initialFullSequences);

			// // Debugging -- get initial change count
			{
				final int changesCount = bagMover.getChangedElements(targetSimilarityState, initialRawSequences).size();
				System.out.println("Initial changes " + changesCount);
			}

			final IEvaluationState evaluationState = new EvaluationState();
			for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
				if (!evaluationProcess.evaluate(initialFullSequences, evaluationState)) {
					// We expect the initial solution to be valid....
					assert false;
				}
			}

			final ScheduledSequences initialScheduledSequences = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
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

			// This will return a set of job states in the BRANCH state with a single change in the list.

			List<JobState> l = new LinkedList<>();
			final ChangeChecker changeChecker = injector.getInstance(ChangeChecker.class);
			changeChecker.init(null, targetSimilarityState, initialFullSequences);
			for (int i = 0; i < initialSearchSize; i++) {
				final JobState job = new JobState(new Sequences(initialRawSequences), changeSets, changes, changeChecker.getFullDifferences());
				job.setMetric(MetricType.PNL, initialPNL, 0, 0);
				job.setMetric(MetricType.LATENESS, initialLateness, 0, 0);
				job.setMetric(MetricType.CAPACITY, initialCapacity, 0, 0);
				job.setMetric(MetricType.COMPULSARY_SLOT, initialUnusedCompulsarySlot, 0, 0);
				l.add(job);
			}

			boolean betterSolutionFound = false;
			try {
				bagMover.setDepthRange(0, 3);
				Collection<JobState> fullChangesSets = Collections.EMPTY_LIST;
				Collection<JobState> oldFullChangesSets = Collections.EMPTY_LIST;
				Collection<JobState> initialPopulation = addChangeSetLevel(targetSimilarityState, l, changeChecker, oldFullChangesSets, initialPopulationSize);

				if (DEBUG) {
					System.out.println("initial pop");
					printJobStates(initialPopulation);
				}

				Collection<JobState> finalPopulation = new LinkedList<>();
				Collection<JobState> limitedStates = new LinkedList<>();
				progressMonitor.beginTask("Generate changes", initialPopulation.size());
				progressMonitor.worked(1);
				for (JobState root : initialPopulation) {
					if (root.mode == JobStateMode.LEAF) {
						finalPopulation.add(root);
					} else {
						fullChangesSets = null;
						List<JobState> states = new LinkedList<JobState>();
						List<JobState> leafStates = new LinkedList<JobState>();
						for (int i = 0; i < normalSearchSize; i++) {
							states.add(new JobState(root));
						}
						oldFullChangesSets = new LinkedList<>(states);
						int zz = 0;
						while (!foundLeaf(states) && !(fullChangesSets != null && fullChangesSets.size() == 0)) {
							if (DEBUG) {
								System.out.println("--------------------------- root ----------------- " + (zz++));
							}
							fullChangesSets = addChangeSetLevel(targetSimilarityState, states, changeChecker, oldFullChangesSets, 10);
							if (fullChangesSets.isEmpty()) {
								int iii = 0;
							}
							oldFullChangesSets = fullChangesSets;
						}

						// TODO: make more efficient
						if (foundLeaf(fullChangesSets)) {
							limitedStates.addAll(getPromisingBranches(fullChangesSets));
						}

						finalPopulation.addAll(getLeafs(fullChangesSets));
						progressMonitor.worked(1);
						if (finalPopulation.size() > maxLeafs) {
							break;
						}
					}
				}

				if (finalPopulation.size() < maxLeafs) {
					for (JobState promising : limitedStates) {
						finalPopulation.addAll(expandNode(promising, 250, targetSimilarityState, changeChecker));
					}
				}

				// TODO: Sort by changset P&L and group size.
				final List<JobState> sortedChangeStates = new ArrayList<>(finalPopulation);
				Collections.sort(sortedChangeStates, new Comparator<JobState>() {

					@Override
					public int compare(final JobState o1, final JobState o2) {
						if (o1.mode == JobStateMode.LEAF && o2.mode != JobStateMode.LEAF) {
							return -1;
						} else if (o1.mode != JobStateMode.LEAF && o2.mode == JobStateMode.LEAF) {
							return 1;
						}
						double pnlPC1 = StochasticActionSetUtils.getTotalPNLPerChange(o1.changeSetsAsList);
						double pnlPC2 = StochasticActionSetUtils.getTotalPNLPerChange(o2.changeSetsAsList);
						if (pnlPC1 != pnlPC2) {
							return Double.compare(pnlPC1, pnlPC2);
						} else {
							return Double.compare(StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(o1.changeSetsAsList, 0.75),
									StochasticActionSetUtils.getTotalPNLPerChangeForPercentile(o2.changeSetsAsList, 0.5));
						}
					}
				});
				if (DEBUG) {
					int popIndex = 1;
					for (JobState s : finalPopulation) {
						System.out.println("######## Final " + (popIndex++) + "########");
						System.out.println("m:" + s.mode);
						System.out.println("pnl:" + s.metricDelta[MetricType.PNL.ordinal()]);
						System.out.println("late:" + s.metricDelta[MetricType.LATENESS.ordinal()]);
						System.out.println(s.changeSetsAsList.get(0).changesList.size());
						System.out.println("diffs:" + s.getDifferencesList().size());
						if (s.mode == JobStateMode.LEAF && s.metricDeltaToBase[MetricType.LATENESS.ordinal()] <= 0) {
							for (ChangeSet cs : s.changeSetsAsList) {
								System.out.println(String.format("#### CS %s ####", cs.metricDelta[MetricType.PNL.ordinal()]));
								for (Change c : cs.changesList) {
									System.out.println(c.description);
								}
							}
							System.out.println("-----------------");
						}
					}
				}
				if (!sortedChangeStates.isEmpty()) {
					betterSolutionFound = processAndStoreBreakdownSolution(sortedChangeStates.get(0), initialFullSequences, evaluationState, bestFitness);
				}
				if (sortedChangeStates.isEmpty()) {
					LOG.error("Unable to find action sets");
				}

			} catch (final InterruptedException e) {
				e.printStackTrace();
			} catch (final ExecutionException e) {
				e.printStackTrace();
			}
			final long time3 = System.currentTimeMillis();

			System.out.printf("Setup time %d -- Search time %d\n", (time2 - time1) / 1000L, (time3 - time2) / 1000L);
			return betterSolutionFound;
		} finally {
			progressMonitor.done();
		}
	}

	private Collection<JobState> getPromisingBranches(Collection<JobState> fullChangesSets) {
		Collection<JobState> promising = new LinkedList<>();
		for (JobState state : fullChangesSets) {
			if (state.mode == JobStateMode.BRANCH) {
				promising.add(state);
			}
		}
		return promising;
	}

	private Collection<JobState> expandNode(JobState node, int initialIterations, SimilarityState targetSimilarityState, ChangeChecker changeChecker) {
		Collection<JobState> fullChangesSets = null;
		List<JobState> states = new LinkedList<JobState>();
		for (int i = 0; i < initialIterations; i++) {
			states.add(new JobState(node));
		}
		try {
			Collection<JobState> oldFullChangesSets = new LinkedList<>(states);
			while (!foundLeaf(states) || !(fullChangesSets != null && fullChangesSets.size() != 0)) {
				fullChangesSets = addChangeSetLevel(targetSimilarityState, states, changeChecker, oldFullChangesSets, 10);
				oldFullChangesSets = fullChangesSets;
			}
		} catch (Exception e) {
			assert false;
		}
		return fullChangesSets == null ? Collections.<JobState> emptyList() : getLeafs(fullChangesSets);
	}

	private List<JobState> getLeafs(Collection<JobState> states) {
		List<JobState> leafs = new LinkedList<>();
		for (JobState job : states) {
			if (job.mode == JobStateMode.LEAF)
				leafs.add(job);
		}
		return leafs;
	}

	private void printJobStates(Collection<JobState> states) {
		int zz = 0;
		for (JobState s : states) {
			System.out.println("######## state " + (zz++) + "########");
			System.out.println("m:" + s.mode);
			System.out.println("pnl:" + s.metricDelta[MetricType.PNL.ordinal()]);
			System.out.println("late:" + s.metricDelta[MetricType.LATENESS.ordinal()]);
			System.out.println(s.changeSetsAsList.get(0).changesList.size());
			System.out.println("diffs:" + s.getDifferencesList().size());
			for (ChangeSet cs : s.changeSetsAsList) {
				System.out.println(String.format("#### CS %s ####", cs.metricDelta[MetricType.PNL.ordinal()]));
				for (Change c : cs.changesList) {
					System.out.println(c.description);
				}
			}
			System.out.println("-----------------");
		}

	}

	private Collection<JobState> addChangeSetLevel(final SimilarityState targetSimilarityState, List<JobState> l, final ChangeChecker changeChecker, Collection<JobState> oldFullChangesSets,
			int maxStates) throws InterruptedException, ExecutionException {
		System.out.println("in add changeSetLevel");
		Collection<JobState> fullChangesSets;
		List<JobState> best = Collections.EMPTY_LIST;
		fullChangesSets = findChangeSets(targetSimilarityState, l, 1, maxStates);
		int retryCount = 0;
		System.out.println(l == oldFullChangesSets);
		while (fullChangesSets.size() == 0) {
			l.clear();
			best = new LinkedList<JobState>();
			for (JobState js : oldFullChangesSets) {
				best.add(new JobState(js));
			}
			for (int i = 0; i < retrySearchSize; i++) {
				l.add(new JobState(best.get(rdm.nextInt(best.size()))));
			}
			fullChangesSets = findChangeSets(targetSimilarityState, l, 1, maxStates);
			System.out.println("retry:" + (++retryCount));

			if (retryCount > 3) {
				bagMover.setDepthRange(1, 3);
			}
			if (retryCount > 6) {
				bagMover.setDepthRange(3, 4);
			}
			if (retryCount > 9) {
				bagMover.setDepthRange(4, 5);
			}
			if (retryCount > 12) {
				bagMover.setDepthRange(5, 6);
			}
			if (retryCount > 15) {
				bagMover.setDepthRange(6, 7);
			}
			if (retryCount > 18) {
				bagMover.setDepthRange(7, 15);
			}
			if (retryCount > 30 && fullChangesSets.isEmpty()) {
				bagMover.setDepthRange(0, 4);
				return fullChangesSets;
			}
			// break;
		}
		bagMover.setDepthRange(0, 2);
		System.out.printf("Found %d results\n", fullChangesSets.size());
		int zz = 0;
		for (JobState s : fullChangesSets) {
			System.out.println("########" + (zz++) + "########");
			System.out.println("m:" + s.mode);
			System.out.println("pnl:" + s.metricDelta[MetricType.PNL.ordinal()]);
			System.out.println("late:" + s.metricDelta[MetricType.LATENESS.ordinal()]);
			System.out.println(s.changeSetsAsList.get(0).changesList.size());
			System.out.println("diffs:" + s.getDifferencesList().size());
			if (s.mode == JobStateMode.LEAF || s.metricDeltaToBase[MetricType.LATENESS.ordinal()] <= 0) {
				for (ChangeSet cs : s.changeSetsAsList) {
					System.out.println(String.format("#### CS %s ####", cs.metricDelta[MetricType.PNL.ordinal()]));
					for (Change c : cs.changesList) {
						System.out.println(c.description);
					}
				}
				System.out.println("-----------------");
			}
		}
		best = new LinkedList<JobState>();
		l.clear();
		for (JobState js : fullChangesSets) {
			best.add(new JobState(js));
		}
		for (int i = 0; i < normalSearchSize; i++) {
			l.add(new JobState(best.get(rdm.nextInt(best.size()))));
		}
		int i = 1;
		return fullChangesSets;
	}

	private boolean foundLeaf(Collection<JobState> states) {
		for (JobState js : states) {
			if (js.mode == JobStateMode.LEAF) {
				return true;
			}
		}
		return false;
	}

	// TODO: Consider converting to loop rather than recursive method?
	@NonNull
	public Collection<JobState> findChangeSets(@NonNull final SimilarityState similarityState, final Collection<JobState> currentStates, final int depth, final int maxStates)
			throws InterruptedException, ExecutionException {

		Collection<JobState> states = runJobs(similarityState, currentStates, null);
		Collection<JobState> reducedStates = new ArrayList<>();
		states = reduceAndSortStatesPerChange(states);
		HashSet<Pair<Long, Long>> seenList = new HashSet<Pair<Long, Long>>();
		for (JobState js : states) {
			long pnl = 0L;
			long changes = 0L;
			for (ChangeSet cs : js.changeSetsAsList) {
				pnl += cs.metricDelta[MetricType.PNL.ordinal()];
				changes += cs.changesList.size();
			}
			if (!seenList.contains(new Pair<Long, Long>(pnl, changes))) {
				reducedStates.add(js);
				seenList.add(new Pair<Long, Long>(pnl, changes));
			} else {
			}
		}

		int order = 0;
		for (JobState js : reducedStates) {
			long pnl = 0L;
			long changes = 0L;
			for (ChangeSet cs : js.changeSetsAsList) {
				pnl += cs.metricDelta[MetricType.PNL.ordinal()];
				changes += cs.changesList.size();
			}
			System.out.println(String.format("##%s## [%s] / [%s] = %s", ++order, pnl, changes, pnl / changes));
		}
		states.clear();
		for (JobState js : reducedStates) {
			states.add(js);
			if (states.size() >= Math.min(reducedStates.size(), maxStates)) {
				break;
			}
		}
		System.out.println("Chosen state:");
		for (JobState js : states) {
			long pnl = 0;
			long changes = 0;
			for (ChangeSet cs : js.changeSetsAsList) {
				System.out.println("pnl - " + cs.metricDelta[MetricType.PNL.ordinal()]);
				System.out.println("changes - " + cs.changesList.size());
				pnl += cs.metricDelta[MetricType.PNL.ordinal()];
				changes += cs.changesList.size();
			}
			System.out.println(String.format("##%s## [%s] / [%s] = %s", 0, pnl, changes, pnl / changes));
		}
		return states;
	}

	protected void sortJobStates(@NonNull final Collection<JobState> states, @NonNull final Collection<JobState> leafStates, @Nullable Collection<JobState> branchStates) {
		for (final JobState state : states) {
			if (state.mode == JobStateMode.LEAF) {
				leafStates.add(state);
			} else if (state.mode == JobStateMode.LIMITED) {
				// limitedStates.add(state);
				// Should be stored in a JobStore
				assert false;
			} else if (state.mode == JobStateMode.BRANCH) {
				if (branchStates != null) {
					branchStates.add(state);
				} else {
					assert false;
				}
			} else {
				// Invalid, ignore
			}
		}
	}

	/**
	 * Return a sorted (by PNL Delta) list of the input states collection, removing duplicates (as defined by the equals() methods on Change, ChangeSet and JobState).
	 * 
	 * @param currentStates
	 * @return
	 */
	protected List<JobState> reduceAndSortStates(final Collection<JobState> currentStates) {
		List<JobState> sortedJobStates;

		// Use set / equals to reduce ...
		// FIXME: The memory consumption when running jobs in a thread pool showed lots of references to a LinkedHashMap$Entry. Possibly (but seems unlikely?) this is the cause. Does the internal
		// LinkedHashSet state get copied into the LinkedList? Why would it?

		sortedJobStates = new LinkedList<>(currentStates);

		Collections.sort(sortedJobStates, new Comparator<JobState>() {

			@Override
			public int compare(final JobState o1, final JobState o2) {

				final int counter = Math.min(o1.changeSetsAsList.size(), o2.changeSetsAsList.size());
				for (int i = 0; i < counter; ++i) {

					final int c = Long.compare(o2.changeSetsAsList.get(i).metricDelta[MetricType.PNL.ordinal()], o1.changeSetsAsList.get(i).metricDelta[MetricType.PNL.ordinal()]);
					if (c != 0) {
						return c;
					}
				}
				return Long.compare(o2.metricDelta[MetricType.PNL.ordinal()], o1.metricDelta[MetricType.PNL.ordinal()]);
			}
		});
		return sortedJobStates;
	}

	private List<JobState> reduceAndSortStatesPerChange(final Collection<JobState> currentStates) {
		List<JobState> sortedJobStates;

		// Use set / equals to reduce ...
		// FIXME: The memory consumption when running jobs in a thread pool showed lots of references to a LinkedHashMap$Entry. Possibly (but seems unlikely?) this is the cause. Does the internal
		// LinkedHashSet state get copied into the LinkedList? Why would it?

		sortedJobStates = new LinkedList<>(currentStates);
		Collections.sort(sortedJobStates, new Comparator<JobState>() {
			@Override
			public int compare(final JobState o1, final JobState o2) {
				return Double.compare(StochasticActionSetUtils.getTotalPNLPerChange(o1.changeSetsAsList), StochasticActionSetUtils.getTotalPNLPerChange(o2.changeSetsAsList)) * -1;
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
				states.addAll(f.get());
			} catch (final ExecutionException e) {
				throw new RuntimeException(e);
			}
		}

		return states;
	}

	protected boolean processAndStoreBreakdownSolution(final JobState solution, final IModifiableSequences initialFullSequences, final IEvaluationState evaluationState, final long bestSolutionFitness) {
		final List<Pair<ISequences, IEvaluationState>> processedSolution = new LinkedList<Pair<ISequences, IEvaluationState>>();

		processedSolution.add(new Pair<ISequences, IEvaluationState>(initialFullSequences, evaluationState));

		long fitness = Long.MAX_VALUE;
		long lastFitness = Long.MAX_VALUE;
		int bestIdx = -1;
		int idx = 1;
		for (final ChangeSet cs : solution.changeSetsAsList) {
			final IModifiableSequences currentFullSequences = new ModifiableSequences(cs.getRawSequences());
			sequencesManipulator.manipulate(currentFullSequences);

			final IEvaluationState changeSetEvaluationState = bagMover.evaluateSequence(currentFullSequences);
			fitnessHelper.evaluateSequencesFromComponents(currentFullSequences, changeSetEvaluationState, fitnessComponents, null);
			final long currentFitness = fitnessCombiner.calculateFitness(fitnessComponents);

			if (currentFitness == lastFitness) {
				continue;
			}
			if (currentFitness < fitness) {
				fitness = currentFitness;
				bestIdx = idx;
			}
			lastFitness = currentFitness;
			processedSolution.add(new Pair<ISequences, IEvaluationState>(currentFullSequences, changeSetEvaluationState));
			idx++;
		}

		// Have we found a better solution?
		if (fitness < bestSolutionFitness) {
			bestSolutions.add(processedSolution.subList(0, bestIdx + 1));
			return true;
		} else {
			bestSolutions.add(processedSolution);
			return false;
		}

	}

	public List<Pair<ISequences, IEvaluationState>> getBestSolution() {
		if (bestSolutions.size() > 0) {
			return bestSolutions.get(0);
		} else {
			return null;
		}
	}

}
