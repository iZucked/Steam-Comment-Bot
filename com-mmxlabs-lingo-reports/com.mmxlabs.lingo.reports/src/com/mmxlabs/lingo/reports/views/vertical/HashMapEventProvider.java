package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Event provider
 * 
 * @author mmxlabs
 * 
 */
public class HashMapEventProvider extends EventProvider {
	protected Map<Date, List<Event>> events = new HashMap<>();
	protected final Event[] noEvents = new Event[0];

	public HashMapEventProvider(final Date start, final Date end, final EventProvider wrapped) {
		this(null);
		for (final Date day : VerticalReportUtils.getGMTDaysBetween(start, end)) {
			for (final Event event : wrapped.getEvents(day)) {
				addEvent(day, event);
			}
		}
	}

	public HashMapEventProvider(final EventFilter filter) {
		super(filter);
	}

	public void addEvent(final Date date, final Event event) {
		final List<Event> list = events.containsKey(date) ? events.get(date) : new ArrayList<Event>();
		list.add(event);

		// we actually want the events to be keyed on 00:00 GMT of the same day
		this.events.put(VerticalReportUtils.getGMTDayFor(date), list);
	}

	@Override
	protected Event[] getUnfilteredEvents(final Date date) {
		if (events.containsKey(date)) {
			return events.get(date).toArray(noEvents);
		}
		return noEvents;
	}
}
