/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;

@NonNullByDefault
public class PhaseTwoManipulationStrategy implements IMudContainerManipulationStrategy {

	private final Comparator<IAllocationTracker> comparator = (t1, t2) -> {
		if (t1.satisfiesAacq()) {
			return -1;
		} else if (t2.satisfiesAacq()) {
			return 1;
		} else {
			return Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation());
		}
	};

	@Override
	public void undo(final ICargoBlueprint cargoBlueprint, final IMudContainer mudContainer) {
		if (cargoBlueprint.getMudContainer() == mudContainer) {
			mudContainer.increaseAllocation(cargoBlueprint.getAllocatedVolume());
			for (final IAllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
				allocationTracker.undo(cargoBlueprint);
			}
			mudContainer.reassessAACQSatisfaction();
		}
	}

	@Override
	public void dropFixedLoad(final Cargo cargo, final IMudContainer mudContainer) {
		final Slot<?> loadSlot = cargo.getSortedSlots().get(0);
		if (mudContainer.getEntity() == loadSlot.getEntity()) {
			final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
			mudContainer.dropFixedLoad(expectedVolumeLoaded);
			for (final IAllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
				allocationTracker.dropFixedLoad(cargo);
			}
			mudContainer.reassessAACQSatisfaction();
		}
	}

	@Override
	public IAllocationTracker calculateMudAllocationTracker(final IMudContainer mudContainer) {
		return mudContainer.hasMetAllHardAacqs() ? mudContainer.getDesMarketTracker() : mudContainer.getAllocationTrackers().stream().max(comparator).get();
	}

}
