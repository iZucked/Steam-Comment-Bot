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
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class RelatedSlotAllocations {
	private final Map<String, Set<Slot>> slotsAndTheirRelatedSets = new HashMap<>();

	public Set<Slot> getRelatedSetFor(final CargoAllocation cargoAllocation, final boolean buys) {
		final List<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();
		for (final SlotAllocation slotAllocation : slotAllocations) {
			boolean useSlot = false;
			if (buys && slotAllocation.getSlot() instanceof LoadSlot) {
				useSlot = true;
			}
			if (!buys && slotAllocation.getSlot() instanceof DischargeSlot) {
				useSlot = true;
			}
			if (useSlot) {
				final String slotName = slotAllocation.getName();
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
			}
		}
		return ASet.of();
	}

	public void updateRelatedSetsFor(final CargoAllocation cargoAllocation) {
		final List<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();

		for (int i = 1; i < slotAllocations.size(); ++i) {

			// get the names of the slots at either end of this cargo leg
			final SlotAllocation slotA = slotAllocations.get(i - 1);
			final String sA = slotA.getName();
			final SlotAllocation slotB = slotAllocations.get(i);
			final String sB = slotB.getName();

			// get/create the sets of slots these wired slots are related to
			final Set<Slot> setA = slotsAndTheirRelatedSets.containsKey(sA) ? slotsAndTheirRelatedSets.get(sA) : ASet.of(slotA.getSlot());

			final Set<Slot> setB = slotsAndTheirRelatedSets.containsKey(sB) ? slotsAndTheirRelatedSets.get(sB) : ASet.of(slotB.getSlot());

			// merge the two sets
			setA.addAll(setB);
			final Set<Slot> mergedSet = setA;

			// make sure all slots in the mergedSet are related to the new mergedSet
			for (final Slot slot : mergedSet) {
				slotsAndTheirRelatedSets.put(slot.getName(), mergedSet);
			}

		}
	}

	public void clear() {
		slotsAndTheirRelatedSets.clear();
	}
}
