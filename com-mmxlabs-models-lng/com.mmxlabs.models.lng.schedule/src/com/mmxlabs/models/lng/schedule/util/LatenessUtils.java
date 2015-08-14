/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LatenessUtils {

	public static boolean isLate(final Event e) {
		if (e instanceof PortVisit) {
			final PortVisitLateness portVisitLateness = ((PortVisit) e).getLateness();
			if (portVisitLateness != null) {
				return true;
			}
		}
		return false;
	}

	public static DateTime getWindowStartDate(final Object object) {
		if (object instanceof SlotVisit) {
			return ((SlotVisit) object).getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime();
		} else if (object instanceof VesselEventVisit) {
			return ((VesselEventVisit) object).getVesselEvent().getStartAfterAsDateTime();
		}
		return null;
	}

	public static DateTime getWindowEndDate(final Object object) {
		if (object instanceof SlotVisit) {
			return ((SlotVisit) object).getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime();
		} else if (object instanceof VesselEventVisit) {
			return ((VesselEventVisit) object).getVesselEvent().getStartByAsDateTime();
		} else if (object instanceof PortVisit) {
			final PortVisit visit = (PortVisit) object;
			final Sequence seq = visit.getSequence();
			final VesselAvailability vesselAvailability = seq.getVesselAvailability();
			if (vesselAvailability == null) {
				return null;
			}
			if (seq.getEvents().indexOf(visit) == 0) {
				return vesselAvailability.getStartByAsDateTime();
			} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
				return vesselAvailability.getEndByAsDateTime();
			}
		}
		return null;
	}

	public static String formatLatenessHours(final int hours) {
		if (hours / 24 == 0) {
			return String.format("%2dh", hours % 24);
		} else {
			return String.format("%2dd, %2dh", hours / 24, hours % 24);
		}

	}

	public static int getLatenessInHours(final PortVisit visit) {
		final DateTime localStart = visit.getStart();
		final DateTime windowEndDate = getWindowEndDate(visit);
		if (windowEndDate == null || localStart == null) {
			return 0;
		}
		final int diff = Hours.hoursBetween(windowEndDate, localStart).getHours();
		return diff < 0 ? 0 : diff;
	}

}
