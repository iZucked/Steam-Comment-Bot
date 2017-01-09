/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.CollectionsUtil.ASet;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class RelatedSlotAllocations {
	private final Map<String, Set<Slot>> slotsAndTheirRelatedSets = new HashMap<>();
	private final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();
	private final Map<Slot, OpenSlotAllocation> openSlotAllocationMap = new HashMap<>();

	public Set<Slot> getRelatedSetFor(final CargoAllocation cargoAllocation, final boolean buys) {
		final List<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();
		for (final SlotAllocation slotAllocation : slotAllocations) {

			return getRelatedSetFor(slotAllocation, buys);
		}
		return ASet.of();
	}

	public Set<Slot> getRelatedSetFor(final SlotAllocation slotAllocation, final boolean buys) {
		final Slot slot = slotAllocation.getSlot();
		return getRelatedSetFor(slot, buys);
	}

	public Set<Slot> getRelatedSetFor(final Slot slot, final boolean buys) {
		final String slotName = getSlotNameKey(slot);
		if (slotsAndTheirRelatedSets.containsKey(slotName)) {
			final Set<Slot> slots = slotsAndTheirRelatedSets.get(slotName);
			final Set<Slot> returnSet = new HashSet<>();
			for (final Slot s : slots) {

				if (buys && s instanceof LoadSlot) {
					returnSet.add(s);
				}
				if (!buys && s instanceof DischargeSlot) {
					returnSet.add(s);
				}
			}
			return returnSet;
		}
		return ASet.of();
	}

	public Set<Slot> getRelatedSetFor(final OpenSlotAllocation openSlotAllocation, final boolean buys) {
		final Slot slot = openSlotAllocation.getSlot();
		final String slotName = getSlotNameKey(slot);
		if (slotsAndTheirRelatedSets.containsKey(slotName)) {
			final Set<Slot> slots = slotsAndTheirRelatedSets.get(slotName);
			final Set<Slot> returnSet = new HashSet<>();
			for (final Slot s : slots) {
				if (buys && s instanceof LoadSlot) {
					returnSet.add(s);
				}
				if (!buys && s instanceof DischargeSlot) {
					returnSet.add(s);
				}
			}
			return returnSet;
		}
		return ASet.of();
	}

	public void updateRelatedSetsFor(final CargoAllocation cargoAllocation) {
		final List<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();

		for (int i = 1; i < slotAllocations.size(); ++i) {

			// get the names of the slots at either end of this cargo leg
			final SlotAllocation slotAllocationA = slotAllocations.get(i - 1);
			final SlotAllocation slotAllocationB = slotAllocations.get(i);

			final Slot slotA = slotAllocationA.getSlot();
			final Slot slotB = slotAllocationB.getSlot();

			slotAllocationMap.put(slotA, slotAllocationA);
			slotAllocationMap.put(slotB, slotAllocationB);

			addRelatedSlot(slotA, slotB);

		}
	}

	public void updateRelatedSetsFor(final OpenSlotAllocation openSlotAllocation) {
		openSlotAllocationMap.put(openSlotAllocation.getSlot(), openSlotAllocation);
	}

	public SlotAllocation getSlotAllocation(final Slot slot) {
		return slotAllocationMap.get(slot);
	}

	public OpenSlotAllocation getOpenSlotAllocation(final Slot slot) {
		return openSlotAllocationMap.get(slot);
	}

	public void clear() {
		slotsAndTheirRelatedSets.clear();
		slotAllocationMap.clear();
		openSlotAllocationMap.clear();
	}

	public void addRelatedSlot(final Slot slotA, final Slot slotB) {
		if (slotA == null || slotB == null) {
			return;
		}

		final String sA = getSlotNameKey(slotA);
		final String sB = getSlotNameKey(slotB);

		// get/create the sets of slots these wired slots are related to
		final Set<Slot> setA = slotsAndTheirRelatedSets.containsKey(sA) ? slotsAndTheirRelatedSets.get(sA) : ASet.of(slotA);
		final Set<Slot> setB = slotsAndTheirRelatedSets.containsKey(sB) ? slotsAndTheirRelatedSets.get(sB) : ASet.of(slotB);

		// Ensure self is in set
		setA.add(slotA);
		setB.add(slotB);

		// merge the two sets
		setA.addAll(setB);
		final Set<Slot> mergedSet = setA;

		// make sure all slots in the mergedSet are related to the new mergedSet
		for (final Slot slot : mergedSet) {
			slotsAndTheirRelatedSets.put(getSlotNameKey(slot), mergedSet);
		}
	}

	private String getSlotNameKey(final Slot slot) {
		// Add prefix as load and discharge ID's could be the same
		final String prefix;
		if (slot instanceof LoadSlot) {
			prefix = "L-";
		} else {
			prefix = "D-";
		}
		if (slot instanceof SpotSlot) {
			// Use hashcode to try and get a scenario unique name for the slot. Note this is not guaranteed!
			return prefix + slot.getName() + slot.hashCode();
		}

		return prefix + slot.getName();
	}
}
