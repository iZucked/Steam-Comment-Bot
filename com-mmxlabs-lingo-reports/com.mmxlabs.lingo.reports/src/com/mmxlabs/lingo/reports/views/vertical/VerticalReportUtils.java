package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.Event;
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
	public static boolean isDayOutsideActualVisit(final Date day, final SlotVisit visit) {
		final Date nextDay = new Date(day.getTime() + 1000 * 24 * 3600);

		final Slot slot = ((SlotVisit) visit).getSlotAllocation().getSlot();

		return (nextDay.before(visit.getStart()) || (visit.getEnd().after(day) == false));
	}

	
	public static boolean isEventLate(final Event event) {
		if (event instanceof SlotVisit) {
			SlotVisit slotVisit = (SlotVisit) event;
			Slot slot = slotVisit.getSlotAllocation().getSlot();
			if (slotVisit.getStart().after(slot.getWindowEndWithSlotOrPortTime())) {
				return true;
			}
		} else if (event instanceof VesselEventVisit) {
			VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
			VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
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
	public static Event[] getEvents(final Sequence seq, final Date date) {
		return getEvents(seq, date, new Date(date.getTime() + 1000 * 24 * 3600));
	}

	/**
	 * Returns the events, if any, occurring between the two dates specified.
	 */
	public static Event[] getEvents(final Sequence seq, final Date start, final Date end) {
		final ArrayList<Event> result = new ArrayList<>();
		if (seq != null && start != null && end != null) {
			for (final Event event : seq.getEvents()) {
				// when we get to an event after the search window, break the loop
				// NO: events are not guaranteed to be sorted by date :(
				if (event.getStart().after(end)) {
					// break;
				}
				// otherwise, as long as the event is in the search window, add it to the results
				// if the event ends at midnight, we do *not* count it towards this day
				else if (start.before(event.getEnd())) {
					result.add(event);
				}
			}
		}
		return result.toArray(new Event[0]);
	}

	/**
	 * Returns a list of all 00h00 GMT Date objects which fall within the specified range
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getGMTDaysBetween(final Date start, final Date end) {
		final ArrayList<Date> result = new ArrayList<Date>();
		if (start != null && end != null) {
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT"));
			c.setTime(start);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			while (!c.getTime().after(end)) {
				result.add(c.getTime());
				c.add(Calendar.DAY_OF_MONTH, 1);
			}

		}

		return result;
	}

	public static Date getGMTDayFor(final Date date) {
		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT"));
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
}
