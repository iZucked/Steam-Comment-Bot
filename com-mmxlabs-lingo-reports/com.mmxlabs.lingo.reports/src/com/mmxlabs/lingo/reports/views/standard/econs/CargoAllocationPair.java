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
import com.mmxlabs.models.lng.schedule.CargoAllocation;

public class CargoAllocationPair implements DeltaPair {
	private final CargoAllocation first;
	private final CargoAllocation second;

	CargoAllocationPair(final CargoAllocation first, final CargoAllocation second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public CargoAllocation first() {
		return first;
	}

	@Override
	public CargoAllocation second() {
		return second;
	}

	public static List<CargoAllocationPair> generateDeltaPairs(final @Nullable ISelectedDataProvider currentSelectedDataProvider, final List<CargoAllocation> elements) {
		Collections.sort(elements, (a, b) -> a.getName().compareTo(b.getName()));
		final List<CargoAllocationPair> pairs = new ArrayList<>();
		final Set<CargoAllocation> seenElements = new HashSet<>();

		for (int i = 0; i < elements.size() - 1; i++) {
			final CargoAllocation a = elements.get(i);
			final CargoAllocation b = elements.get(i + 1);

			if (a.getName().equals(b.getName())) {
				if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
					pairs.add(new CargoAllocationPair(a, b));
				} else {
					pairs.add(new CargoAllocationPair(b, a));
				}
				seenElements.add(a);
				seenElements.add(b);
				++i;
			} else {
				if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
					pairs.add(new CargoAllocationPair(a, null));
				} else {
					pairs.add(new CargoAllocationPair(null, a));
				}
				seenElements.add(a);
			}
		}
		final Set<CargoAllocation> extraElements = new LinkedHashSet<>(elements);
		extraElements.removeAll(seenElements);

		// Process possible lonely elements
		for (final CargoAllocation a : extraElements) {
			if (currentSelectedDataProvider == null || currentSelectedDataProvider.isPinnedObject(a)) {
				pairs.add(new CargoAllocationPair(a, null));
			} else {
				pairs.add(new CargoAllocationPair(null, a));
			}
		}
		return pairs;
	}

	@Override
	public String getName() {
		return (first != null) ? first.getName() : second.getName();
	}
}