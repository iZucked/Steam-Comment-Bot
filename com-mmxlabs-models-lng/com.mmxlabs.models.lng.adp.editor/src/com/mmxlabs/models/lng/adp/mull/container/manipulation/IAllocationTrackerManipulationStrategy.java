/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;

@NonNullByDefault
public interface IAllocationTrackerManipulationStrategy {
	public void dropAllocation(final long allocationDrop, final IAllocationTracker allocationTracker);

	public void undo(final ICargoBlueprint cargoBlueprint, final IAllocationTracker allocationTracker);

	public void dropFixedLoad(final Cargo cargo, final IAllocationTracker allocationTracker);

	public boolean satisfiesAacq(final IAllocationTracker allocationTracker);

	public boolean bucketSatisfiesAacq(final IAllocationTracker allocationTracker);
}
