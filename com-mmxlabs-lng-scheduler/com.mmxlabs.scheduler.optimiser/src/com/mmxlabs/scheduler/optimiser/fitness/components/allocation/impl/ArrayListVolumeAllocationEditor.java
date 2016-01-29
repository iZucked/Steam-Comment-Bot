/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;

import com.mmxlabs.scheduler.optimiser.components.ITotalVolumeLimit;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitEditor;

/**
 * Allocation provider editor for indexed port slots. Just a list wrapper.
 * 
 * @author hinton
 * 
 */
public class ArrayListVolumeAllocationEditor implements ITotalVolumeLimitEditor {

	private ArrayList<ITotalVolumeLimit> totalVolumeLimits = new ArrayList<ITotalVolumeLimit>();

	@Override
	public Iterable<ITotalVolumeLimit> getTotalVolumeLimits() {
		return totalVolumeLimits;
	}

	@Override
	public void addTotalVolumeLimit(final ITotalVolumeLimit limit) {
		totalVolumeLimits.add(limit);
	}

	@Override
	public boolean isEmpty() {
		return totalVolumeLimits.isEmpty();
	}
}
