/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.common.time.Days;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.cargo.util.SlotMaker;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;

public class DischargeTriggerHelper {
	
	private final DischargeTriggerRecord triggerRecord;
	
	public DischargeTriggerHelper(final DischargeTriggerRecord triggerRecord) {
		this.triggerRecord = triggerRecord;
	}
	
	public void doMatchAndMoveDischargeTrigger(final LNGScenarioModel model) {
			final List<InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(triggerRecord.inventory);
			// modify to take into account start date
			final List<SlotAllocation> dischargeSlotsToConsider = getSortedFilteredDischargeSlots(model, triggerRecord.cutOffDate, triggerRecord.inventory, inventoryInsAndOuts.get(0).date);
			//processWithDischargesAndStartDate(dischargeSlotsToConsider, inventoryInsAndOuts, triggerRecord.cutOffDate);
			// create new discharges
			matchAndMoveSlotsDischargeTrigger(model, dischargeSlotsToConsider, triggerRecord.inventory.getPort(), inventoryInsAndOuts, triggerRecord.trigger, //
					triggerRecord.maxQuantity, triggerRecord.cutOffDate, triggerRecord.matchingFlexibilityDays);
	}
	
	/**
	 * Creates a sorted list of inventory daily events with separate in-take and off-take volumes
	 * @param inventory
	 * @return
	 */
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
			} else {
				iter.remove();
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
	 * @param globalDischargeTrigger
	 * @param cargoVolume
	 * @param start
	 */
	private void matchAndMoveSlotsDischargeTrigger(final LNGScenarioModel model, final List<SlotAllocation> dischargeSlotsToConsider, final Port port, final List<InventoryDailyEvent> inventoryInsAndOuts, final int globalDischargeTrigger,
			final Integer cargoVolume, final LocalDate start, final int matchingFlexibilityDays) {
		final List<DischargeSlot> sortedSlots = getSortedSlots(model, port, start);

		final List<LocalDate> dischargeDates = getDischargeDates(inventoryInsAndOuts, dischargeSlotsToConsider, globalDischargeTrigger, cargoVolume);

		// Iterate through the discharge dates and find the discharge slots which are within 2 days of the date
		// change the date
		// remove the applied dates and discharges which had stuff applied to them
		final Iterator<LocalDate> iter = dischargeDates.iterator();
		while (iter.hasNext()) {
			final LocalDate ld = iter.next();
			final Iterator<DischargeSlot> dsIter = sortedSlots.iterator();
			while (dsIter.hasNext()) {
				final DischargeSlot slot = dsIter.next();
				if (!(slot.getWindowStart().isBefore(ld) || slot.getWindowStart().isAfter(ld.plusDays(matchingFlexibilityDays)))) {
					slot.setWindowStart(ld);
					iter.remove();
					dsIter.remove();
					break;
				}
			}
		}
		
		// Afterwards create more discharges if any discharge dates left
		// Create remaining one
		if (!dischargeDates.isEmpty()) {
			createDischargeSlots(model, port, dischargeDates, sortedSlots, start, false);
		}
	}

	private List<DischargeSlot> getSortedSlots(final LNGScenarioModel model, final Port port, final LocalDate start) {
		final List<DischargeSlot> sortedSlots = model.getCargoModel().getDischargeSlots().stream() //
				.filter(d -> d.getPort() == port && (!d.getWindowStart().isAfter(start))) //
				.sorted((a, b) -> a.getWindowStart().compareTo(b.getWindowStart())) //
				.collect(Collectors.toList());
		return sortedSlots;
	}
	
	/**
	 * Determines the list of dates when global discharge trigger is being hit
	 * @param inventoryInsAndOuts
	 * @param dischargeSlotsToConsider
	 * @param globalDischargeTrigger
	 * @param cargoVolume
	 * @return
	 */
	private List<LocalDate> getDischargeDates(final List<InventoryDailyEvent> inventoryInsAndOuts, final List<SlotAllocation> dischargeSlotsToConsider, final int globalDischargeTrigger, final Integer cargoVolume) {
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
			final SlotAllocation sa = getAllocationForDate(dischargeSlotsToConsider, entry.date);
			if (sa != null) {
				runningVolume += sa.getPhysicalVolumeTransferred();
			}
			if (runningVolume < globalDischargeTrigger) {
				dischargeDates.add(entry.date);
				runningVolume += cargoVolume;
			}
		}
		return dischargeDates;
	}
	
	private SlotAllocation getAllocationForDate(final List<SlotAllocation> dischargeSlotsToConsider, final LocalDate date) {
		for (final SlotAllocation sa : dischargeSlotsToConsider) {
			if (sa.getSlot().getWindowStart().isEqual(date)) {
				return sa;
			}
		}
		return null;
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
			InventoryDailyEvent inventoryDailyEvent = getOrCreateInventoryDailyEvent(capacityTreeMap, idEvents, ld);

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
	
	/**
	 * Returns the {@link InventoryDailyEvent} instance from the idEvents list
	 * if no instance is present for the given date
	 * creates a new instance and adds it to the idEvents list
	 * @param capacityTreeMap
	 * @param idEvents
	 * @param date
	 * @return
	 */
	private InventoryDailyEvent getOrCreateInventoryDailyEvent(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final List<InventoryDailyEvent> idEvents, final LocalDate date) {
		for(final InventoryDailyEvent event : idEvents) {
			if (event.date.equals(date)) {
				return event;
			}
		}
		InventoryDailyEvent inventoryDailyEvent = new InventoryDailyEvent();
		inventoryDailyEvent.date = date;
		setCapacityLimits(capacityTreeMap, inventoryDailyEvent);
		idEvents.add(inventoryDailyEvent);
		return inventoryDailyEvent;
	}
	
	/**
	 * Accounts for the LEVEL and CARGO volume contribution to the inventory
	 * @param capacityTreeMap
	 * @param idEvents
	 * @param inventoryEventRow
	 */
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
	 * @param dates - list of dates on which discharge slots would be created
	 * 
	 */
	private void createDischargeSlots(final LNGScenarioModel scenario, final Port port, final List<LocalDate> dates, final List<DischargeSlot> sortedSlots, final LocalDate start, final boolean clear) {
		// First clear all discharge slots, if necessary
		final EList<DischargeSlot> dischargeSlots = scenario.getCargoModel().getDischargeSlots();
		if (dischargeSlots != null) {
			// Clear all cargoes
			if (clear)
				clearCargoesAndSchedule(scenario, start);
		}
		final CargoModelBuilder builder = new CargoModelBuilder(scenario.getCargoModel());
		final List<String> usedIDStrings = InventoryTriggerUtils.getUsedNames(scenario);
		int counter = 0;

		// for each 
		for (final LocalDate slotDate : dates) {
			assert port != null;
			assert port.getCapabilities().contains(PortCapability.DISCHARGE) == true;
			final SlotMaker<DischargeSlot> dischargeMaker = new SlotMaker<>(builder);
			String suggestedName = String.format("%d-%s-%s-%s", slotDate.getYear(), this.triggerRecord.contract.getName(), "discharge", "trigger");
			{
				while (usedIDStrings.contains(suggestedName)) {
					suggestedName = String.format("%s-%d", suggestedName, counter++);
				}
				usedIDStrings.add(suggestedName);
			}
			
			final LocalDate prev = getPrevious(slotDate, sortedSlots);
			int numDays = Days.between(prev, slotDate) / 2;
			final LocalDate date = prev.plusDays(numDays);
			
			dischargeMaker.withDESSale(suggestedName, date, port, this.triggerRecord.contract, this.triggerRecord.contract.getEntity(), null);
			dischargeMaker.withVolumeLimits(this.triggerRecord.contract.getMinQuantity(), this.triggerRecord.contract.getMaxQuantity(), //
					this.triggerRecord.contract.getVolumeLimitsUnit());
			dischargeMaker.withWindowSize(numDays, TimePeriod.DAYS);
			dischargeMaker.build();
		}
	}
	
	/**
	 * Returns the first most closest to the slotDate previous discharge slot.
	 * @param slotDate
	 * @param sortedSlots
	 * @return
	 */
	private LocalDate getPrevious(final LocalDate slotDate, final List<DischargeSlot> sortedSlots) {
		DischargeSlot selected = null;
		for (final DischargeSlot slot : sortedSlots) {
			if (slot.getWindowStart().isBefore(slotDate)) {
				if (selected == null) {
					selected = slot;
				} else {
					if (selected.getWindowStart().isBefore(slot.getWindowStart())) {
						selected = slot;
					}
				}
			}
		}
		if (selected != null) {
			return selected.getWindowStart();
		} else {
			return slotDate;
		}
	}
	
	/**
	 * Returns the first most closest to the slotDate next discharge slot.
	 * @param slotDate
	 * @param sortedSlots
	 * @return
	 */
	private LocalDate getNext(final LocalDate slotDate, final List<DischargeSlot> sortedSlots) {
		DischargeSlot selected = null;
		for (final DischargeSlot slot : sortedSlots) {
			if (slot.getWindowStart().isAfter(slotDate)) {
				if (selected == null) {
					selected = slot;
				} else {
					if (selected.getWindowStart().isAfter(slot.getWindowStart())) {
						selected = slot;
					}
				}
			}
		}
		if (selected != null) {
			return selected.getWindowStart();
		} else {
			return slotDate;
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
