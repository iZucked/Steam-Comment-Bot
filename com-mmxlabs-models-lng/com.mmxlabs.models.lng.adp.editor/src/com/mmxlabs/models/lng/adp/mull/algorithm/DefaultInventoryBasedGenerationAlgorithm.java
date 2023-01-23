/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
public class DefaultInventoryBasedGenerationAlgorithm extends MullAlgorithm {

	public DefaultInventoryBasedGenerationAlgorithm(final GlobalStatesContainer globalStatesContainer, final AlgorithmState algorithmState, final List<InventoryLocalState> inventoryLocalStates) {
		super(globalStatesContainer, algorithmState, inventoryLocalStates, true);
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
	protected Vessel calculateAssignedVessel(final LocalDateTime currentDateTime, final List<Vessel> vessels) {
		return vessels.get(0);
	}

	@Override
	protected Pair<@Nullable Vessel, Integer> getVesselAndAllocationDrop(LocalDateTime currentDateTime, IAllocationTracker allocationTracker, InventoryLocalState inventoryLocalState) {
		final Vessel assignedVessel = calculateAssignedVessel(currentDateTime, allocationTracker.getVessels());
		final int currentAllocationDrop = (int) (assignedVessel.getVesselOrDelegateCapacity()*assignedVessel.getVesselOrDelegateFillCapacity()) - assignedVessel.getVesselOrDelegateSafetyHeel();
		return Pair.of(assignedVessel, currentAllocationDrop);
	}

}
