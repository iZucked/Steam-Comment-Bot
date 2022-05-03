/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryGlobalState;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.PhaseOneAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.PhaseOneManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.PhaseOneMullComparatorWrapper;

@NonNullByDefault
public class PhaseOneStrategyContainer implements IMullStrategyContainer {

	private final IMudContainerManipulationStrategy mudContainerManipulationStrategy = new PhaseOneManipulationStrategy();
	private final IAllocationTrackerManipulationStrategy allocationTrackerManipulationStrategy = new PhaseOneAllocationTrackerManipulationStrategy();
	
	@Override
	public IMudContainerManipulationStrategy getMudContainerManipulationStrategy() {
		return this.mudContainerManipulationStrategy;
	}

	@Override
	public IAllocationTrackerManipulationStrategy getAllocationTrackerManipulationStrategy() {
		return this.allocationTrackerManipulationStrategy;
	}

	@Override
	public IMullComparatorWrapper getComparatorWrapper(final InventoryGlobalState inventoryGlobalState, final AlgorithmState algorithmState, final GlobalStatesContainer globalStates) {
		return new PhaseOneMullComparatorWrapper(algorithmState.getVesselUsageLookBehind(), algorithmState.getVesselUsageLookAhead(),
				globalStates.getMullGlobalState().getCargoVolume(), algorithmState.getFirstPartyVessels(), globalStates.getMullGlobalState().getFullCargoLotValue(),
				inventoryGlobalState.getLoadDuration());
	}

}
