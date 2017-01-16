/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class CapacityViolationDiffUtils {

	public static class CapacityViolationDifferences {
		public HashMap<CapacityViolationType, Long> additionSet;
		public HashMap<CapacityViolationType, Long> intersectSet;
		public HashMap<CapacityViolationType, Long> subtractionSet;

		public CapacityViolationDifferences() {
			additionSet = new HashMap<CapacityViolationType, Long>();
			intersectSet = new HashMap<CapacityViolationType, Long>();
			subtractionSet = new HashMap<CapacityViolationType, Long>();
		}
	}

	public static String checkSlotAllocationForCapacityViolations(final SlotAllocation nonReference, final SlotAllocation reference) {
		if (nonReference == null || reference == null) {
			return "";
		} else if (!nonReference.getSlot().getName().equals(reference.getSlot().getName())) {
			return "";
		}
		final CapacityViolationDifferences differences = CapacityViolationDiffUtils.getDifferenceInViolations(nonReference, reference);
		if (differences.additionSet.isEmpty() && differences.subtractionSet.isEmpty() && differences.intersectSet.isEmpty()) {
			return "";
		}

		final String additions = createChangedViolationTextFromSet(differences.additionSet);
		final String subtractions = createChangedViolationTextFromSet(differences.subtractionSet);
		final String intersection = createModifiedViolationTextFromSet(differences.intersectSet);
		final String slotType = nonReference.getSlot() instanceof LoadSlot ? "Load" : "Discharge";

		final String additionSemiColon = ((!differences.additionSet.isEmpty() && (!differences.subtractionSet.isEmpty() || !differences.intersectSet.isEmpty())) ? " ; " : "");
		final String subtractionSemiColon = (!differences.subtractionSet.isEmpty() && !differences.intersectSet.isEmpty()) ? " ; " : "";
		final String returnString = (!differences.additionSet.isEmpty() ? String.format("Added %s", additions) : "") + additionSemiColon
				+ (!differences.subtractionSet.isEmpty() ? String.format("Removed %s", subtractions) : "") + subtractionSemiColon
				+ (!differences.intersectSet.isEmpty() ? String.format("Modified %s", intersection) : "") + String.format("on %s ", slotType);

		return returnString;
	}

	private static String createChangedViolationTextFromSet(final Map<CapacityViolationType, Long> map) {
		String text = "";
		if (!map.isEmpty()) {
			final List<CapacityViolationType> violations = new ArrayList<>(map.keySet());
			for (int index = 0; index < violations.size(); index++) {
				if (violations.get(index) != CapacityViolationType.FORCED_COOLDOWN) {
					text += String.format("%s (%sm³)%s", getUserString(violations.get(index)), map.get(violations.get(index)), (index < violations.size() - 1 ? " , " : ""));
				} else {
					text += String.format("%s %s", getUserString(violations.get(index)), (index < violations.size() - 1 ? " , " : ""));
				}
			}
		}
		return text;
	}

	private static String createModifiedViolationTextFromSet(final Map<CapacityViolationType, Long> map) {
		String text = "";
		if (!map.isEmpty()) {
			final List<CapacityViolationType> violations = new ArrayList<>(map.keySet());
			for (int index = 0; index < violations.size(); index++) {
				if (violations.get(index) != CapacityViolationType.FORCED_COOLDOWN) {
					text += String.format("%s (%s%sm³)%s", getUserString(violations.get(index)), map.get(violations.get(index)), map.get(violations.get(index)) > 0 ? "+" : "-",
							(index < violations.size() - 1 ? " , " : ""));
				} else {
					text += String.format("%s %s", getUserString(violations.get(index)), (index < violations.size() - 1 ? " , " : ""));
				}
			}
		}
		return text;
	}

	private static EMap<CapacityViolationType, Long> getViolationMap(final SlotAllocation slotAllocation) {
		final SlotVisit visit = slotAllocation.getSlotVisit();
		if (visit instanceof CapacityViolationsHolder) {
			final CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder) visit;
			final EMap<CapacityViolationType, Long> violationMap = capacityViolationsHolder.getViolations();
			return violationMap;
		} else {
			return null;
		}
	}

	private static CapacityViolationDifferences getDifferenceInViolations(final EMap<CapacityViolationType, Long> setA, final EMap<CapacityViolationType, Long> setB) {
		final CapacityViolationDifferences differences = new CapacityViolationDifferences();
		if (setA != null && setB != null) {
			for (final CapacityViolationType violation : setA.keySet()) {
				if (setB.containsKey(violation)) {
					if ((long) setA.get(violation) != (long) setB.get(violation)) {
						differences.intersectSet.put(violation, setB.get(violation) - setA.get(violation));
					}
				} else {
					differences.additionSet.put(violation, setA.get(violation));
				}
			}
			for (final CapacityViolationType violation : setB.keySet()) {
				if (!setA.containsKey(violation)) {
					differences.subtractionSet.put(violation, setB.get(violation));
				}
			}
		} else {
			if (setA == null) {
				for (final CapacityViolationType violation : setB.keySet())
					differences.subtractionSet.put(violation, setB.get(violation));
			} else if (setB == null) {
				for (final CapacityViolationType violation : setA.keySet())
					differences.additionSet.put(violation, setA.get(violation));
			}
		}
		return differences;
	}

	public static CapacityViolationDifferences getDifferenceInViolations(final SlotAllocation slotA, final SlotAllocation slotB) {
		return getDifferenceInViolations(getViolationMap(slotA), getViolationMap(slotB));
	}

	public static String getUserString(final CapacityViolationType capacityViolationType) {
		switch (capacityViolationType) {
		case FORCED_COOLDOWN:
			return "Forced Cooldown";
		case LOST_HEEL:
			return "Lost Heel";
		case MAX_DISCHARGE:
			return "Max Discharge";
		case MAX_HEEL:
			return "Max Heel";
		case MAX_LOAD:
			return "Max Load";
		case MIN_DISCHARGE:
			return "Min Discharge";
		case MIN_LOAD:
			return "Min Load";
		case VESSEL_CAPACITY:
			return "Vessel Capacity";
		default:
			break;
		}
		return capacityViolationType.toString();

	}
}
