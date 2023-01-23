/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class VesselEventVisitPair implements DeltaPair {
	private final VesselEventVisit first;
	private final VesselEventVisit second;

	VesselEventVisitPair(final VesselEventVisit first, final VesselEventVisit second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public VesselEventVisit first() {
		return first;
	}

	@Override
	public VesselEventVisit second() {
		return second;
	}

	public static List<VesselEventVisitPair> generateDeltaPairs(final @Nullable ISelectedDataProvider currentSelectedDataProvider, final List<VesselEventVisit> elements) {
		Collections.sort(elements, (a, b) -> a.name().compareTo(b.name()));

		final List<VesselEventVisitPair> pairs = new ArrayList<>();

		final Set<VesselEventVisit> seenElements = new HashSet<>();

		// Edge case, only one lonely element in the list

		for (int i = 0; i < elements.size() - 1; i++) {
			final VesselEventVisit a = elements.get(i);
			final VesselEventVisit b = elements.get(i + 1);

			if (a.name().equals(b.name())) {
				if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
					pairs.add(new VesselEventVisitPair(a, b));
				} else {
					pairs.add(new VesselEventVisitPair(b, a));
				}
				seenElements.add(a);
				seenElements.add(b);
				++i;
			} else {
				if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
					pairs.add(new VesselEventVisitPair(a, null));
				} else {
					pairs.add(new VesselEventVisitPair(null, a));
				}
				seenElements.add(a);
			}
		}

		final Set<VesselEventVisit> extraElements = new LinkedHashSet<>(elements);
		extraElements.removeAll(seenElements);

		// Process possible lonely elements
		for (final VesselEventVisit a : extraElements) {
			if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
				pairs.add(new VesselEventVisitPair(a, null));
			} else {
				pairs.add(new VesselEventVisitPair(null, a));
			}
		}

		return pairs;
	}

	@Override
	public String getName() {
		return (first != null) ? first.name() : second.name();
	}
}