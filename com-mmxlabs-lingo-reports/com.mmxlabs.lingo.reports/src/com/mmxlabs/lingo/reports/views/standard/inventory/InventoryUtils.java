/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class InventoryUtils {
	private static void addToInventoryLevelList(final List<InventoryLevel> tableLevels, final InventoryLevel lvl) {
		//can swap with proper search through the list
		final int i = tableLevels.size();
		if (i > 0) {
			final InventoryLevel tlvl = tableLevels.get(i-1);
			if (tlvl.date.equals(lvl.date)) {
				tlvl.merge(lvl);
			} else {
				tableLevels.add(lvl);
			}
		} else {
			tableLevels.add(lvl);
		}
	}

	private static void setInventoryLevelFeed(final InventoryLevel lvl) {
		if (lvl.changeInM3 > 0) {
			lvl.feedIn = lvl.changeInM3;
		}else {
			lvl.feedOut = lvl.changeInM3;
		}
	}
	
	public static List<InventoryLevel> createInventoryLevels(final ScheduleModel scheduleModel, final Port port) {
		
		final List<InventoryLevel> invLevels = new LinkedList<>();  
		Optional<InventoryChangeEvent> firstInventoryData = Optional.empty();

			if (scheduleModel != null) {
				final Schedule schedule = scheduleModel.getSchedule();
				if (schedule != null) {
					for (final InventoryEvents inventoryEvents : schedule.getInventoryLevels()) {
						final Inventory inventory = inventoryEvents.getFacility();
						if (inventory.getName() == null || inventory.getPort() != port) {
							continue;
						}
						// Find the date of the latest position/cargo
						final Optional<LocalDateTime> latestLoad = inventoryEvents.getEvents().stream() //
								.filter(evt -> evt.getSlotAllocation() != null || evt.getOpenSlotAllocation() != null) //
								.map(InventoryChangeEvent::getDate) //
								.reduce((a, b) -> a.isAfter(b) ? a : b);

						// Find the first inventory feed event
						firstInventoryData = inventoryEvents.getEvents().stream() //
								.filter(evt -> evt.getSlotAllocation() == null && evt.getOpenSlotAllocation() == null)
								.reduce((a, b) -> a.getDate().isAfter(b.getDate()) ? b : a);

						{
							final Optional<InventoryChangeEvent> firstInventoryDataFinal = firstInventoryData;
							final List<Pair<LocalDateTime, Integer>> inventoryLevels = inventoryEvents.getEvents().stream() //
									.collect(Collectors.toMap( //
											InventoryChangeEvent::getDate, //
											InventoryChangeEvent::getCurrentLevel, //
											(a, b) -> (b), // Take latest value
											LinkedHashMap::new))
									.entrySet().stream() //
									.filter(x -> {
										if (latestLoad.isPresent() && firstInventoryDataFinal.isPresent()) {
											return x.getKey().isBefore(latestLoad.get()) && x.getKey().isAfter(firstInventoryDataFinal.get().getDate());
										}
										return false;
									}).map(e -> new Pair<>(e.getKey(), e.getValue())) //
									.collect(Collectors.toList());
							if (!inventoryLevels.isEmpty()) {

								inventoryEvents.getEvents().forEach(e -> {
									String type = "Unknown";
									if (e.getSlotAllocation() != null) {
										type = "Cargo";
										final String vessel = e.getSlotAllocation().getCargoAllocation().getEvents().get(0).getSequence().getName();
										final SlotAllocation dischargeAllocation = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(x -> x.getSlot() instanceof DischargeSlot)
												.findFirst().get();
										final SlotAllocation loadAllocation = e.getSlotAllocation().getCargoAllocation().getSlotAllocations().stream().filter(x -> x.getSlot() instanceof LoadSlot)
												.findFirst().get();
										final String dischargeId = dischargeAllocation.getName();
										final String dischargePort = dischargeAllocation.getPort().getName();
										final String loadId = loadAllocation.getName();
										final String loadPort = loadAllocation.getPort().getName();
										Contract contract = dischargeAllocation.getContract();
										String salesContract = "";
										if (contract != null) {
											salesContract = contract.getName();
										}
										contract = loadAllocation.getContract();
										String purchaseContract = "";
										if (contract != null) {
											purchaseContract = contract.getName();
										}
										
										ZonedDateTime dischargeTime = dischargeAllocation.getSlotVisit().getStart();
										ZonedDateTime loadTime = loadAllocation.getSlotVisit().getStart();
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), type, e.getChangeQuantity(), vessel, //
												dischargeId, dischargePort, salesContract, loadId, loadPort, purchaseContract, //
												dischargeTime == null ? null : dischargeTime.toLocalDate(), //
												loadTime == null ? null : loadTime.toLocalDate());
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										if (e.getEvent() != null) {
											lvl.volumeLow = e.getEvent().getVolumeLow();
											lvl.volumeHigh = e.getEvent().getVolumeHigh();
										}
										// FM cargo out and in happens only when there's a vessel
										if (inventory.getFacilityType() == InventoryFacilityType.DOWNSTREAM 
												|| inventory.getFacilityType() == InventoryFacilityType.HUB) {
											lvl.cargoIn = lvl.changeInM3;
										}
										if (inventory.getFacilityType() == InventoryFacilityType.UPSTREAM 
												|| inventory.getFacilityType() == InventoryFacilityType.HUB){
											lvl.cargoOut = lvl.changeInM3;
										}
										InventoryUtils.addToInventoryLevelList(invLevels, lvl);
									} else if (e.getOpenSlotAllocation() != null) {
										type = "Open";
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), type, e.getChangeQuantity(), //
												null, null, null, null, null, null, null, null, null);
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										
										// FM
										InventoryUtils.setInventoryLevelFeed(lvl);
										InventoryUtils.addToInventoryLevelList(invLevels, lvl);
									} else if (e.getEvent() != null) {
										final InventoryLevel lvl = new InventoryLevel(e.getDate().toLocalDate(), e.getEvent().getPeriod(), e.getChangeQuantity(), //
												null, null, null, null, null, null, null, null, null);
										lvl.breach = e.isBreachedMin() || e.isBreachedMax();
										
										// FM
										InventoryUtils.setInventoryLevelFeed(lvl);
										InventoryUtils.addToInventoryLevelList(invLevels, lvl);
									}
								});
							}
						}
					}
				}
			}
			int total = 0;
			if (firstInventoryData.isPresent()) {
				// Set the current level to the first data in the list. Remove the change quantity so the first iteration of the loop tallies.
				final InventoryChangeEvent evt = firstInventoryData.get();
				total = evt.getCurrentLevel() - evt.getChangeQuantity();
			}
			for (final InventoryLevel lvl : invLevels) {
				lvl.runningTotal = total + lvl.changeInM3;
				total = lvl.runningTotal;
			}
			return invLevels;
		}
	}
