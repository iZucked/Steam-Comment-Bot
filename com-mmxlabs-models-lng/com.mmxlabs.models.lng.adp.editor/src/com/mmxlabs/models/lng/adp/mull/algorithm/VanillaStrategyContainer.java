/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;


import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.IMullStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.VanillaAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.VanillaManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.VanillaMullComparatorWrapper;

@NonNullByDefault
public class VanillaStrategyContainer implements IMullStrategyContainer {

	private final IMudContainerManipulationStrategy mudContainerManipulationStrategy = new VanillaManipulationStrategy();
	private final IAllocationTrackerManipulationStrategy allocationTrackerManipulationStrategy = new VanillaAllocationTrackerManipulationStrategy();
	private final IMullComparatorWrapper comparatorWrapper = new VanillaMullComparatorWrapper();
	
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
