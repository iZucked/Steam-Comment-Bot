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
public interface IMudContainerManipulationStrategy {

	public void undo(final ICargoBlueprint cargoBlueprint, final IMudContainer mudContainer);

	public void dropFixedLoad(final Cargo cargo, final IMudContainer mudContainer);

	public IAllocationTracker calculateMudAllocationTracker(final IMudContainer mudContainer);
}
