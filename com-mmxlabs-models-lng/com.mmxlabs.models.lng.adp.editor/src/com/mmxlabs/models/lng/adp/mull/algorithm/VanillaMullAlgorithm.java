/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class VanillaMullAlgorithm extends MullAlgorithm {

	public VanillaMullAlgorithm(GlobalStatesContainer globalStatesContainer, AlgorithmState algorithmState, List<InventoryLocalState> inventoryLocalStates) {
		super(globalStatesContainer, algorithmState, inventoryLocalStates);
		for (final InventoryLocalState inventoryLocalState : this.inventoryLocalStates) {
			for (final IMudContainer mudContainer : inventoryLocalState.getMullContainer().getMudContainers()) {
				for (final IAllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
					allocationTracker.setVesselSharing(false);
				}
			}
		}
	}

	@Override
	protected void runStartOfMonthTasks(LocalDateTime currentDateTime, InventoryLocalState inventoryLocalState) {
		// Do nothing
	}

	@Override
	protected Vessel calculateAssignedVessel(LocalDateTime currentDateTime, List<Vessel> vessels) {
		// There should only be one vessel
		return vessels.get(0);
	}

	@Override
	protected Pair<@Nullable Vessel, Integer> getVesselAndAllocationDrop(LocalDateTime currentDateTime, IAllocationTracker allocationTracker, InventoryLocalState inventoryLocalState) {
		final Vessel assignedVessel = calculateAssignedVessel(currentDateTime, allocationTracker.getVessels());
		final int currentAllocationDrop =  (int) (assignedVessel.getVesselOrDelegateCapacity()*assignedVessel.getVesselOrDelegateFillCapacity()) - assignedVessel.getSafetyHeel();
		return Pair.of(assignedVessel, currentAllocationDrop);
	}
}
