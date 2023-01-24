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
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;

public class OpenAllocationPair implements DeltaPair {
	private final OpenSlotAllocation first;
	private final OpenSlotAllocation second;

	OpenAllocationPair(final OpenSlotAllocation first, final OpenSlotAllocation second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public OpenSlotAllocation first() {
		return first;
	}

	@Override
	public OpenSlotAllocation second() {
		return second;
	}

	public static List<OpenAllocationPair> generateDeltaPairs(final @Nullable ISelectedDataProvider currentSelectedDataProvider, final List<OpenSlotAllocation> elements) {
		Collections.sort(elements, (a, b) -> a.getSlot().getName().compareTo(b.getSlot().getName()));
		final List<OpenAllocationPair> pairs = new ArrayList<>();
		Set<OpenSlotAllocation> seenElements = new HashSet<>();

		for (int i = 0; i < elements.size() - 1; i++) {
			final OpenSlotAllocation a = elements.get(i);
			final OpenSlotAllocation b = elements.get(i + 1);

			if (a.getSlot().getName().equals(b.getSlot().getName()) && a.getSlot().eClass() == b.getSlot().eClass()) {
				if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
					pairs.add(new OpenAllocationPair(a, b));
				} else {
					pairs.add(new OpenAllocationPair(b, a));
				}
				seenElements.add(a);
				seenElements.add(b);
				++i;
			} else {
				if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
					pairs.add(new OpenAllocationPair(a, null));
				} else {
					pairs.add(new OpenAllocationPair(null, a));
				}
				seenElements.add(a);
			}
		}
		Set<OpenSlotAllocation> extraElements = new LinkedHashSet<>(elements);
		extraElements.removeAll(seenElements);

		// Process possible lonely elements
		for (OpenSlotAllocation a : extraElements) {
			if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
				pairs.add(new OpenAllocationPair(a, null));
			} else {
				pairs.add(new OpenAllocationPair(null, a));
			}
		}
		return pairs;
	}

	@Override
	public String getName() {
		return (first != null) ? first.getSlot().getName() : second.getSlot().getName();
	}
}