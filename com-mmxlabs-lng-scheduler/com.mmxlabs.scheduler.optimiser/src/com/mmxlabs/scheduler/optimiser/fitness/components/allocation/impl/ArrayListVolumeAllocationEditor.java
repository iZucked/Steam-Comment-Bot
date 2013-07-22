/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
 * @since 6.0
 * 
 */
public class ArrayListVolumeAllocationEditor implements ITotalVolumeLimitEditor {

	public ArrayListVolumeAllocationEditor(final String name) {
		super();
		this.name = name;
	}

	private final String name;

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
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		totalVolumeLimits = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return totalVolumeLimits.isEmpty();
	}
}
