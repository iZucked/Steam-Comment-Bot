/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.inventory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lingo.reports.views.standard.inventory.InventoryLevel;
import com.mmxlabs.lingo.reports.views.standard.inventory.InventoryUtils;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

public class InventoryJSONGenerator {
	public static InventoryExportModel createInventoryData(CargoModel cargoModel, ScheduleModel scheduleModel, Port port) {
		// check schedule not null, check
		final List<InventoryLevel> invLevels = InventoryUtils.createInventoryLevels(scheduleModel, port);
		
		InventoryExportModel newExportModel = new InventoryExportModel();
		List<InventoryInput> newInputs = new LinkedList<InventoryInput>();
		List<InventoryOutput> newOutputs = new LinkedList<InventoryOutput>();
		List<InventoryLevelPerDay> newLevels = new LinkedList<InventoryLevelPerDay>();
		for(final InventoryLevel level : invLevels) {
			InventoryInput input = new InventoryInput();
			InventoryOutput output = new InventoryOutput();
			InventoryLevelPerDay lvl = new InventoryLevelPerDay();
					
			input.setDate(level.date);
					
			input.setReliability(100.0);
			input.setVolume(level.feedIn - level.feedOut);
			newInputs.add(input);
					
			if (level.dischargeId != "") {
				output.setDate(level.date);
				output.setVolume(Math.abs(level.cargoOut));
				output.setEventName(level.dischargeId);
				newOutputs.add(output);
			}
			
			lvl.setDate(level.date);
			lvl.setChange(Math.abs(level.changeInM3));
			lvl.setVolume(level.runningTotal);
			newLevels.add(lvl);
		}
		newExportModel.setLevels(newLevels);
		newExportModel.setInput(newInputs);
		newExportModel.setOutput(newOutputs);
		
		setMinMaxVolume(newExportModel, scheduleModel);
		return newExportModel;
	}
	
	public static void setMinMaxVolume(final InventoryExportModel exportModel, final ScheduleModel scheduleModel) {
		final InventoryEvents event = scheduleModel.getSchedule().getInventoryLevels().get(0);
		final InventoryChangeEvent ce = event.getEvents().get(0);
		exportModel.setMaxVolume(ce.getCurrentMax());
		exportModel.setMinVolume(ce.getCurrentMin());
	}
	
	private static void jsonOutput(Object inventoryLevel) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), inventoryLevel);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
