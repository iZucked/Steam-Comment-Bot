/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.Vessel;

public class DefaultShippedAllocationTrackerManipulationStrategy implements IAllocationTrackerManipulationStrategy {

	@Override
	public void dropAllocation(long allocationDrop, @NonNull IAllocationTracker allocationTracker) {
		// Do nothing
	}

	@Override
	public void undo(@NonNull ICargoBlueprint cargoBlueprint, @NonNull IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Vanilla model should only be operating at an entity level");
	}

	@Override
	public void dropFixedLoad(@NonNull Cargo cargo, @NonNull IAllocationTracker allocationTracker) {
		throw new IllegalStateException("Vanilla model should only be operating at an entity level");
	}

	@Override
	public boolean satisfiesAacq(@NonNull IAllocationTracker allocationTracker) {
		return true;
	}

	@Override
	public boolean bucketSatisfiesAacq(@NonNull IAllocationTracker allocationTracker) {
		return true;
	}

	@Override
	public int calculateExpectedBoiloff(@NonNull Vessel vessel, int loadDuration, @NonNull IAllocationTracker allocationTracker) {
		return 0;
	}

}
