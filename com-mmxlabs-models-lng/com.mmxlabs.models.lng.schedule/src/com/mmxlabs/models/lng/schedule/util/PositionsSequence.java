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
	
	public static final String OTHER_GROUP_DESCRIPTION = "Other";

	private static final SlotAllocationPredicateProvider SLOT_ALLOCATION_PREDICATE_PROVIDER = new SlotAllocationPredicateProvider();
	private static final OpenSlotAllocationPredicateProvider OPEN_SLOT_ALLOCATION_PREDICATE_PROVIDER = new OpenSlotAllocationPredicateProvider();

	private final List<Object> elements;
	private final String providerId;
	private final String description;
	private final Schedule schedule;
	private final boolean isBuy;
	private final boolean isPartition;

	public PositionsSequence(final String providerId, final String description, final boolean isBuy, final Schedule schedule, final List<Object> elements, final boolean isPartition) {
		this.providerId = providerId;
		this.description = description;
		this.isBuy = isBuy;
		this.elements = elements;
		this.schedule = schedule;
		this.isPartition = isPartition;
	}

	@Override
	public @NonNull String toString() {
		final String side = isBuy ? "Buy" : "Sell";
		return (description == null || description.isBlank()) ? side : side + " - " + description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isPartition() {
		return isPartition;
	}

	public List<?> getElements() {
		return elements;
	}

	public Schedule getSchedule() {
		return schedule;
	}
	
	public String getProviderId() {
		return providerId;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public static List<@NonNull PositionsSequence> makeBuySellSequences(final Schedule schedule, String providerId) {
		return List.of(makeSequence(schedule, providerId, true), makeSequence(schedule, providerId, false));
	}
	
	public static List<@NonNull PositionsSequence> makeBuySellSequencesFromPortGroups(final Schedule schedule, String providerID, List<@NonNull PortGroup> portGroups, boolean allowOverlap, boolean addOtherGroup) {
		return makeBuySellSequencesFromPortGroups(schedule, providerID, portGroups, allowOverlap, addOtherGroup, null);
	}

	public static List<@NonNull PositionsSequence> makeBuySellSequencesFromPortGroups(final Schedule schedule, String providerId, List<@NonNull PortGroup> portGroups, boolean allowOverlap, boolean addOtherGroup, String otherGroupDescription) {
		List<Predicate<SlotAllocation>> slotAllocationPredicates = portGroups.stream().map(SLOT_ALLOCATION_PREDICATE_PROVIDER::isInPortGroup).collect(Collectors.toList());
		List<Predicate<OpenSlotAllocation>> openSlotAllocationPredicates = portGroups.stream().map(OPEN_SLOT_ALLOCATION_PREDICATE_PROVIDER::isInPortGroup).collect(Collectors.toList());
		List<String> descriptions = portGroups.stream().map(NamedObject::getName).collect(Collectors.toList());
		return makeSequencesFromExtraPredicates(schedule, providerId, slotAllocationPredicates, openSlotAllocationPredicates, descriptions, allowOverlap, addOtherGroup, otherGroupDescription);
	}
	
	private static List<@NonNull PositionsSequence> makeSequencesFromExtraPredicates(final Schedule schedule, String providerId, List<Predicate<SlotAllocation>> extraSlotAllocationPredicates, List<Predicate<OpenSlotAllocation>> extraOpenSlotAllocationPredicates, List<String> descriptions,
			boolean allowOverlap, boolean addOtherGroup, String otherGroupDescription) {
		assert extraSlotAllocationPredicates.size() == descriptions.size() && extraOpenSlotAllocationPredicates.size() == descriptions.size();
		List<@NonNull PositionsSequence> positionsSequences = new ArrayList<>();

		Set<Object> seen = allowOverlap ? new HashSet<>() : null;
		for (int i = 0; i < descriptions.size(); i++) {
			positionsSequences.add(makeSequence(schedule, providerId, descriptions.get(i), true, extraSlotAllocationPredicates.get(i), extraOpenSlotAllocationPredicates.get(i), seen, true));
			positionsSequences.add(makeSequence(schedule, providerId, descriptions.get(i), false, extraSlotAllocationPredicates.get(i), extraOpenSlotAllocationPredicates.get(i), seen, true));
		}
		
		if (addOtherGroup) {
			Predicate<SlotAllocation> otherSlotAllocationPredicate = sa -> extraSlotAllocationPredicates.stream().noneMatch(p -> p.test(sa));
			Predicate<OpenSlotAllocation> otherOpenSlotAllocationPredicate = sa -> extraOpenSlotAllocationPredicates.stream().noneMatch(p -> p.test(sa));

			positionsSequences.add(makeSequence(schedule, providerId, otherGroupDescription, true, otherSlotAllocationPredicate, otherOpenSlotAllocationPredicate, seen, true));
			positionsSequences.add(makeSequence(schedule, providerId, otherGroupDescription, false, otherSlotAllocationPredicate, otherOpenSlotAllocationPredicate, seen, true));
		}

		return positionsSequences;
	}
	
	private static @NonNull PositionsSequence makeSequence(final Schedule schedule, String providerId, boolean isBuy) {
		return makeSequence(schedule, providerId, "", isBuy, x -> true, x -> true, null, false);
	}

	private static @NonNull PositionsSequence makeSequence(final Schedule schedule, String providerId, String description, boolean isBuy, Predicate<SlotAllocation> extraFilterSlotAllocation, Predicate<OpenSlotAllocation> extraFilterOpenSlotAllocation, Set<Object> seen, boolean isPartition) {
		final Map<LocalDate, List<Object>> collectedDates = new TreeMap<>();

		final var filterSlotAllocation = isBuy ? SLOT_ALLOCATION_PREDICATE_PROVIDER.buy() : SLOT_ALLOCATION_PREDICATE_PROVIDER.sell();
		final var filterOpenSlotAllocation = isBuy ? OPEN_SLOT_ALLOCATION_PREDICATE_PROVIDER.buy() : OPEN_SLOT_ALLOCATION_PREDICATE_PROVIDER.sell();
		collectDatesForOpenSlotAllocations(collectedDates, schedule, filterOpenSlotAllocation.and(extraFilterOpenSlotAllocation), seen);
		collectDatesForCargoAllocations(collectedDates, schedule, filterSlotAllocation.and(extraFilterSlotAllocation), seen);

		final List<Object> elements = createElementList(collectedDates);
		return new PositionsSequence(providerId, description, isBuy, schedule, elements, isPartition);
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
