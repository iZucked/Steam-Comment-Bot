/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.cargo.Cargo;

@NonNullByDefault
public class DefaultShippedManipulationStrategy implements IMudContainerManipulationStrategy {

	@Override
	public void undo(@NonNull ICargoBlueprint cargoBlueprint, @NonNull IMudContainer mudContainer) {
		throw new IllegalStateException("Vanilla should not undo cargoes");

	}

	@Override
	public void dropFixedLoad(@NonNull Cargo cargo, @NonNull IMudContainer mudContainer) {
		
	}

	@Override
	public @NonNull IAllocationTracker calculateMudAllocationTracker(@NonNull IMudContainer mudContainer) {
		return mudContainer.getDesMarketTracker();
	}

}
