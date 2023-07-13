/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.cargo.util.SlotMaker;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class LoadTriggerHelper {
	
	public LoadTriggerHelper(Inventory selectedInventory, PurchaseContract selectedContract) {
		this.selectedInventory = selectedInventory;
		this.selectedContract = selectedContract;
	}

	private final Inventory selectedInventory;
	private final PurchaseContract selectedContract;

	public void doMatchAndMoveLoadTrigger(final LNGScenarioModel model, final int globalLoadTrigger, final Integer cargoVolume, final LocalDate start) {
		final EList<Inventory> inventoryModels = model.getCargoModel().getInventoryModels();
		if (inventoryModels.size() != 1) {
			throw new RuntimeException("Only 1 inventory model is supported at present.");
		}
			final TreeMap<LocalDate, InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(selectedInventory);
			// modify to take into account start date
			final List<SlotAllocation> loadSlotsToConsider = getSortedFilteredLoadSlots(model, start, selectedInventory, inventoryInsAndOuts);
			processWithLoadsAndStartDate(loadSlotsToConsider, inventoryInsAndOuts, start);
			// create new Loads
			matchAndMoveSlotsLoadTrigger(model, selectedInventory.getPort(), inventoryInsAndOuts, globalLoadTrigger, cargoVolume, start);
	}

	public List<Pair<LocalDate, LoadSlot>> getLoadDatesForExistingSlotsFromLoadTrigger(final LNGScenarioModel model, final Inventory inventory, final int globalLoadTrigger, final Integer cargoVolume,
			final LocalDate start) {
		final TreeMap<LocalDate, InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(inventory);
		// modify to take into account start date
		final List<SlotAllocation> loadSlotsToConsider = getSortedFilteredLoadSlots(model, start, inventory, inventoryInsAndOuts);
		processWithLoadsAndStartDate(loadSlotsToConsider, inventoryInsAndOuts, start);
		// create new Loads
		return getSlotsEarliestDate(model, inventory.getPort(), inventoryInsAndOuts, globalLoadTrigger, cargoVolume, start);
	}

	private TreeMap<LocalDate, InventoryDailyEvent> getInventoryInsAndOuts(final Inventory inventory) {
		final EList<InventoryCapacityRow> capacities = inventory.getCapacities();

		final TreeMap<LocalDate, InventoryCapacityRow> capcityTreeMap = capacities.stream().collect(Collectors.toMap(InventoryCapacityRow::getDate, c -> c, (oldValue, newValue) -> newValue, TreeMap::new));

		final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = new TreeMap<>();

		// add all feeds to map
		addNetVolumes(inventory.getFeeds(), capcityTreeMap, insAndOuts, IntUnaryOperator.identity());
		addNetVolumes(inventory.getOfftakes(), capcityTreeMap, insAndOuts, a -> -a);
		return insAndOuts;
	}

	private void processWithLoadsAndStartDate(final List<SlotAllocation> loadSlotsToConsider, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final LocalDate start) {
		// get sum of feeds
		int totalInventoryVolume = 0;
		while (insAndOuts.firstKey().isBefore(start)) {
			final InventoryDailyEvent event = insAndOuts.remove(insAndOuts.firstKey());
			totalInventoryVolume += event.netVolumeIn;
		}
		// get sum of loads
		final int totalVolumeOut = -1 * loadSlotsToConsider.stream().mapToInt(l -> l.getPhysicalVolumeTransferred()).sum();
		final InventoryDailyEvent firstEvent = insAndOuts.firstEntry().getValue();
		firstEvent.addVolume(totalInventoryVolume + totalVolumeOut);
	}

	/**
	 * Clears all existing slots and replaces with a slot whenever the volume reaches the global load trigger value. Slots are assumed to be of cargoVolume size
	 * 
	 * @param model
	 * @param port
	 * @param insAndOuts
	 * @param globalLoadTrigger
	 * @param cargoVolume
	 * @param start
	 */
	private void standardLoadTrigger(final LNGScenarioModel model, final Port port, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int globalLoadTrigger, final int cargoVolume,
			final LocalDate start) {
		final List<LocalDate> loadDates = new LinkedList<>();
		int runningVolume = 0;
		clearCargoesAndSchedule(model, start);
		for (final Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			runningVolume += entry.getValue().netVolumeIn;
			if (runningVolume > globalLoadTrigger) {
				loadDates.add(entry.getKey());
				runningVolume -= cargoVolume;
			}
		}
		createLoadSlots(model, port, loadDates, cargoVolume, start, true);
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
	private void moveSlotsLoadTrigger(final LNGScenarioModel model, final Port port, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int cargoVolume, final LocalDate start) {
		final List<LoadSlot> sortedSlots = model.getCargoModel().getLoadSlots().stream().filter(l -> l.getPort() == port).sorted((a, b) -> a.getWindowStart().compareTo(b.getWindowStart()))
				.collect(Collectors.toList());
		final List<LocalDate> loadDates = new LinkedList<>();
		int runningVolume = 0;
		clearCargoesAndSchedule(model, start);
		assert (insAndOuts.firstKey().isBefore(sortedSlots.get(0).getWindowStart()) || insAndOuts.firstKey().isEqual(sortedSlots.get(0).getWindowStart()));

		for (final Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			runningVolume += entry.getValue().netVolumeIn;
			if (runningVolume > entry.getValue().maxVolume) {
				if (sortedSlots.size() > 0) {
					final LoadSlot remove = sortedSlots.remove(0);
					remove.setWindowStart(entry.getKey());
				} else {
					loadDates.add(entry.getKey());
				}
				runningVolume -= cargoVolume;
			}
			System.out.println(entry.getKey() + ":" + runningVolume);
		}

		createLoadSlots(model, port, loadDates, cargoVolume, start, false);
		clearLoadSlots(sortedSlots, model.getCargoModel(), model);
	}

	/**
	 * Identifies slot dates where inventory equals a global load trigger. Existing slots are moved to match these new dates, creating and removing slots as needed. Slots are assumed to be of
	 * cargoVolume size.
	 * 
	 * @param model
	 * @param port
	 * @param insAndOuts
	 * @param globalLoadTrigger
	 * @param cargoVolume
	 * @param start
	 */
	private void matchAndMoveSlotsLoadTrigger(final LNGScenarioModel model, final Port port, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int globalLoadTrigger,
			final Integer cargoVolume, final LocalDate start) {
		final List<LoadSlot> sortedSlots = getSortedSlots(model, port, start);

		final List<LocalDate> loadDates = getLoadDates(insAndOuts, globalLoadTrigger, cargoVolume);

		// Assign the new load date to the current load slot
		final int interSize = Math.min(sortedSlots.size(), loadDates.size());

		for (int i = 0; i < interSize; i++) {
			sortedSlots.get(i).setWindowStart(loadDates.get(i));
		}

		// Case A: Not enough loads
		// Create remaining one
		if (loadDates.size() > sortedSlots.size()) {
			createLoadSlots(model, port, loadDates.subList(sortedSlots.size(), loadDates.size() - 1), cargoVolume, start, false);
		}
		// Case B: Too many slot
		else {
			clearLoadSlots(sortedSlots.subList(loadDates.size(), sortedSlots.size()), model.getCargoModel(), model);
		}
	}

	private List<LoadSlot> getSortedSlots(final LNGScenarioModel model, final Port port, final LocalDate start) {
		final List<LoadSlot> sortedSlots = model.getCargoModel().getLoadSlots().stream() //
				.filter(l -> l.getPort() == port && (l.getWindowStart().isAfter(start) || l.getWindowStart().isEqual(start))) //
				.sorted((a, b) -> a.getWindowStart().compareTo(b.getWindowStart())) //
				.collect(Collectors.toList());
		return sortedSlots;
	}

	private List<Pair<LocalDate, LoadSlot>> getSlotsEarliestDate(final LNGScenarioModel model, final Port port, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int globalLoadTrigger,
			final Integer cargoVolume, final LocalDate start) {
		final List<Pair<LocalDate, LoadSlot>> slotsWithDates = new LinkedList<>();
		final List<LoadSlot> sortedSlots = getSortedSlots(model, port, start);
		final List<LocalDate> loadDates = getLoadDates(insAndOuts, globalLoadTrigger, cargoVolume);
		for (int i = 0; i < Math.min(sortedSlots.size(), loadDates.size()); i++) {
			slotsWithDates.add(new Pair<LocalDate, LoadSlot>(loadDates.get(i), sortedSlots.get(i)));
		}
		return slotsWithDates;
	}

	private List<LocalDate> getLoadDates(final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, final int globalLoadTrigger, final Integer cargoVolume) {
		// Create all the load date
		final List<LocalDate> loadDates = new LinkedList<>();
		int runningVolume = 0;
		for (final Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			runningVolume += entry.getValue().netVolumeIn;
			if (runningVolume > globalLoadTrigger) {
				loadDates.add(entry.getKey());
				runningVolume -= cargoVolume;
			}
		}
		return loadDates;
	}

	private void addNetVolumes(final List<InventoryEventRow> events, final TreeMap<LocalDate, InventoryCapacityRow> capcityTreeMap, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts,
			final IntUnaryOperator volumeFunction) {
		for (final InventoryEventRow inventoryEventRow : events) {
			if (inventoryEventRow.getStartDate() != null) {
				InventoryDailyEvent inventoryDailyEvent = insAndOuts.get(inventoryEventRow.getStartDate());
				if (inventoryDailyEvent == null) {
					inventoryDailyEvent = new InventoryDailyEvent();
					inventoryDailyEvent.date = inventoryEventRow.getStartDate();
					final InventoryCapacityRow capacityRow = capcityTreeMap.get(inventoryDailyEvent.date) == null //
							? capcityTreeMap.lowerEntry(inventoryDailyEvent.date).getValue() //
							: capcityTreeMap.get(inventoryDailyEvent.date);
					inventoryDailyEvent.minVolume = capacityRow.getMinVolume();
					inventoryDailyEvent.maxVolume = capacityRow.getMaxVolume();
					insAndOuts.put(inventoryEventRow.getStartDate(), inventoryDailyEvent);
				}
				inventoryDailyEvent.addVolume(volumeFunction.applyAsInt(inventoryEventRow.getReliableVolume()));
			}
		}
	}

	/**
	 * Entry point to create Load slots Taking the very first contract. (due to client L preferences)
	 * 
	 */
	public void createLoadSlots(final LNGScenarioModel scenario, final Port port, final List<LocalDate> dates, final int cargoVolume, final LocalDate start, final boolean clear) {
		// First clear all load slots
		final EList<LoadSlot> loadSlots = scenario.getCargoModel().getLoadSlots();
		if (loadSlots != null) {
			// Clear all cargoes
			if (clear)
				clearCargoesAndSchedule(scenario, start);
		}
		final CargoModelBuilder builder = new CargoModelBuilder(scenario.getCargoModel());
		int i = 0;


		for (final LocalDate slotDate : dates) {
			assert port != null;
			final SlotMaker<LoadSlot> loadMaker = new SlotMaker<>(builder);
			loadMaker.withFOBPurchase(String.format("%s_%s", "load", ++i), slotDate, port, selectedContract, scenario.getReferenceModel().getCommercialModel().getEntities().get(0), null, null);
			loadMaker.withVolumeLimits(0, cargoVolume, VolumeUnits.M3);
			loadMaker.withWindowSize(0, TimePeriod.DAYS);
			loadMaker.build();
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
		final Iterator<LoadSlot> loadsIterator = scenario.getCargoModel().getLoadSlots().iterator();
		while (loadsIterator.hasNext()) {
			final LoadSlot loadSlot = loadsIterator.next();
			if (loadSlot.getWindowStart().isAfter(start.minusDays(1))) {
				loadsIterator.remove();
			}
		}
		final Schedule schedule = scenario.getScheduleModel().getSchedule();
		clearSchedule(scenario, schedule);
	}

	private void clearLoadSlots(final Collection<LoadSlot> loadSlots, final CargoModel cargoModel, final LNGScenarioModel scenario) {
		for (final LoadSlot loadSlot : loadSlots) {
			final Cargo cargo = loadSlot.getCargo();

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
			cargoModel.getLoadSlots().remove(loadSlot);
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

	private List<SlotAllocation> getSortedFilteredLoadSlots(final LNGScenarioModel model, final LocalDate start, final Inventory inventory, final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts) {
		return model.getScheduleModel().getSchedule().getSlotAllocations().stream().filter(s -> s.getSlot() instanceof LoadSlot)
				.filter(l -> (l.getSlot().getPort() != null && l.getSlot().getPort().equals(inventory.getPort())))
				.filter(l -> (l.getSlot().getWindowStart().isAfter(insAndOuts.firstEntry().getKey().minusDays(1)) && l.getSlot().getWindowStart().isBefore(start))).sorted((a, b) -> {
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
