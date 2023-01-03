/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class VanillaMullAlgorithm extends MullAlgorithm {

	private final Map<InventoryLocalState, IMudContainer> mullCache = new HashMap<>();

	public VanillaMullAlgorithm(GlobalStatesContainer globalStatesContainer, AlgorithmState algorithmState, List<InventoryLocalState> inventoryLocalStates) {
		super(globalStatesContainer, algorithmState, inventoryLocalStates, false);
		for (final InventoryLocalState inventoryLocalState : this.inventoryLocalStates) {
			for (final IMudContainer mudContainer : inventoryLocalState.getMullContainer().getMudContainers()) {
				for (final IAllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
					allocationTracker.setVesselSharing(false);
				}
			}
			mullCache.put(inventoryLocalState, inventoryLocalState.getMullContainer().calculateMull(globalStatesContainer.getAdpGlobalState().getStartDateTime()));
		}
	}

	@Override
	protected void runFixedCargoHandlingTasks(LocalDateTime currentDateTime, InventoryLocalState inventoryLocalState) {
		// Hacky override of this method
		if (currentDateTime.getHour() == 0) {
			final IMudContainer mudContainer = inventoryLocalState.getMullContainer().calculateMull(currentDateTime);
			mullCache.put(inventoryLocalState, mudContainer);
		}
	}

	@Override
	protected IMudContainer calculateMull(InventoryLocalState inventoryLocalState, LocalDateTime currentDateTime) {
		return mullCache.get(inventoryLocalState);
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
		final int currentAllocationDrop =  (int) (assignedVessel.getVesselOrDelegateCapacity()*assignedVessel.getVesselOrDelegateFillCapacity()) - assignedVessel.getVesselOrDelegateSafetyHeel();
		return Pair.of(assignedVessel, currentAllocationDrop);
	}
}
