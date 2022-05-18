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
public class PhaseOneAAllocationTrackerManipulationStrategy implements IAllocationTrackerManipulationStrategy {

	@Override
	public void dropAllocation(long allocationDrop, IAllocationTracker allocationTracker) {
		allocationTracker.decreaseRunningAllocation((int) allocationDrop);
		allocationTracker.incrementCurrentAllocatedAacq();
	}

	@Override
	public void undo(ICargoBlueprint cargoBlueprint, IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Phase 1a should not have to undo cargoes");
	}

	@Override
	public void dropFixedLoad(Cargo cargo, IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Phase 1a should not be considering fixed cargoes");
		
	}

	@Override
	public boolean satisfiesAacq(IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Phase 1a should not be considering AACQ satisfaction");
	}

	@Override
	public boolean bucketSatisfiesAacq(IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Phase 1a should not be considering AACQ satisfaction");
	}

	@Override
	public int calculateExpectedBoiloff(Vessel vessel, int loadDuration, IAllocationTracker allocationTracker) {
		return (int) (loadDuration * (vessel.getLadenAttributes().getVesselOrDelegateInPortNBORate() / 24.0));
	}

}
