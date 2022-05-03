/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public class DefaultMudContainer extends AbstractMudContainer {

	public DefaultMudContainer(final BaseLegalEntity entity, final double relativeEntitlement, final long initialAllocation, final List<IAllocationTracker> allocationTrackers,
			final IMudContainerManipulationStrategy mudContainerManipulationStrategy) {
		super(entity, relativeEntitlement, initialAllocation, allocationTrackers, mudContainerManipulationStrategy);
	}

	

}
