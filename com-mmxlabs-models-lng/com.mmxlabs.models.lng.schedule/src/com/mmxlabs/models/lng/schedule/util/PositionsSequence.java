/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.NamedObject;

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
		return makeSequence(schedule, true);
	}

	public static PositionsSequence makeSellSequence(final Schedule schedule) {
		return makeSequence(schedule, false);
	}
	
	public static List<PositionsSequence> makeSequencesFromPortGroups(final Schedule schedule, List<PortGroup> portGroups, boolean allowOverlap, boolean addOtherGroup) {
		List<@NonNull Predicate<SlotAllocation>> predicates = portGroups.stream().map(PositionsSequencePredicateUtils::isInPortGroup).collect(Collectors.toList());
		List<String> descriptions = portGroups.stream().map(NamedObject::getName).collect(Collectors.toList());
		if (addOtherGroup) {
			predicates.add(PositionsSequencePredicateUtils.isNotInPortGroups(portGroups));
			descriptions.add("Other");
		}
		return makeSequencesFromExtraPredicates(schedule, predicates, descriptions, allowOverlap);
	}
	
	public Schedule getSchedule() {
		return schedule;
	}

	public boolean isBuy() {
		return isBuy;
	}

	private static List<PositionsSequence> makeSequencesFromExtraPredicates(final Schedule schedule, List<@NonNull Predicate<SlotAllocation>> extraPredicates, List<String> descriptions,
			boolean allowOverlap) {
		assert extraPredicates.size() == descriptions.size();
		List<PositionsSequence> positionsSequences = new ArrayList<>();

		Set<Object> seen = allowOverlap ? new HashSet<>() : null;
		for (int i = 0; i < extraPredicates.size(); i++) {
			positionsSequences.add(makeSequence(schedule, " - " + descriptions.get(i), true, extraPredicates.get(i), seen));
			positionsSequences.add(makeSequence(schedule, " - " + descriptions.get(i), false, extraPredicates.get(i), seen));
		}

		return positionsSequences;
	}

	private static PositionsSequence makeSequence(final Schedule schedule, boolean isBuy) {
		return makeSequence(schedule, "", isBuy, x -> true, null);
	}

	private static PositionsSequence makeSequence(final Schedule schedule, String description, boolean isBuy, @NonNull Predicate<SlotAllocation> extraFilter, Set<Object> seen) {
		final Map<LocalDate, List<Object>> collectedDates = new TreeMap<>();
		collectDatesForOpenSlotAllocations(collectedDates, schedule, isBuy, seen);

		Predicate<SlotAllocation> filter = (isBuy ? PositionsSequencePredicateUtils.buy() : PositionsSequencePredicateUtils.sell());
		collectDatesForCargoAllocations(collectedDates, schedule, filter.and(extraFilter), seen);

		final List<Object> elements = createElementList(collectedDates);
		return new PositionsSequence((isBuy ? "Buy" : "Sell") + description, isBuy, schedule, elements);
	}

	private static void collectDatesForOpenSlotAllocations(final Map<LocalDate, List<Object>> collectedDates, final Schedule schedule, boolean isBuy, Set<Object> seen) {
		boolean allowOverlap = seen == null;
		Class<?> slotClass = isBuy ? LoadSlot.class : DischargeSlot.class;
		schedule.getOpenSlotAllocations().stream().filter(sa -> slotClass.isInstance(sa.getSlot())).forEach(sa -> {
			if (allowOverlap || !seen.contains(sa)) {
				collectedDates.computeIfAbsent(sa.getSlot().getWindowStart(), k -> new LinkedList<>()).add(sa);
				if (!allowOverlap)
					seen.add(sa);
			}
		});
	}

	private static void collectDatesForCargoAllocations(final Map<LocalDate, List<Object>> collectedDates, final Schedule schedule, Predicate<SlotAllocation> filterFunc, Set<Object> seen) {
		boolean allowOverlap = seen == null;
		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			if (ca.getCargoType() == CargoType.DES || ca.getCargoType() == CargoType.FOB) {
				for (final SlotAllocation sa : ca.getSlotAllocations()) {
					// Skip spot market slots
					if (sa.getSpotMarket() != null) {
						continue;
					}
					if (filterFunc.test(sa) && (allowOverlap || !seen.contains(sa))) {
						final SlotVisit sv = sa.getSlotVisit();
						collectedDates.computeIfAbsent(sv.getStart().toLocalDate(), k -> new LinkedList<>()).add(sv);
						if (!allowOverlap)
							seen.add(sa);
					}
				}
			}
		}
	}

	private static List<Object> createElementList(final Map<LocalDate, List<Object>> collectedDates) {
		final List<Object> elements = new LinkedList<>();
		for (final var e : collectedDates.entrySet()) {
			final List<Object> l = e.getValue();
			if (l.size() == 1) {
				elements.addAll(l);
			} else {
				elements.add(new MultiEvent(l));
			}
		}
		return elements;
	}

}
