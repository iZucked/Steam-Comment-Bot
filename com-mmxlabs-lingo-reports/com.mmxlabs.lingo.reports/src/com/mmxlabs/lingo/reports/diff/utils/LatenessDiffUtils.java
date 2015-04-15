/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import java.util.Calendar;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;

public class LatenessDiffUtils {


	public static long getLateness(SlotVisit visit) {
		final Calendar localStart = visit.getLocalStart();
		final Calendar windowEndDate = LatenessUtils.getWindowEndDate(visit);

		long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();
		return diff < 0 ? 0 : diff;
	}

	protected static boolean filter(final Event e) {
		return LatenessUtils.isLate(e);
	}

	public static String checkSlotAllocationForLateness(SlotAllocation nonReference, SlotAllocation reference) {
		if (nonReference == null || reference == null) {
			return "";
		} else if (!nonReference.getSlot().getName().equals(reference.getSlot().getName())) {
			return "";
		} else if ((!LatenessDiffUtils.filter(nonReference.getSlotVisit()) && !LatenessDiffUtils.filter(reference.getSlotVisit()))) {
			return "";
		}
		long nonReferenceLateness = LatenessDiffUtils.getLateness(nonReference.getSlotVisit());
		long referenceLateness = LatenessDiffUtils.getLateness(reference.getSlotVisit());
		long diff = nonReferenceLateness - referenceLateness;
		if (diff == 0) {
			return "";
		} else {
			String prepend = "";
			String slotType = nonReference.getSlot() instanceof LoadSlot ? "Load" : "Discharge";
			if (diff > 0) {
				prepend = slotType + " increased lateness by";
			} else {
				prepend = slotType + " decreased lateness by";
			}
			return String.format("%s %s", prepend, LatenessUtils.formatLateness(diff));
		}
	}
}
