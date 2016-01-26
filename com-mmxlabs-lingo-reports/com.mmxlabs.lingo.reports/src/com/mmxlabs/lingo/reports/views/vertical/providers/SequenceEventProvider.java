/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.providers;

import java.time.LocalDate;
import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalReportVisualiser;
import com.mmxlabs.lingo.reports.views.vertical.filters.EventFilter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;

/**
 * Class to provide the events on a given date from one or more Sequence objects.
 * 
 * 
 * Example Usage:
 * 
 * column.setLabelProvider(new EventColumnLabelProvider(new SequenceEventProvider(sequence));
 * 
 * Override the {@link#filterEventOut(LocalDate date, Event event)} method to filter the events more specifically.
 * 
 * @author Simon McGregor
 * 
 */
public class SequenceEventProvider extends EventProvider {
	protected final Sequence[] data;
	protected final AbstractVerticalReportVisualiser verticalReportVisualiser;

	public SequenceEventProvider(@NonNull final Sequence[] data, @Nullable final EventFilter filter, @NonNull final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		super(filter);
		this.data = data;
		this.verticalReportVisualiser = verticalReportVisualiser;
	}

	public SequenceEventProvider(@NonNull final Sequence seq, @Nullable final EventFilter filter, @NonNull final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		this(new Sequence[] { seq }, filter, verticalReportVisualiser);
	}

	@Override
	public Event[] getUnfilteredEvents(@NonNull final LocalDate date) {
		final ArrayList<Event> result = new ArrayList<>();

		for (final Sequence seq : data) {
			if (seq != null) {
				final Event[] events = getEvents(date, seq);
				for (final Event event : events) {
					result.add(event);
				}
			}
		}

		return result.toArray(new Event[0]);
	}

	protected Event[] getEvents(@NonNull final LocalDate date, @NonNull final Sequence seq) {
		final Event[] events = verticalReportVisualiser.getEventsByScheduledDate(seq, date);
		return events;
	}
}