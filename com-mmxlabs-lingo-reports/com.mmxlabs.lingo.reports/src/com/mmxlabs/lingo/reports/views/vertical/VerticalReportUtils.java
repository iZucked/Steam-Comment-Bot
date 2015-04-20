/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public final class VerticalReportUtils {

	public static List<Event> eventsFromSequences(final Sequence... sequences) {
		final List<Event> result = new ArrayList<Event>();
		if (sequences != null) {
			for (final Sequence seq : sequences) {
				if (seq != null) {
					result.addAll(seq.getEvents());
				}
			}
		}
		return result;
	}

	public static boolean isEventLate(final Event event) {

		if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			final Slot slot = slotVisit.getSlotAllocation().getSlot();

			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isDESPurchase()) {
					return false;
				}
			}

			if (slotVisit.getStart().isAfter(slot.getWindowEndWithSlotOrPortTime())) {
				return true;
			}
		} else if (event instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
			final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			if (vesselEventVisit.getStart().isAfter(vesselEvent.getStartByAsDateTime())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a list of all 00h00 UTC Date objects which fall within the specified range
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<LocalDate> getUTCDaysBetween(final LocalDate start, final LocalDate end) {
		final ArrayList<LocalDate> result = new ArrayList<>();
		if (start != null && end != null) {
			LocalDate current = start;
			while (!current.isAfter(end)) {
				result.add(current);
				current = current.plusDays(1);
			}

		}

		return result;
	}
//
//	public static LocalDateTime getLocalDateFor(final Date date, final TimeZone timeZone, final boolean asUTCEquivalent) {
//		final Calendar cal = Calendar.getInstance(timeZone);
//		cal.setTime(date);
//		return getLocalDateFor(cal, asUTCEquivalent);
//	}
//
//	public static LocalDateTime getLocalDateFor(final DateTime cal, final boolean asUTCEquivalent) {
//		if (asUTCEquivalent) {
//			return cal.toLocalDateTime();
//		} else {
//			return cal.withZone(DateTimeZone.UTC).toLocalDateTime();
//		}
//	}
//
//	public static <T extends ITimezoneProvider & EObject> LocalDateTime getLocalDateFor(final T object, final EAttribute feature, final boolean asUTCEquivalent) {
//		final Date date = (Date) object.eGet(feature);
//		return getLocalDateFor(date, TimeZone.getTimeZone(object.getTimeZone(feature)), asUTCEquivalent);
//
//	}
//
//	public static <T extends ITimezoneProvider & EObject> LocalDateTime getLocalDateFor(final T object, final Date date, final EAttribute feature, final boolean asUTCEquivalent) {
//		return getLocalDateFor(date, TimeZone.getTimeZone(object.getTimeZone(feature)), asUTCEquivalent);
//
//	}
}
