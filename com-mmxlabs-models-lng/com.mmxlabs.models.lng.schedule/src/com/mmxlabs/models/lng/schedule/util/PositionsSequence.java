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
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.NamedObject;

public class PositionsSequence {

	private static final SlotAllocationPredicateProvider SLOT_ALLOCATION_PREDICATE_PROVIDER = new SlotAllocationPredicateProvider();
	private static final OpenSlotAllocationPredicateProvider OPEN_SLOT_ALLOCATION_PREDICATE_PROVIDER = new OpenSlotAllocationPredicateProvider();

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

	public static List<PositionsSequence> makeBuySellSequences(final Schedule schedule) {
		return List.of(makeSequence(schedule, true), makeSequence(schedule, false));
	}
	
	public static List<PositionsSequence> makeBuySellSequencesFromPortGroups(final Schedule schedule, List<PortGroup> portGroups, boolean allowOverlap, boolean addOtherGroup) {
		List<Predicate<SlotAllocation>> slotAllocationPredicates = portGroups.stream().map(SLOT_ALLOCATION_PREDICATE_PROVIDER::isInPortGroup).collect(Collectors.toList());
		List<Predicate<OpenSlotAllocation>> openSlotAllocationPredicates = portGroups.stream().map(OPEN_SLOT_ALLOCATION_PREDICATE_PROVIDER::isInPortGroup).collect(Collectors.toList());
		List<String> descriptions = portGroups.stream().map(NamedObject::getName).collect(Collectors.toList());
		if (addOtherGroup) {
			slotAllocationPredicates.add(sa -> slotAllocationPredicates.stream().noneMatch(p -> p.test(sa)));
			openSlotAllocationPredicates.add(sa -> openSlotAllocationPredicates.stream().noneMatch(p -> p.test(sa)));
			descriptions.add("Other");
		}
		return makeSequencesFromExtraPredicates(schedule, slotAllocationPredicates, openSlotAllocationPredicates, descriptions, allowOverlap);
	}
	
	public Schedule getSchedule() {
		return schedule;
	}

	public boolean isBuy() {
		return isBuy;
	}

	private static List<PositionsSequence> makeSequencesFromExtraPredicates(final Schedule schedule, List<Predicate<SlotAllocation>> extraSlotAllocationPredicates, List<Predicate<OpenSlotAllocation>> extraOpenSlotAllocationPredicates, List<String> descriptions,
			boolean allowOverlap) {
		assert extraSlotAllocationPredicates.size() == descriptions.size() && extraOpenSlotAllocationPredicates.size() == descriptions.size();
		List<PositionsSequence> positionsSequences = new ArrayList<>();

		Set<Object> seen = allowOverlap ? new HashSet<>() : null;
		for (int i = 0; i < descriptions.size(); i++) {
			positionsSequences.add(makeSequence(schedule, " - " + descriptions.get(i), true, extraSlotAllocationPredicates.get(i), extraOpenSlotAllocationPredicates.get(i), seen));
			positionsSequences.add(makeSequence(schedule, " - " + descriptions.get(i), false, extraSlotAllocationPredicates.get(i), extraOpenSlotAllocationPredicates.get(i), seen));
		}

		return positionsSequences;
	}

	private static PositionsSequence makeSequence(final Schedule schedule, boolean isBuy) {
		return makeSequence(schedule, "", isBuy, x -> true, x -> true, null);
	}

	private static PositionsSequence makeSequence(final Schedule schedule, String description, boolean isBuy, Predicate<SlotAllocation> extraFilterSlotAllocation, Predicate<OpenSlotAllocation> extraFilterOpenSlotAllocation, Set<Object> seen) {
		final Map<LocalDate, List<Object>> collectedDates = new TreeMap<>();

		final var filterSlotAllocation = isBuy ? SLOT_ALLOCATION_PREDICATE_PROVIDER.buy() : SLOT_ALLOCATION_PREDICATE_PROVIDER.sell();
		final var filterOpenSlotAllocation = isBuy ? OPEN_SLOT_ALLOCATION_PREDICATE_PROVIDER.buy() : OPEN_SLOT_ALLOCATION_PREDICATE_PROVIDER.sell();
		collectDatesForOpenSlotAllocations(collectedDates, schedule, filterOpenSlotAllocation.and(extraFilterOpenSlotAllocation), seen);
		collectDatesForCargoAllocations(collectedDates, schedule, filterSlotAllocation.and(extraFilterSlotAllocation), seen);

		final List<Object> elements = createElementList(collectedDates);
		return new PositionsSequence((isBuy ? "Buy" : "Sell") + description, isBuy, schedule, elements);
	}

	private static void collectDatesForOpenSlotAllocations(final Map<LocalDate, List<Object>> collectedDates, final Schedule schedule, Predicate<OpenSlotAllocation> extraFilter, Set<Object> seen) {
		boolean allowOverlap = seen == null;
		for (final OpenSlotAllocation sa : schedule.getOpenSlotAllocations()) {
			if (extraFilter.test(sa) && (allowOverlap || !seen.contains(sa))) {
				collectedDates.computeIfAbsent(sa.getSlot().getWindowStart(), k -> new LinkedList<>()).add(sa);
				if (!allowOverlap)
					seen.add(sa);
			}
		}
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
	
	private static interface PositionSequencePredicateProvider<T> {
		Predicate<T> buy();
		Predicate<T> sell();
		Predicate<T> isInPortGroup(PortGroup portGroup);
	}
	
	private static class SlotAllocationPredicateProvider implements PositionSequencePredicateProvider<SlotAllocation> {

		@Override
		public Predicate<SlotAllocation> buy() {
			return sa -> sa.getSlotAllocationType() == SlotAllocationType.PURCHASE;
		}

		@Override
		public Predicate<SlotAllocation> sell() {
			return sa -> sa.getSlotAllocationType() == SlotAllocationType.SALE;
		}

		@Override
		public Predicate<SlotAllocation> isInPortGroup(PortGroup portGroup) {
			Set<?> ports = SetUtils.getObjects(portGroup);
			return sa -> ports.contains(sa.getSlotVisit().getPort());
		}
	}
	
	private static class OpenSlotAllocationPredicateProvider implements PositionSequencePredicateProvider<OpenSlotAllocation> {

		@Override
		public Predicate<OpenSlotAllocation> buy() {
			return LoadSlot.class::isInstance;
		}

		@Override
		public Predicate<OpenSlotAllocation> sell() {
			return DischargeSlot.class::isInstance;
		}

		@Override
		public Predicate<OpenSlotAllocation> isInPortGroup(PortGroup portGroup) {
			Set<?> ports = SetUtils.getObjects(portGroup);
			return sa -> ports.contains(sa.getSlot().getPort());
		}
	}

}
