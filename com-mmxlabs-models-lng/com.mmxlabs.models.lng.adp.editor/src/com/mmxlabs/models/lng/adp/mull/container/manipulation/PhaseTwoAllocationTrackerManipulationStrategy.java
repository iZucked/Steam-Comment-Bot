package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;

@NonNullByDefault
public class PhaseTwoAllocationTrackerManipulationStrategy implements IAllocationTrackerManipulationStrategy {

	@Override
	public void dropAllocation(final long allocationDrop, final IAllocationTracker allocationTracker) {
		allocationTracker.decreaseRunningAllocation((int) allocationDrop);
		allocationTracker.incrementCurrentAllocatedAacq();
	}

	@Override
	public void undo(final ICargoBlueprint cargoBlueprint, final IAllocationTracker allocationTracker) {
		if (cargoBlueprint.getAllocationTracker() == allocationTracker) {
			allocationTracker.increaseRunningAllocation(cargoBlueprint.getAllocatedVolume());
			allocationTracker.decrementCurrentAllocatedAacq();
		}
	}

	@Override
	public void dropFixedLoad(final Cargo cargo, final IAllocationTracker allocationTracker) {
		if (allocationTracker.matches(cargo)) {
			final int expectedVolumeLoaded = cargo.getSlots().get(0).getSlotOrDelegateMaxQuantity();
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
}
