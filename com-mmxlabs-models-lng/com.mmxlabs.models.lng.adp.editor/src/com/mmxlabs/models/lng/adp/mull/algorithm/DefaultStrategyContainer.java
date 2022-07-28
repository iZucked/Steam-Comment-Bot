/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.IMullStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.DefaultAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.DefaultManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.DefaultMullComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;

@NonNullByDefault
public class DefaultStrategyContainer implements IMullStrategyContainer {
	private final IMudContainerManipulationStrategy mudContainerManipulationStrategy = new DefaultManipulationStrategy();
	private final IAllocationTrackerManipulationStrategy allocationTrackerManipulationStrategy = new DefaultAllocationTrackerManipulationStrategy();
	private final IMullComparatorWrapper comparatorWrapper = new DefaultMullComparatorWrapper();
	
	@Override
	public IMudContainerManipulationStrategy getMudContainerManipulationStrategy() {
		return mudContainerManipulationStrategy;
	}
	@Override
	public IAllocationTrackerManipulationStrategy getAllocationTrackerManipulationStrategy() {
		return allocationTrackerManipulationStrategy;
	}
	@Override
	public IMullComparatorWrapper getComparatorWrapper(InventoryGlobalState inventoryGlobalState, AlgorithmState algorithmState, GlobalStatesContainer globalStates) {
		return comparatorWrapper;
	}
}
