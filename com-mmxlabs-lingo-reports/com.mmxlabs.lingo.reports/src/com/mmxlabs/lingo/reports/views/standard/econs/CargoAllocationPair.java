/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.models.lng.schedule.CargoAllocation;

public class CargoAllocationPair extends DeltaPair {
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

	static public List<CargoAllocationPair> generateCargoPair(final List<CargoAllocation> cargoAllocations) {
		Collections.sort(cargoAllocations, (a, b) -> a.getName().compareTo(b.getName()));
		final List<CargoAllocationPair> pairs = new ArrayList<>();

		// Edge case, only one lonely element in the list
		if (cargoAllocations.size() == 1) {
			final CargoAllocation a = cargoAllocations.get(0);
			// pairs.add(new CargoAllocationPair(a, null));
		}

		for (int i = 0; i < cargoAllocations.size() - 1; i++) {
			final CargoAllocation a = cargoAllocations.get(i);
			final CargoAllocation b = cargoAllocations.get(i + 1);

			if (a.getName().equals(b.getName())) {
				pairs.add(new CargoAllocationPair(a, b));
				i++;
			} else {
				// pairs.add(new CargoAllocationPair(a, null));
			}
		}

		// Process possible final lonely element
		if (cargoAllocations.size() > 1) {
			final CargoAllocation a = cargoAllocations.get(cargoAllocations.size() - 2);
			final CargoAllocation b = cargoAllocations.get(cargoAllocations.size() - 1);
			if (!a.getName().equals(b.getName())) {
				// pairs.add(new CargoAllocationPair(b, null));
			}
		}
		return pairs;
	}

	@Override
	public String getName() {
		return first.getName();
	}
}