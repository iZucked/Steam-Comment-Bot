/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.inventory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class InventoryJSONGenerator {
	public static InventoryExportModel createInventoryData(CargoModel cargoModel, ScheduleModel scheduleModel, Port port) {
		// check schedule not null, check
		for (Inventory inventory : cargoModel.getInventoryModels()) {
			if (inventory.getPort() == port) {
				// check inventory inputs not null
				List<InventoryInput> inputs = getInputs(inventory);
				LocalDate latestInventoryDate = inputs.get(inputs.size()-1).date;
				List<InventoryOutput> outputs = getOutputs(scheduleModel, inventory, latestInventoryDate);
				Collections.sort(outputs, (a,b) -> a.date.compareTo(b.date));
				int currentLevel = 0;
				InventoryInput currentIn = inputs.size() > 0 ? inputs.get(0) : null;
				InventoryOutput currentOut = outputs.size() > 0 ? outputs.get(0) : null;
				LocalDate minDate = getMinDate(currentIn, currentOut);
				
				List<InventoryLevelPerDay> levels = getLevelsPerDay(inputs, outputs, currentLevel, currentIn, currentOut, minDate);
				InventoryExportModel exportModel = new InventoryExportModel();
				exportModel.setLevels(levels);
				exportModel.setInput(inputs);
				exportModel.setOutput(outputs);
//				jsonOutput(exportModel);
				return exportModel;
			}
		}
		return null;
	}

	private static List<InventoryLevelPerDay> getLevelsPerDay(List<InventoryInput> inputs, List<InventoryOutput> outputs, int currentLevel, InventoryInput currentIn, InventoryOutput currentOut,
			LocalDate minDate) {
		List<InventoryLevelPerDay> levels = new LinkedList<>();
		InventoryLevelPerDay level = new InventoryLevelPerDay();
		level.date = minDate;
		currentLevel = 0;
		int idxIn, idxOut; idxIn = idxOut = 0;
		while (idxIn < inputs.size() || idxOut < outputs.size()) {
			currentIn = idxIn < inputs.size() ? inputs.get(idxIn) : null;
			currentOut = idxOut < outputs.size() ? outputs.get(idxOut) : null;
			minDate = getMinDate(currentIn, currentOut);
			if (level.date != minDate) {
				levels.add(level);
				level = new InventoryLevelPerDay();
				level.date = minDate;
			}
			if (currentIn != null && currentIn.date == level.date) {
				currentLevel += currentIn.volume;
				level.change += currentIn.volume;
				level.volume = currentLevel;
				idxIn++;
			}
			if (currentOut != null && currentOut.date == level.date) {
				currentLevel -= currentOut.volume;
				level.change -= currentOut.volume;
				level.volume = currentLevel;
				idxOut++;
			}
		}
		levels.add(level);
		return levels;
	}

	private static int updateInventoryLevel(int currentLevel, InventoryInput currentIn, InventoryOutput currentOut, InventoryLevelPerDay level) {
		if (currentIn != null && currentIn.date == level.date) {
			currentLevel += currentIn.volume;
			level.change += currentIn.volume;
			level.volume = currentLevel;
		}
		if (currentOut != null && currentOut.date == level.date) {
			currentLevel += currentIn.volume;
			level.change += currentIn.volume;
			level.volume = currentLevel;
		}
		return currentLevel;
	}

	private static LocalDate getMinDate(InventoryInput currentIn, InventoryOutput currentOut) {
		return getMin(currentIn != null ? currentIn.date : null, currentOut != null ? currentOut.date : null);
	}

	private static List<InventoryInput> getInputs(Inventory inventory) {
		List<InventoryInput> inputs = new LinkedList<>();
		for (InventoryEventRow inventoryEventRow : inventory.getFeeds().stream()
												.sorted((a,b)->a.getStartDate()
												.compareTo(b.getStartDate()))
												.collect(Collectors.toList())) {
			InventoryInput input = new InventoryInput();
			input.date = inventoryEventRow.getStartDate();
			input.volume = inventoryEventRow.getReliableVolume();
			inputs.add(input);
		}
		return inputs;
	}

	private static List<InventoryOutput> getOutputs(ScheduleModel scheduleModel, Inventory inventory, LocalDate latestInventoryDate) {
		List<InventoryOutput> outputs = new LinkedList<>();
		final Schedule schedule = scheduleModel.getSchedule();
		for (final SlotAllocation slotAllocation : schedule.getSlotAllocations()) {
			final Slot slot = slotAllocation.getSlot();
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isDESPurchase()) {
					continue;
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (dischargeSlot.isFOBSale()) {
					continue;
				}
			}
			if (slotAllocation.getPort() == inventory.getPort()) {
				ZonedDateTime start = slotAllocation.getSlotVisit().getStart();
				final LocalDate date = start.toLocalDate();
				
				// don't look at slots after end of inventory
				if (latestInventoryDate.compareTo(date) < 0) {
					continue;
				}
				InventoryOutput output = new InventoryOutput();
				output.date = date;
				output.volume = slotAllocation.getPhysicalVolumeTransferred();
				output.setEventName(slot.getName());
				outputs.add(output);
			}
		}
		for (final OpenSlotAllocation slotAllocation : schedule.getOpenSlotAllocations()) {
			final Slot slot = slotAllocation.getSlot();
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isDESPurchase()) {
					continue;
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (dischargeSlot.isFOBSale()) {
					continue;
				}
			}
			if (slotAllocation.getSlot().getPort() == inventory.getPort()) {
				int change = slot.getSlotOrDelegateMaxQuantity();

				if (slot.getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.MMBTU) {
					if (slot instanceof LoadSlot) {
						double cv = ((LoadSlot) slot).getSlotOrDelegateCV();
						change = (int) (change / cv);
					} else {
						continue;
					}
				}

				final LocalDate date = slot.getWindowStart();
				InventoryOutput output = new InventoryOutput();
				output.date = date;
				output.volume = change;
				output.setEventName(slot.getName());
				outputs.add(output);
			}
		}
		return outputs;
	}
	
	private static LocalDate getMin(LocalDate a, LocalDate b) {
		if (a == null && b == null) {
			return null;
		}
		if (a != null && b == null) {
			return a;
		}
		if (a == null && b != null) {
			return b;
		}
		return a.isBefore(b) ? a : b;
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
