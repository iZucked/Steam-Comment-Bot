/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.ITimezoneProvider;

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

			if (slotVisit.getStart().after(slot.getWindowEndWithSlotOrPortTime())) {
				return true;
			}
		} else if (event instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
			final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			if (vesselEventVisit.getStart().after(vesselEvent.getStartBy())) {
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

	public static LocalDateTime getLocalDateFor(final Date date, final TimeZone timeZone, final boolean asUTCEquivalent) {
		final Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(date);
		return getLocalDateFor(cal, asUTCEquivalent);
	}

	public static LocalDateTime getLocalDateFor(final Calendar cal, final boolean asUTCEquivalent) {
		if (asUTCEquivalent) {
			return new LocalDateTime(cal.get(Calendar.YEAR), 1 + cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		} else {
			final Calendar cal2 = (Calendar) cal.clone();
			cal2.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
			return new LocalDateTime(cal2.get(Calendar.YEAR), 1 + cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

		}
	}

	public static <T extends ITimezoneProvider & EObject> LocalDateTime getLocalDateFor(final T object, final EAttribute feature, final boolean asUTCEquivalent) {
		final Date date = (Date) object.eGet(feature);
		return getLocalDateFor(date, TimeZone.getTimeZone(object.getTimeZone(feature)), asUTCEquivalent);

	}

	public static <T extends ITimezoneProvider & EObject> LocalDateTime getLocalDateFor(final T object, final Date date, final EAttribute feature, final boolean asUTCEquivalent) {
		return getLocalDateFor(date, TimeZone.getTimeZone(object.getTimeZone(feature)), asUTCEquivalent);

	}
}
