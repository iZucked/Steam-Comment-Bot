/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class HarmonisationPhaseAllocationTrackerManipulationStrategy implements IAllocationTrackerManipulationStrategy {

	@Override
	public void dropAllocation(final long allocationDrop, final IAllocationTracker allocationTracker) {
		allocationTracker.decreaseRunningAllocation((int) allocationDrop);
		allocationTracker.incrementCurrentMonthLifted();
	}

	@Override
	public void undo(final ICargoBlueprint cargoBlueprint, final IAllocationTracker allocationTracker) {
		if (cargoBlueprint.getAllocationTracker() == allocationTracker) {
			allocationTracker.increaseRunningAllocation(cargoBlueprint.getAllocatedVolume());
			allocationTracker.decrementCurrentAllocatedAacq();
			allocationTracker.decrementCurrentMonthLifted();
		}

	}

	@Override
	public void dropFixedLoad(final Cargo cargo, final IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Fixed cargoes should be handled directly");
	}

	@Override
	public boolean satisfiesAacq(final IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Harmonisation phase should not be concerned with AACQ satisfaction");
	}

	@Override
	public boolean bucketSatisfiesAacq(final IAllocationTracker allocationTracker) {
		throw new IllegalStateException();
	}

	@Override
	public int calculateExpectedBoiloff(Vessel vessel, int loadDuration, IAllocationTracker allocationTracker) {
		return (int) (loadDuration * (vessel.getLadenAttributes().getVesselOrDelegateInPortNBORate() / 24.0));
	}

}
