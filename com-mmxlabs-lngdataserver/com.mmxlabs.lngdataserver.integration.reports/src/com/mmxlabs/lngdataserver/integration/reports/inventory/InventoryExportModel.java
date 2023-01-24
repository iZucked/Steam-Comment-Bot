/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.inventory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.common.io.Files;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

@SchemaVersion(1)
public class InventoryExportModel {
	
	public List<InventoryInput> input = null;
	public List<InventoryOutput> output = null;
	public List<InventoryLevelPerDay> levels = null;
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
	

	public static void main(String[] args) throws Exception {
		String schema = new SchemaGenerator().generateHubSchema(InventoryExportModel.class, Mode.SUMMARY);
		System.out.println(schema);
		Files.write(schema, new File("target/invent.json"), StandardCharsets.UTF_8);
	}
}
