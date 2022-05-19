package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class DefaultShippedInventoryBasedGenerationAlgorithm extends MullAlgorithm {

	public DefaultShippedInventoryBasedGenerationAlgorithm(final LNGScenarioModel sm, GlobalStatesContainer globalStatesContainer, AlgorithmState algorithmState, List<@NonNull InventoryLocalState> inventoryLocalStates) {
		super(globalStatesContainer, algorithmState, inventoryLocalStates);
		final Set<Vessel> vesselsInFleet = sm.getCargoModel().getVesselAvailabilities().stream().map(VesselAvailability::getVessel).collect(Collectors.toSet());
		for (final InventoryLocalState inventoryLocalState : this.inventoryLocalStates) {
			for (final IMudContainer mudContainer : inventoryLocalState.getMullContainer().getMudContainers()) {
				for (final IAllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
					if (allocationTracker.getVessels().stream().allMatch(vesselsInFleet::contains)) {
						allocationTracker.setVesselSharing(true);
					} else {
						allocationTracker.setVesselSharing(false);
					}
				}
			}
		}
	}

	@Override
	protected void runStartOfMonthTasks(LocalDateTime currentDateTime, InventoryLocalState inventoryLocalState) {
		// Do nothing
	}
}
