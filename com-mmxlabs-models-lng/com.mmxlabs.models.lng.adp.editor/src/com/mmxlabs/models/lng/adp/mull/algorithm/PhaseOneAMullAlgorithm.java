/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.MullUtil;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public class PhaseOneAMullAlgorithm extends MullAlgorithm implements IMonthEndTrackingAlgorithm {

	private final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Long>>> monthEndEntitlements;

	public PhaseOneAMullAlgorithm(GlobalStatesContainer globalStatesContainer, AlgorithmState algorithmState, List<InventoryLocalState> inventoryLocalStates) {
		super(globalStatesContainer, algorithmState, inventoryLocalStates);
		monthEndEntitlements = new HashMap<>();
		inventoryLocalStates.stream() //
				.map(InventoryLocalState::getMullContainer) //
				.forEach(mullContainer -> {
					final Inventory inventory = mullContainer.getInventory();
					final Map<BaseLegalEntity, Map<YearMonth, Long>> entityMonthEndEntitlements = new HashMap<>();
					monthEndEntitlements.put(inventory, entityMonthEndEntitlements);
					mullContainer.getMudContainers().forEach(mudContainer -> {
						entityMonthEndEntitlements.put(mudContainer.getEntity(), new HashMap<>());
					});
				});
	}

	@Override
	protected void runPostInnerLoopTasks(final LocalDateTime currentDateTime, final IMullContainer mullContainer) {
		if (MullUtil.isAtEndHourOfMonth(currentDateTime)) {
			final YearMonth currentYm = YearMonth.from(currentDateTime);
			for (final IMudContainer mudContainer : mullContainer.getMudContainers()) {
				monthEndEntitlements.get(mullContainer.getInventory()).get(mudContainer.getEntity()).put(currentYm, mudContainer.getRunningAllocation());
			}
		}
	}

	@Override
	public Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Long>>> getMonthEndEntitlements() {
		return this.monthEndEntitlements;
	}

}
