/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;

@NonNullByDefault
public class VanillaRollingLoadWindow extends RollingLoadWindow {

	protected final LocalTime liftTriggerTime;
	protected final HourlyToDailyEventIterator lookaheadIterator;

	public VanillaRollingLoadWindow(InventoryGlobalState inventoryGlobalState, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> entries, int maxExistingLoadDuration, int initialTankVolume,
			final LocalTime liftTriggerTime) {
		super(inventoryGlobalState, entries, maxExistingLoadDuration, initialTankVolume);
		this.liftTriggerTime = liftTriggerTime;
		lookaheadIterator = new HourlyToDailyEventIterator(inventoryGlobalState.getInsAndOuts().entrySet().iterator());
	}

	@Override
	public boolean isLoading() {
		// vanilla uses a daily granularity
		return false;
	}

	@Override
	public boolean canLift(int allocationDrop) {
		final LocalTime localLoadTime = LocalTime.of(this.startDateTime.getHour(), 0);
		if (!localLoadTime.equals(this.liftTriggerTime)) {
			return false;
		}

		final int productionAmount;
		final LocalDate currentDate = this.startDateTime.toLocalDate();
		if (currentDate.equals(lookaheadIterator.getCachedLastNextDate())) {
			productionAmount = lookaheadIterator.getCachedLastNextVolume();
		} else {
			if (lookaheadIterator.hasNext()) {
				final Pair<LocalDate, Integer> nextPair = lookaheadIterator.next();
				assert currentDate.equals(nextPair.getFirst());
				productionAmount = nextPair.getSecond();
			} else {
				productionAmount = 0;
			}
		}
		return this.beforeWindowTankVolume + productionAmount >= allocationDrop + this.endWindowTankMin;
	}

	@Override
	public int getProductionToAllocate() {
		final LocalTime currentTime = this.startDateTime.toLocalTime();
		if (currentTime != this.liftTriggerTime) {
			return 0;
		}
		final LocalDate currentDate = this.startDateTime.toLocalDate();
		if (currentDate.equals(lookaheadIterator.getCachedLastNextDate())) {
			return lookaheadIterator.getCachedLastNextVolume();
		} else {
			if (lookaheadIterator.hasNext()) {
				final Pair<LocalDate, Integer> nextPair = lookaheadIterator.next();
				assert currentDate.equals(nextPair.getFirst());
				return nextPair.getSecond();
			} else {
				return 0;
			}
		}
	}
}
