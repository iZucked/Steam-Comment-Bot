/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IReducingContraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ISlotGroupCountProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.SlotGroup;

/**
 * The {@link SlotGroupCountConstraintChecker} is a {@link IReducingContraintChecker} implementation. This constraint checker ensures that there is never more than the specified number of elements
 * from a {@link SlotGroup} present in the {@link ISequences} solution. However this is a {@link IReducingContraintChecker} and the initial {@link ISequences} is used to relax these constraints to the
 * current count. This permits some initial violation of the {@link SlotGroup} counts, but does not allow the violation to get worse. I.e. this constraint only allows the optimiser to make changes
 * which preserve the current state or improve it.
 * 
 * As an {@link IReducingContraintChecker} state is retained.
 * 
 * @author Simon Goodall
 * 
 */
public class SlotGroupCountConstraintChecker implements IReducingContraintChecker {
	private final String name;

	private IVesselProvider vesselProvider;
	private ISlotGroupCountProvider slotGroupProvider;

	/**
	 * Small class to track slot group element count.
	 * 
	 */
	private class SlotGroupTracker {

		int currentCount;
		int constraintedCount;
		int permittedCount;
	}

	private final Map<SlotGroup, SlotGroupTracker> trackers = new HashMap<SlotGroup, SlotGroupTracker>();

	public SlotGroupCountConstraintChecker(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

		this.slotGroupProvider = optimisationData.getDataComponentProvider(SchedulerConstants.DCP_slotGroupProvider, ISlotGroupCountProvider.class);
		this.vesselProvider = optimisationData.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);

		// Initialise state. Create tracker for each slot group and record the "count";
		for (final SlotGroup group : slotGroupProvider.getGroups()) {
			final SlotGroupTracker tracker = new SlotGroupTracker();
			tracker.currentCount = 0;
			tracker.permittedCount = 0;
			tracker.constraintedCount = group.getCount();
			trackers.put(group, tracker);
		}
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {

		// Reset counters;
		for (final SlotGroupTracker tracker : trackers.values()) {
			tracker.currentCount = 0;
		}

		for (final IResource resource : sequences.getResources()) {

			final ISequence sequence = sequences.getSequence(resource);
			final IVessel vessel = vesselProvider.getVessel(resource);

			final boolean isVirtualVessel = vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE;
			final boolean checkSequence = (!isVirtualVessel) || (isVirtualVessel && sequence.size() > 1);
			if (checkSequence) {
				for (final ISequenceElement element : sequence) {
					final Collection<SlotGroup> groups = slotGroupProvider.getGroupsForElement(element);
					for (final SlotGroup group : groups) {
						final SlotGroupTracker tracker = trackers.get(group);
						// Increment group count and reject solution if we break our permitted count limit.
						if (++tracker.currentCount > tracker.permittedCount) {
							if (messages != null) {
								// TODO: Add a message
							}
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public void sequencesAccepted(final ISequences sequences) {

		// Reset current counts to zero;
		for (final SlotGroupTracker tracker : trackers.values()) {
			tracker.currentCount = 0;
		}

		// Loop through and update the current count;
		for (final IResource resource : sequences.getResources()) {

			final ISequence sequence = sequences.getSequence(resource);
			final IVessel vessel = vesselProvider.getVessel(resource);

			// Virtual vessels need special treatment. If there is only one element on the route, then it is considered not to be used.
			final boolean isVirtualVessel = vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE;
			final boolean checkSequence = (!isVirtualVessel) || (isVirtualVessel && sequence.size() > 1);
			if (checkSequence) {
				for (final ISequenceElement element : sequence) {
					final Collection<SlotGroup> groups = slotGroupProvider.getGroupsForElement(element);
					for (final SlotGroup group : groups) {
						final SlotGroupTracker tracker = trackers.get(group);
						tracker.currentCount++;
					}
				}
			}
		}

		// Finally, set the new permitted count value - make sure it is not less than the specified count value.
		for (final SlotGroupTracker tracker : trackers.values()) {
			tracker.permittedCount = Math.max(tracker.constraintedCount, tracker.currentCount);
		}
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}
}
