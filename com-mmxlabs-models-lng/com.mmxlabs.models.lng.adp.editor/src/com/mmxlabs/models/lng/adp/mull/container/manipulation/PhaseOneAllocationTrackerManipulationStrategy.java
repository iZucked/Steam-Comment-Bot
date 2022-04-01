package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.cargo.Cargo;

@NonNullByDefault
public class PhaseOneAllocationTrackerManipulationStrategy implements IAllocationTrackerManipulationStrategy {

	@Override
	public void dropAllocation(final long allocationDrop, final IAllocationTracker allocationTracker) {
		allocationTracker.decreaseRunningAllocation((int) allocationDrop);
		// DES Market tracker should not need to do this since the allocated aacq is ignored
		allocationTracker.incrementCurrentAllocatedAacq();
	}

	@Override
	public void undo(final ICargoBlueprint cargoBlueprint, final IAllocationTracker allocationTracker) {
		if (cargoBlueprint.getAllocationTracker() == allocationTracker) {
			allocationTracker.increaseRunningAllocation(cargoBlueprint.getAllocatedVolume());
			// DES Market tracker should not need to do this since the allocated aacq is ignored
			allocationTracker.decrementCurrentAllocatedAacq();
		}
	}

	@Override
	public void dropFixedLoad(final Cargo cargo, final IAllocationTracker allocationTracker) {
		if (allocationTracker.matches(cargo)) {
			final int expectedVolumeLoaded = cargo.getSlots().get(0).getSlotOrDelegateMaxQuantity();
			allocationTracker.decreaseRunningAllocation(expectedVolumeLoaded);
			// DES Market tracker should not need to do this since the allocated aacq is ignored
			allocationTracker.incrementCurrentAllocatedAacq();
		}
	}

	@Override
	public boolean satisfiesAacq(final IAllocationTracker allocationTracker) {
		return allocationTracker.getAacq() == allocationTracker.getCurrentAllocatedAacq();
	}

	@Override
	public boolean bucketSatisfiesAacq(final IAllocationTracker allocationTracker) {
		return true;
	}

}
