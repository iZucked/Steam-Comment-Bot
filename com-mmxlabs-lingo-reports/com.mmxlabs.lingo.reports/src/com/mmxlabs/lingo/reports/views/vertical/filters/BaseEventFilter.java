/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.filters;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Concrete implementation of the EventFilter interface. Implementations of {@link BaseEventFilter} can be chained together.
 * 
 * @author Simon McGregor
 * 
 */
public abstract class BaseEventFilter implements EventFilter {
	protected final EventFilter filter; // allow filters to be chained if necessary

	public BaseEventFilter(@Nullable final EventFilter filter) {
		this.filter = filter;
	}

	/**
	 * Per event filtering method. Returns true to filter out an {@link Event}. Sub-classes must implement this method.
	 * 
	 * @param date
	 * @param event
	 * @return Return true is event should be filtered out.
	 */
	protected abstract boolean isEventDirectlyFiltered(@NonNull LocalDate date, @NonNull Event event);

	@Override
	public boolean isEventFiltered(@NonNull final LocalDate date, @NonNull final Event event) {
		if (filter != null) {
			// if the previous filter filtered stuff out
			if (filter.isEventFiltered(date, event) == true) {
				return true;
			}
		}

		return isEventDirectlyFiltered(date, event);
	}
}