/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LatenessUtils {
	public static boolean isLate(Event e) {
		if (e instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) e;
			final Sequence seq = visit.getSequence();
			if (seq.getSequenceType() == SequenceType.DES_PURCHASE || seq.getSequenceType() == SequenceType.FOB_SALE) {
				// ignore load slots as we don't care when the load was performed
				if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
					return false;
				}
			}
			if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
				return true;
			}

		} else if (e instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) e;
			if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
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

				final Date startBy = availability.getStartBy();
				if (startBy != null && visit.getStart().after(startBy)) {
					return true;
				}

			} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
				final Date endBy = availability.getEndBy();
				if (endBy != null && visit.getStart().after(endBy)) {
					return true;
				}
			}
			// setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
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

	public static String formatLateness(long diff) {
		// Strip milliseconds
		diff /= 1000;
		// Strip seconds;
		diff /= 60;
		// Strip
		diff /= 60;
		if (diff / 24 == 0) {
			return String.format("%2dh", diff % 24);
		} else {
			return String.format("%2dd, %2dh", diff / 24, diff % 24);
		}
	}
	
	public static long getLatenessInHours(PortVisit portVisit) {
		final PortVisit slotVisit = portVisit;
		final Calendar localStart = slotVisit.getLocalStart();
		final Calendar windowEndDate = LatenessUtils.getWindowEndDate(portVisit);

		long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();

		// Strip milliseconds
		diff /= 1000;
		// Strip seconds;
		diff /= 60;
		// Strip minutes
		diff /= 60;

		return diff;
	}

}
