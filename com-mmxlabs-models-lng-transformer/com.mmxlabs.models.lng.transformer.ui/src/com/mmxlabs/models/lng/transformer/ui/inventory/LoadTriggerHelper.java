/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import java.util.function.Function;
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

	public void doMatchAndMoveLoadTrigger(LNGScenarioModel model, int globalLoadTrigger, Integer cargoVolume, LocalDate start) {
		EList<Inventory> inventoryModels = model.getCargoModel().getInventoryModels();
		if (inventoryModels.size() != 1) {
			throw new RuntimeException("Only 1 inventory model is supported at present.");
		}
		for (Inventory inventory : inventoryModels) {
			TreeMap<LocalDate, InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(inventory);
			// modify to take into account start date
			List<SlotAllocation> loadSlotsToConsider = getSortedFilteredLoadSlots(model, start, inventory, inventoryInsAndOuts);
			processWithLoadsAndStartDate(loadSlotsToConsider, inventoryInsAndOuts, start);
			// create new Loads
			matchAndMoveSlotsLoadTrigger(model, inventory.getPort(), inventoryInsAndOuts, globalLoadTrigger, cargoVolume, start);
		}
	}
	
	public List<Pair<LocalDate, LoadSlot>> getLoadDatesForExistingSlotsFromLoadTrigger(LNGScenarioModel model, Inventory inventory, int globalLoadTrigger, Integer cargoVolume, LocalDate start) {
		TreeMap<LocalDate, InventoryDailyEvent> inventoryInsAndOuts = getInventoryInsAndOuts(inventory);
		// modify to take into account start date
		List<SlotAllocation> loadSlotsToConsider = getSortedFilteredLoadSlots(model, start, inventory, inventoryInsAndOuts);
		processWithLoadsAndStartDate(loadSlotsToConsider, inventoryInsAndOuts, start);
		// create new Loads
		return getSlotsEarliestDate(model, inventory.getPort(), inventoryInsAndOuts, globalLoadTrigger, cargoVolume, start);
	}

	
	private TreeMap<LocalDate, InventoryDailyEvent> getInventoryInsAndOuts(Inventory inventory) {
		EList<InventoryCapacityRow> capacities = inventory.getCapacities();
		Port port = inventory.getPort();
		
		TreeMap<LocalDate, InventoryCapacityRow> capcityTreeMap = 
				capacities.stream()
				.collect(Collectors.toMap((c) -> c.getDate(),
							c -> c,
							(oldValue, newValue) -> newValue,
							TreeMap::new));
		
		TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = new TreeMap<>();
		
		// add all feeds to map
		addNetVolumes(inventory.getFeeds(), capcityTreeMap, insAndOuts, Function.identity());
		addNetVolumes(inventory.getOfftakes(), capcityTreeMap, insAndOuts, a -> -a);
		return insAndOuts;
	}


	private void processWithLoadsAndStartDate(List<SlotAllocation> loadSlotsToConsider, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, LocalDate start) {
		// get sum of feeds
		int totalInventoryVolume = 0; 
		while (insAndOuts.firstKey().isBefore(start)) {
			InventoryDailyEvent event = insAndOuts.remove(insAndOuts.firstKey());
			totalInventoryVolume += event.netVolumeIn;
		}
		// get sum of loads
		int totalVolumeOut = -1*loadSlotsToConsider.stream()
		.mapToInt(l->l.getPhysicalVolumeTransferred())
		.sum();
		InventoryDailyEvent firstEvent = insAndOuts.firstEntry().getValue();
		firstEvent.addVolume(totalInventoryVolume + totalVolumeOut);
	}

	/**
	 * Clears all existing slots and replaces with a slot whenever the volume reaches the global load trigger value.
	 * Slots are assumed to be of cargoVolume size
	 * @param model
	 * @param port
	 * @param insAndOuts
	 * @param globalLoadTrigger
	 * @param cargoVolume
	 * @param start
	 */
	private void standardLoadTrigger(LNGScenarioModel model, Port port, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, int globalLoadTrigger, int cargoVolume, LocalDate start) {
		List<LocalDate> loadDates = new LinkedList<>();
		int runningVolume = 0;
		clearCargoesAndSchedule(model, start);
		for (Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			runningVolume += entry.getValue().netVolumeIn;
			if (runningVolume > globalLoadTrigger) {
				loadDates.add(entry.getKey());
				runningVolume -= cargoVolume;
			}
		}
		createLoadSlots(model, port, loadDates, cargoVolume, start, true);
	}

	/**
	 * Identifies slot dates where inventory equals a global load trigger. Existing slots are moved to match these new dates, creating and removing slots as needed.
	 * Slots are assumed to be of cargoVolume size.
	 * @param model
	 * @param port
	 * @param insAndOuts
	 * @param cargoVolume
	 * @param start
	 */
	private void moveSlotsLoadTrigger(LNGScenarioModel model, Port port, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, int cargoVolume, LocalDate start) {
		List<LoadSlot> sortedSlots = model.getCargoModel().getLoadSlots().stream().filter(l->l.getPort() == port).sorted((a,b) -> a.getWindowStart().compareTo(b.getWindowStart())).collect(Collectors.toList());
		List<LocalDate> loadDates = new LinkedList<>();
		int runningVolume = 0;
		clearCargoesAndSchedule(model, start);
		assert (insAndOuts.firstKey().isBefore(sortedSlots.get(0).getWindowStart())
				|| insAndOuts.firstKey().isEqual(sortedSlots.get(0).getWindowStart()));
		
		for (Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			runningVolume += entry.getValue().netVolumeIn;
			if (runningVolume > entry.getValue().maxVolume) {
				if (sortedSlots.size() > 0) {
					LoadSlot remove = sortedSlots.remove(0);
					remove.setWindowStart(entry.getKey());
				} else {
					loadDates.add(entry.getKey());
				}
				runningVolume -= cargoVolume;
			}
			System.out.println(entry.getKey()+":"+runningVolume);
		}
		
		createLoadSlots(model, port, loadDates, cargoVolume, start, false);
		clearLoadSlots(sortedSlots, model.getCargoModel(), model);
	}

	/**
	 * Identifies slot dates where inventory equals a global load trigger. Existing slots are moved to match these new dates, creating and removing slots as needed.
	 * Slots are assumed to be of cargoVolume size.
	 * @param model
	 * @param port
	 * @param insAndOuts
	 * @param globalLoadTrigger
	 * @param cargoVolume
	 * @param start
	 */
	private void matchAndMoveSlotsLoadTrigger(LNGScenarioModel model, Port port, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, int globalLoadTrigger, Integer cargoVolume, LocalDate start) {
		List<LoadSlot> sortedSlots = getSortedSlots(model, port, start);
		
		List<LocalDate> loadDates = getLoadDates(insAndOuts, globalLoadTrigger, cargoVolume);
		
		// Assign the new load date to the current load slot
		int interSize = Math.min(sortedSlots.size(), loadDates.size());
		
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

	private List<LoadSlot> getSortedSlots(LNGScenarioModel model, Port port, LocalDate start) {
		List<LoadSlot> sortedSlots = model.getCargoModel().getLoadSlots().stream() //
				.filter(l->l.getPort() == port && (l.getWindowStart().isAfter(start) || l.getWindowStart().isEqual(start))) //
				.sorted((a,b) -> a.getWindowStart().compareTo(b.getWindowStart())) //
				.collect(Collectors.toList());
		return sortedSlots;
	}
	
	private List<Pair<LocalDate, LoadSlot>> getSlotsEarliestDate(LNGScenarioModel model, Port port, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, int globalLoadTrigger, Integer cargoVolume, LocalDate start) {
		List<Pair<LocalDate, LoadSlot>> slotsWithDates = new LinkedList<>();
		List<LoadSlot> sortedSlots = getSortedSlots(model, port, start);
		List<LocalDate> loadDates = getLoadDates(insAndOuts, globalLoadTrigger, cargoVolume);
		for (int i = 0; i < Math.min(sortedSlots.size(), loadDates.size()); i++) {
			slotsWithDates.add(
					new Pair<LocalDate, LoadSlot>(loadDates.get(i), sortedSlots.get(i))
					);
		}
		return slotsWithDates;
	}

	private List<LocalDate> getLoadDates(TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, int globalLoadTrigger, Integer cargoVolume) {
		// Create all the load date
		List<LocalDate> loadDates = new LinkedList<>();
		int runningVolume = 0;
		for (Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			runningVolume += entry.getValue().netVolumeIn;
			if (runningVolume > globalLoadTrigger) {
				loadDates.add(entry.getKey());
				runningVolume -= cargoVolume;
			}
		}
		return loadDates;
	}
	
	private void addNetVolumes(List<InventoryEventRow> events, TreeMap<LocalDate, InventoryCapacityRow> capcityTreeMap, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, Function<Integer, Integer> volumeFunction) {
		for (InventoryEventRow inventoryEventRow : events) {
			if (inventoryEventRow.getStartDate() != null) {
				InventoryDailyEvent inventoryDailyEvent = insAndOuts.get(inventoryEventRow.getStartDate());
				if (inventoryDailyEvent == null) {
					inventoryDailyEvent = new InventoryDailyEvent();
					inventoryDailyEvent.date = inventoryEventRow.getStartDate();
					InventoryCapacityRow capacityRow = capcityTreeMap.get(inventoryDailyEvent.date) == null //
							? capcityTreeMap.lowerEntry(inventoryDailyEvent.date).getValue() //
							: capcityTreeMap.get(inventoryDailyEvent.date);
					inventoryDailyEvent.minVolume = capacityRow.getMinVolume();
					inventoryDailyEvent.maxVolume = capacityRow.getMaxVolume();
					insAndOuts.put(inventoryEventRow.getStartDate(), inventoryDailyEvent);
				}
				inventoryDailyEvent.addVolume(volumeFunction.apply(inventoryEventRow.getReliableVolume()));
			}
		}
	}

	/**
	 * Entry point to create Load slots
	 * Taking the very first contract. (due to client L preferences)
	 * 
	 */
	public void createLoadSlots(final LNGScenarioModel scenario, final Port port, final List<LocalDate> dates, final int cargoVolume, final LocalDate start, boolean clear) {
		// First clear all load slots
		final EList<LoadSlot> loadSlots = scenario.getCargoModel().getLoadSlots();
		if (loadSlots != null) {
			// Clear all cargoes
			if (clear)
				clearCargoesAndSchedule(scenario, start);
		}
		CargoModelBuilder builder = new CargoModelBuilder(scenario.getCargoModel());
		int i = 0;
		
		PurchaseContract mdpc = null;
		if (!scenario.getReferenceModel().getCommercialModel().getPurchaseContracts().isEmpty()) {
			mdpc = scenario.getReferenceModel().getCommercialModel().getPurchaseContracts().get(0);
		}
		
		for (LocalDate slotDate : dates) {
			assert port != null;
			SlotMaker<LoadSlot> loadMaker = new SlotMaker<>(builder);
			loadMaker.withFOBPurchase(String.format("%s_%s","load", ++i), slotDate, port, mdpc,
					scenario.getReferenceModel().getCommercialModel().getEntities().get(0), null, null);
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
		Iterator<Cargo> cargoIterator = scenario.getCargoModel().getCargoes().iterator();
		while (cargoIterator.hasNext()) {
			Cargo cargo = cargoIterator.next();
			if (cargo.getSlots().get(0).getWindowStart().isAfter(start.minusDays(1))) {
				cargo.getSlots().forEach(s->s.setCargo(null));
				cargo.getSlots().clear();
				cargoIterator.remove();
			}
		}
		Iterator<LoadSlot> loadsIterator = scenario.getCargoModel().getLoadSlots().iterator();
		while (loadsIterator.hasNext()) {
			LoadSlot loadSlot = loadsIterator.next();
			if (loadSlot.getWindowStart().isAfter(start.minusDays(1))) {
				loadsIterator.remove();
			}
		}
		final Schedule schedule = scenario.getScheduleModel().getSchedule();
		clearSchedule(scenario, schedule);
	}
	
	private void clearLoadSlots(Collection<LoadSlot> loadSlots, CargoModel cargoModel, LNGScenarioModel scenario) {
		for (LoadSlot loadSlot : loadSlots) {
			Cargo cargo = loadSlot.getCargo();

			if (cargo != null) {
				List<Slot> slots = new ArrayList<>(cargo.getSlots());
				for (Slot slot : slots) {
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


	private List<SlotAllocation> getSortedFilteredLoadSlots(LNGScenarioModel model, LocalDate start, Inventory inventory, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts) {
		return model.getScheduleModel().getSchedule().getSlotAllocations().stream()
				.filter(s->s.getSlot() instanceof LoadSlot)
				.filter(l->(l.getSlot().getPort() != null && l.getSlot().getPort().equals(inventory.getPort())))
				.filter(l->(l.getSlot().getWindowStart().isAfter(insAndOuts.firstEntry().getKey().minusDays(1))
				&& l.getSlot().getWindowStart().isBefore(start)))
				.sorted((a,b)->{
					if (a.getSlot().getWindowStart() != null && b.getSlot().getWindowStart() != null) {
						return a.getSlot().getWindowStart().compareTo(b.getSlot().getWindowStart());
					} else if (a.getSlot().getWindowStart() == null) {
						return -1;
					} else if (a.getSlot().getWindowStart() == null && b.getSlot().getWindowStart() == null) {
						return 0;
					}
					else {
						return 1;
					}
					})
				.collect(Collectors.toList());
	}
	
}
