/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryGlobalState;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.FinalPhaseAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.FinalPhaseManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.FinalPhaseMullComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;

@NonNullByDefault
public class FinalPhaseStrategyContainer implements IMullStrategyContainer {

	private final IMudContainerManipulationStrategy mudContainerManipulationStrategy = new FinalPhaseManipulationStrategy();
	private final IAllocationTrackerManipulationStrategy allocationTrackerManipulationStrategy = new FinalPhaseAllocationTrackerManipulationStrategy();
	
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
		return new FinalPhaseMullComparatorWrapper();
	}

}
