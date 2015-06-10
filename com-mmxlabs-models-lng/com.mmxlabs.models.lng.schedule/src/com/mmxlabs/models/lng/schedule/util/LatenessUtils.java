/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.mmxlabs.models.lng.cargo.CargoPackage;
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

	public static Calendar getWindowStartDate(final Object object) {
		if (object instanceof SlotVisit) {
			final Date date = ((SlotVisit) object).getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime();
			String timeZone = ((SlotVisit) object).getSlotAllocation().getSlot().getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof VesselEventVisit) {
			final Date date = ((VesselEventVisit) object).getVesselEvent().getStartAfter();
			String timeZone = ((VesselEventVisit) object).getVesselEvent().getTimeZone(CargoPackage.eINSTANCE.getVesselEvent_StartBy());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		}
		return null;
	}

	public static Calendar getWindowEndDate(final Object object) {
		final Date date;
		if (object instanceof SlotVisit) {
			date = ((SlotVisit) object).getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime();
			String timeZone = ((SlotVisit) object).getSlotAllocation().getSlot().getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof VesselEventVisit) {
			date = ((VesselEventVisit) object).getVesselEvent().getStartBy();
			String timeZone = ((VesselEventVisit) object).getVesselEvent().getTimeZone(CargoPackage.eINSTANCE.getVesselEvent_StartBy());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof PortVisit) {
			final PortVisit visit = (PortVisit) object;
			final Sequence seq = visit.getSequence();
			final VesselAvailability vesselAvailability = seq.getVesselAvailability();
			if (vesselAvailability == null) {
				return null;
			}
			if (seq.getEvents().indexOf(visit) == 0) {
				final Date startBy = vesselAvailability.getStartBy();
				if (startBy != null) {
					final Calendar c = Calendar.getInstance();
					c.setTime(startBy);
					return c;
				}
			} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
				final Date endBy = vesselAvailability.getEndBy();
				if (endBy != null) {
					final Calendar c = Calendar.getInstance();
					c.setTime(endBy);
					return c;
				}
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

	public static int getLatenessInHours(final PortVisit portVisit) {
		final PortVisit slotVisit = portVisit;
		final Calendar localStart = slotVisit.getLocalStart();
		final Calendar windowEndDate = LatenessUtils.getWindowEndDate(portVisit);

		long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();

		// Strip milliseconds
		diff /= 1000L;
		// Strip seconds;
		diff /= 60L;
		// Strip minutes
		diff /= 60L;

		assert diff == (int) diff;

		return (int) diff;
	}

}
