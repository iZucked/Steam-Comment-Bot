/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.inventory;

import java.util.List;

public class InventoryExportModel {
	List<InventoryInput> input = null;
	List<InventoryOutput> output = null;
	List<InventoryLevelPerDay> levels = null;
	public int minVolume = 0;
	public int maxVolume = 0;

	public List<InventoryInput> getInput() {
		return input;
	}
	public void setInput(List<InventoryInput> input) {
		this.input = input;
	}
	public List<InventoryOutput> getOutput() {
		return output;
	}
	public void setOutput(List<InventoryOutput> output) {
		this.output = output;
	}
	public List<InventoryLevelPerDay> getLevels() {
		return levels;
	}
	public void setLevels(List<InventoryLevelPerDay> levels) {
		this.levels = levels;
	}
	public int getMinVolume() {
		return minVolume;
	}
	public void setMinVolume(int minVolume) {
		this.minVolume = minVolume;
	}
	public int getMaxVolume() {
		return maxVolume;
	}
	public void setMaxVolume(int maxVolume) {
		this.maxVolume = maxVolume;
	}
}
