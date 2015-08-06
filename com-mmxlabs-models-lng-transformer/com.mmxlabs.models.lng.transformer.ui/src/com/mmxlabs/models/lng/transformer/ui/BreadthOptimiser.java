/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.ui.breakdown.BreakdownOptimiserMover;
import com.mmxlabs.models.lng.transformer.ui.breakdown.Change;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSet;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSetFinderJob;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobState;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStateMode;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStateSerialiser;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStore;
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
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
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
 * change sets to get to a valid solution (note we retain these incase we are not able to find desirable solutions continue searching them by permitting large change sets). It may be that some initial change sets generate less P&L than others. Typically we want the "best" changes sets first, and the least useful change sets
 * last. 
 * 
 * 
 * TODO: (lots!)
 * @formatter:off
 * * Fix gaps 
 *   -- slots optimised in/out
 *   -- vessel events
 *   -- complex cargoes
 *   
 *  * Generate better change instructions (e.g. load swap is also a vessel swap) for the user
 *  * Generate output EMF data model of change descriptions. Include information to reconstruct instructions for end user and possible right-click apply menu. Might also be possible to derive useful information about the reason for the change (e.g. price changes, vessel capacity, heel rollover, triangulation etc)
 *  * Fix multi-threaded usage. (Evaluation process has concurrency issues. Using a thread pool has mysterious memory issues. For some reason all created job states are stored in a thead local store, even after we persist the "LIMITED" states. Heap analysis shows they are one the call stack (labelled as "<Java local>") in some way to the top level worker thread.
 *  * change set analysis
 *    -- Sometimes we see a change set such as: vessel swap, cargo swap, vessel swap. In reality these are two change sets, one with the vessel swaps and one with the cargo swap. We should see these as separate change sets in another search branch. We should be able to detect this and prefer the two over one.
 *    -- We currently generate a single list of changes. As we generate many options, it should be possible to infer which changesets are independent of each other. If the relative order of change sets swap, then they must be independent. If they are always before/after another one, then they must be dependent. We can hopefully generate a change tree rather than change list.
 *    
 *    ** Currently we sort on P&L delta and weed out other options. However this may lead to sub-optimal change paths. The high P&L first change set may not lead to an actionable set of changes later on -- resulting in no final change lists. In a less extreme case, the first high P&L may lead to many low P&L change sets later. Where as it may be possible to generate a lower initial P&L change set and a few higher subsequent changesets after this.
 *    ** Guide search - e.g. if vessel swap, probably need some space, find element we interact with and generate correcting moves for them
 *    ** Currently only tested on a scenario with no lateness and high p&l gains (resulting in ~12 changes). other scenarios might have a lower p&l gain, or even a loss with lateness (or other issue) correction. This needs to be taken into account in the search process. Changeset criteria should include lateness (and other issue) fix as valid state. 
 *    ** Change set creation is minimal - we pick the first valid state. We may want to consider allowing minimal, minimal+1, minimal+2 etc
 *    ** Change set creation is bounded. It might not always be possible to find a valid state within the size limits. We may need to relax this constraint in such cases. E.g. Have a target size, and sort change sets larger than this to below changesets within the target size (even if better P&L etc). We still need an upper bound otherwise this will have a large impact on runtime, particularly for solutions with many changes.
 *    ** Link up to a on/off feature
 *    ** Link up to separate right-click action as separate opt. step.
 *    
 * @formatter:on
 * @author Simon Goodall
 * 
 */
public class BreadthOptimiser {

	@Inject
	private ISequencesManipulator sequencesManipulator;

	@Inject
	private IOptimisationContext optimisationContext;

	@Inject
	private List<IConstraintChecker> constraintCheckers;

	@Inject
	private List<IEvaluationProcess> evaluationProcesses;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private Injector injector;

	@NonNull
	@Inject
	private BreakdownOptimiserMover breakdownOptimiserMover;

	/**
	 * Main entry point, taking a target state, optimised over the injected initial state (from the optimiser context). Generate (in c:\temp\1 -- remember to make the dir!) various instructions for
	 * getting from a to b.
	 * 
	 * TODO: Return a data structure for the best set of instructions and then convert to EMF.
	 * 
	 * @param bestRawSequences
	 */
	public void optimise(@NonNull final ISequences bestRawSequences) {

		final long time1 = System.currentTimeMillis();

		final SimilarityState similarityState = injector.getInstance(SimilarityState.class);

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
			similarityState.init(potentialFullSequences);
		}

		// Prepare initial solution state
		final ISequences initialRawSequences = new ModifiableSequences(optimisationContext.getInitialSequences());

		//// Debugging -- get initial change count
		{
			final int changesCount = breakdownOptimiserMover.getChangedElements(similarityState, initialRawSequences).size();
			System.out.println("Initial changes " + changesCount);
		}

		final IModifiableSequences initialFullSequences = new ModifiableSequences(initialRawSequences);
		sequencesManipulator.manipulate(initialFullSequences);
		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(initialFullSequences, evaluationState)) {
				// We expect the initial solution to be valid....
				assert false;
			}
		}

		final ScheduledSequences initialScheduledSequences = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
		assert initialScheduledSequences != null;

		final long initialPNL = breakdownOptimiserMover.calculateSchedulePNL(initialFullSequences, initialScheduledSequences);
		final long initialLateness = breakdownOptimiserMover.calculateScheduleLateness(initialFullSequences, initialScheduledSequences);
		final long initialCapacity = breakdownOptimiserMover.calculateScheduleCapacity(initialFullSequences, initialScheduledSequences);

		similarityState.baseMetrics[MetricType.LATENESS.ordinal()] = initialLateness;
		similarityState.baseMetrics[MetricType.CAPACITY.ordinal()] = initialCapacity;
		similarityState.baseMetrics[MetricType.PNL.ordinal()] = initialPNL;

		// Generate the initial set of changes, one level deep
		final long time2 = System.currentTimeMillis();

		// This will return a set of job states in the PARTIAL state with a single change in the list.

		final List<JobState> l = new LinkedList<>();
		JobState initialState = new JobState(new Sequences(initialRawSequences), new LinkedList<ChangeSet>(), new LinkedList<Change>());
		initialState.setMetric(MetricType.PNL, initialPNL, 0, 0);
		initialState.setMetric(MetricType.LATENESS, initialLateness, 0, 0);
		initialState.setMetric(MetricType.CAPACITY, initialCapacity, 0, 0);
		l.add(initialState);

		try {
			final Collection<JobState> fullChangesSets = findChangeSets(similarityState, l, 1);
			System.out.printf("Found %d results\n", fullChangesSets.size());

			// TODO: Sort by changset P&L and group size.
			final List<JobState> sortedChangeStates = new ArrayList<>(fullChangesSets);
			Collections.sort(sortedChangeStates, new Comparator<JobState>() {

				@Override
				public int compare(final JobState o1, final JobState o2) {
					final int counter = Math.min(o1.changeSetsAsList.size(), o2.changeSetsAsList.size());
					for (int i = 0; i < counter; ++i) {

						final int c = Long.compare(o2.changeSetsAsList.get(i).metricDelta[MetricType.PNL.ordinal()], o1.changeSetsAsList.get(i).metricDelta[MetricType.PNL.ordinal()]);
						if (c != 0) {
							return c;
						}
					}
					return o1.changeSetsAsList.size() - o2.changeSetsAsList.size();

				}
			});

			// Save results.
			int i = 0;
			for (final JobState jobState : sortedChangeStates) {
				if (jobState.mode == JobStateMode.LEAF) {
					// TODO: Change this method to generate more useful file names e.g. include sort index
					evaluateLeaf(similarityState, jobState.changesAsList, jobState.changeSetsAsList, new ModifiableSequences(jobState.rawSequences));
					// Save top 20 results
					if (++i >= 5) {
						// break;
					}
				}
			}

		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final ExecutionException e) {
			e.printStackTrace();
		}
		final long time3 = System.currentTimeMillis();

		System.out.printf("Setup time %d -- Search time %d\n", (time2 - time1) / 1000L, (time3 - time2) / 1000L);
	}

	// TODO: Consider converting to loop rather than recursive method?
	@NonNull
	public Collection<JobState> findChangeSets(@NonNull final SimilarityState similarityState, final Collection<JobState> currentStates, final int depth)
			throws InterruptedException, ExecutionException {

		System.out.printf("Find change sets %d\n", depth);

		// List of temp files containing persisted LIMITED states.
		final List<File> files = new LinkedList<>();
		List<JobState> branchStates = new LinkedList<>();
		try {
			int persistedLimitedStates = 0;
			final List<JobState> leafStates = new LinkedList<>();
			{
				// Evolve current change set
				final JobStore jobStore = new JobStore(depth);
				System.out.printf("Finding change sets (%d) - page C %d L %d\n", depth, currentStates.size(), persistedLimitedStates);
				final long timeX = System.currentTimeMillis();
				final Collection<JobState> states = runJobs(similarityState, currentStates, jobStore);
				System.out.printf("Run jobs complete -- %d\n", (System.currentTimeMillis() - timeX) / 1000L);
				// Process results by mode.
				sortJobStates(states, leafStates, branchStates);

				// Found some partially completed change sets, persist those in favour of the completed ones.
				files.addAll(jobStore.getFiles());
				persistedLimitedStates += jobStore.getPersistedStateCount();

				System.out.printf("Found change sets (%d) - C %d L %d B %d\n", depth, currentStates.size(), jobStore.getPersistedStateCount(), branchStates.size());
			}
			// Break out early?
			if (!leafStates.isEmpty()) {
				// Found a result! return it
				return leafStates;
			}

			if (branchStates.isEmpty() && persistedLimitedStates > 0) {
				// Unable to find any branches, re-load the persisted limited change sets and continue.
				System.out.printf("No leaf or branch states found (%d), retry extending limited states changeset size\n", depth);

				// Could end up with many limited states, run on limited
				LOOP_LIMITED: while (persistedLimitedStates > 0 && files.size() > 0) {
					// Instead of loading everything in, just load in one file at a time,
					List<JobState> limitedStates = null;
					try {
						final File f = files.remove(0);
						limitedStates = JobStateSerialiser.load(optimisationContext.getOptimisationData(), f);
						// Delete file now it has been reloaded
						f.delete();
						// Update persisted state count
						persistedLimitedStates -= limitedStates.size();
					} catch (final Exception e) {
						throw new RuntimeException(e);
					}
					assert limitedStates != null;
					limitedStates = reduceAndSortStates(limitedStates);

					while (!limitedStates.isEmpty()) {
						System.out.printf("Limited states run (%d) - L %d\n", depth, limitedStates.size());
						final List<JobState> subList = new LinkedList<>();//
						// Run in batches of 100. Smaller number may be quicker, but return a less diverse set of results as we return as soon as we have a leaf result.
						final int limit = Math.min(limitedStates.size(), 100);
						for (int i = 0; i < limit; ++i) {
							subList.add(limitedStates.remove(0));
						}
						final JobStore jobStore = new JobStore(depth);
						final Collection<JobState> states = runJobs(similarityState, subList, jobStore);

						// Found some partially completed change sets, persist those in favour of the completed ones.
						files.addAll(jobStore.getFiles());
						persistedLimitedStates += jobStore.getPersistedStateCount();

						// Sort results into leaves and branches.
						sortJobStates(states, leafStates, branchStates);

						if (!leafStates.isEmpty()) {
							// Found a result, break out early
							return leafStates;
						} else if (!branchStates.isEmpty()) {
							// Found some branch states, break out of the LIMITEDs search
							break LOOP_LIMITED;
						}
					}
				}
			}
		} finally {
			// Remove any left-over limited state dumps
			for (final File f : files) {
				f.delete();
			}
			files.clear();
		}

		// Ok, found some complete change sets, recurse down to the next changeset
		if (!branchStates.isEmpty()) {
			final List<JobState> reducedAndSortedStates = reduceAndSortStates(branchStates);
			// Clean up mem
			branchStates.clear();
			branchStates = null;

			// Run small chunks at a time, to limit amount of memory the returned data set will take up
			while (!reducedAndSortedStates.isEmpty()) {
				final List<JobState> subList = new LinkedList<>();
				// Run up to 20 at once. Note larger sizes may take up more memory with the returned change set count.
				// changesets can be detected more easily
				final int limit = Math.min(reducedAndSortedStates.size(), 6);
				for (int i = 0; i < limit; ++i) {
					subList.add(reducedAndSortedStates.remove(0));
				}
				Collection<JobState> states = findChangeSets(similarityState, subList, depth + 1);
				final List<JobState> leafStates = new LinkedList<>();
				sortJobStates(states, leafStates, null);
				states.clear();
				states = null;

				// Good, results, return them
				if (!leafStates.isEmpty()) {
					return leafStates;
				}
			}
		}
		System.out.printf("No leaf or branch states found (%d), moving back up\n", depth);
		return Collections.emptyList();
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

		// Use set / equals to reduce ...
		// FIXME: The memory consumption when running jobs in a thread pool showed lots of references to a LinkedHashMap$Entry. Possibly (but seems unlikely?) this is the cause. Does the internal
		// LinkedHashSet state get copied into the LinkedList? Why would it?

		int initialSize = currentStates.size();
		final List<JobState> sortedJobStates = new LinkedList<>(new LinkedHashSet<>(currentStates));
		int newSize = sortedJobStates.size();
		System.out.printf("Reduced %d -> %d\n", initialSize, newSize);

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

	@NonNull
	protected Collection<JobState> runJobs(@NonNull final SimilarityState similarityState, final Collection<JobState> sortedJobStates, final JobStore jobStore) throws InterruptedException {
		final List<Future<Collection<JobState>>> futures = new LinkedList<>();
		// final ExecutorService executorService = Executors.newFixedThreadPool(1);

		final Iterator<JobState> itr = sortedJobStates.iterator();
		while (itr.hasNext()) {
			final JobState state = itr.next();
			itr.remove();
			// // Should not really need these...
			if (state.mode == JobStateMode.LEAF) {
				assert false;
				continue;
			}
			if (state.mode == JobStateMode.INVALID) {
				assert false;
				continue;
			}

			// Submit a new change set search to the executor
			// futures.add(executorService.submit(new ChangeSetFinderJob(this, state, elementResourceMap, loadDischargeMap)));

			futures.add(new MyFuture(new ChangeSetFinderJob(breakdownOptimiserMover, state, similarityState, jobStore)));
		}
		// Block until all jobs complete
		// executorService.shutdown();
		// while (!executorService.awaitTermination(1, TimeUnit.SECONDS))
		// ;

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

	private void evaluateLeaf(@NonNull final SimilarityState similarityState, @NonNull final List<Change> changes, @NonNull final List<ChangeSet> changeSets,
			@NonNull final ISequences bestRawSequences) {

		final IModifiableSequences currentFullSequences = new ModifiableSequences(bestRawSequences);
		sequencesManipulator.manipulate(currentFullSequences);

		// boolean different;
		{

			final int changesCount = breakdownOptimiserMover.getChangedElements(similarityState, currentFullSequences).size();

			assert changesCount == 0;
			// Apply hard constraint checkers
			for (final IConstraintChecker checker : constraintCheckers) {
				if (checker.checkConstraints(currentFullSequences) == false) {
					return;
				}
			}

			// TODO: Instead generate a EMF based change description model for presenting to the user.

			// Leaf node.
			// Dump out the change tree
			final String string = UUID.randomUUID().toString();
			final File f = new File(String.format("C:\\temp\\1\\%d - %s.txt", changesCount, string));
			try (FileOutputStream fos = new FileOutputStream(f)) {

				final PrintWriter writer = new PrintWriter(fos);
				for (final ChangeSet changeSet : changeSets) {
					writer.println("==ChangeSet== " + String.format("%,d %,d", changeSet.metricDelta[MetricType.PNL.ordinal()] / 1000L, changeSet.metricDeltaToBase[MetricType.PNL.ordinal()] / 1000L));
					for (final Change change : changeSet.changesList) {
						writer.println(change.description);
					}
				}
				writer.println("==LeftOvers==");
				for (final Change change : changes) {
					writer.println(change.description);
				}
				writer.close();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
