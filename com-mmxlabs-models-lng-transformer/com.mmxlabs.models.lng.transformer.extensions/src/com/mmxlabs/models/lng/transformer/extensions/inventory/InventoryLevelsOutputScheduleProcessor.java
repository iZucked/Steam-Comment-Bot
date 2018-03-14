/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.inventory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BiFunction;

import javax.inject.Inject;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class InventoryLevelsOutputScheduleProcessor implements IOutputScheduleProcessor {

	@Inject
	private LNGScenarioModel scenarioModel;

	@Override
	public void process(final Schedule schedule) {
		if (LicenseFeatures.isPermitted("features:inventory-model")) {

			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

			for (final Inventory facility : cargoModel.getInventoryModels()) {
				final InventoryEvents inventoryChangeEvents = ScheduleFactory.eINSTANCE.createInventoryEvents();
				inventoryChangeEvents.setFacility(facility);

				schedule.getInventoryLevels().add(inventoryChangeEvents);

				final BiFunction<LocalDate, LocalDate, LocalDate> f_minDate = (a, b) -> {
					if (a == null) {
						return b;
					}
					if (b == null || a.isBefore(b)) {
						return a;
					}
					return b;
				};
				final BiFunction<LocalDate, LocalDate, LocalDate> f_maxDate = (a, b) -> {
					if (a == null) {
						return b;
					}
					if (b == null || a.isAfter(b)) {
						return a;
					}
					return b;
				};

				// int colour = 2;
				LocalDate minDate = null;
				LocalDate maxDate = null;
				LocalDate maxLevelDate = null;

				final List<InventoryChangeEvent> events = new LinkedList<>();

				for (final InventoryEventRow r : facility.getFeeds()) {
					minDate = f_minDate.apply(minDate, r.getStartDate());
					maxDate = f_maxDate.apply(maxDate, r.getStartDate());

					if (r.getPeriod() == InventoryFrequency.LEVEL || r.getPeriod() == InventoryFrequency.CARGO) {
						final InventoryChangeEvent evt = ScheduleFactory.eINSTANCE.createInventoryChangeEvent();
						evt.setEvent(r);
						evt.setDate(r.getStartDate().atStartOfDay());
						evt.setChangeQuantity(r.getReliableVolume());
						if (r.getPeriod() == InventoryFrequency.LEVEL) {
							maxLevelDate = f_maxDate.apply(maxLevelDate, r.getStartDate());
						}
						events.add(evt);
					} else {
						maxDate = f_maxDate.apply(maxDate, r.getEndDate());

						LocalDateTime start = r.getStartDate().atStartOfDay();
						if (r.getStartDate() == r.getEndDate()) {
							final InventoryChangeEvent evt = ScheduleFactory.eINSTANCE.createInventoryChangeEvent();
							evt.setEvent(r);
							evt.setDate(r.getStartDate().atStartOfDay());
							evt.setChangeQuantity(r.getReliableVolume());
							events.add(evt);
						} else {
							while (start.isBefore(r.getEndDate().plusDays(1).atStartOfDay())) {

								final InventoryChangeEvent evt = ScheduleFactory.eINSTANCE.createInventoryChangeEvent();
								evt.setEvent(r);
								evt.setDate(start);
								evt.setChangeQuantity(r.getReliableVolume());

								if (r.getPeriod() == InventoryFrequency.HOURLY) {
									start = start.plusHours(1);
								} else if (r.getPeriod() == InventoryFrequency.DAILY) {
									start = start.plusDays(1);
								} else if (r.getPeriod() == InventoryFrequency.MONTHLY) {
									start = start.plusMonths(1);
								} else {
									assert false;
								}
								events.add(evt);
							}
						}
					}
				}
				for (final InventoryEventRow r : facility.getOfftakes()) {

					minDate = f_minDate.apply(minDate, r.getStartDate());
					maxDate = f_maxDate.apply(maxDate, r.getStartDate());

					if (r.getPeriod() == InventoryFrequency.LEVEL || r.getPeriod() == InventoryFrequency.CARGO) {
						final InventoryChangeEvent evt = ScheduleFactory.eINSTANCE.createInventoryChangeEvent();
						evt.setEvent(r);
						evt.setDate(r.getStartDate().atStartOfDay());
						evt.setChangeQuantity(-r.getReliableVolume());
						if (r.getPeriod() == InventoryFrequency.LEVEL) {
							maxLevelDate = f_maxDate.apply(maxLevelDate, r.getStartDate());
						}
						events.add(evt);
					} else {
						maxDate = f_maxDate.apply(maxDate, r.getEndDate());

						LocalDateTime start = r.getStartDate().atStartOfDay();
						if (r.getStartDate() == r.getEndDate()) {
							final InventoryChangeEvent evt = ScheduleFactory.eINSTANCE.createInventoryChangeEvent();
							evt.setEvent(r);
							evt.setDate(r.getStartDate().atStartOfDay());
							evt.setChangeQuantity(-r.getReliableVolume());
							events.add(evt);
						} else {
							while (start.isBefore(r.getEndDate().plusDays(1).atStartOfDay())) {

								final InventoryChangeEvent evt = ScheduleFactory.eINSTANCE.createInventoryChangeEvent();
								evt.setEvent(r);
								evt.setDate(start);
								evt.setChangeQuantity(-r.getReliableVolume());

								if (r.getPeriod() == InventoryFrequency.HOURLY) {
									start = start.plusHours(1);
								} else if (r.getPeriod() == InventoryFrequency.DAILY) {
									start = start.plusDays(1);
								} else if (r.getPeriod() == InventoryFrequency.MONTHLY) {
									start = start.plusMonths(1);
								} else {
									assert false;
								}
								events.add(evt);
							}
						}
					}
				}
				// }
				//
				// LocalDate latestInventoryDate = null;
				// if (tableLevels.size() > 0) {
				// Optional<InventoryLevel> inventoryLevel = tableLevels.stream().sorted((a,b) -> -a.date.compareTo(b.date)).findFirst();
				// if (inventoryLevel.isPresent()) {
				// latestInventoryDate = inventoryLevel.get().date;
				// }
				// }
				// final ScheduleModel scheduleModel = toDisplay.getTypedResult(ScheduleModel.class);
				if (maxDate != null && schedule != null) {
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
						if (slotAllocation.getPort() == facility.getPort()) {
							final int change = (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) ? -slotAllocation.getPhysicalVolumeTransferred()
									: slotAllocation.getPhysicalVolumeTransferred();
							final ZonedDateTime start = slotAllocation.getSlotVisit().getStart();
							final LocalDate date = start.toLocalDate();

							// don't look at slots after end of inventory
							if (maxDate.compareTo(date) < 0) {
								continue;
							}

							final InventoryChangeEvent evt = ScheduleFactory.eINSTANCE.createInventoryChangeEvent();
							evt.setSlotAllocation(slotAllocation);
							evt.setDate(start.toLocalDateTime());
							evt.setChangeQuantity(change);
							events.add(evt);
						}
					}
					// Include open positions?
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
						if (slotAllocation.getSlot().getPort() == facility.getPort()) {
							int change = (slotAllocation.getSlot() instanceof LoadSlot) ? -slot.getSlotOrDelegateMaxQuantity() : slot.getSlotOrDelegateMaxQuantity();

							if (slot.getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.MMBTU) {
								if (slot instanceof LoadSlot) {
									final double cv = ((LoadSlot) slot).getSlotOrDelegateCV();
									change = (int) (change / cv);
								} else {
									continue;
								}
							}

							final InventoryChangeEvent evt = ScheduleFactory.eINSTANCE.createInventoryChangeEvent();
							evt.setOpenSlotAllocation(slotAllocation);
							evt.setDate(slotAllocation.getSlot().getWindowStartWithSlotOrPortTime().toLocalDateTime());
							evt.setChangeQuantity(change);
							events.add(evt);
						}
					}
				}

				final TreeMap<LocalDate, Integer> min_levels = new TreeMap<>();
				final TreeMap<LocalDate, Integer> max_levels = new TreeMap<>();

				for (final InventoryCapacityRow r : facility.getCapacities()) {
					min_levels.put(r.getDate(), r.getMinVolume());
					max_levels.put(r.getDate(), r.getMaxVolume());
				}

				Collections.sort(events, (a, b) -> a.getDate().compareTo(b.getDate()));

				int inventoryLevel = 0;
				for (final InventoryChangeEvent evt : events) {
					boolean isLevel = false;
					if (evt.getEvent() instanceof InventoryEventRow) {
						final InventoryEventRow inventoryEventRow = (InventoryEventRow) evt.getEvent();
						if (inventoryEventRow.getPeriod() == InventoryFrequency.LEVEL) {
							isLevel = true;
						}
					}
					if (isLevel) {
						inventoryLevel = evt.getChangeQuantity();
					} else {
						inventoryLevel += evt.getChangeQuantity();
					}
					evt.setCurrentLevel(inventoryLevel);
					{
						final Entry<LocalDate, Integer> minLevel = min_levels.floorEntry(evt.getDate().toLocalDate());
						if (minLevel != null) {
							evt.setCurrentMin(minLevel.getValue());
							if (evt.getCurrentLevel() < evt.getCurrentMin()) {
								evt.setBreachedMin(true);
							}
						}
					}
					{
						final Entry<LocalDate, Integer> maxLevel = max_levels.floorEntry(evt.getDate().toLocalDate());
						if (maxLevel != null) {
							evt.setCurrentMax(maxLevel.getValue());
							if (evt.getCurrentLevel() > evt.getCurrentMax()) {
								evt.setBreachedMax(true);
							}
						}
					}
				}
				inventoryChangeEvents.getEvents().addAll(events);
			}
		}
	}
}
