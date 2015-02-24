/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		public HashMap<CapacityViolationType, Long> additionSet;
		public HashMap<CapacityViolationType, Long> intersectSet;
		public HashMap<CapacityViolationType, Long> subtractionSet;
		
		public CapacityViolationDifferences () {
			additionSet = new HashMap<CapacityViolationType, Long>();
			intersectSet = new HashMap<CapacityViolationType, Long>();
			subtractionSet = new HashMap<CapacityViolationType, Long>();
		}
	}
	
	public static String checkSlotAllocationForCapacityViolations(SlotAllocation nonReference, SlotAllocation reference) {
		if (nonReference == null || reference == null) {
			return "";
		} else if (!nonReference.getSlot().getName().equals(reference.getSlot().getName())) {
			return "";
		}
		CapacityViolationDifferences differences = CapacityViolationDiffUtils.getDifferenceInViolations(nonReference, reference);
		if (differences.additionSet.isEmpty() && differences.subtractionSet.isEmpty() && differences.intersectSet.isEmpty()) {
			return "";
		}

		String additions = createChangedViolationTextFromSet(differences.additionSet);
		String subtractions = createChangedViolationTextFromSet(differences.subtractionSet);
		String intersection = createModifiedViolationTextFromSet(differences.intersectSet);
		String slotType = nonReference.getSlot() instanceof LoadSlot ? "Load" : "Discharge";
		
		String additionSemiColon = ((!differences.additionSet.isEmpty() && (!differences.subtractionSet.isEmpty() || !differences.intersectSet.isEmpty())) ? " ; " : "");
		String subtractionSemiColon = (!differences.subtractionSet.isEmpty() && !differences.intersectSet.isEmpty()) ? " ; " : "";
		String returnString = String.format("%s ",slotType) + (!differences.additionSet.isEmpty() ? String.format("added violations: %s", additions) : "") + additionSemiColon + (!differences.subtractionSet.isEmpty() ? String.format("removed violations: %s", subtractions) : "") + subtractionSemiColon + (!differences.intersectSet.isEmpty() ? String.format("modified violations: %s", intersection) : ""); 
		
		return returnString;
	}

	private static String createChangedViolationTextFromSet(Map<CapacityViolationType, Long> map) {
		String text = "";
		if (!map.isEmpty()) {
			List<CapacityViolationType> violations = new ArrayList<>(map.keySet());
			for (int index = 0; index < violations.size(); index++) {
				if (violations.get(index) != CapacityViolationType.FORCED_COOLDOWN) {
					text += String.format("%s (%sm??)%s", violations.get(index).getName(), map.get(violations.get(index)),(index < violations.size() - 1 ? " , " : ""));
				} else {
					text += String.format("%s %s", violations.get(index).getName(), (index < violations.size() - 1 ? " , " : ""));
				}
			}
		}
		return text;
	}

	private static String createModifiedViolationTextFromSet(Map<CapacityViolationType, Long> map) {
		String text = "";
		if (!map.isEmpty()) {
			List<CapacityViolationType> violations = new ArrayList<>(map.keySet());
			for (int index = 0; index < violations.size(); index++) {
				if (violations.get(index) != CapacityViolationType.FORCED_COOLDOWN) {
					text += String.format("%s (%s%sm??)%s", violations.get(index).getName(), map.get(violations.get(index)),map.get(violations.get(index)) > 0 ? "+" : "-", (index < violations.size() - 1 ? " , " : ""));
				} else {
					text += String.format("%s %s", violations.get(index).getName(), (index < violations.size() - 1 ? " , " : ""));
				}
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
				if (setB.containsKey(violation) && setA.get(violation) != setB.get(violation)) {
					differences.intersectSet.put(violation, setB.get(violation) - setA.get(violation));
				} else {
					differences.additionSet.put(violation, setA.get(violation));
				}
			}
			for (CapacityViolationType violation : setB.keySet()) {
				if (!setA.containsKey(violation)) {
					differences.subtractionSet.put(violation, setB.get(violation));
				}
			}
		} else {
			if (setA == null) {
				for (CapacityViolationType violation : setB.keySet())
					differences.subtractionSet.put(violation, setB.get(violation));
			} else if (setB == null) {
				for (CapacityViolationType violation : setA.keySet())
					differences.additionSet.put(violation, setA.get(violation));
			}
		}
		return differences;
	}
	
	public static CapacityViolationDifferences getDifferenceInViolations(SlotAllocation slotA, SlotAllocation slotB) {
		return getDifferenceInViolations(getViolationMap(slotA), getViolationMap(slotB));
	}
}
