package com.mmxlabs.lingo.reports.views.vertical;

import org.joda.time.LocalDate;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Concrete implementation of the EventFilter interface.
 * 
 * @author mmxlabs
 * 
 */
public abstract class BaseEventFilter implements EventFilter {
	protected final EventFilter filter; // allow filters to be chained if necessary

	public BaseEventFilter(final EventFilter filter) {
		this.filter = filter;
	}

	protected abstract boolean isEventDirectlyFiltered(LocalDate date, Event event);

	@Override
	public boolean isEventFiltered(final LocalDate date, final Event event) {
		if (filter != null) {
			// if the previous filter filtered stuff out
			if (filter.isEventFiltered(date, event) == true) {
				return true;
			}
		}

		return isEventDirectlyFiltered(date, event);
	}
}