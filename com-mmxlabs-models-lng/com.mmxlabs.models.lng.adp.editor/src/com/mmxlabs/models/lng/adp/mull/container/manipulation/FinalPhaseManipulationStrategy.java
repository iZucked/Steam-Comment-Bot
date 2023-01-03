/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.cargo.Cargo;

@NonNullByDefault
public class FinalPhaseManipulationStrategy implements IMudContainerManipulationStrategy {

	@Override
	public void undo(ICargoBlueprint cargoBlueprint, IMudContainer mudContainer) {
		throw new IllegalStateException("Final phase should not have to undo cargoes");
		
	}

	@Override
	public void dropFixedLoad(Cargo cargo, IMudContainer mudContainer) {
		throw new IllegalStateException("Final phase should not be interacting with cargoes");
	}

	@Override
	public IAllocationTracker calculateMudAllocationTracker(IMudContainer mudContainer) {
		throw new IllegalStateException("Final phase should not have to calculate MUD");
	}

}
