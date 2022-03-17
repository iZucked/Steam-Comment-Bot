/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.schedule.Event;

public class OtherPair implements DeltaPair {
	private final Event first;
	private final Event second;

	OtherPair(final Event first, final Event second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public Event first() {
		return first;
	}

	@Override
	public Event second() {
		return second;
	}

	public static List<OtherPair> generateDeltaPairs(ISelectedDataProvider currentSelectedDataProvider, final List<Event> elements) {
		final List<OtherPair> pairs = new ArrayList<>();
		Set<Event> seenElements = new HashSet<>();

		for (int i = 0; i < elements.size() - 1; i++) {
			final Event a = elements.get(i);
			final Event b = elements.get(i + 1);

			boolean pinned = currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a);

			if (a.name().equals(b.name())) {
				if (pinned) {
					pairs.add(new OtherPair(a, b));
				} else {
					pairs.add(new OtherPair(b, a));
				}
				seenElements.add(a);
				seenElements.add(b);
				i++;
			} else {
				if (pinned) {
					pairs.add(new OtherPair(a, null));
				} else {
					pairs.add(new OtherPair(null, a));
				}
				seenElements.add(a);
			}
		}

		Set<Event> extraElements = new LinkedHashSet<>(elements);
		extraElements.removeAll(seenElements);

		// Process possible lonely elements
		for (Event a : extraElements) {
			if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
				pairs.add(new OtherPair(a, null));
			} else {
				pairs.add(new OtherPair(null, a));
			}
		}

		return pairs;
	}

	public static List<OtherPair> generateSingles(ISelectedDataProvider currentSelectedDataProvider, final List<Event> events) {
		final List<OtherPair> pairs = new ArrayList<>();
		for (int i = 0; i < events.size(); i++) {
			final Event a = events.get(i);
			boolean pinned = currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a);
			if (pinned) {
				pairs.add(new OtherPair(a, null));
			} else {
				pairs.add(new OtherPair(null, a));
			}
		}

		return pairs;
	}

	@Override
	public String getName() {
		return first != null ? first.name() : second.name();
	}
}