/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IReducingConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.ISlotGroupCountProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.SlotGroup;

/**
 * The {@link SlotGroupCountConstraintChecker} is a {@link IReducingConstraintChecker} implementation. This constraint checker ensures that there is never more than the specified number of elements
 * from a {@link SlotGroup} present in the {@link ISequences} solution. However this is a {@link IReducingConstraintChecker} and the initial {@link ISequences} is used to relax these constraints to the
 * current count. This permits some initial violation of the {@link SlotGroup} counts, but does not allow the violation to get worse. I.e. this constraint only allows the optimiser to make changes
 * which preserve the current state or improve it.
 * 
 * As an {@link IReducingConstraintChecker} state is retained.
 * 
 * @author Simon Goodall
 * 
 */
public class SlotGroupCountConstraintChecker implements IReducingConstraintChecker {
	@NonNull
	private final String name;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private ISlotGroupCountProvider slotGroupProvider;

	/**
	 * Small class to track slot group element count.
	 * 
	 */
	private static class SlotGroupTracker {

		int currentCount;
		int constrainedCount;
		int permittedCount;
	}

	@NonNull
	private final Map<SlotGroup, SlotGroupTracker> trackers = new HashMap<SlotGroup, SlotGroupTracker>();

	public SlotGroupCountConstraintChecker(@NonNull final String name) {
		super();
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Inject
	public void init() {

//		// Initialise state. Create tracker for each slot group and record the "count";
//		for (final SlotGroup group : slotGroupProvider.getGroups()) {
//			final SlotGroupTracker tracker = new SlotGroupTracker();
//			tracker.currentCount = 0;
//			tracker.permittedCount = 0;
//			tracker.constrainedCount = group.getCount();
//			trackers.put(group, tracker);
//		}
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {

//		// Reset counters;
//		for (final SlotGroupTracker tracker : trackers.values()) {
//			tracker.currentCount = 0;
//		}

//		for (final IResource resource : sequences.getResources()) {
//			assert resource != null;
//			final ISequence sequence = sequences.getSequence(resource);
//			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
//
//			final boolean isVirtualVessel = vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE;
//			final boolean checkSequence = (!isVirtualVessel) || (isVirtualVessel && sequence.size() > 1);
//			if (checkSequence) {
//				for (final ISequenceElement element : sequence) {
//					final Collection<SlotGroup> groups = slotGroupProvider.getGroupsForElement(element);
//					for (final SlotGroup group : groups) {
//						final SlotGroupTracker tracker = trackers.get(group);
//						// Increment group count and reject solution if we break our permitted count limit.
//						if (++tracker.currentCount > tracker.permittedCount) {
//							if (messages != null) {
//								// TODO: Add a message
//							}
//							return false;
//						}
//					}
//				}
//			}
//		}
		return true;
	}

	@Override
	public void sequencesAccepted(@NonNull final ISequences sequences) {

//		// Reset current counts to zero;
//		for (final SlotGroupTracker tracker : trackers.values()) {
//			tracker.currentCount = 0;
//		}
//
//		// Loop through and update the current count;
//		for (final IResource resource : sequences.getResources()) {
//			assert resource != null;
//			final ISequence sequence = sequences.getSequence(resource);
//			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
//
//			// Virtual vessels need special treatment. If there is only one element on the route, then it is considered not to be used.
//			final boolean isVirtualVessel = vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE;
//			final boolean checkSequence = (!isVirtualVessel) || (isVirtualVessel && sequence.size() > 1);
//			if (checkSequence) {
//				for (final ISequenceElement element : sequence) {
//					final Collection<SlotGroup> groups = slotGroupProvider.getGroupsForElement(element);
//					for (final SlotGroup group : groups) {
//						final SlotGroupTracker tracker = trackers.get(group);
//						tracker.currentCount++;
//					}
//				}
//			}
//		}
//
//		// Finally, set the new permitted count value - make sure it is not less than the specified count value.
//
//		for (final SlotGroupTracker tracker : trackers.values()) {
//			tracker.permittedCount = Math.max(Math.max(tracker.constrainedCount, tracker.currentCount), tracker.permittedCount);
//		}
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public void setOptimisationData(@NonNull IOptimisationData optimisationData) {

	}
}
