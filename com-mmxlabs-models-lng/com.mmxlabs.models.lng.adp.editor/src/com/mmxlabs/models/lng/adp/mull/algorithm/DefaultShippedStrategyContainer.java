package com.mmxlabs.models.lng.adp.mull.algorithm;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation.IMullStrategyContainer;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.DefaultMullComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.DefaultShippedAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.DefaultShippedManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;

public class DefaultShippedStrategyContainer implements IMullStrategyContainer {

	private final IMudContainerManipulationStrategy mudContainerManipulationStrategy = new DefaultShippedManipulationStrategy();
	private final IAllocationTrackerManipulationStrategy allocationTrackerManipulationStrategy = new DefaultShippedAllocationTrackerManipulationStrategy();
	private final IMullComparatorWrapper comparatorWrapper = new DefaultMullComparatorWrapper();
	
	@Override
	public @NonNull IMudContainerManipulationStrategy getMudContainerManipulationStrategy() {
		return mudContainerManipulationStrategy;
	}

	@Override
	public @NonNull IAllocationTrackerManipulationStrategy getAllocationTrackerManipulationStrategy() {
		return allocationTrackerManipulationStrategy;
	}

	@Override
	public @NonNull IMullComparatorWrapper getComparatorWrapper(@NonNull InventoryGlobalState inventoryGlobalState, @NonNull AlgorithmState algorithmState,
			@NonNull GlobalStatesContainer globalStates) {
		return comparatorWrapper;
	}

}
