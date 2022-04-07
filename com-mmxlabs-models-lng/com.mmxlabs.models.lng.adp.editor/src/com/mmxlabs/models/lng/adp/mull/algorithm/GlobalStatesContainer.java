/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.cargo.Inventory;

@NonNullByDefault
public class GlobalStatesContainer {
	final AdpGlobalState adpGlobalState;
	final Map<Inventory, InventoryGlobalState> inventoryGlobalStates;
	final MullGlobalState mullGlobalState;

	public GlobalStatesContainer(final AdpGlobalState adpGlobalState, final List<InventoryGlobalState> inventoryGlobalStates, final MullGlobalState mullGlobalState) {
		this.adpGlobalState = adpGlobalState;
		this.inventoryGlobalStates = new HashMap<>();
		inventoryGlobalStates.forEach(gs -> this.inventoryGlobalStates.put(gs.getInventory(), gs));
		this.mullGlobalState = mullGlobalState;
	}

	public AdpGlobalState getAdpGlobalState() {
		return adpGlobalState;
	}

	public Map<Inventory, InventoryGlobalState> getInventoryGlobalStates() {
		return inventoryGlobalStates;
	}

	public MullGlobalState getMullGlobalState() {
		return mullGlobalState;
	}
}
