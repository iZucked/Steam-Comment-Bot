package com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryGlobalState;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.PhaseOneAAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.PhaseOneAManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.PhaseOneAMullComparatorWrapper;

@NonNullByDefault
public class PhaseOneAStrategyContainer implements IMullStrategyContainer {

	private final IMudContainerManipulationStrategy mudContainerManipulationStrategy = new PhaseOneAManipulationStrategy();
	private final IAllocationTrackerManipulationStrategy allocationTrackerManipulationStrategy = new PhaseOneAAllocationTrackerManipulationStrategy();

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
		return new PhaseOneAMullComparatorWrapper(algorithmState.getVesselUsageLookBehind(), algorithmState.getVesselUsageLookAhead(),
				globalStates.getMullGlobalState().getCargoVolume(), algorithmState.getFirstPartyVessels(), globalStates.getMullGlobalState().getFullCargoLotValue(),
				inventoryGlobalState.getLoadDuration());
	}

}
