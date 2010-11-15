/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocationEditor;

/**
 * Allocation provider editor for indexed port slots. Uses an arraylist of pairs
 * to hold the constraints.
 * 
 * @author hinton
 * 
 */
public class ArrayListCargoAllocationEditor<T> implements
		ICargoAllocationEditor<T> {

	public ArrayListCargoAllocationEditor(String name) {
		super();
		this.name = name;
	}

	private final String name;
	private ArrayList<Pair<Long, Set<IPortSlot>>> limits = new ArrayList<Pair<Long, Set<IPortSlot>>>();

	@Override
	public Iterable<Pair<Long, Set<IPortSlot>>> getCargoAllocationLimits() {
		return limits;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		limits = null;
	}

	@Override
	public void addCargoAllocationLimit(final Set<IPortSlot> slots,
			final long maximumVolumeM3) {
		limits.add(new Pair<Long, Set<IPortSlot>>(maximumVolumeM3, slots));
	}
}
