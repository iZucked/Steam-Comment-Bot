package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.LocalDate;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public final class VerticalReportUtils {

	/**
	 * Is the specified day outside of the actual slot visit itself? (A SlotVisit event can be associated with a particular day if its slot window includes that day.)
	 * 
	 * @param day
	 * @param visit
	 * @return
	 */
	public static boolean isDayOutsideActualVisit(final LocalDate day, final SlotVisit visit, boolean asUTCEquivalent) {
		final LocalDate nextDay = day.plusDays(1);

		LocalDate eventStart = VerticalReportUtils.getLocalDateFor(visit.getStart(), TimeZone.getTimeZone(visit.getTimeZone(SchedulePackage.Literals.EVENT__START)), asUTCEquivalent);
		LocalDate eventEnd = VerticalReportUtils.getLocalDateFor(visit.getEnd(), TimeZone.getTimeZone(visit.getTimeZone(SchedulePackage.Literals.EVENT__END)), asUTCEquivalent);

		return (nextDay.isBefore(eventStart) || (eventEnd.isAfter(day) == false));
	}

	public static boolean isEventLate(final Event event) {
		if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			final Slot slot = slotVisit.getSlotAllocation().getSlot();
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
	 * Returns all events in the specified sequence which overlap with the 24 hr period starting with the specified date
	 * 
	 * @param seq
	 * @param date
	 * @return
	 */
	public static Event[] getEvents(final Sequence seq, final LocalDate date, boolean asUTCEquivalent) {
		return getEvents(seq, date, date.plusDays(1), asUTCEquivalent);
	}

	/**
	 * Returns the events, if any, occurring between the two dates specified.
	 */
	public static Event[] getEvents(final Sequence seq, final LocalDate start, final LocalDate end, boolean asUTCEquivalent) {
		final ArrayList<Event> result = new ArrayList<>();
		if (seq != null && start != null && end != null) {
			for (final Event event : seq.getEvents()) {

				LocalDate eventStart = VerticalReportUtils.getLocalDateFor(event.getStart(), TimeZone.getTimeZone(event.getTimeZone(SchedulePackage.Literals.EVENT__START)), asUTCEquivalent);
				LocalDate eventEnd = VerticalReportUtils.getLocalDateFor(event.getEnd(), TimeZone.getTimeZone(event.getTimeZone(SchedulePackage.Literals.EVENT__END)), asUTCEquivalent);

				// when we get to an event after the search window, break the loop
				// NO: events are not guaranteed to be sorted by date :(
				if (eventStart.isAfter(end)) {
					// break;
				}
				// otherwise, as long as the event is in the search window, add it to the results
				// if the event ends at midnight, we do *not* count it towards this day
				else if (start.isBefore(eventEnd)) {
					result.add(event);
				}
			}
		}
		return result.toArray(new Event[0]);
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
			// final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Etc/UTC"));
			// c.setTime(start);
			// c.set(Calendar.HOUR_OF_DAY, 0);
			// c.set(Calendar.MINUTE, 0);
			// c.set(Calendar.SECOND, 0);
			// c.set(Calendar.MILLISECOND, 0);
			while (!current.isAfter(end)) {
				result.add(current);
				// result.add(c.getTime());
				// c.add(Calendar.DAY_OF_MONTH, 1);
				current = current.plusDays(1);
			}

		}

		return result;
	}

	// public static LocalDate getUTCDayFor(final Date date) {
	// final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Etc/UTC"));
	// c.setTime(date);
	// c.set(Calendar.HOUR_OF_DAY, 0);
	// c.set(Calendar.MINUTE, 0);
	// c.set(Calendar.SECOND, 0);
	// c.set(Calendar.MILLISECOND, 0);
	// // return c.getTime();
	// return new LocalDate(c.get(Calendar.YEAR), 1 + c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	// }

	public static LocalDate getLocalDateFor(final Date date, final TimeZone timeZone, final boolean asUTCEquivalent) {
		final Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(date);
		return getLocalDateFor(cal, asUTCEquivalent);
	}

	public static LocalDate getLocalDateFor(final Calendar cal, final boolean asUTCEquivalent) {
		if (asUTCEquivalent) {
			return new LocalDate(cal.get(Calendar.YEAR), 1 + cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		} else {
			final Calendar cal2 = (Calendar) cal.clone();
			cal2.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
			return new LocalDate(cal.get(Calendar.YEAR), 1 + cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

		}
	}

}
