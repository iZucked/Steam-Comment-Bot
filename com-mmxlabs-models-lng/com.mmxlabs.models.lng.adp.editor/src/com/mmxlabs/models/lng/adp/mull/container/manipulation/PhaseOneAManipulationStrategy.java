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

@NonNullByDefault
public class PhaseOneAManipulationStrategy implements IMudContainerManipulationStrategy {

	private final Comparator<IAllocationTracker> comparator = (t1, t2) -> {
		return Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation());
	};
	
	@Override
	public void undo(ICargoBlueprint cargoBlueprint, IMudContainer mudContainer) {
		throw new IllegalStateException("Phase 1a should not have to undo cargoes");
	}

	@Override
	public void dropFixedLoad(Cargo cargo, IMudContainer mudContainer) {
		throw new IllegalStateException("Phase 1a should not be considering fixed cargoes");
	}

	@Override
	public IAllocationTracker calculateMudAllocationTracker(IMudContainer mudContainer) {
		return mudContainer.getAllocationTrackers().stream().max(comparator).get();
	}



}
