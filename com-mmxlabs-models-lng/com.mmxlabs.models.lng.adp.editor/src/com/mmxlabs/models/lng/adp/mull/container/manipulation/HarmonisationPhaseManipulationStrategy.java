package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.cargo.Cargo;

@NonNullByDefault
public class HarmonisationPhaseManipulationStrategy implements IMudContainerManipulationStrategy {

	private final Comparator<IAllocationTracker> comparator = (t1, t2) -> {
		final boolean t1Satisfied = t1.satisfiedMonthlyAllocation();
		final boolean t2Satisfied = t2.satisfiedMonthlyAllocation();
		if (!t1Satisfied && t2Satisfied) {
			return 1;
		} else if (t1Satisfied && !t2Satisfied) {
			return -1;
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
		throw new IllegalStateException("Harmonisation phase cargoes should be handled directly.");
	}

	@Override
	public IAllocationTracker calculateMudAllocationTracker(final IMudContainer mudContainer) {
		return mudContainer.hasMetAllHardAacqs() ? mudContainer.getDesMarketTracker() : mudContainer.getAllocationTrackers().stream().max(comparator).get();
	}

}
