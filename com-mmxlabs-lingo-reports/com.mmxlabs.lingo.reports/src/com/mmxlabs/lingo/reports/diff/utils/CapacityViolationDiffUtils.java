package com.mmxlabs.lingo.reports.diff.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class CapacityViolationDiffUtils {

	public static class CapacityViolationDifferences {
		public HashSet<CapacityViolationType> additionSet = new HashSet<CapacityViolationType>();
		public HashSet<CapacityViolationType> intersectSet = new HashSet<CapacityViolationType>();
		public HashSet<CapacityViolationType> subtractionSet = new HashSet<CapacityViolationType>();
		
		public CapacityViolationDifferences () {
			additionSet = new HashSet<CapacityViolationType>();
			intersectSet = new HashSet<CapacityViolationType>();
			subtractionSet = new HashSet<CapacityViolationType>();
		}
	}
	
	public static String checkSlotAllocationForCapacityViolations(SlotAllocation nonReference, SlotAllocation reference) {
		if (nonReference == null || reference == null) {
			return "";
		} else if (!nonReference.getSlot().getName().equals(reference.getSlot().getName())) {
			return "";
		}
		CapacityViolationDifferences differences = CapacityViolationDiffUtils.getDifferenceInViolations(nonReference, reference);
		if (differences.additionSet.isEmpty() && differences.subtractionSet.isEmpty()) {
			return "";
		}

		String additions = createViolationTextFromSet(differences.additionSet);
		String subtractions = createViolationTextFromSet(differences.subtractionSet);
		String slotType = nonReference.getSlot() instanceof LoadSlot ? "Load" : "Discharge";
		
		if (!differences.subtractionSet.isEmpty() && differences.additionSet.isEmpty()) {
			return String.format("%s removed violations: %s", slotType, subtractions);
		} else if (!differences.additionSet.isEmpty() && differences.subtractionSet.isEmpty()) {
			return String.format("%s added violations: %s", slotType, additions);
		} else {
			return String.format("%s removed violations: %s; added violations: %s", slotType, subtractions, additions);
		}
	}

	private static String createViolationTextFromSet(Set<CapacityViolationType> set) {
		String text = "";
		if (!set.isEmpty()) {
			List<CapacityViolationType> violations = new ArrayList<>(set);
			for (int index = 0; index < violations.size(); index++) {
				text += (violations.get(index).getName() + (index < violations.size() - 1 ? " , " : ""));
			}
		}
		return text;
	}

	private static EMap<CapacityViolationType, Long> getViolationMap(SlotAllocation slotAllocation) {
		SlotVisit visit = slotAllocation.getSlotVisit();
		if (visit instanceof CapacityViolationsHolder) {
			final CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder) visit;
			final EMap<CapacityViolationType, Long> violationMap = capacityViolationsHolder.getViolations();
			return violationMap;
		} else {
			return null;
		}
	}

	private static CapacityViolationDifferences getDifferenceInViolations(EMap<CapacityViolationType, Long> setA, EMap<CapacityViolationType, Long> setB) {
		CapacityViolationDifferences differences = new CapacityViolationDifferences();
		if (setA != null && setB != null) {
			for (CapacityViolationType violation : setA.keySet()) {
				if (setB.containsKey(violation)) {
					differences.intersectSet.add(violation);
				} else {
					differences.additionSet.add(violation);
				}
			}
			for (CapacityViolationType violation : setB.keySet()) {
				if (!setA.containsKey(violation)) {
					differences.subtractionSet.add(violation);
				}
			}
		} else {
			if (setA == null) {
				differences.subtractionSet.addAll(setB.keySet());
			} else if (setB == null) {
				differences.additionSet.addAll(setA.keySet());
			}
		}
		return differences;
	}
	
	public static CapacityViolationDifferences getDifferenceInViolations(SlotAllocation slotA, SlotAllocation slotB) {
		return getDifferenceInViolations(getViolationMap(slotA), getViolationMap(slotB));
	}
}
