/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.Port;

@NonNullByDefault
public class InventoryGlobalState {
	private final Inventory inventory;
	private final PurchaseContract purchaseContract;
	private final int yearlyProduction;
	private final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts;
	private final Map<YearMonth, Integer> monthlyProductions;
	private final int preAdpStartTankVolume;
	private final ILiftTimeSpecifier liftTimeSpecifier;

	public InventoryGlobalState(final Inventory inventory, final PurchaseContract purchaseContract, final LocalDateTime productionStartInclusive, final LocalDateTime productionStopExclusive, final ILiftTimeSpecifier liftTimeSpecifier) {
		this.inventory = inventory;
		if (inventory.getPort() == null) {
			throw new IllegalStateException("Inventory must be at a port");
		}
		if (inventory.getPort().getLoadDuration() <= 0) {
			throw new IllegalStateException("Inventory port must have a positive load duration");
		}
		this.purchaseContract = purchaseContract;
		this.yearlyProduction = calculateYearlyProduction(inventory, productionStartInclusive, productionStopExclusive);
		if (this.yearlyProduction < 0) {
			throw new IllegalStateException("Inventory yearly production should be non-negative");
		}
		this.insAndOuts = calculateInventoryInsAndOutsHourly(inventory, productionStartInclusive, productionStopExclusive);
		int startVolume = 0;
		while (this.insAndOuts.firstKey().isBefore(productionStartInclusive)) {
			final InventoryDateTimeEvent event = this.insAndOuts.remove(this.insAndOuts.firstKey());
			startVolume += event.getNetVolumeIn();
		}
		this.preAdpStartTankVolume = startVolume;
		this.monthlyProductions = new HashMap<>();
		this.insAndOuts.forEach((dateTime, event) -> this.monthlyProductions.compute(YearMonth.from(dateTime), (k, v) -> v != null ? v + event.getNetVolumeIn() : event.getNetVolumeIn()));
		this.liftTimeSpecifier = liftTimeSpecifier;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public PurchaseContract getPurchaseContract() {
		return this.purchaseContract;
	}

	public Port getPort() {
		return inventory.getPort();
	}

	public int getLoadDuration() {
		return this.inventory.getPort().getLoadDuration();
	}

	public int getYearlyProduction() {
		return this.yearlyProduction;
	}

	public int getNumberOfBerths() {
		return this.inventory.getPort().getBerths();
	}

	public TreeMap<LocalDateTime, InventoryDateTimeEvent> getInsAndOuts() {
		return this.insAndOuts;
	}

	public int getPreAdpStartTankVolume() {
		return this.preAdpStartTankVolume;
	}

	public int getProductionInMonth(final YearMonth yearMonth) {
		return this.monthlyProductions.get(yearMonth);
	}

	public ILiftTimeSpecifier getLiftTimeSpecifier() {
		return liftTimeSpecifier;
	}

	private static int calculateYearlyProduction(final Inventory inventory, final LocalDateTime startTimeInclusive, final LocalDateTime endTimeExclusive) {
		int production = 0;
		for (final InventoryEventRow eventRow : inventory.getFeeds()) {
			final LocalDate eventStart = eventRow.getStartDate();
			if (eventStart != null && !eventStart.isBefore(startTimeInclusive.toLocalDate()) && eventStart.isBefore(endTimeExclusive.toLocalDate())) {
				final InventoryFrequency freq = eventRow.getPeriod();
				if (freq == InventoryFrequency.DAILY) {
					final LocalDate eventEnd = eventRow.getEndDate();
					for (LocalDate currentDate = eventStart; !currentDate.isAfter(eventEnd); currentDate = currentDate.plusDays(1L)) {
						production += eventRow.getReliableVolume();
					}
				} else if (freq == InventoryFrequency.HOURLY) {
					final LocalDate eventEnd = eventRow.getEndDate();
					final int delta = 24 * eventRow.getReliableVolume();
					for (LocalDate currentDate = eventStart; !currentDate.isAfter(eventEnd); currentDate = currentDate.plusDays(1L)) {
						production += delta;
					}
				} else if (freq == InventoryFrequency.LEVEL) {
					production += eventRow.getReliableVolume();
				}
			}
		}
		return production;
	}

	private static TreeMap<LocalDateTime, InventoryDateTimeEvent> calculateInventoryInsAndOutsHourly(final Inventory inventory, final LocalDateTime startTimeInclusive,
			final LocalDateTime endDateTimeExclusive) {
		final List<InventoryCapacityRow> capacities = inventory.getCapacities();
		final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap = capacities.stream()
				.collect(Collectors.toMap(InventoryCapacityRow::getDate, Function.identity(), (oldVal, newVal) -> newVal, TreeMap::new));
		final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts = new TreeMap<>();

		final InventoryEventRow lastPreAdpLevel = getLastPreTimeLevelFeed(inventory.getFeeds(), startTimeInclusive);
		final Stream<InventoryEventRow> inTimeRangeFeedsStream = inventory.getFeeds().stream().filter(f -> !f.getStartDate().isBefore(startTimeInclusive.toLocalDate()));
		final Stream<InventoryEventRow> feedsOfInterest = lastPreAdpLevel == null ? inTimeRangeFeedsStream : Stream.concat(Stream.of(lastPreAdpLevel), inTimeRangeFeedsStream);
		final List<InventoryEventRow> filteredFeeds = feedsOfInterest.toList();

		addHourlyNetVolumes(filteredFeeds, capacityTreeMap, insAndOuts, IntUnaryOperator.identity());
		addHourlyNetVolumes(inventory.getOfftakes(), capacityTreeMap, insAndOuts, a -> -a);

		for (LocalDateTime currentDateTime = startTimeInclusive; currentDateTime.isBefore(endDateTimeExclusive); currentDateTime = currentDateTime.plusHours(1L)) {
			if (!insAndOuts.containsKey(currentDateTime)) {
				InventoryCapacityRow capacityRow = capacityTreeMap.get(currentDateTime.toLocalDate());
				if (capacityRow == null) {
					capacityRow = capacityTreeMap.lowerEntry(currentDateTime.toLocalDate()).getValue();
				}
				final InventoryDateTimeEvent inventoryDateTimeEvent = new InventoryDateTimeEvent(currentDateTime, 0, capacityRow.getMinVolume(), capacityRow.getMaxVolume());
				insAndOuts.put(currentDateTime, inventoryDateTimeEvent);
			}
		}
		return insAndOuts;
	}

	@Nullable
	private static InventoryEventRow getLastPreTimeLevelFeed(final List<InventoryEventRow> feeds, final LocalDateTime startDateTimeInclusive) {
		final LocalDate startLocalDateInclusive = startDateTimeInclusive.toLocalDate();
		final Iterator<InventoryEventRow> levelFeedsBeforeAdpStartIter = feeds.stream().filter(f -> f.getPeriod() == InventoryFrequency.LEVEL && f.getStartDate().isBefore(startLocalDateInclusive))
				.iterator();

		if (!levelFeedsBeforeAdpStartIter.hasNext()) {
			return null;
		}
		InventoryEventRow lastPreAdpLevel = levelFeedsBeforeAdpStartIter.next();
		while (levelFeedsBeforeAdpStartIter.hasNext()) {
			final InventoryEventRow feed = levelFeedsBeforeAdpStartIter.next();
			if (lastPreAdpLevel.getStartDate().isBefore(feed.getStartDate())) {
				lastPreAdpLevel = feed;
			}
		}
		return lastPreAdpLevel;
	}

	private static void addHourlyNetVolumes(final List<InventoryEventRow> events, final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap,
			final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts, final IntUnaryOperator volumeFunction) {
		for (final InventoryEventRow inventoryEventRow : events) {
			final LocalDate eventStart = inventoryEventRow.getStartDate();
			final LocalDate eventEnd = inventoryEventRow.getEndDate();
			if (eventStart != null && eventEnd != null) {
				if (inventoryEventRow.getPeriod() == InventoryFrequency.LEVEL) {
					final LocalDateTime expectedDateTime = LocalDateTime.of(eventStart, LocalTime.of(0, 0));
					addSingleVolume(capacityTreeMap, insAndOuts, volumeFunction, expectedDateTime, inventoryEventRow.getReliableVolume());
				} else if (inventoryEventRow.getPeriod() == InventoryFrequency.DAILY) {
					final int delta = inventoryEventRow.getReliableVolume() / 24;
					final int firstAmount = delta + inventoryEventRow.getReliableVolume() % 24;
					final LocalDate exclusiveEnd = eventStart.equals(eventEnd) ? eventStart.plusDays(1L) : eventEnd;
					for (LocalDate currentDate = eventStart; currentDate.isBefore(exclusiveEnd); currentDate = currentDate.plusDays(1)) {
						final LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.of(0, 0));
						addSingleVolume(capacityTreeMap, insAndOuts, volumeFunction, currentDateTime, firstAmount);
						for (int hour = 1; hour < 24; ++hour) {
							final LocalDateTime expectedDateTime = LocalDateTime.of(currentDate, LocalTime.of(hour, 0));
							addSingleVolume(capacityTreeMap, insAndOuts, volumeFunction, expectedDateTime, delta);
						}
					}
				} else if (inventoryEventRow.getPeriod() == InventoryFrequency.HOURLY) {
					final LocalDate eventStop = inventoryEventRow.getEndDate();
					if (eventStop != null && eventStart.isBefore(eventStop)) {
						final int volume = inventoryEventRow.getReliableVolume();
						final LocalDateTime endDateTime = LocalDateTime.of(eventStop, LocalTime.of(0, 0));
						LocalDateTime dateTimeCounter;
						for (dateTimeCounter = LocalDateTime.of(eventStart, LocalTime.of(0, 0)); dateTimeCounter.isBefore(endDateTime); dateTimeCounter = dateTimeCounter.plusHours(1L)) {
							addSingleVolume(capacityTreeMap, insAndOuts, volumeFunction, dateTimeCounter, volume);
						}
					}
				}
			}
		}
	}

	private static void addSingleVolume(final TreeMap<LocalDate, @Nullable InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDateTime, @Nullable InventoryDateTimeEvent> insAndOuts,
			final IntUnaryOperator volumeFunction, final LocalDateTime expectedDateTime, final int absoluteVolume) {
		InventoryDateTimeEvent inventoryDateTimeEvent = insAndOuts.get(expectedDateTime);
		if (inventoryDateTimeEvent == null) {
			InventoryCapacityRow capacityRow = capacityTreeMap.get(expectedDateTime.toLocalDate());
			if (capacityRow == null) {
				capacityRow = capacityTreeMap.lowerEntry(expectedDateTime.toLocalDate()).getValue();
			}
			final int minVolume = capacityRow.getMinVolume();
			final int maxVolume = capacityRow.getMaxVolume();
			inventoryDateTimeEvent = new InventoryDateTimeEvent(expectedDateTime, volumeFunction.applyAsInt(absoluteVolume), minVolume, maxVolume);
			insAndOuts.put(expectedDateTime, inventoryDateTimeEvent);
		} else {
			inventoryDateTimeEvent.addVolume(volumeFunction.applyAsInt(absoluteVolume));
		}
	}
}
