/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LatenessUtils {
	public static boolean isLate(final Event e) {
		if (e instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) e;
			final Sequence seq = visit.getSequence();
			if (seq.getSequenceType() == SequenceType.DES_PURCHASE || seq.getSequenceType() == SequenceType.FOB_SALE) {
				// ignore load slots as we don't care when the load was performed
				if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
					return false;
				}
			}
			if (visit.getStart().isAfter(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
				return true;
			}

		} else if (e instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) e;
			if (vev.getStart().isAfter(vev.getVesselEvent().getStartByAsDateTime())) {
				return true;
			}
		} else if (e instanceof PortVisit) {
			final PortVisit visit = (PortVisit) e;
			final Sequence seq = visit.getSequence();

			final VesselAvailability availability = seq.getVesselAvailability();
			if (availability == null) {
				return false;
			}
			if (seq.getEvents().indexOf(visit) == 0) {

				final DateTime startBy = availability.getStartByAsDateTime();
				if (startBy != null && visit.getStart().isAfter(startBy)) {
					return true;
				}

			} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
				final DateTime endBy = availability.getEndByAsDateTime();
				if (endBy != null && visit.getStart().isAfter(endBy)) {
					return true;
				}
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

	public static String formatLateness(int diffHours) {

		if (diffHours / 24 == 0) {
			return String.format("%2dh", diffHours % 24);
		} else {
			return String.format("%2dd, %2dh", diffHours / 24, diffHours % 24);
		}

	}

	public static int getLatenessInHours(final PortVisit visit) {
		final DateTime localStart = visit.getStart();
		final DateTime windowEndDate = getWindowEndDate(visit);

		final int diff = Hours.hoursBetween(windowEndDate, localStart).getHours();
		return diff < 0 ? 0 : diff;
	}

}
