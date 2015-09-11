/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
///**
// * Copyright (C) Minimax Labs Ltd., 2010 - 2015
// * All rights reserved.
// */
//package com.mmxlabs.models.lng.transformer.ui;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//import java.util.UUID;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//
//import org.eclipse.jdt.annotation.NonNull;
//import org.eclipse.jdt.annotation.Nullable;
//
//import com.google.inject.Inject;
//import com.google.inject.Injector;
//import com.mmxlabs.common.Pair;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.BagMover;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.BreakdownOptimiserMover;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.Change;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeChecker;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSet;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSetFinderJob;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.Difference;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.JobState;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStateMode;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStateSerialiser;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStore;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.MyFuture;
//import com.mmxlabs.models.lng.transformer.ui.breakdown.SimilarityState;
//import com.mmxlabs.optimiser.core.IModifiableSequences;
//import com.mmxlabs.optimiser.core.IOptimisationContext;
//import com.mmxlabs.optimiser.core.ISequences;
//import com.mmxlabs.optimiser.core.ISequencesManipulator;
//import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
//import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
//import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
//import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
//import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
//import com.mmxlabs.optimiser.core.impl.Sequences;
//import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
//import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
//import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
//import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
//
///**
// * An "optimiser" to generate the sequence of steps required by a user to go from one {@link ISequences} state to another one. I.e. from a pre-optimised state to an optimised state.
// * 
// * This process generates changes and groups them into change sets. A change is a single change to move closer to the target state. This may be a single slot swap or moving a cargo pair onto the
// * correct vessel. A change set is the minimal set of changes required to pass the constraint checkers and other criteria (such as positive P&L change). A single change may create invalid solutions.
// * Overall we aim to generate a list of change sets to get between the initial and target solutions.
// * 
// * The process is a breadth first search. Given the initial state, create the full set of possible initial changes and generate the first change set. We then recurse to generate the successive change
// * sets until we get to the target state. At each level we prune out undesirable solutions to avoid a fully exhaustive search. Undesirable solutions may be those which require large (and unactionable)
// * change sets to get to a valid solution (note we retain these incase we are not able to find desirable solutions continue searching them by permitting large change sets). It may be that some initial
// * change sets generate less P&L than others. Typically we want the "best" changes sets first, and the least useful change sets last.
// * 
// * @author achurchill/simon
// */
//public class BagOptimiser {
//
//	@Inject
//	private ISequencesManipulator sequencesManipulator;
//
//	@Inject
//	private IOptimisationContext optimisationContext;
//
//	@Inject
//	private List<IConstraintChecker> constraintCheckers;
//
//	@Inject
//	private List<IEvaluationProcess> evaluationProcesses;
//
//	@Inject
//	private IPortTypeProvider portTypeProvider;
//
//	@Inject
//	private Injector injector;
//
//	@NonNull
//	@Inject
//	private BagMover bagMover;
//
//	private final int limitedStateFileLimit = 10000;
//
//	private final Random rdm = new Random(0);
//
//	private List<List<Pair<ISequences, IEvaluationState>>> bestSolutions = new LinkedList<>();
//
//	/**
//	 * Main entry point, taking a target state, optimised over the injected initial state (from the optimiser context). Generate (in c:\temp\1 -- remember to make the dir!) various instructions for
//	 * getting from a to b.
//	 * 
//	 * TODO: Return a data structure for the best set of instructions and then convert to EMF.
//	 * 
//	 * @param bestRawSequences
//	 */
//	public void optimise(@NonNull final ISequences bestRawSequences) {
//
//		final long time1 = System.currentTimeMillis();
//
//		final SimilarityState targetSimilarityState = injector.getInstance(SimilarityState.class);
//
//		// Generate the similarity data structures to the target solution
//		{
//			final IModifiableSequences potentialFullSequences = new ModifiableSequences(bestRawSequences);
//			sequencesManipulator.manipulate(potentialFullSequences);
//
//			final IEvaluationState evaluationState = new EvaluationState();
//			for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
//				if (!evaluationProcess.evaluate(potentialFullSequences, evaluationState)) {
//					// We expect the input solution to be valid....
//					assert false;
//				}
//			}
//			targetSimilarityState.init(potentialFullSequences);
//		}
//
//		// Prepare initial solution state
//		final ISequences initialRawSequences = new ModifiableSequences(optimisationContext.getInitialSequences());
//		final IModifiableSequences initialFullSequences = new ModifiableSequences(initialRawSequences);
//		sequencesManipulator.manipulate(initialFullSequences);
//
//		// // Debugging -- get initial change count
//		{
//			final int changesCount = bagMover.countChanges(targetSimilarityState, initialFullSequences);
//			System.out.println("Initial changes " + changesCount);
//		}
//
//		final IEvaluationState evaluationState = new EvaluationState();
//		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
//			if (!evaluationProcess.evaluate(initialFullSequences, evaluationState)) {
//				// We expect the initial solution to be valid....
//				assert false;
//			}
//		}
//
//		final ScheduledSequences initialScheduledSequences = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
//		assert initialScheduledSequences != null;
//
//		final long initialPNL = bagMover.calculateSchedulePNL(initialFullSequences, initialScheduledSequences);
//		final long initialLateness = bagMover.calculateScheduleLateness(initialFullSequences, initialScheduledSequences);
//
//		// Generate the initial set of changes, one level deep
//		final List<ChangeSet> changeSets = new LinkedList<>();
//		final List<Change> changes = new LinkedList<>();
//		final long time2 = System.currentTimeMillis();
//
//		// This will return a set of job states in the BRANCH state with a single change in the list.
//
//		List<JobState> l = new LinkedList<>();
//		final ChangeChecker changeChecker = injector.getInstance(ChangeChecker.class);
//		changeChecker.init(null, targetSimilarityState, initialFullSequences);
//		for (int i = 0; i < 10000; i++) {
//			l.add(new JobState(new Sequences(initialRawSequences), changeSets, changes, initialPNL, 0, initialLateness, 0, changeChecker.getFullDifferences()));
//		}
//
//		try {
//
//			for (int i = 0; i < 10000; i++) {
//				l.add(new JobState(new Sequences(initialRawSequences), changeSets, changes, initialPNL, 0, initialLateness, 0, changeChecker.getFullDifferences()));
//			}
//			bagMover.setDepthRange(1, 5);
//			Collection<JobState> fullChangesSets = Collections.EMPTY_LIST;
//			Collection<JobState> oldFullChangesSets = Collections.EMPTY_LIST;
//			while (!foundLeaf(l)) {
//				System.out.println("--------------------------------------------------");
//				List<JobState> best = Collections.EMPTY_LIST;
//				fullChangesSets = findChangeSets(targetSimilarityState, l, 1);
//				int retryCount = 0;
//				while (fullChangesSets.size() == 0) {
//					l.clear();
//					best = new LinkedList<JobState>();
//					for (JobState js : oldFullChangesSets) {
//						best.add(js);
//					}
//					for (int i = 0; i < 1000; i++) {
//						l.add(best.get(rdm.nextInt(best.size())));
//					}
//					fullChangesSets = findChangeSets(targetSimilarityState, l, 1);
//					System.out.println("retry:" + (++retryCount));
//					if (retryCount > 5) {
//						bagMover.setDepthRange(5, 9);
//					}
//					if (retryCount > 20) {
//						bagMover.setDepthRange(6, 10);
//					}
//					// break;
//				}
//				bagMover.setDepthRange(1, 6);
//				System.out.printf("Found %d results\n", fullChangesSets.size());
//				int zz = 0;
//				for (JobState s : fullChangesSets) {
//					System.out.println("########" + (zz++) + "########");
//					System.out.println("m:" + s.mode);
//					System.out.println("pnl:" + s.currentPNLDelta);
//					System.out.println("late:" + s.currentLatenessDelta);
//					System.out.println(s.changeSetsAsList.get(0).changesList.size());
//					System.out.println("diffs:" + s.getDifferencesList().size());
//					if (s.mode == JobStateMode.LEAF && s.currentLatenessDelta <= 0) {
//						for (ChangeSet cs : s.changeSetsAsList) {
//							System.out.println(String.format("#### CS %s ####", cs.pnlDelta));
//							for (Change c : cs.changesList) {
//								System.out.println(c.description);
//							}
//						}
//						System.out.println("-----------------");
//					}
//				}
//				best = new LinkedList<JobState>();
//				l.clear();
//				for (JobState js : fullChangesSets) {
//					best.add(js);
//				}
//				for (int i = 0; i < 5000; i++) {
//					l.add(best.get(rdm.nextInt(best.size())));
//				}
//				oldFullChangesSets = fullChangesSets;
//			}
//
//			// TODO: Sort by changset P&L and group size.
//			final List<JobState> sortedChangeStates = new ArrayList<>(fullChangesSets);
//			Collections.sort(sortedChangeStates, new Comparator<JobState>() {
//
//				@Override
//				public int compare(final JobState o1, final JobState o2) {
//					final int counter = Math.min(o1.changeSetsAsList.size(), o2.changeSetsAsList.size());
//					for (int i = 0; i < counter; ++i) {
//
//						final int c = Long.compare(o2.changeSetsAsList.get(i).pnlDelta, o1.changeSetsAsList.get(i).pnlDelta);
//						if (c != 0) {
//							return c;
//						}
//					}
//					return o1.changeSetsAsList.size() - o2.changeSetsAsList.size();
//
//				}
//			});
//			processAndStoreBreakdownSolution(sortedChangeStates.get(0));
//
//		} catch (final InterruptedException e) {
//			e.printStackTrace();
//		} catch (final ExecutionException e) {
//			e.printStackTrace();
//		}
//		final long time3 = System.currentTimeMillis();
//
//		System.out.printf("Setup time %d -- Search time %d\n", (time2 - time1) / 1000L, (time3 - time2) / 1000L);
//	}
//
//	private void processAndStoreBreakdownSolution(JobState solution) {
//		List<Pair<ISequences, IEvaluationState>> processedSolution = new LinkedList<Pair<ISequences, IEvaluationState>>();
//		for (ChangeSet cs : solution.changeSetsAsList) {
//			processedSolution.add(new Pair<ISequences, IEvaluationState>(cs.getFullSequences(), bagMover.evaluateSequence((IModifiableSequences) cs.getFullSequences())));
//		}
//		getBestSolutions().add(processedSolution);
//	}
//
//	private boolean foundLeaf(Collection<JobState> states) {
//		for (JobState js : states) {
//			if (js.mode == JobStateMode.LEAF) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	// TODO: Consider converting to loop rather than recursive method?
//	@NonNull
//	public Collection<JobState> findChangeSets(@NonNull final SimilarityState similarityState, final Collection<JobState> currentStates, final int depth) throws InterruptedException,
//			ExecutionException {
//
//		Collection<JobState> states = runJobs(similarityState, currentStates, null);
//		Collection<JobState> reducedStates = new ArrayList<>();
//		states = reduceAndSortStatesPerChange(states);
//		HashSet<Pair<Long, Long>> seenList = new HashSet<Pair<Long,Long>>();
//		for (JobState js : states) {
//			long pnl = 0L;
//			long changes = 0L;
//			for (ChangeSet cs : js.changeSetsAsList) {
//				pnl += cs.pnlDelta;
//				changes += cs.changesList.size();
//			}
//			if (!seenList.contains(new Pair<Long, Long>(pnl, changes))) {
//				reducedStates.add(js);
//				seenList.add(new Pair<Long, Long>(pnl, changes));
//			}
//		}
//
//		int order = 0;
//		for (JobState js : reducedStates) {
//			long pnl = 0L;
//			long changes = 0L;
//			for (ChangeSet cs : js.changeSetsAsList) {
//				pnl += cs.pnlDelta;
//				changes += cs.changesList.size();
//			}
//			System.out.println(String.format("##%s## [%s] / [%s] = %s", ++order, pnl, changes, pnl/changes));
//		}
//		states.clear();
//		for (JobState js : reducedStates) {
//			states.add(js);
//			if (states.size() >= Math.min(reducedStates.size(), 10)) {
//				break;
//			}
//		}
//		System.out.println("Chosen state:");
//		for (JobState js : states) {
//		long pnl = 0;
//		long changes = 0;
//		for (ChangeSet cs : js.changeSetsAsList) {
//			System.out.println("pnl - "+cs.pnlDelta);
//			System.out.println("changes - "+cs.changesList.size());
//			pnl += cs.pnlDelta;
//			changes += cs.changesList.size();
//		}
//		System.out.println(String.format("##%s## [%s] / [%s] = %s", 0, pnl, changes, pnl/changes));
//		}
//		return states;
//		// return Collections.emptyList();
//	}
//
//	protected void sortJobStates(@NonNull final Collection<JobState> states, @NonNull final Collection<JobState> leafStates, @Nullable Collection<JobState> branchStates) {
//		for (final JobState state : states) {
//			if (state.mode == JobStateMode.LEAF) {
//				leafStates.add(state);
//			} else if (state.mode == JobStateMode.LIMITED) {
//				// limitedStates.add(state);
//				// Should be stored in a JobStore
//				assert false;
//			} else if (state.mode == JobStateMode.BRANCH) {
//				if (branchStates != null) {
//					branchStates.add(state);
//				} else {
//					assert false;
//				}
//			} else {
//				// Invalid, ignore
//			}
//		}
//	}
//
//	/**
//	 * Return a sorted (by PNL Delta) list of the input states collection, removing duplicates (as defined by the equals() methods on Change, ChangeSet and JobState).
//	 * 
//	 * @param currentStates
//	 * @return
//	 */
//	protected List<JobState> reduceAndSortStates(final Collection<JobState> currentStates) {
//		List<JobState> sortedJobStates;
//
//		// Use set / equals to reduce ...
//		// FIXME: The memory consumption when running jobs in a thread pool showed lots of references to a LinkedHashMap$Entry. Possibly (but seems unlikely?) this is the cause. Does the internal
//		// LinkedHashSet state get copied into the LinkedList? Why would it?
//
//		sortedJobStates = new LinkedList<>(currentStates);
//
//		Collections.sort(sortedJobStates, new Comparator<JobState>() {
//
//			@Override
//			public int compare(final JobState o1, final JobState o2) {
//
//				final int counter = Math.min(o1.changeSetsAsList.size(), o2.changeSetsAsList.size());
//				for (int i = 0; i < counter; ++i) {
//
//					final int c = Long.compare(o2.changeSetsAsList.get(i).pnlDelta, o1.changeSetsAsList.get(i).pnlDelta);
//					if (c != 0) {
//						return c;
//					}
//				}
//				return Long.compare(o2.currentPNLDelta, o1.currentPNLDelta);
//			}
//		});
//		return sortedJobStates;
//	}
//
//	private List<JobState> reduceAndSortStatesPerChange(final Collection<JobState> currentStates) {
//		List<JobState> sortedJobStates;
//
//		// Use set / equals to reduce ...
//		// FIXME: The memory consumption when running jobs in a thread pool showed lots of references to a LinkedHashMap$Entry. Possibly (but seems unlikely?) this is the cause. Does the internal
//		// LinkedHashSet state get copied into the LinkedList? Why would it?
//
//		sortedJobStates = new LinkedList<>(currentStates);
//		Set<Pair<Long, Long>> seenList = new HashSet<>(); 
//		Collections.sort(sortedJobStates, new Comparator<JobState>() {
//
//			@Override
//			public int compare(final JobState o1, final JobState o2) {
////				final int counter = Math.min(o1.changeSetsAsList.size(), o2.changeSetsAsList.size());
////				for (int i = 0; i < counter; ++i) {
////					int c = Double.compare((double) o1.changeSetsAsList.get(i).pnlDelta / (double) o1.changeSetsAsList.get(i).changesList.size(), (double) o2.changeSetsAsList.get(i).pnlDelta
////							/ (double) o2.changeSetsAsList.get(i).changesList.size());
////					if (c != 0) {
////						return c * -1;
////					}
////				}
//				JobState[] jobStates = { o1, o2 };
//				double[] totalPnl = { 0, 0 };
//				double[] totalChanges = { 0, 0 };
//				for (int i = 0; i < 2; i++) {
//					JobState o = jobStates[i];
//					for (ChangeSet cs : o.changeSetsAsList) {
//						totalChanges[i] += cs.changesList.size();
//						totalPnl[i] += cs.pnlDelta;
//					}
//				}
//
//				return Double.compare(totalPnl[0] / totalChanges[0], totalPnl[1] / totalChanges[1])*-1;
//			}
//		});
//		return sortedJobStates;
//	}
//
//	@NonNull
//	protected Collection<JobState> runJobs(@NonNull final SimilarityState similarityState, final Collection<JobState> sortedJobStates, final JobStore jobStore) throws InterruptedException {
//		final List<Future<Collection<JobState>>> futures = new LinkedList<>();
//		// final ExecutorService executorService = Executors.newFixedThreadPool(1);
//
//		final Iterator<JobState> itr = sortedJobStates.iterator();
//		while (itr.hasNext()) {
//			final JobState state = itr.next();
//			itr.remove();
//
//			// Submit a new change set search to the executor
//			// futures.add(executorService.submit(new ChangeSetFinderJob(this, state, elementResourceMap, loadDischargeMap)));
//
//			futures.add(new MyFuture(new ChangeSetFinderJob(bagMover, state, similarityState, jobStore)));
//		}
//		// Block until all jobs complete
//		// executorService.shutdown();
//		// while (!executorService.awaitTermination(1, TimeUnit.SECONDS))
//		// ;
//
//		final List<JobState> states = new LinkedList<>();
//		// Collect all results
//		for (final Future<Collection<JobState>> f : futures) {
//			try {
//				states.addAll(f.get());
//			} catch (final ExecutionException e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		return states;
//	}
//
//	public List<List<Pair<ISequences, IEvaluationState>>> getBestSolutions() {
//		return bestSolutions;
//	}
//
//}
