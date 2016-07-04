/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.providers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.vertical.VerticalReportUtils;
import com.mmxlabs.lingo.reports.views.vertical.filters.EventFilter;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * An {@link EventProvider} which uses a {@link HashMap} to cache events by date. It can be used as a cache around a pre-existing {@link EventProvider} and/or be directly populated via external code
 * through {@link #addEvent(LocalDate, Event)}.
 * 
 * @author Simon McGregor
 * 
 */
public class HashMapEventProvider extends EventProvider {
	protected Map<LocalDate, List<@NonNull Event>> events = new HashMap<>();
	protected final @NonNull Event[] noEvents = new @NonNull Event[0];
	private final List<@NonNull Event> allEvents = new LinkedList<>();

	public HashMapEventProvider(@NonNull final LocalDate start, @NonNull final LocalDate end, @NonNull final EventProvider wrapped) {
		this(null);
		for (final LocalDate day : VerticalReportUtils.getUTCDaysBetween(start, end)) {
			for (final Event event : wrapped.getEvents(day)) {
				addEvent(day, event);
			}
		}
	}

	public HashMapEventProvider(@Nullable final EventFilter filter) {
		super(filter);
	}

	public void addEvent(@NonNull final LocalDate date, @NonNull final Event event) {
		final List<@NonNull Event> list = events.containsKey(date) ? events.get(date) : new ArrayList<@NonNull Event>();
		list.add(event);
		this.events.put(date, list);
		if (!allEvents.contains(event)) {
			allEvents.add(event);
		}
	}

	@Override
	protected @NonNull Event[] getUnfilteredEvents(@NonNull final LocalDate date) {
		if (events.containsKey(date)) {
			return events.get(date).toArray(noEvents);
		}
		return noEvents;
	}

	public List<Event> getEvents() {
		return allEvents;
	}
}
