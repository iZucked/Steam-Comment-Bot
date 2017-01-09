/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.providers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.vertical.filters.EventFilter;
import com.mmxlabs.lingo.reports.views.vertical.labellers.IBorderProvider;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Extended version of {@link HashMapEventProvider} with basic implementation for an {@link IBorderProvider}.
 */
public class HashMapEventAndBorderProvider extends HashMapEventProvider implements IBorderProvider {
	protected final Map<Pair<LocalDate, Event>, Integer> borderResults = new HashMap<>();

	public HashMapEventAndBorderProvider(@NonNull final LocalDate start, @NonNull final LocalDate end, @NonNull final EventProvider wrapped) {
		super(start, end, wrapped);
	}

	public HashMapEventAndBorderProvider(@Nullable final EventFilter filter) {
		super(filter);
	}

	@Override
	public int getBorders(final LocalDate date, final Event event) {
		final Pair<LocalDate, Event> key = new Pair<>(date, event);
		if (borderResults.containsKey(key)) {
			return borderResults.get(key);
		}
		return 0;
	}
}
