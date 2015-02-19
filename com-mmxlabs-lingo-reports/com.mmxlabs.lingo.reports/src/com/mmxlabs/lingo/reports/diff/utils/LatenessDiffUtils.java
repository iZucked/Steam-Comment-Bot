package com.mmxlabs.lingo.reports.diff.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LatenessDiffUtils {

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

	public static long getLateness(SlotVisit visit) {
		final Calendar localStart = visit.getLocalStart();
		final Calendar windowEndDate = getWindowEndDate(visit);

		long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();
		return diff < 0 ? 0 : diff;
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


	protected static boolean filter(final Event e) {
		if (e instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) e;
			// Exclude DES Purchase and fob sales
			final Sequence seq = visit.getSequence();
			if (seq.getSequenceType() == SequenceType.DES_PURCHASE || seq.getSequenceType() == SequenceType.FOB_SALE) {
				return false;
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
		}
		return false;
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
			return String.format("%s %s", prepend, LatenessDiffUtils.formatLateness(diff));
		}
	}
}
