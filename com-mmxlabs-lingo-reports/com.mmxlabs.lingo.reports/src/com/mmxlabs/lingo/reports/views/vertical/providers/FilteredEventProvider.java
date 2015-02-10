/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.LocalDate;

import com.mmxlabs.lingo.reports.views.vertical.filters.EventFilter;
import com.mmxlabs.lingo.reports.views.vertical.labellers.IBorderProvider;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Event provider wrapper to further filter the data.
 * 
 * @author Simon Goodall
 * 
 */
public final class FilteredEventProvider extends EventProvider implements IBorderProvider {

	private final EventProvider wrapped;
	private final IBorderProvider borderProvider;

	public FilteredEventProvider(@NonNull final EventProvider wrapped, @Nullable final EventFilter filter) {
		super(filter);
		this.wrapped = wrapped;
		if (wrapped instanceof IBorderProvider) {
			borderProvider = (IBorderProvider) wrapped;
		} else {
			borderProvider = null;
		}
	}

	@Override
	protected Event[] getUnfilteredEvents(final LocalDate date) {
		return wrapped.getEvents(date);
	}

	@Override
	public int getBorders(final LocalDate date, final Event event) {
		if (borderProvider != null) {
			return borderProvider.getBorders(date, event);
		}
		return IBorderProvider.NONE;
	}
}
