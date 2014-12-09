package com.mmxlabs.lingo.reports.views.vertical.providers;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.LocalDate;

import com.mmxlabs.lingo.reports.views.vertical.filters.EventFilter;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class to provide events to an EventDisplay column.
 * 
 * Descendant classes should override {@link#getUnfilteredEvents(LocalDate date)} and / or {@link#filterEventOut(LocalDate date, Event event)} to maintain filtered behaviour.
 * 
 * If {@link#getEvents(LocalDate date)} is overridden without preserving the filter logic, {@link#filterEventOut} should be made {@code final} in the overriding class.
 * 
 * 
 * @author Simon McGregor
 * 
 */
public abstract class EventProvider {
	protected final EventFilter filter;

	public EventProvider(@Nullable final EventFilter filter) {
		this.filter = filter;
	}

	public Event[] getEvents(@NonNull final LocalDate date) {
		final ArrayList<Event> result = new ArrayList<>();

		for (final Event event : getUnfilteredEvents(date)) {
			if (filterEventOut(date, event) == false) {
				result.add(event);
			}
		}

		return result.toArray(new Event[0]);
	}

	/** Must be overridden to provide a list of events for any particular date */
	protected abstract Event[] getUnfilteredEvents(@NonNull LocalDate date);

	/** Returns {@code true} if an event should not be returned by this event provider for a particular date. */
	protected boolean filterEventOut(@NonNull final LocalDate date, @NonNull final Event event) {
		if (filter != null) {
			return filter.isEventFiltered(date, event);
		}
		return false;
	}
}
