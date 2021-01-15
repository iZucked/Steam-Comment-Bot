/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class CapacityViolationDiffUtils {

	private CapacityViolationDiffUtils() {

	}

	public static class CapacityViolationDifferences {
		public final @NonNull Map<CapacityViolationType, Long> additionSet = new EnumMap<>(CapacityViolationType.class);
		public final @NonNull Map<CapacityViolationType, Long> intersectSet = new EnumMap<>(CapacityViolationType.class);
		public final @NonNull Map<CapacityViolationType, Long> subtractionSet = new EnumMap<>(CapacityViolationType.class);
	}

	public static String checkSlotAllocationForCapacityViolations(final SlotAllocation nonReference, final SlotAllocation reference) {
		if (nonReference == null || reference == null) {
			return "";
		}

		Slot<?> nonReferenceSlot = nonReference.getSlot();
		Slot<?> referenceSlot = reference.getSlot();
		if (nonReferenceSlot == null || referenceSlot == null) {
			return "";
		}
		if (!nonReferenceSlot.getName().equals(referenceSlot.getName())) {
			return "";
		}
		final CapacityViolationDifferences differences = CapacityViolationDiffUtils.getDifferenceInViolations(nonReference, reference);
		if (differences.additionSet.isEmpty() && differences.subtractionSet.isEmpty() && differences.intersectSet.isEmpty()) {
			return "";
		}

		final String additions = createChangedViolationTextFromSet(differences.additionSet);
		final String subtractions = createChangedViolationTextFromSet(differences.subtractionSet);
		final String intersection = createModifiedViolationTextFromSet(differences.intersectSet);
		final String slotType = nonReferenceSlot instanceof LoadSlot ? "Load" : "Discharge";

		final String additionSemiColon = ((!differences.additionSet.isEmpty() && (!differences.subtractionSet.isEmpty() || !differences.intersectSet.isEmpty())) ? " ; " : "");
		final String subtractionSemiColon = (!differences.subtractionSet.isEmpty() && !differences.intersectSet.isEmpty()) ? " ; " : "";
		final String returnString = (!differences.additionSet.isEmpty() ? String.format("Added %s", additions) : "") + additionSemiColon
				+ (!differences.subtractionSet.isEmpty() ? String.format("Removed %s", subtractions) : "") + subtractionSemiColon
				+ (!differences.intersectSet.isEmpty() ? String.format("Modified %s", intersection) : "") + String.format("on %s ", slotType);

		return returnString;
	}

	private static String createChangedViolationTextFromSet(final Map<CapacityViolationType, Long> map) {
		StringBuilder text = new StringBuilder();
		if (!map.isEmpty()) {
			final List<CapacityViolationType> violations = new ArrayList<>(map.keySet());
			for (int index = 0; index < violations.size(); index++) {
				if (violations.get(index) != CapacityViolationType.FORCED_COOLDOWN) {
					text.append(String.format("%s (%sm³)%s", getUserString(violations.get(index)), map.get(violations.get(index)), (index < violations.size() - 1 ? " , " : "")));
				} else {
					text.append(String.format("%s %s", getUserString(violations.get(index)), (index < violations.size() - 1 ? " , " : "")));
				}
			}
		}
		return text.toString();
	}

	private static String createModifiedViolationTextFromSet(final @NonNull Map<CapacityViolationType, Long> map) {
		StringBuilder text = new StringBuilder();
		if (!map.isEmpty()) {
			final List<CapacityViolationType> violations = new ArrayList<>(map.keySet());
			for (int index = 0; index < violations.size(); index++) {
				if (violations.get(index) != CapacityViolationType.FORCED_COOLDOWN) {
					text.append(String.format("%s (%s%sm³)%s", getUserString(violations.get(index)), map.get(violations.get(index)), map.get(violations.get(index)) > 0 ? "+" : "-",
							(index < violations.size() - 1 ? " , " : "")));
				} else {
					text.append(String.format("%s %s", getUserString(violations.get(index)), (index < violations.size() - 1 ? " , " : "")));
				}
			}
		}
		return text.toString();
	}

	private static @Nullable EMap<CapacityViolationType, Long> getViolationMap(final SlotAllocation slotAllocation) {
		final SlotVisit visit = slotAllocation.getSlotVisit();
		if (visit != null) {
			return visit.getViolations();
		} else {
			return null;
		}
	}

	private static CapacityViolationDifferences getDifferenceInViolations(final @Nullable EMap<CapacityViolationType, Long> setA, final @Nullable EMap<CapacityViolationType, Long> setB) {
		final CapacityViolationDifferences differences = new CapacityViolationDifferences();
		if (setA != null && setB != null) {

			for (final Map.Entry<CapacityViolationType, Long> e : setA.entrySet()) {
				CapacityViolationType violation = e.getKey();
				if (setB.containsKey(violation)) {
					if ((long) e.getValue() != (long) setB.get(violation)) {
						differences.intersectSet.put(violation, setB.get(violation) - e.getValue());
					}
				} else {
					differences.additionSet.put(violation, e.getValue());
				}
			}

			for (final Map.Entry<CapacityViolationType, Long> e : setB.entrySet()) {
				CapacityViolationType violation = e.getKey();
				if (!setA.containsKey(violation)) {
					differences.subtractionSet.put(violation, e.getValue());
				}
			}
		} else {
			if (setB != null) {
				for (final Map.Entry<CapacityViolationType, Long> e : setB.entrySet()) {
					CapacityViolationType violation = e.getKey();
					differences.subtractionSet.put(violation, e.getValue());
				}
			} else if (setA != null) {
				for (final Map.Entry<CapacityViolationType, Long> e : setA.entrySet()) {
					CapacityViolationType violation = e.getKey();
					differences.additionSet.put(violation, e.getValue());
				}
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
