package com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryGlobalState;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;

@NonNullByDefault
public interface IMullStrategyContainer {
	public IMudContainerManipulationStrategy getMudContainerManipulationStrategy();

	public IAllocationTrackerManipulationStrategy getAllocationTrackerManipulationStrategy();

	public IMullComparatorWrapper getComparatorWrapper(final InventoryGlobalState inventoryGlobalState, final AlgorithmState algorithmState, final GlobalStatesContainer globalStates);
}
