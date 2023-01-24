/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class FinalPhaseAllocationTrackerManipulationStrategy implements IAllocationTrackerManipulationStrategy {

	@Override
	public void dropAllocation(final long allocationDrop, final IAllocationTracker allocationTracker) {
		allocationTracker.decreaseRunningAllocation((int) allocationDrop);
		allocationTracker.incrementCurrentAllocatedAacq();
	}

	@Override
	public void undo(final ICargoBlueprint cargoBlueprint, final IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Cargoes should not have to be undone in final phase");
	}

	@Override
	public void dropFixedLoad(final Cargo cargo, final IAllocationTracker allocationTracker) {
		if (allocationTracker.matches(cargo)) {
			final int expectedVolumeLoaded = cargo.getSortedSlots().get(0).getSlotOrDelegateMaxQuantity();
			allocationTracker.decreaseRunningAllocation(expectedVolumeLoaded);
		}
	}

	@Override
	public boolean satisfiesAacq(final IAllocationTracker allocationTracker) {
		return allocationTracker.getAacq() == allocationTracker.getCurrentAllocatedAacq();
	}

	@Override
	public boolean bucketSatisfiesAacq(final IAllocationTracker allocationTracker) {
		return this.satisfiesAacq(allocationTracker);
	}

	@Override
	public int calculateExpectedBoiloff(Vessel vessel, int loadDuration, IAllocationTracker allocationTracker) {
		return (int) (loadDuration * (vessel.getLadenAttributes().getVesselOrDelegateInPortNBORate() / 24.0));
	}

}
