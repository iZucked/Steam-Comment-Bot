/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;

public class LatenessDiffUtils {

	protected static boolean filter(final Event e) {
		return LatenessUtils.isLateExcludingFlex(e);
	}

	public static String checkSlotAllocationForLateness(SlotAllocation nonReference, SlotAllocation reference) {
		if (nonReference == null || reference == null) {
			return "";
		}
		Slot referenceSlot = reference.getSlot();
		Slot nonReferenceSlot = nonReference.getSlot();
		if (nonReferenceSlot == null || referenceSlot == null) {
			return "";
		}
		{
			if (!nonReferenceSlot.getName().equals(referenceSlot.getName())) {
				return "";
			} else if ((!LatenessDiffUtils.filter(nonReference.getSlotVisit()) && !LatenessDiffUtils.filter(reference.getSlotVisit()))) {
				return "";
			}
		}
		int nonReferenceLateness = LatenessUtils.getLatenessInHours(nonReference.getSlotVisit());
		int referenceLateness = LatenessUtils.getLatenessInHours(reference.getSlotVisit());
		int diff = nonReferenceLateness - referenceLateness;
		if (diff == 0) {
			return "";
		} else {
			String prepend = "";
			String slotType = nonReferenceSlot instanceof LoadSlot ? "Load" : "Discharge";
			if (diff > 0) {
				prepend = slotType + " increased lateness by";
			} else {
				prepend = slotType + " decreased lateness by";
			}
			return String.format("%s %s", prepend, LatenessUtils.formatLatenessHours(diff));
		}
	}
}
