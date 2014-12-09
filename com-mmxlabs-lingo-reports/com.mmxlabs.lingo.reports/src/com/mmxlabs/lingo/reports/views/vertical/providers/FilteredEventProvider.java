package com.mmxlabs.lingo.reports.views.vertical.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.LocalDate;

import com.mmxlabs.lingo.reports.views.vertical.filters.EventFilter;
import com.mmxlabs.models.lng.schedule.Event;

public final class FilteredEventProvider extends EventProvider {

	private final EventProvider wrapped;

	public FilteredEventProvider(@NonNull final EventProvider wrapped, @Nullable final EventFilter filter) {
		super(filter);
		this.wrapped = wrapped;
	}

	@Override
	protected Event[] getUnfilteredEvents(final LocalDate date) {
		return wrapped.getEvents(date);
	}

}
