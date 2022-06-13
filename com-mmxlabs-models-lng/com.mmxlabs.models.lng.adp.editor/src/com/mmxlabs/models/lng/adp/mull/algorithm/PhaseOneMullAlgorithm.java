/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;

@NonNullByDefault
public class PhaseOneMullAlgorithm extends MullAlgorithm {

	public PhaseOneMullAlgorithm(GlobalStatesContainer globalStatesContainer, AlgorithmState algorithmState, List<InventoryLocalState> inventoryLocalStates) {
		super(globalStatesContainer, algorithmState, inventoryLocalStates, true);
	}

	@Override
	protected void runPostAllocationDropTasks(final IMullContainer mullContainer, final IMudContainer mudContainer, final IAllocationTracker allocationTracker) {
		mudContainer.reassessAACQSatisfaction();
	}
}
