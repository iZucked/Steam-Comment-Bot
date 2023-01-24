/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.LinkedList;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;

@NonNullByDefault
public class InventoryLocalState {

	private final InventoryGlobalState inventoryGlobalState;
	private final IRollingWindow rollingLoadWindow;
	private final IMullContainer mullContainer;
	private final LinkedList<ICargoBlueprint> cargoBlueprintsToGenerate;
	private int inventorySlotCounter = 0;

	public InventoryLocalState(final InventoryGlobalState inventoryGlobalState, final IRollingWindow rollingLoadWindow, final IMullContainer mullContainer) {
		this.inventoryGlobalState = inventoryGlobalState;
		this.rollingLoadWindow = rollingLoadWindow;
		this.mullContainer = mullContainer;
		this.cargoBlueprintsToGenerate = new LinkedList<>();
	}

	public InventoryGlobalState getInventoryGlobalState() {
		return this.inventoryGlobalState;
	}

	public IRollingWindow getRollingLoadWindow() {
		return rollingLoadWindow;
	}

	public IMullContainer getMullContainer() {
		return mullContainer;
	}

	public LinkedList<ICargoBlueprint> getCargoBlueprintsToGenerate() {
		return cargoBlueprintsToGenerate;
	}

	public int getInventorySlotCounter() {
		return inventorySlotCounter;
	}

	public void incrementInventorySlotCounter() {
		++this.inventorySlotCounter;
	}

	public void setInventorySlotCount(final int newValue) {
		this.inventorySlotCounter = newValue;
	}

}
