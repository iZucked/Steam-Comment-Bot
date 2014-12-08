package com.mmxlabs.lingo.reports.views.vertical.providers;

import java.util.ArrayList;

import org.joda.time.LocalDate;

import com.mmxlabs.lingo.reports.views.vertical.filters.EventFilter;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class to provide events to an EventDisplay column.
 * 
 * Descendant classes should override {@link#getUnfilteredEvents(Date date)} and / or {@link#filterEventOut(Date date, Event event)} to maintain filtered behaviour.
 * 
 * If {@link#getEvents(Date date)} is overridden without preserving the filter logic, {@link#filterEventOut} should be made {@code final} in the overriding class.
 * 
 * 
 * @author Simon McGregor
 * 
 * @param <T>
 *            The data type to initialise the event provider with.
 */
public abstract class EventProvider {
	final protected EventFilter filter;

	public EventProvider(final EventFilter filter) {
		this.filter = filter;
	}

	public Event[] getEvents(final LocalDate date) {
		final ArrayList<Event> result = new ArrayList<>();

		for (final Event event : getUnfilteredEvents(date)) {
			if (filterEventOut(date, event) == false) {
				result.add(event);
			}
		}

		return result.toArray(new Event[0]);
	}

	/** Must be overridden to provide a list of events for any particular date */
	protected abstract Event[] getUnfilteredEvents(LocalDate date);

	/** Returns {@code true} if an event should not be returned by this event provider for a particular date. */
	protected boolean filterEventOut(final LocalDate date, final Event event) {
		if (filter != null) {
			return filter.isEventFiltered(date, event);
		}
		return false;
	}
}
