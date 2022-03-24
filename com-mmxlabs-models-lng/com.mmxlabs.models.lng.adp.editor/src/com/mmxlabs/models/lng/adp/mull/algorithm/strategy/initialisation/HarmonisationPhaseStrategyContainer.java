package com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryGlobalState;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.HarmonisationPhaseAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.HarmonisationPhaseManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.HarmonisationPhaseMullComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;

@NonNullByDefault
public class HarmonisationPhaseStrategyContainer implements IMullStrategyContainer {

	private final IMudContainerManipulationStrategy mudContainerManipulationStrategy = new HarmonisationPhaseManipulationStrategy();
	private final IAllocationTrackerManipulationStrategy allocationTrackerManipulationStrategy = new HarmonisationPhaseAllocationTrackerManipulationStrategy();

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
		return new HarmonisationPhaseMullComparatorWrapper(algorithmState.getVesselUsageLookBehind(), algorithmState.getVesselUsageLookAhead(), globalStates.getMullGlobalState().getCargoVolume(),
				algorithmState.getFirstPartyVessels(), globalStates.getMullGlobalState().getFullCargoLotValue(), inventoryGlobalState.getLoadDuration());
	}

}
