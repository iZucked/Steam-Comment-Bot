package com.mmxlabs.lingo.reports.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

import com.mmxlabs.common.CollectionsUtil.ASet;

public class RelatedSlotAllocations {
	private Map<String, Set<String>> slotsAndTheirRelatedSets = new HashMap<String, Set<String>>(); 

	public Set<String> getRelatedSetFor(CargoAllocation cargoAllocation) {
		List<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();
		for (SlotAllocation slotAllocation : slotAllocations) {
			String slotName = slotAllocation.getName();
			if (slotsAndTheirRelatedSets.containsKey(slotName)) {
				return slotsAndTheirRelatedSets.get(slotName);
			}
		}
		return ASet.of("");
	}
	
	public void updateRelatedSetsFor(CargoAllocation cargoAllocation) {
		List<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();
		
		for (int i = 1; i < slotAllocations.size(); ++i) {

			// get the names of the slots at either end	of this cargo leg
			String sA = slotAllocations.get(i-1).getName();
			String sB = slotAllocations.get(i).getName();
			
			// get/create the sets of slots these wired slots are related to
			Set<String> setA = slotsAndTheirRelatedSets.containsKey(sA) ? 
						slotsAndTheirRelatedSets.get(sA)
						: ASet.of(sA);
						
			Set<String> setB = slotsAndTheirRelatedSets.containsKey(sB) ? 
						slotsAndTheirRelatedSets.get(sB)
						: ASet.of(sB);
			
			// merge the two sets
			setA.addAll(setB);
			Set<String> mergedSet = setA;

			// make sure all slots in the mergedSet are related to the new mergedSet
			for (final String slotName : mergedSet) {
				slotsAndTheirRelatedSets.put(slotName, mergedSet);
			}
			
		}
	}

	public void clear() {
		slotsAndTheirRelatedSets.clear();
	}
}
