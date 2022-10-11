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
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
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
	private final int matchingFlexibilityDays;
	
	public DischargeTriggerHelper(final SalesContract selectedContract, final Inventory selectedInventory, int matchingFlexibilityDays) {
		this.selectedContract = selectedContract;
		this.selectedInventory = selectedInventory;
		this.matchingFlexibilityDays = matchingFlexibilityDays;
	}
	public void doMatchAndMoveDischargeTrigger(final LNGScenarioModel model, final int globalDischargeTrigger, final Integer cargoVolume, final LocalDate start) {
			final List<InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(selectedInventory);
			// modify to take into account start date
			final List<SlotAllocation> dischargeSlotsToConsider = getSortedFilteredDischargeSlots(model, start, selectedInventory, inventoryInsAndOuts.get(0).date);
			processWithDischargesAndStartDate(dischargeSlotsToConsider, inventoryInsAndOuts, start);
			// create new discharges
			matchAndMoveSlotsDischargeTrigger(model, selectedInventory.getPort(), inventoryInsAndOuts, globalDischargeTrigger, cargoVolume, start, this.matchingFlexibilityDays);
	}

	public List<Pair<LocalDate, DischargeSlot>> getDischargeDatesForExistingSlotsFromDischargeTrigger(final LNGScenarioModel model, final Inventory inventory, final int globalDischargeTrigger, final Integer cargoVolume,
			final LocalDate start) {
		final List<InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(inventory);
		// modify to take into account start date
		final List<SlotAllocation> loadSlotsToConsider = getSortedFilteredDischargeSlots(model, start, inventory, inventoryInsAndOuts.get(0).date);
		processWithDischargesAndStartDate(loadSlotsToConsider, inventoryInsAndOuts, start);
		// create new Loads
		return getSlotsEarliestDate(model, inventory.getPort(), inventoryInsAndOuts, globalDischargeTrigger, cargoVolume, start);
	}
	
	private List<InventoryDailyEvent> getInventoryInsAndOuts(final Inventory inventory) {
		final List<InventoryCapacityRow> capacities = inventory.getCapacities();

		final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap = capacities.stream().collect(Collectors.toMap(InventoryCapacityRow::getDate, c -> c, (oldValue, newValue) -> newValue, TreeMap::new));

		final List<InventoryDailyEvent> idEvents = new LinkedList<>();

		// add all feeds to map
		processNetVolumes(inventory.getFeeds(), capacityTreeMap, idEvents, true);
		processNetVolumes(inventory.getOfftakes(), capacityTreeMap, idEvents, false);
		return idEvents.stream()
				.sorted( (a,b) -> {
					int r = a.date.compareTo(b.date);
					if (r == 0) {
						if (a.level) {
							return -1;
						} else if (b.level) {
							return 1;
						}
					}
					return r;
					})
				.collect(Collectors.toList());
	}

	/**
	 * Count volume, get rid of the entries before the Discharge trigger start date
	 * @param dischargeSlotsToConsider
	 * @param insAndOuts
	 * @param start
	 */
	private void processWithDischargesAndStartDate(final List<SlotAllocation> dischargeSlotsToConsider, final List<InventoryDailyEvent> inventoryInsAndOuts, final LocalDate start) {
		// get sum of feeds
		int totalInventoryVolume = 0;
		// sum all the feeds and off-takes within inventory
		final Iterator<InventoryDailyEvent> iter = inventoryInsAndOuts.iterator();
		while (iter.hasNext()) {
			final InventoryDailyEvent event = iter.next();
			if (event.date.isBefore(start)) {
				totalInventoryVolume += event.netVolumeIn;
				totalInventoryVolume += event.netVolumeOut;
				iter.remove();
			} else {
				break;
			}
		}
		// get the feeds from the cargoes
		final int totalVolumeIn = dischargeSlotsToConsider.stream().mapToInt(d -> d.getPhysicalVolumeTransferred()).sum();
		// set the first entry as level
		if (!inventoryInsAndOuts.isEmpty()) {
			inventoryInsAndOuts.get(0).setLevelVolume(totalInventoryVolume + totalVolumeIn);
		}
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
	private void matchAndMoveSlotsDischargeTrigger(final LNGScenarioModel model, final Port port, final List<InventoryDailyEvent> inventoryInsAndOuts, final int globalDischargeTrigger,
			final Integer cargoVolume, final LocalDate start, final int matchingFlexibilityDays) {
		final List<DischargeSlot> sortedSlots = getSortedSlots(model, port, start);

		final List<LocalDate> dischargeDates = getDischargeDates(inventoryInsAndOuts, globalDischargeTrigger, cargoVolume);

		// Iterate through the discharge dates and find the discharge slots which are within 2 days of the date
		// change the date
		// remove the applied dates and discharges which had stuff applied to them
		final Iterator<LocalDate> iter = dischargeDates.iterator();
		while (iter.hasNext()) {
			final LocalDate ld = iter.next();
			final Iterator<DischargeSlot> dsIter = sortedSlots.iterator();
			while (dsIter.hasNext()) {
				final DischargeSlot slot = dsIter.next();
				if (isWithinRange(slot.getWindowStart(), ld, matchingFlexibilityDays)) {
					slot.setWindowStart(ld);
					iter.remove();
					dsIter.remove();
					break;
				}
			}
		}
		
		// Afterwards create more discharges if any discharge dates left
		// Case A: Not enough supply
		// Create remaining one
		if (!dischargeDates.isEmpty()) {
			createDischargeSlots(model, port, dischargeDates, cargoVolume, start, false);
		}
		// seems like an obsolete case
		// Case B: Too many deliveries
//		else {
//			clearDischargeSlots(sortedSlots.subList(dischargeDates.size(), sortedSlots.size()), model.getCargoModel(), model);
//		}
	}
	
	private boolean isWithinRange(final LocalDate date, final LocalDate rangeMid, int rangeDays) {
		return !(date.isBefore(rangeMid.minusDays(rangeDays)) || date.isAfter(rangeMid.plusDays(rangeDays)));
	}

	private List<DischargeSlot> getSortedSlots(final LNGScenarioModel model, final Port port, final LocalDate start) {
		final List<DischargeSlot> sortedSlots = model.getCargoModel().getDischargeSlots().stream() //
				.filter(d -> d.getPort() == port && (d.getWindowStart().isAfter(start) || d.getWindowStart().isEqual(start))) //
				.sorted((a, b) -> a.getWindowStart().compareTo(b.getWindowStart())) //
				.collect(Collectors.toList());
		return sortedSlots;
	}

	private List<Pair<LocalDate, DischargeSlot>> getSlotsEarliestDate(final LNGScenarioModel model, final Port port, final List<InventoryDailyEvent> inventoryInsAndOuts, final int globalDischargeTrigger,
			final Integer cargoVolume, final LocalDate start) {
		final List<Pair<LocalDate, DischargeSlot>> slotsWithDates = new LinkedList<>();
		final List<DischargeSlot> sortedSlots = getSortedSlots(model, port, start);
		final List<LocalDate> dischargeDates = getDischargeDates(inventoryInsAndOuts, globalDischargeTrigger, cargoVolume);
		for (int i = 0; i < Math.min(sortedSlots.size(), dischargeDates.size()); i++) {
			slotsWithDates.add(new Pair<LocalDate, DischargeSlot>(dischargeDates.get(i), sortedSlots.get(i)));
		}
		return slotsWithDates;
	}

	private List<LocalDate> getDischargeDates(final List<InventoryDailyEvent> inventoryInsAndOuts, final int globalLoadTrigger, final Integer cargoVolume) {
		// Create all the discharge date
		final List<LocalDate> dischargeDates = new LinkedList<>();
		int runningVolume = 0;
		for (final InventoryDailyEvent entry : inventoryInsAndOuts) {
			if (entry.isLevel()) {
				runningVolume = entry.netVolumeIn;
			} else {
				runningVolume += entry.netVolumeOut;
				runningVolume += entry.netVolumeIn;
			}
			if (runningVolume < globalLoadTrigger) {
				dischargeDates.add(entry.date);
				runningVolume += cargoVolume;
			}
		}
		return dischargeDates;
	}
	
	private void processNetVolumes(final List<InventoryEventRow> events, final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final List<InventoryDailyEvent> idEvents, final boolean isIntake) {
		for (final InventoryEventRow inventoryEventRow : events) {
			if (inventoryEventRow.getStartDate() != null && inventoryEventRow.getEndDate() != null) {
				switch(inventoryEventRow.getPeriod()) {
				case HOURLY, DAILY, MONTHLY:
					processPeriodEventRow(capacityTreeMap, idEvents, isIntake, inventoryEventRow);
					break;
				case LEVEL, CARGO:
					processLevelOrCargoEventRow(capacityTreeMap, idEvents, inventoryEventRow);
					break;					
				default:
					break;
				}
			}
		}
	}
	
	private void processPeriodEventRow(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final List<InventoryDailyEvent> idEvents, final boolean isIntake,
			final InventoryEventRow inventoryEventRow) {
		for (LocalDate ld = inventoryEventRow.getStartDate(); inventoryEventRow.getEndDate().isAfter(ld); ld = ld.plusDays(1)) {
			// Get existing or create new inventory daily event
			InventoryDailyEvent inventoryDailyEvent = new InventoryDailyEvent();
			inventoryDailyEvent.date = ld;
			setCapacityLimits(capacityTreeMap, inventoryDailyEvent);
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
			idEvents.add(inventoryDailyEvent);
		}
	}
	
	private void processLevelOrCargoEventRow(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final List<InventoryDailyEvent> idEvents,
			final InventoryEventRow inventoryEventRow) {
		LocalDate ld = inventoryEventRow.getStartDate(); 
		InventoryDailyEvent inventoryDailyEvent = new InventoryDailyEvent();
		inventoryDailyEvent.date = ld;
		setCapacityLimits(capacityTreeMap, inventoryDailyEvent);
		int reliableVolume = inventoryEventRow.getReliableVolume();
		if (inventoryEventRow.getPeriod() == InventoryFrequency.LEVEL) {
			inventoryDailyEvent.setLevelVolume(reliableVolume);
		} else {
			if (reliableVolume >= 0) {
				inventoryDailyEvent.addVolume(reliableVolume);
			} else {
				inventoryDailyEvent.subtractVolume(-reliableVolume);
			}
		}
		idEvents.add(inventoryDailyEvent);
	}
	
	/**
	 * set the capacity limits for the inventory daily event's date
	 * @param capacityTreeMap
	 * @param inventoryDailyEvent
	 */
	private void setCapacityLimits(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final InventoryDailyEvent inventoryDailyEvent) {
		final InventoryCapacityRow capacityRow = capacityTreeMap.get(inventoryDailyEvent.date) == null //
				? capacityTreeMap.lowerEntry(inventoryDailyEvent.date).getValue() //
				: capacityTreeMap.get(inventoryDailyEvent.date);
		inventoryDailyEvent.minVolume = capacityRow.getMinVolume();
		inventoryDailyEvent.maxVolume = capacityRow.getMaxVolume();
	}

	/**
	 * Entry point to create discharge slots
	 * 
	 */
	public void createDischargeSlots(final LNGScenarioModel scenario, final Port port, final List<LocalDate> dates, final int cargoVolume, final LocalDate start, final boolean clear) {
		// First clear all discharge slots, if necessary
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
			dischargeMaker.withDESSale(String.format("%d-%s-%s-%s", slotDate.getYear(), this.selectedContract.getName().toUpperCase(), "discharge-trigger", ++i), slotDate, port, this.selectedContract, this.selectedContract.getEntity(), null);
			dischargeMaker.withVolumeLimits(this.selectedContract.getMinQuantity(), this.selectedContract.getMaxQuantity(), this.selectedContract.getVolumeLimitsUnit());
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

	/**
	 * Get all discharge slots which supply the inventory and are between the trigger start date and inventory first event date 
	 * @param model
	 * @param start
	 * @param inventory
	 * @param firstDate
	 * @return
	 */
	private List<SlotAllocation> getSortedFilteredDischargeSlots(final LNGScenarioModel model, final LocalDate start, final Inventory inventory, final LocalDate firstDate) {
		return model.getScheduleModel().getSchedule().getSlotAllocations().stream()
				.filter(s -> s.getSlot() instanceof DischargeSlot)
				.filter(d -> (d.getSlot().getPort() != null && d.getSlot().getPort().equals(inventory.getPort())))
				.filter(d -> (d.getSlot().getWindowStart().isAfter(firstDate.minusDays(1)) && d.getSlot().getWindowStart().isBefore(start)))
				.sorted((a, b) -> {
					if (a.getSlot().getWindowStart() != null && b.getSlot().getWindowStart() != null) {
						return a.getSlot().getWindowStart().compareTo(b.getSlot().getWindowStart());
					} else if (a.getSlot().getWindowStart() == null) {
						return -1;
					} else if (a.getSlot().getWindowStart() == null && b.getSlot().getWindowStart() == null) {
						return 0;
					} else {
						return 1;
					}
				})
				.collect(Collectors.toList());
	}

}
