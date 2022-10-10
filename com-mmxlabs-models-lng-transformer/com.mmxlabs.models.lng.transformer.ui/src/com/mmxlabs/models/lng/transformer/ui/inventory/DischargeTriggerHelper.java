/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.cargo.util.SlotMaker;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class DischargeTriggerHelper {
	
	private final SalesContract selectedContract;
	private final Inventory selectedInventory;
	
	public DischargeTriggerHelper(SalesContract selectedContract, Inventory selectedInventory) {
		this.selectedContract = selectedContract;
		this.selectedInventory = selectedInventory;
	}
	public void doMatchAndMoveDischargeTrigger(final LNGScenarioModel model, final int globalDischargeTrigger, final Integer cargoVolume, final LocalDate start) {
			final TreeMap<LocalDate, InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(selectedInventory);
			// modify to take into account start date
			final List<SlotAllocation> dischargeSlotsToConsider = getSortedFilteredDischargeSlots(model, start, selectedInventory, inventoryInsAndOuts.firstEntry().getValue().date);
			processWithDischargesAndStartDate(dischargeSlotsToConsider, inventoryInsAndOuts, start);
			// create new discharges
			matchAndMoveSlotsDischargeTrigger(model, selectedInventory.getPort(), inventoryInsAndOuts, globalDischargeTrigger, cargoVolume, start);
	}

	public List<Pair<LocalDate, DischargeSlot>> getDischargeDatesForExistingSlotsFromDischargeTrigger(final LNGScenarioModel model, final Inventory inventory, final int globalDischargeTrigger, final Integer cargoVolume,
			final LocalDate start) {
		final TreeMap<LocalDate, InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(inventory);
		// modify to take into account start date
		final List<SlotAllocation> loadSlotsToConsider = getSortedFilteredDischargeSlots(model, start, inventory, inventoryInsAndOuts.firstEntry().getValue().date);
		processWithDischargesAndStartDate(loadSlotsToConsider, inventoryInsAndOuts, start);
		// create new Loads
		return getSlotsEarliestDate(model, inventory.getPort(), inventoryInsAndOuts, globalDischargeTrigger, cargoVolume, start);
	}
	
	private TreeMap<LocalDate, InventoryDailyEvent> getInventoryInsAndOuts(final Inventory inventory) {
		final EList<InventoryCapacityRow> capacities = inventory.getCapacities();

		final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap = capacities.stream().collect(Collectors.toMap(InventoryCapacityRow::getDate, c -> c, (oldValue, newValue) -> newValue, TreeMap::new));

		final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = new TreeMap<>();

		// add all feeds to map
		processNetVolumes(inventory.getFeeds(), capacityTreeMap, insAndOuts, true);
		processNetVolumes(inventory.getOfftakes(), capacityTreeMap, insAndOuts, false);
		return insAndOuts;
	}

	/**
	 * Count volume, get rid of the entries before the Discharge trigger start date
	 * @param dischargeSlotsToConsider
	 * @param insAndOuts
	 * @param start
	 */
	private void processWithDischargesAndStartDate(final List<SlotAllocation> dischargeSlotsToConsider, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final LocalDate start) {
		// get sum of feeds
		int totalInventoryVolume = 0;
		// sum all the feeds and off-takes within inventory
		while (insAndOuts.firstKey().isBefore(start)) {
			final InventoryDailyEvent event = insAndOuts.remove(insAndOuts.firstKey());
			totalInventoryVolume += event.netVolumeIn;
			totalInventoryVolume += event.netVolumeOut;
		}
		// get the feeds from the cargoes
		final int totalVolumeIn = dischargeSlotsToConsider.stream().mapToInt(d -> d.getPhysicalVolumeTransferred()).sum();
		insAndOuts.firstEntry().getValue().setVolume(totalInventoryVolume + totalVolumeIn);
	}

	/**
	 * Identifies slot dates where inventory equals a global load trigger. Existing slots are moved to match these new dates, creating and removing slots as needed. Slots are assumed to be of
	 * cargoVolume size.
	 * 
	 * @param model
	 * @param port
	 * @param insAndOuts
	 * @param cargoVolume
	 * @param start
	 */
	private void moveSlotsDischargeTrigger(final LNGScenarioModel model, final Port port, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int cargoVolume, final LocalDate start) {
		final List<DischargeSlot> sortedSlots = model.getCargoModel().getDischargeSlots().stream().filter(l -> l.getPort() == port).sorted((a, b) -> a.getWindowStart().compareTo(b.getWindowStart()))
				.collect(Collectors.toList());
		final List<LocalDate> dischargeDates = new LinkedList<>();
		int runningVolume = 0;
		clearCargoesAndSchedule(model, start);
		assert (insAndOuts.firstKey().isBefore(sortedSlots.get(0).getWindowStart()) || insAndOuts.firstKey().isEqual(sortedSlots.get(0).getWindowStart()));

		for (final Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			runningVolume += entry.getValue().netVolumeIn;
			if (runningVolume > entry.getValue().maxVolume) {
				if (sortedSlots.size() > 0) {
					final DischargeSlot remove = sortedSlots.remove(0);
					remove.setWindowStart(entry.getKey());
				} else {
					dischargeDates.add(entry.getKey());
				}
				runningVolume -= cargoVolume;
			}
			System.out.println(entry.getKey() + ":" + runningVolume);
		}

		createDischargeSlots(model, port, dischargeDates, cargoVolume, start, false);
		clearDischargeSlots(sortedSlots, model.getCargoModel(), model);
	}

	/**
	 * Identifies slot dates where inventory equals a global load trigger. Existing slots are moved to match these new dates, creating and removing slots as needed. Slots are assumed to be of
	 * cargoVolume size.
	 * 
	 * @param model
	 * @param port
	 * @param insAndOuts
	 * @param globalDischargeTrigger
	 * @param cargoVolume
	 * @param start
	 */
	private void matchAndMoveSlotsDischargeTrigger(final LNGScenarioModel model, final Port port, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int globalDischargeTrigger,
			final Integer cargoVolume, final LocalDate start) {
		final List<DischargeSlot> sortedSlots = getSortedSlots(model, port, start);

		final List<LocalDate> dischargeDates = getDischargeDates(insAndOuts, globalDischargeTrigger, cargoVolume);

		// Assign the new load date to the current load slot
		final int interSize = Math.min(sortedSlots.size(), dischargeDates.size());

		for (int i = 0; i < interSize; i++) {
			sortedSlots.get(i).setWindowStart(dischargeDates.get(i));
		}

		// Case A: Not enough loads
		// Create remaining one
		if (dischargeDates.size() > sortedSlots.size()) {
			createDischargeSlots(model, port, dischargeDates.subList(sortedSlots.size(), dischargeDates.size() - 1), cargoVolume, start, false);
		}
		// Case B: Too many slot
		else {
			clearDischargeSlots(sortedSlots.subList(dischargeDates.size(), sortedSlots.size()), model.getCargoModel(), model);
		}
	}

	private List<DischargeSlot> getSortedSlots(final LNGScenarioModel model, final Port port, final LocalDate start) {
		final List<DischargeSlot> sortedSlots = model.getCargoModel().getDischargeSlots().stream() //
				.filter(d -> d.getPort() == port && (d.getWindowStart().isAfter(start) || d.getWindowStart().isEqual(start))) //
				.sorted((a, b) -> a.getWindowStart().compareTo(b.getWindowStart())) //
				.collect(Collectors.toList());
		return sortedSlots;
	}

	private List<Pair<LocalDate, DischargeSlot>> getSlotsEarliestDate(final LNGScenarioModel model, final Port port, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int globalDischargeTrigger,
			final Integer cargoVolume, final LocalDate start) {
		final List<Pair<LocalDate, DischargeSlot>> slotsWithDates = new LinkedList<>();
		final List<DischargeSlot> sortedSlots = getSortedSlots(model, port, start);
		final List<LocalDate> dischargeDates = getDischargeDates(insAndOuts, globalDischargeTrigger, cargoVolume);
		for (int i = 0; i < Math.min(sortedSlots.size(), dischargeDates.size()); i++) {
			slotsWithDates.add(new Pair<LocalDate, DischargeSlot>(dischargeDates.get(i), sortedSlots.get(i)));
		}
		return slotsWithDates;
	}

	private List<LocalDate> getDischargeDates(final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int globalLoadTrigger, final Integer cargoVolume) {
		// Create all the discharge date
		final List<LocalDate> dischargeDates = new LinkedList<>();
		int runningVolume = 0;
		for (final Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			runningVolume += entry.getValue().netVolumeOut;
			if (runningVolume < globalLoadTrigger) {
				dischargeDates.add(entry.getKey());
				runningVolume += cargoVolume;
			}
		}
		return dischargeDates;
	}
	
	private void processNetVolumes(final List<InventoryEventRow> events, final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final boolean isIntake) {
		for (final InventoryEventRow inventoryEventRow : events) {
			if (inventoryEventRow.getStartDate() != null && inventoryEventRow.getEndDate() != null) {
				switch(inventoryEventRow.getPeriod()) {
				case HOURLY, DAILY, MONTHLY:
					processPeriodEventRow(capacityTreeMap, insAndOuts, isIntake, inventoryEventRow);
					break;
				case LEVEL, CARGO:
					processLevelOrCargoEventRow(capacityTreeMap, insAndOuts, inventoryEventRow);
					break;					
				default:
					break;
				}
			}
		}
	}
	
	private void processPeriodEventRow(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final boolean isIntake,
			final InventoryEventRow inventoryEventRow) {
		for (LocalDate ld = inventoryEventRow.getStartDate(); inventoryEventRow.getEndDate().isAfter(ld); ld = ld.plusDays(1)) {
			// Get existing or create new inventory daily event
			InventoryDailyEvent inventoryDailyEvent = getOrCreateInventoryDailyEvent(capacityTreeMap, insAndOuts, ld);
			int reliableVolume = inventoryEventRow.getReliableVolume();
			switch (inventoryEventRow.getPeriod()) {
			case HOURLY:
				reliableVolume *= 24;
				break;
			case MONTHLY:
				reliableVolume = (int)((double) inventoryEventRow.getReliableVolume() / (double) ld.lengthOfMonth());
				break;
			default:
				break;
			}
			// Account for the volume in the inventory daily event
			if (isIntake) {
				inventoryDailyEvent.addVolume(reliableVolume);
			} else {
				inventoryDailyEvent.subtractVolume(reliableVolume);
			}
		}
	}
	
	private void processLevelOrCargoEventRow(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts,
			final InventoryEventRow inventoryEventRow) {
		LocalDate ld = inventoryEventRow.getStartDate(); 
		InventoryDailyEvent inventoryDailyEvent = getOrCreateInventoryDailyEvent(capacityTreeMap, insAndOuts, ld);
		int reliableVolume = inventoryEventRow.getReliableVolume();
		if (reliableVolume >= 0) {
			inventoryDailyEvent.addVolume(reliableVolume);
		} else {
			inventoryDailyEvent.subtractVolume(reliableVolume);
		}
	}
	
	private InventoryDailyEvent getOrCreateInventoryDailyEvent(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, LocalDate ld) {
		InventoryDailyEvent inventoryDailyEvent = insAndOuts.get(ld);
		if (inventoryDailyEvent == null) {
			inventoryDailyEvent = new InventoryDailyEvent();
			inventoryDailyEvent.date = ld;
			setCapacityLimits(capacityTreeMap, inventoryDailyEvent);
			insAndOuts.put(ld, inventoryDailyEvent);
		}
		return inventoryDailyEvent;
	}
	
	private void setCapacityLimits(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final InventoryDailyEvent inventoryDailyEvent) {
		final InventoryCapacityRow capacityRow = capacityTreeMap.get(inventoryDailyEvent.date) == null //
				? capacityTreeMap.lowerEntry(inventoryDailyEvent.date).getValue() //
				: capacityTreeMap.get(inventoryDailyEvent.date);
		inventoryDailyEvent.minVolume = capacityRow.getMinVolume();
		inventoryDailyEvent.maxVolume = capacityRow.getMaxVolume();
	}

	/**
	 * Entry point to create Load slots Taking the very first contract. (due to client L preferences)
	 * 
	 */
	public void createDischargeSlots(final LNGScenarioModel scenario, final Port port, final List<LocalDate> dates, final int cargoVolume, final LocalDate start, final boolean clear) {
		// First clear all load slots
		final EList<DischargeSlot> dischargeSlots = scenario.getCargoModel().getDischargeSlots();
		if (dischargeSlots != null) {
			// Clear all cargoes
			if (clear)
				clearCargoesAndSchedule(scenario, start);
		}
		final CargoModelBuilder builder = new CargoModelBuilder(scenario.getCargoModel());
		int i = 0;

		for (final LocalDate slotDate : dates) {
			assert port != null;
			assert port.getCapabilities().contains(PortCapability.DISCHARGE) == true;
			final SlotMaker<DischargeSlot> dischargeMaker = new SlotMaker<>(builder);
			dischargeMaker.withDESSale(String.format("%s_%s", "discharge", ++i), slotDate, port, this.selectedContract, scenario.getReferenceModel().getCommercialModel().getEntities().get(0), null);
			dischargeMaker.withVolumeLimits(0, cargoVolume, VolumeUnits.M3);
			dischargeMaker.withWindowSize(0, TimePeriod.DAYS);
			dischargeMaker.build();
		}
	}

	/**
	 * Clears data in the scenario
	 * 
	 * @param scenario
	 * @param start
	 */
	private void clearCargoesAndSchedule(final LNGScenarioModel scenario, final LocalDate start) {
		final Iterator<Cargo> cargoIterator = scenario.getCargoModel().getCargoes().iterator();
		while (cargoIterator.hasNext()) {
			final Cargo cargo = cargoIterator.next();
			if (cargo.getSlots().get(0).getWindowStart().isAfter(start.minusDays(1))) {
				cargo.getSlots().forEach(s -> s.setCargo(null));
				cargo.getSlots().clear();
				cargoIterator.remove();
			}
		}
		final Iterator<DischargeSlot> dischargeIterator = scenario.getCargoModel().getDischargeSlots().iterator();
		while (dischargeIterator.hasNext()) {
			final DischargeSlot dischargeSlot = dischargeIterator.next();
			if (dischargeSlot.getWindowStart().isAfter(start.minusDays(1))) {
				dischargeIterator.remove();
			}
		}
		final Schedule schedule = scenario.getScheduleModel().getSchedule();
		clearSchedule(scenario, schedule);
	}

	private void clearDischargeSlots(final Collection<DischargeSlot> dischargeSlots, final CargoModel cargoModel, final LNGScenarioModel scenario) {
		for (final DischargeSlot dischargeSlot : dischargeSlots) {
			final Cargo cargo = dischargeSlot.getCargo();

			if (cargo != null) {
				final List<Slot> slots = new ArrayList<>(cargo.getSlots());
				for (final Slot slot : slots) {
					slot.setCargo(null);
				}

				if (cargo.getSlots() != null) {
					cargo.getSlots().clear();
				}

				cargoModel.getCargoes().remove(cargo);
			}
			cargoModel.getDischargeSlots().remove(dischargeSlot);
		}

		final Schedule schedule = scenario.getScheduleModel().getSchedule();
		clearSchedule(scenario, schedule);
	}

	private void clearSchedule(final LNGScenarioModel scenario, final Schedule schedule) {
		if (schedule != null) {
			final EList<CargoAllocation> cargoAllocations = schedule.getCargoAllocations();
			if (cargoAllocations != null) {
				cargoAllocations.clear();
			}
			final EList<SlotAllocation> slotAllocations = schedule.getSlotAllocations();
			if (slotAllocations != null) {
				slotAllocations.clear();
			}
			final EList<OpenSlotAllocation> openAllocations = schedule.getOpenSlotAllocations();
			if (openAllocations != null) {
				openAllocations.clear();
			}
		}
		scenario.getScheduleModel().setSchedule(null); // delete schedule
	}

	private List<SlotAllocation> getSortedFilteredDischargeSlots(final LNGScenarioModel model, final LocalDate start, final Inventory inventory, final LocalDate firstDate) {
		return model.getScheduleModel().getSchedule().getSlotAllocations().stream().filter(s -> s.getSlot() instanceof DischargeSlot)
				.filter(d -> (d.getSlot().getPort() != null && d.getSlot().getPort().equals(inventory.getPort())))
				.filter(d -> (d.getSlot().getWindowStart().isAfter(firstDate.minusDays(1)) && d.getSlot().getWindowStart().isBefore(start))).sorted((a, b) -> {
					if (a.getSlot().getWindowStart() != null && b.getSlot().getWindowStart() != null) {
						return a.getSlot().getWindowStart().compareTo(b.getSlot().getWindowStart());
					} else if (a.getSlot().getWindowStart() == null) {
						return -1;
					} else if (a.getSlot().getWindowStart() == null && b.getSlot().getWindowStart() == null) {
						return 0;
					} else {
						return 1;
					}
				}).collect(Collectors.toList());
	}

}
