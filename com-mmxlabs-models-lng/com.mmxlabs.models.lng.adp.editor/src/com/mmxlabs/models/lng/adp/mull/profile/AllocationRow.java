/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.MullAllocationRow;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public abstract class AllocationRow implements IAllocationRow {
	
	private final int weight;
	private final List<Vessel> vessels;
	
	protected AllocationRow(final MullAllocationRow eAllocationRow) {
		this.weight = eAllocationRow.getWeight();
		final List<Vessel> localVessels = new ArrayList<>();
		for (final Vessel vessel : eAllocationRow.getVessels()) {
			if (vessel == null) {
				throw new IllegalStateException("Vessels must be non-null");
			}
			localVessels.add(vessel);
		}
		this.vessels = localVessels;
	}

	protected AllocationRow(final int weight, final List<Vessel> vessels) {
		this.weight = weight;
		this.vessels = vessels;
	}

	protected void validateConstruction() {
		if (this.weight < 0) {
			throw new IllegalStateException("Allocation row weight must be positive");
		}
		if (this.vessels.isEmpty()) {
			throw new IllegalStateException("No vessels found");
		}
	}

	@Override
	public int getWeight() {
		return this.weight;
	}

	@Override
	public List<Vessel> getVessels() {
		return vessels;
	}
}
