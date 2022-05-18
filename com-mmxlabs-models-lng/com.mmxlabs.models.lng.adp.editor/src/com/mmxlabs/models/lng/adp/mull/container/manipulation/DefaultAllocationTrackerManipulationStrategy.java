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
public class DefaultAllocationTrackerManipulationStrategy implements IAllocationTrackerManipulationStrategy {

	@Override
	public void dropAllocation(long allocationDrop, IAllocationTracker allocationTracker) {
		// Do nothing
	}

	@Override
	public void undo(ICargoBlueprint cargoBlueprint, IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Vanilla model should only be operating at an entity level");
	}

	@Override
	public void dropFixedLoad(Cargo cargo, IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Vanilla model should only be operating at an entity level");
	}

	@Override
	public boolean satisfiesAacq(IAllocationTracker allocationTracker) {
		return true;
	}

	@Override
	public boolean bucketSatisfiesAacq(IAllocationTracker allocationTracker) {
		return true;
	}

	@Override
	public int calculateExpectedBoiloff(Vessel vessel, int loadDuration, IAllocationTracker allocationTracker) {
		return 0;
	}



}
