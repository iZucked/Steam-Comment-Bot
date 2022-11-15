/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class PositionsSequence {
	private final List<Object> elements;
	private final String lbl;
	private final Schedule schedule;
	private final boolean isBuy;

	public PositionsSequence(final String lbl, final boolean isBuy, final Schedule schedule, final List<Object> elements) {
		this.lbl = lbl;
		this.isBuy = isBuy;
		this.elements = elements;
		this.schedule = schedule;
	}

	@Override
	public @NonNull String toString() {
		return lbl;
	}

	public List<?> getElements() {
		return elements;
	}

	public static PositionsSequence makeBuySequence(final Schedule schedule) {

		final Map<LocalDate, List<Object>> collectedDates = new TreeMap<>();
		schedule.getOpenSlotAllocations().stream().filter(sa -> sa.getSlot() instanceof LoadSlot)
				.forEach(sa -> collectedDates.computeIfAbsent(sa.getSlot().getWindowStart(), k -> new LinkedList<>()).add(sa));

		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			if (ca.getCargoType() == CargoType.DES || ca.getCargoType() == CargoType.FOB) {
				for (final SlotAllocation sa : ca.getSlotAllocations()) {
					if (sa.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
						final SlotVisit sv = sa.getSlotVisit();
						collectedDates.computeIfAbsent(sv.getStart().toLocalDate(), k -> new LinkedList<>()).add(sv);
					}
				}
			}
		}
		final List<Object> elements = new LinkedList<>();
		for (final var e : collectedDates.entrySet()) {
			final List<Object> l = e.getValue();
			if (l.size() == 1) {
				elements.addAll(l);
			} else {
				elements.add(new MultiEvent(l));
			}
		}

		return new PositionsSequence("Buy", true, schedule, elements);
	}

	public static PositionsSequence makeSellSequence(final Schedule schedule) {
		final Map<LocalDate, List<Object>> collectedDates = new TreeMap<>();
		schedule.getOpenSlotAllocations().stream().filter(sa -> sa.getSlot() instanceof DischargeSlot)
				.forEach(sa -> collectedDates.computeIfAbsent(sa.getSlot().getWindowStart(), k -> new LinkedList<>()).add(sa));

		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			if (ca.getCargoType() == CargoType.DES || ca.getCargoType() == CargoType.FOB) {
				for (final SlotAllocation sa : ca.getSlotAllocations()) {
					if (sa.getSlotAllocationType() == SlotAllocationType.SALE) {
						final SlotVisit sv = sa.getSlotVisit();
						collectedDates.computeIfAbsent(sv.getStart().toLocalDate(), k -> new LinkedList<>()).add(sv);
					}
				}
			}
		}
		final List<Object> elements = new LinkedList<>();
		for (final var e : collectedDates.entrySet()) {
			final List<Object> l = e.getValue();
			if (l.size() == 1) {
				elements.addAll(l);
			} else {
				elements.add(new MultiEvent(l));
			}
		}

		return new PositionsSequence("Sell", false, schedule, elements);
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public boolean isBuy() {
		return isBuy;
	}

}
