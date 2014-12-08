package com.mmxlabs.lingo.reports.views.vertical.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.mmxlabs.lingo.reports.views.vertical.VerticalReportUtils;
import com.mmxlabs.lingo.reports.views.vertical.filters.EventFilter;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Event provider
 * 
 * @author mmxlabs
 * 
 */
public class HashMapEventProvider extends EventProvider {
	protected Map<LocalDate, List<Event>> events = new HashMap<>();
	protected final Event[] noEvents = new Event[0];

	public HashMapEventProvider(final LocalDate start, final LocalDate end, final EventProvider wrapped) {
		this(null);
		for (final LocalDate day : VerticalReportUtils.getUTCDaysBetween(start, end)) {
			for (final Event event : wrapped.getEvents(day)) {
				addEvent(day, event);
			}
		}
	}

	public HashMapEventProvider(final EventFilter filter) {
		super(filter);
	}

	public void addEvent(final LocalDate date, final Event event) {
		final List<Event> list = events.containsKey(date) ? events.get(date) : new ArrayList<Event>();
		list.add(event);
		this.events.put(date, list);
	}

	@Override
	protected Event[] getUnfilteredEvents(final LocalDate date) {
		if (events.containsKey(date)) {
			return events.get(date).toArray(noEvents);
		}
		return noEvents;
	}
}
