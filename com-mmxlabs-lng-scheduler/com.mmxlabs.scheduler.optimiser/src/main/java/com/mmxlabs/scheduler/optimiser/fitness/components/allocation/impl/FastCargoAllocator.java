/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * A faster than LP cargo allocator. Does a bit of finagling to make it all work,
 * which isn't hugely pretty. Each cargo is assigned an index by the base class;
 * this class maps ports slots to a single load/discharge sum constraint (i.e.
 * presumes that there cannot be two contractual limits applying to a single slot)
 * using the cargoConstraints array.
 * 
 * Cargoes are then sorted by their unit price, and allocated the minimum of
 * (a) vessel capacity, (b) the remaining load limit and (c) the remaining discharge limit
 * for those cargoes.
 * 
 * @author hinton
 * 
 */
public class FastCargoAllocator<T> extends BaseCargoAllocator<T> {
	final Map<IPortSlot, Integer> volumeConstraintMap = new HashMap<IPortSlot, Integer>();
	final ArrayList<Long> initialConstraintValues = new ArrayList<Long>();

	final ArrayList<IPortSlot> loadSlots = new ArrayList<IPortSlot>();

	@Override
	public void init() {
		super.init();
		for (final Pair<Long, Set<IPortSlot>> constraint : cargoAllocationProvider
				.getCargoAllocationLimits()) {
			final int index = initialConstraintValues.size();
			initialConstraintValues.add(constraint.getFirst());

			for (final IPortSlot slot : constraint.getSecond()) {
				volumeConstraintMap.put(slot, index);
				if (slot instanceof ILoadSlot) {
					loadSlots.add(slot);
				}
			}
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		cargoConstraints.clear();
	}

	@Override
	public void addCargo(PortDetails loadDetails, PortDetails dischargeDetails,
			int loadTime, int dischargeTime, long requiredLoadVolume,
			long vesselCapacity) {
		super.addCargo(loadDetails, dischargeDetails, loadTime, dischargeTime,
				requiredLoadVolume, vesselCapacity);
		
		cargoConstraints.add(
				new Pair<Integer, Integer>(
						volumeConstraintMap.get(loadDetails.getPortSlot()),
						volumeConstraintMap.get(dischargeDetails.getPortSlot())));
	}

	final ArrayList<Pair<Integer, Integer>> cargoConstraints = 
		new ArrayList<Pair<Integer, Integer>>();

	@Override
	protected long[] allocateSpareVolume() {
		// sort cargoes by unit cost
		final Integer[] variables = new Integer[cargoIndex];
		for (int i = 0; i < variables.length; i++)
			variables[i] = i;
		final Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer arg0, Integer arg1) {
				final double d0 = unitPrices[arg0];
				final double d1 = unitPrices[arg1];

				if (d0 > d1) {
					return -1;
				} else if (d1 < d0) {
					return 1;
				} else {
					return 0;
				}
			}
		};

		Arrays.sort(variables, comparator);

		// set up constraint table
		final long[] remainders = CollectionsUtil
				.longsToLongArray(initialConstraintValues);

		final long[] allocations = new long[cargoIndex];
		
		// remove volume which load slots <em>must</em> load
		for (final IPortSlot slot : loadSlots) {
			final int variable = variableForSlot(slot);
			if (variable != -1) {
				remainders[volumeConstraintMap.get(slot)] -= forcedLoadVolume[variable];
			}
		}
		
		for (final int variable : variables) {
			final Pair<Integer, Integer> constraintIndices = 
				cargoConstraints.get(variable);
			final long slack1 = remainders[constraintIndices.getFirst()];
			final long slack2 = remainders[constraintIndices.getSecond()];
			
			final long allocation = 
				Math.min(
						vesselCapacity[variable],
						Math.min(slack1, slack2));
			
			remainders[constraintIndices.getFirst()] -= allocation;
			remainders[constraintIndices.getSecond()] -= allocation;
			
			allocations[variable] = allocation;
		}
		return allocations;
	}
}
