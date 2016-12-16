/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeChecker.DifferenceType;
import com.mmxlabs.models.lng.transformer.ui.breakdown.chain.LNGParameters_ActionPlanSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.DischargeRewireChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.InsertUnusedCargoChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.LoadRewireChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.RemoveCargoChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.UnusedToUsedDischargeChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.UnusedToUsedLoadChange;
import com.mmxlabs.models.lng.transformer.ui.breakdown.independence.VesselChange;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class BagMover {

	private static final boolean DEBUG_VALIDATION = false;

	public static final int DEPTH_START = -1;

	// Move types are for information only
	public static final int MOVE_TYPE_NONE = 0;
	private static final int MOVE_TYPE_VESSEL_SWAP = 1;
	private static final int MOVE_TYPE_LOAD_SWAP = 2;
	private static final int MOVE_TYPE_DISCHARGE_SWAP = 3;
	private static final int MOVE_TYPE_CARGO_REMOVE = 4;
	private static final int MOVE_TYPE_CARGO_INSERT = 5;
	private static final int MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED = 6;
	private static final int MOVE_TYPE_UNUSED_LOAD_SWAPPED = 7;

	private static final int RECURSION_LIMIT = 20;

	private int depthStart = 1;
	private int depthEnd = 8;

	@Inject
	@NonNull
	private Injector injector;

	@Inject
	@NonNull
	private IResourceAllocationConstraintDataComponentProvider resourceAllocationProvider;

	@Inject
	@NonNull
	private ISequencesManipulator sequencesManipulator;

	@Inject
	@NonNull
	private IPortTypeProvider portTypeProvider;

	@Inject
	@NonNull
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	private ActionSetEvaluationHelper evaluationHelper;

	@Inject
	@Named(LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE)
	private boolean isReevaluating;

	@Inject
	@Named(LNGParameters_ActionPlanSettingsModule.ACTION_PLAN_MAX_SEARCH_DEPTH)
	private int MAX_SEARCH_STATES;

	
	public Collection<JobState> search(@NonNull final ISequences currentRawSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final int moveType, final long[] currentMetrics, @NonNull final JobStore jobStore,
			@Nullable final List<ISequenceElement> targetElements, final List<Difference> differencesList, @NonNull final BreakdownSearchData searchData,
			@Nullable Collection<@NonNull IResource> currentChangedResources) {
		return search(currentRawSequences, similarityState, changes, changeSets, tryDepth, moveType, currentMetrics, jobStore, targetElements, differencesList, searchData, currentChangedResources, 0);
	}
	
	public Collection<JobState> search(@NonNull final ISequences currentRawSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final int moveType, final long[] currentMetrics, @NonNull final JobStore jobStore,
			@Nullable final List<ISequenceElement> targetElements, final List<Difference> differencesList, @NonNull final BreakdownSearchData searchData,
			@Nullable Collection<@NonNull IResource> currentChangedResources, int recursion) {
		// get data structures from job
		final List<JobState> newStates = new LinkedList<>();
		final BreakdownSearchStatistics searchStatistics = searchData.getSearchStatistics();
		final Random rdm = searchData.getRandom();
		searchStatistics.logStateSeen();
		if (searchStatistics.getStatesSeen() > MAX_SEARCH_STATES) {
			return newStates;
		}

		// Remove nulls
		if (currentChangedResources != null) {
			while (currentChangedResources.remove(null))
				;
		}

		if (DEBUG_VALIDATION) {
			// check no spurious differences
			{
				final ChangeChecker cc = injector.getInstance(ChangeChecker.class);
				cc.init(similarityState, similarityState, currentRawSequences);
				final List<Difference> otherDL = cc.getFullDifferences();
				for (final Difference d : differencesList) {
					if (!otherDL.contains(d)) {
						assert false;
					}
				}
				for (final Difference d : otherDL) {
					if (!differencesList.contains(d)) {
						System.out.println(d);
						assert false;
					}
				}
			}
			// Sanity check -- elements only used once.
			{
				final Set<ISequenceElement> unique = new HashSet<>();
				for (final IResource resource : currentRawSequences.getResources()) {
					final ISequence sequence = currentRawSequences.getSequence(resource);
					for (final ISequenceElement current : sequence) {
						if (unique.contains(current)) {
							System.out.println(String.format("%s|%s", resource.getName(), current.getName()));
						}
						assert unique.add(current);
					}
				}
			}
		}

		final IModifiableSequences currentFullSequences = sequencesManipulator.createManipulatedSequences(currentRawSequences);

		if (tryDepth == 0 || differencesList.size() == 0) {
			boolean failedEvaluation = false;

			final long @Nullable [] thisMetrics = evaluationHelper.evaluateState(currentRawSequences, currentFullSequences, currentChangedResources, similarityState, searchStatistics);
			if (thisMetrics != null) {
				final long thisLateness = thisMetrics[MetricType.LATENESS.ordinal()];
				final long thisPNL = thisMetrics[MetricType.PNL.ordinal()];

				if (thisPNL - currentMetrics[MetricType.PNL.ordinal()] < 0 && thisLateness >= similarityState.getBaseMetrics()[MetricType.LATENESS.ordinal()]) {
					failedEvaluation = true;
				} else {
					// currentPNL = thisPNL;
				}

				if (!failedEvaluation) {

					// Convert change list set into a change set and record sate.
					// TOOD: Get fitness change and only accept improving solutions. (similarity, similarity plus others etc)
					final ChangeSet cs = new ChangeSet(changes);

					for (final MetricType type : MetricType.values()) {
						final long thisValue = thisMetrics[type.ordinal()];
						final long currentValue = currentMetrics[type.ordinal()];
						cs.setMetric(type, thisValue, thisValue - currentValue, thisValue - similarityState.getBaseMetrics()[type.ordinal()]);
					}

					cs.setRawSequences(currentRawSequences);
					changes.clear();
					changeSets.add(cs);

					final JobState jobState = new JobState(new Sequences(currentRawSequences), changeSets, new LinkedList<Change>(), differencesList, searchData);
					for (final MetricType type : MetricType.values()) {
						final long thisValue = thisMetrics[type.ordinal()];
						final long currentValue = currentMetrics[type.ordinal()];
						jobState.setMetric(type, thisValue, thisValue - currentValue, thisValue - similarityState.getBaseMetrics()[type.ordinal()]);
					}

					final int changesCount = differencesList.size();
					if (changesCount == 0) {
						jobState.mode = JobStateMode.LEAF;
					}

					newStates.add(jobState);

					return newStates;
				}
			} else {
				failedEvaluation = true;
			}

			if (failedEvaluation) {
				// Failed to to find valid state at the end of the search depth. Record a limited state and exit
				final JobState jobState = new JobState(searchData);

				jobState.mode = JobStateMode.LIMITED;
				newStates.add(jobState);
				return newStates;
			}
		}

		if (currentChangedResources == null)

		{
			currentChangedResources = new HashSet<>();
		}

		if (differencesList.size() > 0) {
			final StateManager stateManager = new StateManager(currentRawSequences, currentFullSequences);
			// (1) Choose a difference
			Difference difference = pickRandomElementFromList(differencesList, rdm);
			while (difference.move != DifferenceType.CARGO_WRONG_WIRING && difference.move != DifferenceType.CARGO_WRONG_VESSEL && difference.move != DifferenceType.CARGO_NOT_IN_TARGET
					&& difference.move != DifferenceType.DISCHARGE_UNUSED_IN_BASE && difference.move != DifferenceType.LOAD_UNUSED_IN_BASE
					&& difference.move != DifferenceType.UNUSED_DISCHARGE_IN_TARGET && difference.move != DifferenceType.UNUSED_LOAD_IN_TARGET) {
				difference = pickRandomElementFromList(differencesList, rdm);
			}
			// (2) Fix a difference
			if (difference.move == DifferenceType.CARGO_WRONG_WIRING) {
				if (differencesList.contains(new Difference(DifferenceType.LOAD_WRONG_VESSEL, difference.load, null, difference.currentResource))
						&& differencesList.contains(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, difference.discharge, difference.currentResource))) {
					// either do a load swap or a discharge swap
					if (rdm.nextBoolean()) {
						if (!currentRawSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getLoadForDischarge(difference.discharge)))) {
							// load
							newStates.addAll(swapLoad(currentRawSequences, similarityState, changes, changeSets, tryDepth, difference.currentResource, difference.load, difference.discharge,
									currentMetrics, jobStore, null, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
						}
					} else {
						if (!currentRawSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getDischargeForLoad(difference.load)))) {
							// discharge
							newStates.addAll(swapDischarge(currentRawSequences, similarityState, changes, changeSets, tryDepth, difference.currentResource, difference.load, difference.discharge,
									currentMetrics, jobStore, null, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
						}
					}

				} else if (differencesList.contains(new Difference(DifferenceType.LOAD_WRONG_VESSEL, difference.load, null, difference.currentResource))) {
					if (!currentRawSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getLoadForDischarge(difference.discharge)))) {
						// load swap
						newStates.addAll(swapLoad(currentRawSequences, similarityState, changes, changeSets, tryDepth, difference.currentResource, difference.load, difference.discharge,
								currentMetrics, jobStore, null, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
					}
				} else if (differencesList.contains(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, difference.discharge, difference.currentResource))) {
					if (!currentRawSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getDischargeForLoad(difference.load)))) {
						// discharge swap
						newStates.addAll(swapDischarge(currentRawSequences, similarityState, changes, changeSets, tryDepth, difference.currentResource, difference.load, difference.discharge,
								currentMetrics, jobStore, null, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
					}
				} else {
					if (!currentRawSequences.getUnusedElements().contains(similarityState.getElementForIndex(similarityState.getDischargeForLoad(difference.load)))) {
						// discharge swap
						newStates.addAll(swapDischarge(currentRawSequences, similarityState, changes, changeSets, tryDepth, difference.currentResource, difference.load, difference.discharge,
								currentMetrics, jobStore, null, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
					}
				}
			} else if (difference.move == DifferenceType.CARGO_WRONG_VESSEL) {
				// vessel swap
				final ISequence originalResource = currentRawSequences.getSequence(similarityState.getResourceForElement(difference.load));
				final ISequence currentResource = currentRawSequences.getSequence(difference.currentResource);

				// TODO: Create and apply change.
				for (int i = 0; i < currentResource.size(); ++i) {
					if (currentResource.get(i) == difference.load) {

						// Iterate over all possible positions and try it. Note we really could do with original index information to reduce the quantity of options generated. This
						// can get very long and quickly explodes the search space.
						final List<Integer> insertionPoints = findInsertPoints(similarityState, originalResource, difference.currentResource, difference.load, difference.discharge);
						for (final int j : insertionPoints) {
							if (portTypeProvider.getPortType(originalResource.get(j)) != PortType.Discharge) {
								newStates.addAll(swapVessel(currentRawSequences, similarityState, changes, changeSets, tryDepth, currentMetrics, difference.currentResource, difference.load,
										difference.discharge, j, jobStore, null, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
							}
						}
						break;
					}
				}
			} else if (difference.move == DifferenceType.UNUSED_DISCHARGE_IN_TARGET) {
				newStates.addAll(processOneHalfOfCargoUnused(currentRawSequences, similarityState, changes, changeSets, tryDepth, difference.currentResource, difference.load, difference.discharge,
						currentMetrics, false, jobStore, targetElements, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
			} else if (difference.move == DifferenceType.UNUSED_LOAD_IN_TARGET) {
				newStates.addAll(processOneHalfOfCargoUnused(currentRawSequences, similarityState, changes, changeSets, tryDepth, difference.currentResource, difference.discharge, difference.load,
						currentMetrics, true, jobStore, targetElements, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));

			} else if (difference.move == DifferenceType.CARGO_NOT_IN_TARGET) {
				newStates.addAll(removeElementsFromSequence(currentRawSequences, similarityState, changes, changeSets, tryDepth, difference.currentResource, difference.load, difference.discharge,
						currentMetrics, jobStore, targetElements, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
			} else if (difference.move == DifferenceType.LOAD_UNUSED_IN_BASE) {
				newStates.addAll(insertUnusedElementsIntoSequence(currentRawSequences, similarityState, stateManager, changes, changeSets, tryDepth, difference.load, currentMetrics, jobStore,
						targetElements, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
			} else if (difference.move == DifferenceType.DISCHARGE_UNUSED_IN_BASE) {
				newStates.addAll(insertUnusedElementsIntoSequence(currentRawSequences, similarityState, stateManager, changes, changeSets, tryDepth, difference.discharge, currentMetrics, jobStore,
						targetElements, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources));
			}
		}
		// (3) if not able to fix the particular difference, try again
		if (newStates.size() == 0) {
			if (recursion < RECURSION_LIMIT) {
				return search(currentRawSequences, similarityState, changes, changeSets, tryDepth, moveType, currentMetrics, jobStore, targetElements, ChangeChecker.copyDifferenceList(differencesList), searchData, currentChangedResources, recursion+1);
			} else {
				searchData.getSearchStatistics().logEvaluationsFailedConstraints();
				JobState failedState = new JobState(searchData);
				failedState.mode = JobStateMode.INVALID;
				newStates.add(failedState);
			}
		}
		// (4) finish
		return newStates;
	}

	private <T> T pickRandomElementFromList(final List<T> list, final Random randomState) {
		return list.get(randomState.nextInt(list.size()));
	}

	protected Collection<JobState> swapVessel(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final long[] currentMetrics, @NonNull final IResource resource, @NonNull final ISequenceElement prev,
			@NonNull final ISequenceElement current, final int j, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differences,
			@NonNull final BreakdownSearchData searchData, @Nullable final Collection<@NonNull IResource> currentChangedResources) {
		{

			if (similarityState.getResourceForElement(prev).equals(resource)) {
				assert false;
			}

			final IModifiableSequences copy = new ModifiableSequences(currentSequences);
			final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceForElement(prev));
			modifiableSequence.insert(j, current);
			modifiableSequence.insert(j, prev);
			copy.getModifiableSequence(resource).remove(prev);
			copy.getModifiableSequence(resource).remove(current);

			final int depth = getNextDepth(tryDepth, searchData.getRandom());
			final List<Change> changes2 = new ArrayList<>(changes);
			changes2.add(new VesselChange(String.format("Vessel %s from %s to %s\n", prev.getName(), resource.getName(), similarityState.getResourceForElement(prev).getName()), prev, current,
					modifiableSequence.get(j - 1), modifiableSequence.get(j + 2), resource, similarityState.getResourceForElement(prev)));
			if (copy.equals(currentSequences)) {
				// FIXME: Why do we get here?
				// return Collections.emptyList();
			}

			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, current, resource));
			for (final Difference d : new LinkedList<>(differences)) {
				if (d.move == DifferenceType.CARGO_WRONG_VESSEL && ((prev == d.load && current == d.discharge) || (current == d.load && prev == d.discharge))) {
					differences.remove(d);
				}
			}

			@Nullable
			final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
			changedResources.add(resource);
			changedResources.add(similarityState.getResourceForElement(prev));

			return search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_VESSEL_SWAP, currentMetrics, jobStore, null, differences, searchData, changedResources);
		}
	}

	/**
	 * Given a load and discharge, assume the discharge is on the correct resource. Find the correct load for the discharge and swap it with the current load.
	 */
	protected Collection<JobState> swapLoad(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long[] currentMetrics, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differences,
			@NonNull final BreakdownSearchData searchData, @Nullable final Collection<@NonNull IResource> currentChangedResources) {

		if (!(portSlotProvider.getPortSlot(prev) instanceof ILoadSlot)) {
			return Collections.emptyList();
		}

		// Find the original load index
		final Integer originalLoadIdx = similarityState.getLoadForDischarge(current);
		assert originalLoadIdx != null;
		if (isElementUnused(currentSequences, similarityState.getElementForIndex(originalLoadIdx))) {
			// matching load is in unused list, handle elsewhere
			return processOneHalfOfCargoUnused(currentSequences, similarityState, changes, changeSets, tryDepth, resource, current, prev, currentMetrics, true, jobStore, targetElements, differences,
					searchData, currentChangedResources);
		}

		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource);
		boolean swapped = false;

		// Find the original load element and swap with current load element
		ISequenceElement otherLoad = null;
		ISequenceElement originalDischarge = null;
		IResource otherResource = null;
		int firstLoadIdx = -1;
		LOOP: for (final IResource r : copy.getResources()) {
			final IModifiableSequence s = copy.getModifiableSequence(r);

			for (int j = 0; j < s.size(); ++j) {
				if (s.get(j).getIndex() == originalLoadIdx.intValue()) {
					otherLoad = s.get(j);
					if (!(portSlotProvider.getPortSlot(otherLoad) instanceof ILoadSlot)) {
						return Collections.emptyList();
					}
					if ((portSlotProvider.getPortSlot(s.get(j + 1)) instanceof IDischargeOption)) {
						originalDischarge = s.get(j + 1);
					}
					otherResource = r;
					s.set(j, prev);
					firstLoadIdx = j;
					swapped = true;
					break LOOP;
				}
			}
		}
		assert otherResource != null;
		assert otherLoad != null;
		assert swapped;
		assert originalDischarge != null;
		swapped = false;
		// Swap the current load element with the original one.
		for (int j = 0; j < currentResource.size(); ++j) {
			if (currentResource.get(j) == prev) {
				if (resource.getIndex() == otherResource.getIndex() && firstLoadIdx == j) {
					continue;
				}
				currentResource.set(j, otherLoad);
				swapped = true;
				break;
			}
		}

		final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
		changedResources.add(resource);
		changedResources.add(otherResource);

		assert swapped;
		assert prev != null;
		final int depth = getNextDepth(tryDepth, searchData.getRandom());
		final List<Change> changes2 = new ArrayList<>(changes);
		final String description = String.format("Swap %s onto %s with %s onto %s\n", prev.getName(), otherResource.getName(), otherLoad.getName(), resource.getName());
		changes2.add(new LoadRewireChange(description, prev, current, otherLoad, originalDischarge, resource, otherResource));
		// now modify differences
		// (1) remove initial wrong vessel
		differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
		differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, otherLoad, null, otherResource));
		// and any existing unused
		differences.remove(new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, prev, current, resource));
		differences.remove(new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, otherLoad, originalDischarge, otherResource));
		// (2) fixed wiring change
		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, current, resource));
		// (3) new load wrong vessel?
		if (!similarityState.getResourceForElement(otherLoad).equals(resource)) {
			differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, resource));
			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
		} else {
			differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, resource));
		}
		// (4) old load on wrong vessel? or correct discharge?
		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, originalDischarge, otherResource));
		final ISequenceElement matchedDischargeForOldLoad = similarityState.getElementForIndex(similarityState.getDischargeForLoad(prev));
		final boolean prevOnCorrectVessel = similarityState.getResourceForElement(prev).equals(otherResource);
		if (similarityState.getLoadForDischarge(originalDischarge) == null) {
			// current discharge is not in target
			final Difference unusedDischarge = new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, prev, originalDischarge, otherResource);
			if (!differences.contains(unusedDischarge)) {
				differences.add(unusedDischarge);
				if (!prevOnCorrectVessel) {
					differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, otherResource));
				}
			}
			return processOneHalfOfCargoUnused(copy, similarityState, changes2, new ArrayList<>(changeSets), tryDepth, otherResource, prev, originalDischarge, currentMetrics, false, jobStore,
					targetElements, differences, searchData, changedResources);
		} else {
			if (originalDischarge != null && originalDischarge.equals(matchedDischargeForOldLoad)) {
				if (!prevOnCorrectVessel) {
					differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, otherResource));
					differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, originalDischarge, otherResource));
				}
			} else {
				differences.add(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, originalDischarge, otherResource));
				if (!prevOnCorrectVessel) {
					differences.add(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, otherResource));
				}
			}
		}

		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_LOAD_SWAP, currentMetrics, jobStore, null, differences, searchData, changedResources);
	}

	/**
	 * Given a load and discharge, assume the load is on the correct resource. Find the correct discharge for the load and swap it with the current discharge.
	 */
	protected Collection<JobState> swapDischarge(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long[] currentMetrics, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differences,
			@NonNull final BreakdownSearchData searchData, @Nullable final Collection<@NonNull IResource> currentChangedResources) {
		if (!(portSlotProvider.getPortSlot(current) instanceof IDischargeSlot)) {
			return Collections.emptyList();
		}
		// Find the matching discharge for load
		final int originalDischargeIdx = similarityState.getDischargeForLoad(prev);
		if (isElementUnused(currentSequences, similarityState.getElementForIndex(originalDischargeIdx))) {
			// matching discharge is in unused list, handle elsewhere
			return processOneHalfOfCargoUnused(currentSequences, similarityState, changes, changeSets, tryDepth, resource, prev, current, currentMetrics, false, jobStore, targetElements, differences,
					searchData, currentChangedResources);
		}
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence currentResource = copy.getModifiableSequence(resource);

		boolean swapped = false;
		ISequenceElement originalDischarge = null;
		ISequenceElement otherLoad = null;
		IResource otherResource = null;
		ISequence otherResourceSequence = null;
		int firstDischargeSwapIdx = -1;
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
					firstDischargeSwapIdx = j;
					otherResourceSequence = s;
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
				if (resource.getIndex() == otherResource.getIndex() && firstDischargeSwapIdx == j) {
					continue;
				}
				currentResource.set(j, originalDischarge);
				swapped = true;
				break;
			}
		}

		final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
		changedResources.add(resource);
		changedResources.add(otherResource);

		assert swapped;
		final int depth = getNextDepth(tryDepth, searchData.getRandom());
		final List<Change> changes2 = new ArrayList<>(changes);
		final String description = String.format("Swap %s (to %s) with %s ( to %s)\n", current.getName(), otherLoad.getName(), originalDischarge.getName(), prev.getName());
		changes2.add(new DischargeRewireChange(description, prev, current, otherLoad, originalDischarge, resource, otherResource));

		// now modify differences
		// (1) remove initial wrong vessel
		differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, resource));
		differences.remove(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, originalDischarge, otherResource));
		// and any unused issue
		differences.remove(new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, prev, current, resource));
		differences.remove(new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, otherLoad, originalDischarge, otherResource));
		// (2) fixed wiring change
		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, prev, current, resource));
		// (3) new cargo wrong vessel?
		if (!similarityState.getResourceForElement(originalDischarge).equals(resource)) {
			differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, resource));
			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
		} else {
			differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, prev, null, resource));
			differences.remove(new Difference(DifferenceType.CARGO_WRONG_VESSEL, prev, originalDischarge, resource));
		}
		// (4) old discharge on wrong vessel?
		differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, originalDischarge, otherResource));
		final ISequenceElement matchedLoadForOldDischarge = similarityState.getElementForIndex(similarityState.getLoadForDischarge(current));
		final boolean currOnCorrectResource = similarityState.getResourceForElement(current).equals(otherResource);
		if (similarityState.getDischargeForLoad(otherLoad) == null) {
			// current load is not in target
			final Difference unusedLoad = new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, otherLoad, current, otherResource);
			if (!differences.contains(unusedLoad)) {
				differences.add(unusedLoad);
				if (!currOnCorrectResource) {
					differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, otherResource));
				}
			}
			return processOneHalfOfCargoUnused(copy, similarityState, changes2, new ArrayList<>(changeSets), tryDepth, otherResource, current, otherLoad, currentMetrics, true, jobStore,
					targetElements, differences, searchData, changedResources);
		} else {
			if (otherLoad != null && otherLoad.equals(matchedLoadForOldDischarge)) {
				if (!currOnCorrectResource) {
					differences.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, otherLoad, current, otherResource));
					differences.remove(new Difference(DifferenceType.LOAD_WRONG_VESSEL, otherLoad, null, otherResource));
				}
			} else {
				differences.add(new Difference(DifferenceType.CARGO_WRONG_WIRING, otherLoad, current, otherResource));
				if (!currOnCorrectResource) {
					differences.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, current, otherResource));
				}
			}
		}

		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_DISCHARGE_SWAP, currentMetrics, jobStore, null, differences, searchData, changedResources);
	}

	/**
	 * Completely remove a load and discharge from a Sequence
	 */
	protected Collection<JobState> removeElementsFromSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, final IResource resource, @NonNull final ISequenceElement prev, @NonNull final ISequenceElement current,
			final long[] currentMetrics, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differences,
			@NonNull final BreakdownSearchData searchData, @Nullable final Collection<@NonNull IResource> currentChangedResources) {
		final IModifiableSequences copy = new ModifiableSequences(currentSequences);
		final IModifiableSequence copyOfTargetSequence = copy.getModifiableSequence(resource);
		copyOfTargetSequence.remove(prev);
		copyOfTargetSequence.remove(current);
		copy.getModifiableUnusedElements().add(prev);
		copy.getModifiableUnusedElements().add(current);

		final int depth = getNextDepth(tryDepth, searchData.getRandom());
		final List<Change> changes2 = new ArrayList<>(changes);
		final String description = String.format("Remove %s and %s\n", prev.getName(), current.getName());
		changes2.add(new RemoveCargoChange(description, prev, current, resource));
		differences.remove(new Difference(DifferenceType.CARGO_NOT_IN_TARGET, prev, current, resource));
		differences.remove(new Difference(DifferenceType.CARGO_NOT_IN_TARGET, current, prev, resource));

		final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
		changedResources.add(resource);

		if (resource != null) {
			ISequenceElement load;
			ISequenceElement discharge;
			if (portTypeProvider.getPortType(prev) == PortType.Load) {
				load = prev;
				discharge = current;
			} else {
				load = current;
				discharge = prev;
			}
			differences.remove(new Difference(DifferenceType.UNUSED_LOAD_IN_TARGET, load, discharge, resource));
			updateWrongVesselDifferenceLoad(differences, load);
			differences.remove(new Difference(DifferenceType.UNUSED_DISCHARGE_IN_TARGET, load, discharge, resource));
			updateWrongVesselDifferenceDischarge(differences, discharge);
			differences.remove(new Difference(DifferenceType.CARGO_WRONG_WIRING, load, discharge, resource));
			final Difference loadNeedsInserting = new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, load, null, null);
			final Difference dischargeNeedsInserting = new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, discharge, null);
			updateDifferencesListAfterElementsRemoval(similarityState, differences, load, discharge);
			if (similarityState.getLoadForDischarge(discharge) != null && !differences.contains(dischargeNeedsInserting)) {
				differences.add(dischargeNeedsInserting);
			}
			if (similarityState.getDischargeForLoad(load) != null & !differences.contains(loadNeedsInserting)) {
				differences.add(loadNeedsInserting);
			}
			for (final Difference d : new LinkedList<>(differences)) {
				if (d.move == DifferenceType.CARGO_WRONG_VESSEL && ((prev == d.load && current == d.discharge) || (current == d.load && prev == d.discharge))) {
					differences.remove(d);
				}
			}
		}

		return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentMetrics, jobStore, targetElements, differences, searchData, changedResources);
	}

	/**
	 * Completely remove a load and discharge from a Sequence
	 */
	private Collection<JobState> processOneHalfOfCargoUnused(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement elementToKeep,
			@NonNull final ISequenceElement elementToRemove, final long[] currentMetrics, final boolean isLoadSwap, @NonNull final JobStore jobStore,
			@Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differencesList, @NonNull final BreakdownSearchData searchData,
			@Nullable final Collection<@NonNull IResource> currentChangedResources) {
		ISequenceElement matchedElement;
		if (isLoadSwap) {
			// find correct load
			matchedElement = similarityState.getElementForIndex(similarityState.getLoadForDischarge(elementToKeep));
		} else {
			// find correct discharge
			matchedElement = similarityState.getElementForIndex(similarityState.getDischargeForLoad(elementToKeep));
		}
		// (1) is the correct element in the unused? Swap it in
		if ((portSlotProvider.getPortSlot(elementToRemove) instanceof ILoadSlot)
				|| (portSlotProvider.getPortSlot(elementToRemove) instanceof IDischargeSlot) && currentSequences.getUnusedElements().contains(matchedElement)) {

			if ((portSlotProvider.getPortSlot(matchedElement) instanceof ILoadSlot) || (portSlotProvider.getPortSlot(matchedElement) instanceof IDischargeSlot)) {
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence currentResource = copy.getModifiableSequence(resource);

				copy.getModifiableUnusedElements().remove(matchedElement);
				Integer elementIdx = -11;
				for (int i = 0; i < currentResource.size(); i++) {
					if (currentResource.get(i).getIndex() == elementToRemove.getIndex()) {
						elementIdx = i;
					}
				}
				assert elementIdx != null;
				currentResource.insert(elementIdx, matchedElement);
				currentResource.remove(elementToRemove);

				copy.getModifiableUnusedElements().add(elementToRemove);
				final int depth = getNextDepth(tryDepth, searchData.getRandom());
				final List<Change> changes2 = new ArrayList<>(changes);
				final int moveType;
				if (isLoadSwap) {
					final String description = String.format("Remove load %s (unused in target solution) and insert load %s (unused in base solution)\n", elementToRemove.getName(),
							matchedElement.getName());
					changes2.add(new UnusedToUsedLoadChange(description, elementToKeep, elementToRemove, matchedElement, resource));
					moveType = MOVE_TYPE_UNUSED_LOAD_SWAPPED;
					updateWrongCargoWiringDifference(differencesList, elementToRemove, elementToKeep);
					updateDifferencesRemoveUnusedLoadInTarget(differencesList, elementToRemove);
					updateWrongVesselDifferenceLoad(differencesList, elementToRemove);
					checkAndAddDifferenceForUnusedLoadInBase(similarityState, differencesList, elementToRemove);
					updateDifferencesRemoveUnusedLoadInBase(differencesList, matchedElement);
					checkAndAddDifferenceForWrongVesselCargo(similarityState, differencesList, matchedElement, elementToKeep, resource);
				} else {
					final String description = String.format("Remove discharge %s (unused in target solution) and insert discharge %s (unused in base solution)\n", elementToRemove.getName(),
							matchedElement.getName());
					changes2.add(new UnusedToUsedDischargeChange(description, elementToKeep, elementToRemove, matchedElement, resource));
					moveType = MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED;
					updateWrongCargoWiringDifference(differencesList, elementToKeep, elementToRemove);
					updateDifferencesRemoveUnusedDischargeInTarget(differencesList, elementToRemove);
					updateWrongVesselDifferenceDischarge(differencesList, elementToRemove);
					checkAndAddDifferenceForUnusedDischargeInBase(similarityState, differencesList, elementToRemove);
					updateDifferencesRemoveUnusedDischargeInBase(differencesList, matchedElement);
					checkAndAddDifferenceForWrongVesselCargo(similarityState, differencesList, elementToKeep, matchedElement, resource);
				}

				final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
				changedResources.add(resource);

				return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, moveType, currentMetrics, jobStore, targetElements, differencesList, searchData, changedResources);
			} else {
				// FOB SALE OR DES PURCHASE
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence currentResource = copy.getModifiableSequence(resource);
				final Collection<IResource> allowedResources = resourceAllocationProvider.getAllowedResources(matchedElement);
				assert allowedResources != null && allowedResources.size() == 1;

				final IModifiableSequence fsSequence = copy.getModifiableSequence(allowedResources.iterator().next());
				final IModifiableSequence currentSequence = copy.getModifiableSequence(resource);
				copy.getModifiableUnusedElements().remove(matchedElement);
				if (!isLoadSwap) {
					fsSequence.insert(1, matchedElement);
					fsSequence.insert(1, elementToKeep);
					currentSequence.remove(elementToKeep);
					currentSequence.remove(elementToRemove);
					copy.getModifiableUnusedElements().add(elementToRemove);
					final int depth = getNextDepth(tryDepth, searchData.getRandom());
					final List<Change> changes2 = new ArrayList<>(changes);
					final String description = String.format("Insert FS  %s (unused in target solution) and remove  load %s (unused in base solution)\n", matchedElement.getName(),
							elementToRemove.getName());
					changes2.add(new UnusedToUsedLoadChange(description, elementToKeep, elementToRemove, matchedElement, resource));
					updateWrongCargoWiringDifference(differencesList, elementToKeep, elementToRemove);
					updateDifferencesRemoveUnusedDischargeInTarget(differencesList, elementToRemove);
					updateDifferencesRemoveUnusedDischargeInBase(differencesList, matchedElement);
					updateWrongVesselDifferenceLoad(differencesList, elementToKeep);
					updateWrongVesselDifferenceDischarge(differencesList, elementToRemove);
					checkAndAddDifferenceForUnusedDischargeInBase(similarityState, differencesList, elementToRemove);

					final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
					changedResources.add(resource);
					changedResources.add(allowedResources.iterator().next());

					return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_UNUSED_LOAD_SWAPPED, currentMetrics, jobStore, targetElements, differencesList,
							searchData, changedResources);
				} else {
					fsSequence.insert(1, elementToKeep);
					fsSequence.insert(1, matchedElement);
					currentSequence.remove(elementToKeep);
					currentSequence.remove(elementToRemove);
					copy.getModifiableUnusedElements().add(elementToRemove);
					final int depth = getNextDepth(tryDepth, searchData.getRandom());
					final List<Change> changes2 = new ArrayList<>(changes);
					final String description = String.format("Insert DP  %s (unused in target solution) and remove Discharge %s (unused in base solution)\n", matchedElement.getName(),
							elementToRemove.getName());
					changes2.add(new UnusedToUsedDischargeChange(description, elementToKeep, elementToRemove, matchedElement, resource));
					updateWrongCargoWiringDifference(differencesList, elementToRemove, elementToKeep);
					updateDifferencesRemoveUnusedLoadInBase(differencesList, matchedElement);
					updateDifferencesRemoveUnusedLoadInTarget(differencesList, elementToRemove);
					updateWrongVesselDifferenceLoad(differencesList, elementToRemove);
					checkAndAddDifferenceForUnusedLoadInBase(similarityState, differencesList, elementToRemove);
					updateWrongVesselDifferenceDischarge(differencesList, elementToKeep);

					final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
					changedResources.add(resource);
					changedResources.add(allowedResources.iterator().next());

					return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_UNUSED_DISCHARGE_SWAPPED, currentMetrics, jobStore, targetElements, differencesList,
							searchData, changedResources);
				}
			}
		} else {
			// (2) remove both slots
			return removeElementsFromSequence(currentSequences, similarityState, changes, new ArrayList<>(changeSets), tryDepth, resource, elementToKeep, elementToRemove, currentMetrics, jobStore,
					targetElements, differencesList, searchData, currentChangedResources);
		}
	}

	private Collection<JobState> insertUnusedElementsIntoSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final StateManager stateManager,
			@NonNull final List<Change> changes, @NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final ISequenceElement element, final long[] currentMetrics,
			@NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differencesList, @NonNull final BreakdownSearchData searchData,
			@Nullable final Collection<@NonNull IResource> currentChangedResources) {
		if (portTypeProvider.getPortType(element) == PortType.Load) {
			final Integer otherDischargeIdx = similarityState.getDischargeForLoad(element);
			final ISequenceElement discharge = similarityState.getElementForIndex(otherDischargeIdx);
			assert isElementUnused(currentSequences, element);
			if (currentSequences.getUnusedElements().contains(discharge)) {
				final IResource resource = similarityState.getResourceForElement(element);
				// move as pair
				return insertUnusedCargoIntoSequence(currentSequences, similarityState, changes, new ArrayList<>(changeSets), tryDepth, resource, element, discharge, currentMetrics, jobStore,
						targetElements, differencesList, searchData, currentChangedResources);
			} else {

				final Pair<IResource, Integer> p = stateManager.getPositionForElement(discharge);

				// step (2) remove both slots
				// FIXME: Currently just unpair both slots and remove from solution
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence currentSequence = copy.getModifiableSequence(p.getFirst());

				for (int i = 0; i < currentSequence.size(); ++i) {
					final ISequenceElement e = currentSequence.get(i);
					if (e == discharge) {
						final ISequenceElement load = currentSequence.get(i - 1);
						copy.getModifiableUnusedElements().add(load);
						copy.getModifiableUnusedElements().add(discharge);

						currentSequence.remove(load);
						currentSequence.remove(discharge);

						final List<ISequenceElement> searchElements = targetElements == null ? new LinkedList<ISequenceElement>() : new LinkedList<>(targetElements);
						// Tell next level to focus on the load
						if (!searchElements.contains(load)) {
							searchElements.add(load);
						}
						if (!searchElements.contains(discharge)) {
							searchElements.add(discharge);
						}

						final int depth = getNextDepth(tryDepth, searchData.getRandom());
						final List<Change> changes2 = new ArrayList<>(changes);
						final String description = String.format("Remove %s and %s\n", load.getName(), discharge.getName());
						changes2.add(new RemoveCargoChange(description, load, discharge, p.getFirst()));
						updateDifferencesListAfterElementsRemoval(similarityState, differencesList, load, discharge);

						final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
						changedResources.add(p.getFirst());

						return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentMetrics, jobStore, searchElements, differencesList,
								searchData, changedResources);
					}
				}

				// Discharge already used, give up on current search and clear hints
				return search(currentSequences, similarityState, new LinkedList<>(changes), new LinkedList<>(changeSets), getNextDepth(tryDepth, searchData.getRandom()), 0, currentMetrics, jobStore,
						null, differencesList, searchData, currentChangedResources);
			}
		} else if (portTypeProvider.getPortType(element) == PortType.Discharge) {
			final Integer otherLoadIdx = similarityState.getLoadForDischarge(element);
			// If we get here, the load is also unused
			final ISequenceElement load = similarityState.getElementForIndex(otherLoadIdx);
			if (currentSequences.getUnusedElements().contains(load)) {
				// as moving as a pair, remove load from unusedElements queue that we're looping through
				final IResource resource = similarityState.getResourceForElement(element);
				// move as a pair
				return insertUnusedCargoIntoSequence(currentSequences, similarityState, changes, new ArrayList<>(changeSets), tryDepth, resource, load, element, currentMetrics, jobStore,
						targetElements, differencesList, searchData, currentChangedResources);
			} else {
				final Pair<IResource, Integer> p = stateManager.getPositionForElement(load);

				// step (2) remove both slots
				// FIXME: Currently just unpair both slots and remove from solution
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence currentSequence = copy.getModifiableSequence(p.getFirst());

				for (int i = 0; i < currentSequence.size(); ++i) {
					final ISequenceElement e = currentSequence.get(i);
					if (e == load) {
						final ISequenceElement discharge = currentSequence.get(i + 1);
						assert discharge != null;
						copy.getModifiableUnusedElements().add(load);
						copy.getModifiableUnusedElements().add(discharge);
						currentSequence.remove(load);
						currentSequence.remove(discharge);

						final List<ISequenceElement> searchElements = targetElements == null ? new LinkedList<ISequenceElement>() : new LinkedList<>(targetElements);
						// Tell next level to focus on the load
						if (!searchElements.contains(load)) {
							searchElements.add(load);
						}
						if (!searchElements.contains(discharge)) {
							searchElements.add(discharge);
						}

						final int depth = getNextDepth(tryDepth, searchData.getRandom());
						final List<Change> changes2 = new ArrayList<>(changes);
						// changes2.add(new Change(String.format("Remove %s and %s\n", load.getName(), discharge.getName())));
						final String description = String.format("Remove %s and %s\n", load.getName(), discharge.getName());
						changes2.add(new RemoveCargoChange(description, load, discharge, p.getFirst()));
						updateDifferencesListAfterElementsRemoval(similarityState, differencesList, load, discharge);

						@Nullable
						final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
						changedResources.add(p.getFirst());

						return search(copy, similarityState, changes2, new ArrayList<>(changeSets), depth, MOVE_TYPE_CARGO_REMOVE, currentMetrics, jobStore, searchElements, differencesList,
								searchData, changedResources);
					}
				}
			}
		} else {
			// assume vessel event?
		}
		return new LinkedList<JobState>();
	}

	private void updateDifferencesListAfterElementsRemoval(final SimilarityState similarityState, final List<Difference> differencesList, final ISequenceElement load,
			final ISequenceElement discharge) {
		checkAndAddDifferenceForUnusedLoadInBase(similarityState, differencesList, load);
		updateDifferencesRemoveUnusedLoadInTarget(differencesList, load);

		checkAndAddDifferenceForUnusedDischargeInBase(similarityState, differencesList, discharge);
		updateDifferencesRemoveUnusedDischargeInTarget(differencesList, discharge);

		updateWrongCargoWiringDifference(differencesList, load, discharge);
		updateWrongVesselDifferenceDischarge(differencesList, discharge);
		updateWrongVesselDifferenceLoad(differencesList, load);
	}

	private void checkAndAddDifferenceForUnusedLoadInBase(final SimilarityState similarityState, final List<Difference> differencesList, final ISequenceElement load) {
		if (similarityState.getDischargeForLoad(load) != null) {
			if (!differencesList.contains(new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, load, null, null))) {
				differencesList.add(new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, load, null, null));
			}
		}
	}

	private void checkAndAddDifferenceForWrongVesselCargo(final SimilarityState similarityState, final List<Difference> differencesList, final ISequenceElement load, final ISequenceElement discharge,
			final IResource resource) {
		if (similarityState.getResourceForElement(load) != resource) {
			updateWrongVesselDifferenceLoad(differencesList, load);
			updateWrongVesselDifferenceDischarge(differencesList, discharge);
			if (!differencesList.contains(new Difference(DifferenceType.CARGO_WRONG_VESSEL, load, discharge, resource))) {
				differencesList.add(new Difference(DifferenceType.CARGO_WRONG_VESSEL, load, discharge, resource));
			}
		} else {
			updateWrongVesselDifferenceLoad(differencesList, load);
			if (similarityState.getResourceForElement(discharge) != resource) {
				if (!differencesList.contains(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, discharge, resource))) {
					differencesList.add(new Difference(DifferenceType.DISCHARGE_WRONG_VESSEL, null, discharge, resource));
				}
			}
		}
	}

	private void checkAndAddDifferenceForUnusedDischargeInBase(final SimilarityState similarityState, final List<Difference> differencesList, final ISequenceElement discharge) {
		if (similarityState.getLoadForDischarge(discharge) != null) {
			if (!differencesList.contains(new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, discharge, null))) {
				differencesList.add(new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, discharge, null));
			}
		}
	}

	private void updateDifferencesRemoveUnusedLoadInBase(final List<Difference> differencesList, final ISequenceElement load) {
		for (final Difference d : differencesList) {
			if (d.move == DifferenceType.LOAD_UNUSED_IN_BASE && d.load == load) {
				differencesList.remove(d);
				break;
			}
		}
	}

	private void updateDifferencesRemoveUnusedDischargeInBase(final List<Difference> differencesList, final ISequenceElement discharge) {
		for (final Difference d : differencesList) {
			if (d.move == DifferenceType.DISCHARGE_UNUSED_IN_BASE && d.discharge == discharge) {
				differencesList.remove(d);
				break;
			}
		}
	}

	private void updateDifferencesRemoveUnusedLoadInTarget(final List<Difference> differencesList, final ISequenceElement load) {
		for (int i = differencesList.size() - 1; i >= 0; i--) {
			final Difference d = differencesList.get(i);
			if (d.move == DifferenceType.UNUSED_LOAD_IN_TARGET && d.load == load) {
				differencesList.remove(i);
				break;
			}
		}
	}

	private void updateDifferencesRemoveUnusedDischargeInTarget(final List<Difference> differencesList, final ISequenceElement discharge) {
		// for (Difference d : differencesList) {
		for (int i = differencesList.size() - 1; i >= 0; i--) {
			final Difference d = differencesList.get(i);
			if (d.move == DifferenceType.UNUSED_DISCHARGE_IN_TARGET && d.discharge == discharge) {
				differencesList.remove(i);
			}
		}
	}

	private void updateWrongCargoWiringDifference(final List<Difference> differencesList, final ISequenceElement load, final ISequenceElement discharge) {
		for (final Difference d : differencesList) {
			if (d.move == DifferenceType.CARGO_WRONG_WIRING && d.load == load && d.discharge == discharge) {
				differencesList.remove(d);
				break;
			}
		}
	}

	private void updateWrongVesselDifferenceDischarge(final List<Difference> differencesList, final ISequenceElement discharge) {
		for (final Difference d : differencesList) {
			if (d.move == DifferenceType.DISCHARGE_WRONG_VESSEL && d.discharge == discharge) {
				differencesList.remove(d);
				break;
			}
		}
	}

	private void updateWrongVesselDifferenceLoad(final List<Difference> differencesList, final ISequenceElement load) {
		for (final Difference d : differencesList) {
			if (d.move == DifferenceType.LOAD_WRONG_VESSEL && d.load == load) {
				differencesList.remove(d);
				break;
			}
		}
	}

	// FIXME: Make sure different is counted in search()
	protected Collection<JobState> insertUnusedCargoIntoSequence(@NonNull final ISequences currentSequences, @NonNull final SimilarityState similarityState, @NonNull final List<Change> changes,
			@NonNull final List<ChangeSet> changeSets, final int tryDepth, @NonNull final IResource resource, @NonNull final ISequenceElement load, @NonNull final ISequenceElement discharge,
			final long[] currentMetrics, @NonNull final JobStore jobStore, @Nullable final List<ISequenceElement> targetElements, @NonNull final List<Difference> differencesList,
			@NonNull final BreakdownSearchData searchData, @Nullable final Collection<@NonNull IResource> currentChangedResources) {
		final ISequence sequenceOfOriginalResource = currentSequences.getSequence(similarityState.getResourceForElement(load));
		for (final IResource r : currentSequences.getResources()) {
			for (final ISequenceElement element : currentSequences.getSequence(r)) {
				assert element != load;
				assert element != discharge;
			}
		}
		final List<JobState> newStates = new LinkedList<>();
		for (final int i : findInsertPoints(similarityState, sequenceOfOriginalResource, similarityState.getResourceForElement(load), load, discharge)) {
			if (portTypeProvider.getPortType(sequenceOfOriginalResource.get(i)) != PortType.Discharge) {
				final IModifiableSequences copy = new ModifiableSequences(currentSequences);
				final IModifiableSequence modifiableSequence = copy.getModifiableSequence(similarityState.getResourceForElement(load));
				modifiableSequence.insert(i, discharge);
				modifiableSequence.insert(i, load);

				copy.getModifiableUnusedElements().remove(load);
				copy.getModifiableUnusedElements().remove(discharge);

				final int depth = getNextDepth(tryDepth, searchData.getRandom());
				final List<Change> changes2 = new ArrayList<>(changes);
				// changes2.add(new Change(String.format("Insert cargo %s -> %s on %s\n", load.getName(), discharge.getName(), similarityState.getResourceForElement(load).getName())));
				final String description = String.format("Insert cargo %s -> %s on %s\n", load.getName(), discharge.getName(), similarityState.getResourceForElement(load).getName());
				changes2.add(new InsertUnusedCargoChange(description, modifiableSequence.get(i - 1), modifiableSequence.get(i + 2), load, discharge, similarityState.getResourceForElement(load)));
				differencesList.remove(new Difference(DifferenceType.CARGO_NOT_IN_TARGET, load, discharge, resource));
				differencesList.remove(new Difference(DifferenceType.LOAD_UNUSED_IN_BASE, load, null, null));
				differencesList.remove(new Difference(DifferenceType.DISCHARGE_UNUSED_IN_BASE, null, discharge, null));

				@Nullable
				final Collection<@NonNull IResource> changedResources = new HashSet<>(currentChangedResources);
				changedResources.add(similarityState.getResourceForElement(load));

				newStates.addAll(search(copy, similarityState, changes2, new LinkedList<>(changeSets), depth, MOVE_TYPE_CARGO_INSERT, currentMetrics, jobStore, targetElements, differencesList,
						searchData, changedResources));
			}
		}
		return newStates;
	}

	protected int getNextDepth(final int tryDepth, final Random rdm) {
		assert rdm != null;
		assert tryDepth >= -1;
		return tryDepth == DEPTH_START ? getRandomDepth(rdm) : tryDepth - 1;
	}

	private int getRandomDepth(final Random rdm) {
		final int diff = depthEnd - depthStart;
		final int offset = rdm.nextInt(diff);
		return depthStart + offset;
	}

	public void setDepthStart(final int depthStart) {
		this.depthStart = depthStart;
	}

	public void setDepthEnd(final int depthEnd) {
		this.depthEnd = depthEnd;
	}

	public void setDepthRange(final int start, final int end) {
		this.depthStart = start;
		this.depthEnd = end;
	}

	public Pair<Integer, Integer> getDepthRange() {
		return new Pair<Integer, Integer>(depthStart, depthEnd);
	}

	private boolean isElementUnused(@NonNull final ISequences currentSequences, @NonNull final ISequenceElement element) {
		return currentSequences.getUnusedElements().contains(element);
	}

	protected List<Integer> findInsertPoints(@NonNull final SimilarityState similarityState, @NonNull final ISequence sequence, @NonNull final IResource resource,
			@NonNull final ISequenceElement insertingLoad, @NonNull final ISequenceElement insertingDischarge) {
		// first see if this cargo will slot right in to the correct position
		final List<Integer> perfectPoints = findPerfectInsertPoint(similarityState, sequence, resource, insertingLoad, insertingDischarge);
		if (!perfectPoints.isEmpty()) {
			// System.out.println("found perfect points");
			return perfectPoints;
		}
		int prevLoadIdx = -1;
		ITimeWindow prevTimeWindow = null;
		int currLoadIdx = -1;
		IPortSlot currPortSlot = null;
		ITimeWindow currTimeWindow = null;
		final ITimeWindow insertingLoadTimeWindow = getTW(portSlotProvider.getPortSlot(insertingLoad), resource);
		final LinkedHashSet<Integer> validPoints = new LinkedHashSet<Integer>();

		// First pass - add in all valid positions.
		for (int j = 1; j < sequence.size(); ++j) {
			final PortType portType = portTypeProvider.getPortType(sequence.get(j));
			if (portType != PortType.Discharge && portType != PortType.Virtual && portType != PortType.Other) {
				validPoints.add(j);
			}
		}

		// Forward pass - remove early timewindows up until the time window order gets out of sync
		for (int j = 1; j < sequence.size(); ++j) {
			final PortType portType = portTypeProvider.getPortType(sequence.get(j));
			if (portType != PortType.Discharge && portType != PortType.Virtual && portType != PortType.Other && portType != PortType.End) {
				currLoadIdx = j;
				currPortSlot = portSlotProvider.getPortSlot(sequence.get(j));
				currTimeWindow = getTW(currPortSlot, resource);
				if (prevLoadIdx != -1) {
					assert prevTimeWindow != null;
					if (prevTimeWindow.getInclusiveStart() >= currTimeWindow.getExclusiveEnd()) {
						// No longer consistent ordering, abort
						break;
					}
					if (prevTimeWindow.getExclusiveEnd() < insertingLoadTimeWindow.getInclusiveStart()) {
						// don't insert before this element
						validPoints.remove(prevLoadIdx);
					}
				}
				prevLoadIdx = currLoadIdx;
				prevTimeWindow = currTimeWindow;
			}
		}
		// Reverse pass, remove tail insertion points.
		// Prev is now "Next"
		prevLoadIdx = -1;
		for (int j = sequence.size() - 1; j > 0; --j) {
			final PortType portType = portTypeProvider.getPortType(sequence.get(j));
			if (portType != PortType.Discharge && portType != PortType.Virtual && portType != PortType.Other && portType != PortType.End) {
				currLoadIdx = j;
				currPortSlot = portSlotProvider.getPortSlot(sequence.get(j));
				currTimeWindow = getTW(currPortSlot, resource);
				if (prevLoadIdx != -1) {
					assert prevTimeWindow != null;
					if (prevTimeWindow.getExclusiveEnd() <= currTimeWindow.getInclusiveStart()) {
						// No longer consistent ordering, abort
						break;
					}
					if (currTimeWindow.getInclusiveStart() > insertingLoadTimeWindow.getExclusiveEnd()) {
						// don't insert before this element
						validPoints.remove(prevLoadIdx);
					}
				}
				prevLoadIdx = currLoadIdx;
				prevTimeWindow = currTimeWindow;
			}
		}

		return new LinkedList<>(validPoints);
	}

	private List<Integer> findPerfectInsertPoint(final SimilarityState similarityState, final ISequence sequence, final IResource resource, final ISequenceElement insertingLoad,
			final ISequenceElement insertingDischarge) {
		final List<Integer> validPoints = new LinkedList<>();
		final Pair<Integer, Integer> insertingCargo = new Pair<>(insertingLoad.getIndex(), insertingDischarge.getIndex());
		final Pair<Integer, Integer> previousTargetCargo = similarityState.getPreviousCargo(insertingCargo);
		final Pair<Integer, Integer> nextTargetCargo = similarityState.getNextCargo(insertingCargo);
		if (previousTargetCargo == null || nextTargetCargo == null) {
			// this cargo isn't in the target cargo
			return validPoints;
		}
		ISequenceElement prev = null;
		ISequenceElement current = null;
		Pair<Integer, Integer> prevCargo = new Pair<>(-2, -2); // start
		Pair<Integer, Integer> currCargo = new Pair<>(-1, -1); // end
		int prevSequenceIndex = -1;
		int currSequenceIndex = -1;
		for (int j = 1; j < sequence.size(); j++) {
			current = sequence.get(j);
			currSequenceIndex = j;
			if (prev != null) {
				if (portTypeProvider.getPortType(prev) == PortType.Load) {
					if (portTypeProvider.getPortType(current) == PortType.Discharge) {
						currCargo = new Pair<>(prev.getIndex(), current.getIndex());
						if (prevCargo.getSecond().equals(previousTargetCargo.getSecond()) && currCargo.getFirst().equals(nextTargetCargo.getFirst())) {
							validPoints.add(prevSequenceIndex);
							return validPoints;
						}
					}
				}
			}
			prev = current;
			prevCargo = currCargo;
			prevSequenceIndex = currSequenceIndex;
		}
		return new LinkedList<>(validPoints);
	}

	private ITimeWindow getTW(final @NonNull IPortSlot portSlot, final @NonNull IResource resource) {
		ITimeWindow tw = null;

		if (portSlot instanceof StartPortSlot) {
			final IStartEndRequirement req = startEndRequirementProvider.getStartRequirement(resource);
			tw = req.getTimeWindow();
		} else if (portSlot instanceof EndPortSlot) {
			final IStartEndRequirement req = startEndRequirementProvider.getEndRequirement(resource);
			tw = req.getTimeWindow();
		}

		if (tw == null) {
			tw = portSlot.getTimeWindow();
		}
		return tw;
	}
}
