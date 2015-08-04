/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
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
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStateSerialiser;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobStore;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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

	public static class SimilarityState {

		@Inject
		private IPortTypeProvider portTypeProvider;

		private final Map<Integer, Integer> elementResourceMap = new HashMap<>();
		private final Map<Integer, Integer> loadDischargeMap = new HashMap<>();
		private final Map<Integer, Integer> dischargeLoadMap = new HashMap<>();
		private final Map<Integer, ISequenceElement> elementMap = new HashMap<>();
		// private final Map<ISequenceElement, IResource> elementResourceMap = new HashMap<>();
		// private final Map<ISequenceElement, ISequenceElement> loadDischargeMap = new HashMap<>();
		// private final Map<ISequenceElement, ISequenceElement> dischargeLoadMap = new HashMap<>();

		// public SimilarityState();

		public void init(@NonNull final ISequences fullSequences) {
			for (final IResource resource : fullSequences.getResources()) {
				final ISequence sequence = fullSequences.getSequence(resource.getIndex());
				ISequenceElement prev = null;
				for (final ISequenceElement current : sequence) {
					if (elementMap.put(current.getIndex(), current) != null) {
						assert false;
					}
					if (prev != null) {
						if (portTypeProvider.getPortType(prev) == PortType.Load) {
							if (portTypeProvider.getPortType(current) == PortType.Discharge) {
								loadDischargeMap.put(prev.getIndex(), current.getIndex());
								dischargeLoadMap.put(current.getIndex(), prev.getIndex());
								// } else {
								// loadDischargeMap.put(prev.getIndex(), null);
							}
						}
					}
					elementResourceMap.put(current.getIndex(), resource.getIndex());
					prev = current;
				}
			}
			for (final ISequenceElement current : fullSequences.getUnusedElements()) {
				if (elementMap.put(current.getIndex(), current) != null) {
					assert false;
				}
			}
		}

		public Integer getLoadForDischarge(@NonNull final ISequenceElement discharge) {
			return dischargeLoadMap.get(discharge.getIndex());
		}

		public Integer getDischargeForLoad(@NonNull final ISequenceElement load) {
			return loadDischargeMap.get(load.getIndex());
		}

		public Integer getResourceForElement(@NonNull final ISequenceElement element) {
			return elementResourceMap.get(element.getIndex());
		}

		public ISequenceElement getElementForIndex(final int index) {
			return elementMap.get(index);
		}
	}

	/**
	 * A class that could be passed into an ExecutorService to attempt to find change sets from the current state.
	 * 
	 * @author Simon Goodall
	 *
	 */
	private static final class ChangeSetFinderJob implements Callable<Collection<JobState>> {
		private final JobState state;
		private final SimilarityState similarityState;
		private final BreadthOptimiser optimiser;
		private final JobStore jobStore;

		private ChangeSetFinderJob(final BreadthOptimiser optimiser, final JobState state, final SimilarityState similarityState, final JobStore jobStore) {
			this.optimiser = optimiser;
			this.state = state;
			this.similarityState = similarityState;
			this.jobStore = jobStore;
		}

		@Override
		public Collection<JobState> call() {
			try {
				// Perform a recursive search to find the next change set.
				final int localDepth = state.mode == JobStateMode.LIMITED ? 2 : (state.changesAsList.size() == 0 && state.changeSetsAsList.size() == 0) ? 5 : DEPTH_START;
				return optimiser.search(new Sequences(state.rawSequences), similarityState, new LinkedList<Change>(state.changesAsList), new LinkedList<ChangeSet>(state.changeSetsAsList), localDepth,
						MOVE_TYPE_NONE, state.currentPNL, state.currentLateness, jobStore);
			} catch (final Throwable e) {
				// Catch issue rather than abort entire search. (Although this should really be debugged).
				assert false;
				return Collections.emptyList();
			}

		}
	}

	/**
	 * Individual change TODO: Add in detailed information to reproduce the change
	 *
	 */
	public static class Change implements Serializable {
		String description;

		public Change(final String s) {
			this.description = s;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof Change) {
				return Objects.equal(description, ((Change) obj).description);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return description.hashCode();
		}
	}

	/**
	 * A set of changes that can be actioned together to get to a new state.
	 * 
	 * @author sg
	 *
	 */
	public static class ChangeSet implements Serializable {
		final List<Change> changesList;
		final Set<Change> changesSet;
		public long pnlDelta;
		public long latenessDelta;
		final int hashCode;

		public ChangeSet(final Collection<Change> changes) {
			this.changesList = Collections.unmodifiableList(new ArrayList<>(changes));
			// Use hash sets as we do not care about ordering
			this.changesSet = Collections.unmodifiableSet(new HashSet<>(changes));
			this.hashCode = changesSet.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof ChangeSet) {
				// Use hash sets as we do not care about ordering
				return Objects.equal(changesSet, new HashSet<>(((ChangeSet) obj).changesSet));
			}
			return false;
		}

		@Override
		public int hashCode() {
			return hashCode;
		}
	}

	/**
	 * The current state of a {@link ChangeSet}.
	 * 
	 * @author sg
	 *
	 */
	private enum JobStateMode implements Serializable {
		/**
		 * Default state, either we have a complete change set - or we have just started (changes.size() == 0, unless first level of recursion)
		 */
		PARTIAL,

		/**
		 * Completed job state. we have a full list of changesets to get to the target solution. Do not expect anything in the changes list
		 */
		LEAF,

		/**
		 * A partial list of changes which does not get to a valid state -- but could do if we allow a larger changeset size.
		 */
		LIMITED,

		/**
		 * We have somehow evolved to a state where we cannot make any further changes, but also we have not reached our target state
		 */
		INVALID
	}

	/**
	 * The current state of a particular track of search. This contains the list of changesets and the current working set of changes. It also include the current sequences state and P&L/lateness
	 * total and last changes set deltas (if available).
	 * 
	 * @author sg
	 *
	 */
	public static class JobState implements Serializable {
		public transient ISequences rawSequences;
		public int[][] persistedSequences;

		final List<Change> changesAsList;
		final Set<Change> changesAsSet;
		final List<ChangeSet> changeSetsAsList;
		final Set<ChangeSet> changeSetsAsSet;
		final int hashCode;
		long currentPNL;
		long currentPNLDelta;
		JobStateMode mode = JobStateMode.PARTIAL;
		long currentLateness;
		long currentLatenessDelta;

		public JobState(final ISequences rawSequences, final List<ChangeSet> changeSets, final List<Change> changes, final long currentPNL, final long currentPNLDelta, final long currentLatness,
				final long currentLatenessDelta) {
			this.rawSequences = rawSequences;
			this.currentPNL = currentPNL;
			this.currentPNLDelta = currentPNLDelta;
			this.currentLateness = currentLatness;
			this.currentLatenessDelta = currentLatenessDelta;

			this.changesAsList = Collections.unmodifiableList(new ArrayList<>(changes));
			this.changesAsSet = Collections.unmodifiableSet(new HashSet<>(changes));
			this.changeSetsAsList = Collections.unmodifiableList(new ArrayList<>(changeSets));
			this.changeSetsAsSet = Collections.unmodifiableSet(new HashSet<>(changeSets));
			this.hashCode = Objects.hashCode(changes, changeSets);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof JobState) {
				return changesAsSet.equals(((JobState) obj).changesAsSet) && changeSetsAsSet.equals(((JobState) obj).changeSetsAsSet) && Objects.equal(rawSequences, ((JobState) obj).rawSequences);
			}
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		private void writeObject(final java.io.ObjectOutputStream out) throws IOException {

			// We cannot persist the rawSequences as this is linked to external data.
			// However we can store the representation as an int array and re-create the sequences with reference to a IOptimisationData instance.
			persistedSequences = new int[rawSequences.getResources().size()][];
			for (int i = 0; i < persistedSequences.length; ++i) {
				final ISequence s = rawSequences.getSequence(i);
				persistedSequences[i] = new int[s.size()];
				for (int j = 0; j < persistedSequences[i].length; ++j) {
					persistedSequences[i][j] = s.get(j).getIndex();
				}
			}

			out.defaultWriteObject();

		}

		private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {

			in.defaultReadObject();

			// Do nothing with the raw sequence as we do not have the information here to generate it from the int array.
			// @see JobStateSerialiser
		}

		// private void readObjectNoData() throws ObjectStreamException;
	}

	private static final int DEPTH_START = -1;
	private static final int DEPTH_SINGLE_CHANGE = -2;
	private static final int DEPTH_SINGLE_CHANGE_FOUND = -3;

	// Move types are for information only
	private static final int MOVE_TYPE_NONE = 0;
	private static final int MOVE_TYPE_VESSEL_SWAP = 1;
	private static final int MOVE_TYPE_LOAD_SWAP = 2;
	private static final int MOVE_TYPE_DISCHARGE_SWAP = 3;
	private static final int MOVE_TYPE_CARGO_REMOVE = 4;
	private static final int MOVE_TYPE_CARGO_INSERT = 5;

	/**
	 * The size of the change sets. This is really n+1 changesets as 0 is also valid. Additionally note the first changeset can be +1 again due to the way we create the initial change set size.
	 * 
	 * TODO: Instead of try depth in the recursive method parameter, check the changes list size. (last attempt got stuck in a recursive loop for some reason, but may have been other bugs rather than
	 * directly from tryDepth == change.size().
	 */
	private final int TRY_DEPTH = 2;

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
	private IPortSlotProvider portSlotProvider;

	@Inject
	private Injector injector;

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
		final IModifiableSequences initialFullSequences = new ModifiableSequences(initialRawSequences);
		sequencesManipulator.manipulate(initialFullSequences);

		//// Debugging -- get initial change count
		{
			final int changesCount = countChanges(similarityState, initialFullSequences);
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

		final long initialPNL = calculateSchedulePNL(initialFullSequences, initialScheduledSequences);
		final long initialLateness = calculateScheduleLateness(initialFullSequences, initialScheduledSequences);

		// Generate the initial set of changes, one level deep
		final List<ChangeSet> changeSets = new LinkedList<>();
		final List<Change> changes = new LinkedList<>();
		final long time2 = System.currentTimeMillis();

		// This will return a set of job states in the PARTIAL state with a single change in the list.

		final List<JobState> l = new LinkedList<>();
		l.add(new JobState(new Sequences(initialRawSequences), changeSets, changes, initialPNL, 0, initialLateness, 0));

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

						final int c = Long.compare(o2.changeSetsAsList.get(i).pnlDelta, o1.changeSetsAsList.get(i).pnlDelta);
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
					evaluateLeaf(similarityState, jobState.changesAsList, jobState.changeSetsAsList, jobState.currentPNL, new ModifiableSequences(jobState.rawSequences));
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

	protected long calculateSchedulePNL(@NonNull final IModifiableSequences fullSequences, @NonNull final ScheduledSequences scheduledSequences) {
		long sumPNL = 0;

		for (final ScheduledSequence scheduledSequence : scheduledSequences) {
			for (final Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> p : scheduledSequence.getVoyagePlans()) {
				sumPNL += scheduledSequences.getVoyagePlanGroupValue(p.getFirst());
			}
			for (final ISequenceElement element : fullSequences.getUnusedElements()) {
				final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
				assert portSlot != null;
				sumPNL += scheduledSequences.getUnusedSlotGroupValue(portSlot);
			}
		}
		return sumPNL;
	}

	protected long calculateScheduleLateness(final IModifiableSequences fullSequences, final ScheduledSequences scheduledSequences) {
		long sumCost = 0;

		for (final IPortSlot lateSlot : scheduledSequences.getLateSlotsSet()) {
			sumCost += scheduledSequences.getLatenessCost(lateSlot).getSecond();
		}
		return sumCost;

	}

	// TODO: Consider converting to loop rather than recursive method?
	public Collection<JobState> findChangeSets(@NonNull final SimilarityState similarityState, final Collection<JobState> currentStates, final int depth)
			throws InterruptedException, ExecutionException {

		System.out.printf("Find change sets %d\n", depth);

		// List of temp files containing persisted LIMITED states.
		final List<File> files = new LinkedList<>();

		int persistedLimitedStates = 0;
		try {
			{
				{
					final List<JobState> leafStates = new LinkedList<>();
					List<JobState> branchStates = new LinkedList<>();
					{ // Evolve current change set
						final JobStore jobStore = new JobStore(depth);
						final Collection<JobState> nextStates;
						{
							System.out.printf("Finding change sets (%d) - page C %d L %d\n", depth, currentStates.size(), persistedLimitedStates);
							final long timeX = System.currentTimeMillis();
							nextStates = runJobs(similarityState, currentStates, jobStore);
							System.out.printf("Run jobs complete -- %d\n", (System.currentTimeMillis() - timeX) / 1000L);
						}

						// Process results by mode.
						for (final JobState state : nextStates) {
							if (state.mode == JobStateMode.LEAF) {
								leafStates.add(state);
							} else if (state.mode == JobStateMode.LIMITED) {
								// Now persisted in JobStore
								assert false;
							} else if (state.mode == JobStateMode.PARTIAL) {
								branchStates.add(state);
							} else {
								// Invalid, ignore
							}
						}

						// Found some partially completed change sets, persist those in favour of the completed ones.
						files.addAll(jobStore.getFiles());
						persistedLimitedStates += jobStore.getPersistedStateCount();

						System.out.printf("Found change sets (%d) - C %d L %d B %d\n", depth, currentStates.size(), jobStore.getPersistedStateCount(), branchStates.size());
					}
					if (!leafStates.isEmpty()) {
						// Found a result! return it
						return leafStates;
					}

					// Ok, found some complete change sets, continue finding the next set
					if (!branchStates.isEmpty()) {
						// Recurse on the branch states
						{
							// TODO: Only run on the top x states (iff we have found some leaf results already)?
							final List<JobState> reducedAndSortedStates = reduceAndSortStates(branchStates);
							// Clean up mem
							branchStates.clear();
							branchStates = null;// new LinkedList<>();
							// Run small chunks at a time, to limit amount of memory the returned data set will take up
							while (!reducedAndSortedStates.isEmpty()) {
								final List<JobState> subList = new LinkedList<>();//
								// Run up to 20 at once. Note larger sizes may take up more memory with the returned change set count.
								// changesets can be detected more easily
								final int limit = Math.min(reducedAndSortedStates.size(), 6);
								for (int i = 0; i < limit; ++i) {
									subList.add(reducedAndSortedStates.remove(0));
								}
								Collection<JobState> states = findChangeSets(similarityState, subList, depth + 1);
								// FIXME: Potentially we may get results where there are left over changes which cannot be fixed by the moves (e.g. within vessel swap is currently disabled, unused
								// slots are currently unsupported, vessel events currently unsupported). I.e. not a leaf position, not an invalid position. Possible a Partial/branch position.
								// Possible a limited state also.
								for (final JobState state : states) {
									if (state.mode == JobStateMode.LEAF) {
										leafStates.add(state);
									} else if (state.mode == JobStateMode.LIMITED) {
										assert false;
										// limitedStates.add(state);
									} else if (state.mode == JobStateMode.PARTIAL) {
										assert false;

										// branchStates.add(state);
									} else {
										// Invalid , ignore
									}
								}
								states.clear();
								states = null;

								if (depth > 0) {

									// Good, results, return them
									if (!leafStates.isEmpty()) {
										return leafStates;
									}
								}
							}
							// Good, results, return them
							if (!leafStates.isEmpty()) {
								return leafStates;
							}
						}
					}

					// TODO: is the below still a valid condition in other scenarios? What if we can never find a solution?

					// // No results, but still more to try. Persist current limited state set.
					// // FIXME: This is mixing up partial changesets of this level with partial changeset in the lower level.
					// // FIXME: In pratice the lower levels should not be found?
					// if (!sortedJobStates.isEmpty() && !limitedStates.isEmpty()) {
					// try {
					// File f = File.createTempFile("breadth-lower-", ".dat");
					// // JobStateSerialiser.save(optimisationContext.getOptimisationData(), limitedStates, f);
					// JobStateSerialiser.save(limitedStates, f);
					// persistedLimitedStates += limitedStates.size();
					// limitedStates.clear();
					// limitedStates = new LinkedList<>();
					// files.add(f);
					// } catch (Exception e) {
					// throw new RuntimeException(e);
					// }
					//
					// }
				}
			}
			// Unable to find anything good, re-load the persisted limited change sets and continue.
			if (persistedLimitedStates > 0) {
				System.out.printf("No leaf or branch states found (%d), retry extending limited states changeset size\n", depth);
			}
			// Could end up with many limited states, run on limited
			while (persistedLimitedStates > 0 && files.size() > 0) {// || !limitedStates.isEmpty()) {
				// Instead of loading everything in, just load in one file at a time,
				List<JobState> limitedStates = null;
				try {
					final File f = files.remove(0);
					limitedStates = JobStateSerialiser.load(optimisationContext.getOptimisationData(), f);
					f.delete();
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
					Collection<JobState> states = findChangeSets(similarityState, subList, depth);
					// limitedStates.clear();
					final List<JobState> leafStates = new LinkedList<>();

					for (final JobState state : states) {
						if (state.mode == JobStateMode.LEAF) {
							leafStates.add(state);
						} else if (state.mode == JobStateMode.LIMITED) {
							// limitedStates.add(state);
							assert false;
						} else if (state.mode == JobStateMode.PARTIAL) {
							assert false;
							// branchStates.add(state);
						} else {
							// Invalid , ignore
						}
					}
					states.clear();
					states = null;
					// System.out.printf("Paged change sets %d - %d %d %d\n", depth, sortedJobStates.size(), limitedStates.size(), branchStates.size());

					if (!leafStates.isEmpty()) {
						return leafStates;
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
		System.out.printf("No leaf or branch states found (%d), moving back up\n", depth);
		return Collections.emptyList();
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

					final int c = Long.compare(o2.changeSetsAsList.get(i).pnlDelta, o1.changeSetsAsList.get(i).pnlDelta);
					if (c != 0) {
						return c;
					}
				}
				return Long.compare(o2.currentPNLDelta, o1.currentPNLDelta);
			}
		});
		return sortedJobStates;
	}

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

			futures.add(new MyFuture(new ChangeSetFinderJob(this, state, similarityState, jobStore)));
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

	static class MyFuture implements Future<Collection<JobState>>

	{

		private final ChangeSetFinderJob job;

		public MyFuture(final ChangeSetFinderJob job) {
			this.job = job;

		}

		@Override
		public boolean cancel(final boolean mayInterruptIfRunning) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isCancelled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Collection<JobState> get() throws InterruptedException, ExecutionException {
			// TODO Auto-generated method stub
			return job.call();
		}

		@Override
		public Collection<JobState> get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public Collection<JobState> search(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final int moveType, final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {
		@NonNull
		final List<JobState> newStates = new LinkedList<>();

		int totalChanges = changes.size();
		for (final ChangeSet cs : changeSets) {
			totalChanges += cs.changesList.size();
		}
		if (totalChanges > 11) {
			// System.out.println(totalChanges);
		}

		final IModifiableSequences currentFullSequences = new ModifiableSequences(currentSequences);
		sequencesManipulator.manipulate(currentFullSequences);

		// Sanity check -- elements only used once.
		{
			final Set<ISequenceElement> unique = new HashSet<>();
			for (final IResource resource : currentSequences.getResources()) {
				final ISequence sequence = currentSequences.getSequence(resource.getIndex());
				for (final ISequenceElement current : sequence) {
					assert unique.add(current);
				}
			}
		}

		if (tryDepth >= 0) {
			//

			boolean failedEvaluation = false;

			// Apply hard constraint checkers
			for (final IConstraintChecker checker : constraintCheckers) {
				if (checker.checkConstraints(currentFullSequences) == false) {
					// Break out
					failedEvaluation = true;

					// String moveTypeStr = "";
					// switch (moveType) {
					// case 0:
					// moveTypeStr = "Initial";
					// break;
					// case 1:
					// moveTypeStr = "Vessel";
					// break;
					// case 2:
					// moveTypeStr = "Load";
					// break;
					// case 3:
					// moveTypeStr = "Discharge";
					// break;
					// }
					// System.out.printf("Failed eval after move type %s -- %s\n", moveTypeStr, checker.getName());

					break;
				}
			}

			if (!failedEvaluation) {
				long thisPNL = 0;
				long thisLateness = 0;
				if (true) {
					final IEvaluationState evaluationState = new EvaluationState();
					for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
						if (!evaluationProcess.evaluate(currentFullSequences, evaluationState)) {

							failedEvaluation = true;
							break;
						}
					}

					final ScheduledSequences ss = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
					assert ss != null;
					thisPNL = calculateSchedulePNL(currentFullSequences, ss);

					if (thisPNL <= currentPNL) {
						// failedEvaluation = true;
					} else {
						// currentPNL = thisPNL;
					}

					thisLateness = calculateScheduleLateness(currentFullSequences, ss);
					if (thisLateness > currentLateness) {
						failedEvaluation = true;
					} else {
						final int ii = 0;
						// currentLateness = thisLateness;
					}
				}
				if (!failedEvaluation) {
					// Convert change list set into a change set and record sate.
					// TOOD: Get fitness change and only accept improving solutions. (similarity, similarity plus others etc)
					final ChangeSet cs = new ChangeSet(changes);

					cs.pnlDelta = thisPNL - currentPNL;
					cs.latenessDelta = thisLateness - currentLateness;
					changes.clear();
					changeSets.add(cs);

					newStates.add(new JobState(new Sequences(currentSequences), changeSets, new LinkedList<Change>(), thisPNL, thisPNL - currentPNL, thisLateness, thisLateness - currentLateness));

					// tryDepth = -1;
					return newStates;

				}
			}
			if (failedEvaluation) {
				// if (changes.size() == TRY_DEPTH) {
				// return newStates;
				// }
				if (tryDepth == 0) { // changes.size() == TRY_DEPTH) {
					final JobState s = new JobState(new Sequences(currentSequences), changeSets, new LinkedList<Change>(changes), currentPNL, 0, currentLateness, 0);
					s.mode = JobStateMode.LIMITED;

					jobStore.store(s);

					// newStates.add(s);

					return newStates;
				}
			}
		}

		int differenceCount = 0;
		boolean different = false;
		for (final IResource resource : currentFullSequences.getResources()) {
			final ISequence sequence = currentFullSequences.getSequence(resource.getIndex());
			ISequenceElement prev = null;
			for (final ISequenceElement current : sequence) {
				if (prev != null) {
					// Currently only looking at LD style cargoes
					if (portTypeProvider.getPortType(prev) == PortType.Load && portTypeProvider.getPortType(current) == PortType.Discharge) {
						// TODO Add to count changes
						// Wiring Change
						boolean wiringChange = false;
						final Integer matchedDischarge = similarityState.getDischargeForLoad(prev);
						final Integer matchedLoad = similarityState.getLoadForDischarge(current);
						if (matchedDischarge == null && matchedLoad == null) {
							// Neither slot appears in target solution.
							different = true;
							wiringChange = true;

							final IModifiableSequences copy = new ModifiableSequences(currentSequences);
							final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
							currentResource.remove(prev);
							currentResource.remove(current);
							copy.getUnusedElements().add(prev);
							copy.getUnusedElements().add(current);

							final int depth = getNextDepth(tryDepth);
							final List<Change> changes2 = new ArrayList<>(changes);
							changes2.add(new Change(String.format("Remove %s and %s\n", prev.getName(), current.getName())));
							newStates.addAll(search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentPNL, currentLateness, jobStore));

						} else if (matchedLoad == null) {

							// TODO Add to count changes
							// Discharge was previous unused, but the load was
							different = true;
							wiringChange = true;

							// FIXME: Currently just unpair both slots and remove from solution
							final IModifiableSequences copy = new ModifiableSequences(currentSequences);
							final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
							currentResource.remove(prev);
							currentResource.remove(current);
							copy.getUnusedElements().add(prev);
							copy.getUnusedElements().add(current);

							final int depth = getNextDepth(tryDepth);
							final List<Change> changes2 = new ArrayList<>(changes);
							changes2.add(new Change(String.format("Remove %s and %s\n", prev.getName(), current.getName())));
							newStates.addAll(search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentPNL, currentLateness, jobStore));

						} else if (matchedDischarge == null) {

							// TODO Add to count changes
							// Load was previous unused, but the discharge was
							different = true;
							wiringChange = true;

							// FIXME: Currently just unpair both slots and remove from solution
							final IModifiableSequences copy = new ModifiableSequences(currentSequences);
							final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());
							currentResource.remove(prev);
							currentResource.remove(current);
							copy.getUnusedElements().add(prev);
							copy.getUnusedElements().add(current);

							final int depth = getNextDepth(tryDepth);
							final List<Change> changes2 = new ArrayList<>(changes);
							changes2.add(new Change(String.format("Remove %s and %s\n", prev.getName(), current.getName())));
							newStates.addAll(search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentPNL, currentLateness, jobStore));

						} else if (matchedDischarge != current.getIndex()) {
							different = true;
							wiringChange = true;
							differenceCount++;
							// Has the load moved vessel?
							if (similarityState.getResourceForElement(prev).intValue() != resource.getIndex()) {
								// Hash the discharge moved vessel?
								if (similarityState.getResourceForElement(current).intValue() != resource.getIndex()) {
									// Both load and discharge have moved

									// Search option 1, swap in original load for this discharge
									{
										// TODO: Remember DES purchases do not move
										newStates.addAll(swapLoad(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore));
									}
									// Case 2: Keep the load and swap in the original discharge
									{
										// TODO: Remember FOB Sales do not move
										newStates.addAll(
												swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore));
									}
								} else {
									// Just the load has moved.
									newStates.addAll(swapLoad(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore));
								}
							} else {
								// Load has stayed put, discharge must have moved.
								// Discharge stayed put
								newStates.addAll(swapDischarge(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentPNL, currentLateness, jobStore));
							}
						}

						// Vessel Change
						if (!wiringChange) {
							assert prev != null;
							if (similarityState.getResourceForElement(prev) == null || similarityState.getResourceForElement(prev).intValue() != resource.getIndex()) {
								different = true;
								differenceCount++;
								// Current Cargo load and discharge can move as a pair.
								if (similarityState.getResourceForElement(prev).equals(similarityState.getResourceForElement(current))) {
									// Find insertion point
									final ISequence originalResource = currentSequences.getSequence(similarityState.getResourceForElement(prev));
									final ISequence currentResource = currentSequences.getSequence(resource.getIndex());

									// TODO: Create and apply change.
									for (int i = 0; i < currentResource.size(); ++i) {
										if (currentResource.get(i) == prev) {

											// Iterate over all possible positions and try it. Note we really could do with original index information to reduce the quantity of options generated. This
											// can get very long and quickly explodes the search space.
											for (int j = 1; j < originalResource.size(); ++j) {
												if (portTypeProvider.getPortType(originalResource.get(j)) != PortType.Discharge) {

													newStates.addAll(swapVessel(currentSequences, similarityState, changes, changeSets, tryDepth, currentPNL, currentLateness, resource, prev, current,
															j, jobStore));
												}
											}

											break;
										}
									}
								} else {
									// Part of a wiring change.
									// ^^^ above
								}
							}
						}

					}
				}
				prev = current;
			}
		}

		// TODO Add to count changes

		for (final ISequenceElement element : currentFullSequences.getUnusedElements()) {
			// Currently unused element needs to be placed onto a resource
			if (similarityState.getResourceForElement(element) != null) {
				// Unused element which should be in the final solution.
				if (portTypeProvider.getPortType(element) == PortType.Load) {
					final Integer otherDischargeIdx = similarityState.getDischargeForLoad(element);
					final ISequenceElement discharge = similarityState.getElementForIndex(otherDischargeIdx);

					different = true;

					if (currentSequences.getUnusedElements().contains(discharge)) {
						final ISequence originalResource = currentSequences.getSequence(similarityState.getResourceForElement(element));
						for (int i = 0; i < originalResource.size(); ++i) {
							if (portTypeProvider.getPortType(originalResource.get(i)) != PortType.Discharge) {
								final IModifiableSequences copy = new ModifiableSequences(currentSequences);
								final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceForElement(element));
								modifiableSequence.insert(i, discharge);
								modifiableSequence.insert(i, element);

								// Iterate over all possible positions and try it. Note we really could do with original index information to reduce the quantity of options generated. This
								//
								final int depth = getNextDepth(tryDepth);
								final List<Change> changes2 = new ArrayList<>(changes);
								changes2.add(new Change(String.format("Insert cargo %s -> %s on %s\n", element.getName(), discharge.getName(),
										copy.getResources().get(similarityState.getResourceForElement(element)).getName())));

								newStates.addAll(search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_CARGO_INSERT, currentPNL, currentLateness, jobStore));
							}
						}
					}

				} else if (portTypeProvider.getPortType(element) == PortType.Discharge) {
					final Integer otherLoadIdx = similarityState.getLoadForDischarge(element);

					different = true;

					// If we get here, the load is also unused
					final ISequenceElement load = similarityState.getElementForIndex(otherLoadIdx);
					if (currentSequences.getUnusedElements().contains(load)) {
						final ISequence originalResource = currentSequences.getSequence(similarityState.getResourceForElement(element));
						for (int i = 0; i < originalResource.size(); ++i) {
							if (portTypeProvider.getPortType(originalResource.get(i)) != PortType.Discharge) {
								final IModifiableSequences copy = new ModifiableSequences(currentSequences);
								final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceForElement(element));
								modifiableSequence.insert(i, element);
								modifiableSequence.insert(i, load);

								// Iterate over all possible positions and try it. Note we really could do with original index information to reduce the quantity of options generated. This
								//
								final int depth = getNextDepth(tryDepth);
								final List<Change> changes2 = new ArrayList<>(changes);
								changes2.add(new Change(String.format("Insert cargo %s -> %s on %s\n", load.getName(), element.getName(),
										copy.getResources().get(similarityState.getResourceForElement(element)).getName())));

								newStates.addAll(search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_CARGO_INSERT, currentPNL, currentLateness, jobStore));
							}
						}
					} else {

						// Load already exists

					}

				} else {
					// assume vessel event?
				}
			}
		}

		// FIXME: Also include alternative slots

		if (different) {
			// Still some (hopefully) correctable changes.
			return newStates;
		} else {

			// End of the line, nothing more we can do. Have we got to a valid state?
			// We should have as we have no more changes. However the change count does not cover everything
			// Exclusion include: Correct vessel, but incorrect position, any vessel event changes, open slot positions.
			// Spurious changes: Same spot market & month, different instance.

			for (final IConstraintChecker checker : constraintCheckers) {
				if (checker.checkConstraints(currentFullSequences) == false) {
					// Break out -- could get here with bad vessel swap position.
					return newStates;
				}
			}

			final IEvaluationState evaluationState = new EvaluationState();
			for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
				if (!evaluationProcess.evaluate(currentFullSequences, evaluationState)) {
					// Ok, don't really expect to get here..
					return newStates;
				}
			}

			// ... valid state, so we are in a leaf position.

			final ScheduledSequences ss = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
			assert ss != null;
			final long thisPNL = calculateSchedulePNL(currentFullSequences, ss);
			final long thisLateness = calculateScheduleLateness(currentFullSequences, ss);

			final ChangeSet cs = new ChangeSet(changes);

			cs.pnlDelta = thisPNL - currentPNL;
			cs.latenessDelta = thisLateness - currentLateness;

			final ArrayList<ChangeSet> copiedChangeSets = new ArrayList<>(changeSets);
			copiedChangeSets.add(cs);

			final JobState leafJobState = new JobState(new Sequences(currentSequences), copiedChangeSets, new LinkedList<Change>(), thisPNL, thisPNL - currentPNL, thisLateness,
					thisLateness - currentLateness);

			// evaluateLeaf(loadDischargeMap, elementResourceMap, leafJobState.changesAsList, leafJobState.changeSetsAsList, leafJobState.currentPNL, new
			// ModifiableSequences(leafJobState.rawSequences));

			leafJobState.mode = JobStateMode.LEAF;
			return Collections.singleton(leafJobState);
		}

	}

	private void evaluateLeaf(@NonNull final SimilarityState similarityState, @NonNull final List<Change> changes, @NonNull final List<ChangeSet> changeSets, final long currentPNL,
			@NonNull final ISequences bestRawSequences) {

		final IModifiableSequences currentFullSequences = new ModifiableSequences(bestRawSequences);
		sequencesManipulator.manipulate(currentFullSequences);

		// boolean different;
		{

			final int changesCount = countChanges(similarityState, currentFullSequences);

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
					writer.println("==ChangeSet== " + String.format("%,d", changeSet.pnlDelta / 1000L));
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

	protected int countChanges(final SimilarityState similarityState, final ISequences fullSequences) {
		int changesCount = 0;
		for (final IResource resource : fullSequences.getResources()) {
			final ISequence sequence = fullSequences.getSequence(resource.getIndex());
			ISequenceElement prev = null;
			for (final ISequenceElement current : sequence) {
				if (prev != null) {
					if (portTypeProvider.getPortType(prev) == PortType.Load) {

						// Wiring Change
						boolean wiringChange = false;
						final Integer matchedDischarge = similarityState.getDischargeForLoad(prev);
						if (matchedDischarge == null && portTypeProvider.getPortType(current) == PortType.Discharge) {

							// Not previously linked to a load
							// different = true;
							wiringChange = true;
							changesCount++;
							// TODO: Implement search path to add or remove slots with the unused slot list
						} else if (matchedDischarge != current.getIndex()) {
							changesCount++;
						}

						// Vessel Change
						if (!wiringChange) {
							assert prev != null;
							if (similarityState.getResourceForElement(prev) == null || similarityState.getResourceForElement(prev).intValue() != resource.getIndex()) {
								// different = true;
								// Current Cargo load and discharge can move as a pair.
								if (similarityState.getResourceForElement(prev).equals(similarityState.getResourceForElement(current))) {
									changesCount++;
								}
							}
						}

					}
				}
				prev = current;
			}
		}
		return changesCount;
	}

	protected Collection<JobState> swapVessel(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final long currentPNL, final long currentLateness, @NonNull final IResource resource, @NonNull final ISequenceElement prev,
			@NonNull final ISequenceElement current, final int j, @NonNull final JobStore jobStore) {
		{

			if (similarityState.getResourceForElement(prev).equals(resource.getIndex())) {
				assert false;
			}

			final IModifiableSequences copy = new ModifiableSequences(currentSequences);
			final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceForElement(prev));
			modifiableSequence.insert(j, current);
			modifiableSequence.insert(j, prev);
			copy.getModifiableSequence(resource.getIndex()).remove(prev);
			copy.getModifiableSequence(resource.getIndex()).remove(current);

			final int depth = getNextDepth(tryDepth);
			final List<Change> changes2 = new ArrayList<>(changes);
			changes2.add(new Change(String.format("Vessel %s from %s to %s\n", prev.getName(), resource.getName(), copy.getResources().get(similarityState.getResourceForElement(prev)).getName())));

			if (copy.equals(currentSequences)) {
				// FIXME: Why do we get here?
				// return Collections.emptyList();
			}

			return search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_VESSEL_SWAP, currentPNL, currentLateness, jobStore);
		}
	}

	/**
	 * Given a load and discharge, assume the discharge is on the correct resource. Find the correct load for the discharge and swap it with the current load.
	 */
	protected Collection<JobState> swapLoad(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {

		if (!(portSlotProvider.getPortSlot(prev) instanceof ILoadSlot)) {
			return Collections.emptyList();
		}

		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());

		// Find the original load index
		final Integer originalLoadIdx = similarityState.getLoadForDischarge(current);
		assert originalLoadIdx != null;
		boolean swapped = false;

		// Find the original load element and swap with current load element
		ISequenceElement originalLoad = null;
		IResource otherResource = null;
		LOOP: for (final IResource r : copy.getResources()) {
			final IModifiableSequence s = copy.getModifiableSequence(r);

			for (int j = 0; j < s.size(); ++j) {
				if (s.get(j).getIndex() == originalLoadIdx.intValue()) {
					originalLoad = s.get(j);
					if (!(portSlotProvider.getPortSlot(originalLoad) instanceof ILoadSlot)) {
						return Collections.emptyList();
					}
					otherResource = r;
					s.set(j, prev);
					swapped = true;
					break LOOP;
				}
			}
		}
		assert otherResource != null;
		assert originalLoad != null;
		assert swapped;

		swapped = false;
		// Swap the current load element with the original one.
		for (int j = 0; j < currentResource.size(); ++j) {
			if (currentResource.get(j) == prev) {
				currentResource.set(j, originalLoad);
				swapped = true;
				break;
			}
		}

		// FIXME: GEtting a solution where we swap loads within a resource. Seems like discharge has not moved. However, my debuggin attempts do not show the load on this resource....
		if (copy.equals(currentSequences)) {
			// assert false;
		}
		if (resource.getIndex() == otherResource.getIndex()) {
			return Collections.emptyList();
		}

		assert swapped;
		assert prev != null;
		final int depth = getNextDepth(tryDepth);
		final List<Change> changes2 = new ArrayList<>(changes);
		changes2.add(new Change(String.format("Swap %s onto %s with %s onto %s\n", prev.getName(), otherResource.getName(), originalLoad.getName(), resource.getName())));
		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_LOAD_SWAP, currentPNL, currentLateness, jobStore);
	}

	/**
	 * Given a load and discharge, assume the load is on the correct resource. Find the correct discharge for the load and swap it with the current discharge.
	 */
	protected Collection<JobState> swapDischarge(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long currentPNL, final long currentLateness, @NonNull final JobStore jobStore) {

		if (!(portSlotProvider.getPortSlot(current) instanceof IDischargeSlot)) {
			return Collections.emptyList();
		}

		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource.getIndex());

		// Find the
		final int originalDischargeIdx = similarityState.getDischargeForLoad(prev);

		boolean swapped = false;
		ISequenceElement originalDischarge = null;
		ISequenceElement otherLoad = null;
		IResource otherResource = null;

		LOOP: for (final IResource r : copy.getResources()) {
			assert r != null;
			final IModifiableSequence s = copy.getModifiableSequence(r);

			for (int j = 0; j < s.size(); ++j) {
				if (s.get(j).getIndex() == originalDischargeIdx) {
					originalDischarge = s.get(j);
					if (!(portSlotProvider.getPortSlot(originalDischarge) instanceof IDischargeSlot)) {
						return Collections.emptyList();
					}
					otherLoad = s.get(j - 1);
					otherResource = r;
					s.set(j, current);
					swapped = true;
					break LOOP;
				}
			}
		}
		assert swapped;
		swapped = false;
		assert originalDischarge != null;
		assert otherLoad != null;
		assert otherResource != null;
		for (int j = 0; j < currentResource.size(); ++j) {
			if (currentResource.get(j) == current) {
				currentResource.set(j, originalDischarge);
				swapped = true;
				break;

			}
		}
		if (copy.equals(currentSequences)) {
			// assert false;
		}
		if (resource.getIndex() == otherResource.getIndex()) {
			return Collections.emptyList();
		}

		assert swapped;
		final int depth = getNextDepth(tryDepth);
		final List<Change> changes2 = new ArrayList<>(changes);
		changes2.add(new Change(String.format("Swap %s (to %s) with %s ( to %s)\n", current.getName(), otherLoad.getName(), originalDischarge.getName(), prev.getName())));
		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_DISCHARGE_SWAP, currentPNL, currentLateness, jobStore);
	}

	protected int getNextDepth(final int tryDepth) {

		if (tryDepth == DEPTH_SINGLE_CHANGE) {
			return DEPTH_SINGLE_CHANGE_FOUND;
		}
		return tryDepth == DEPTH_START ? TRY_DEPTH : tryDepth - 1;
	}

}
