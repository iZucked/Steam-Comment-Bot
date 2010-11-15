/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.BaseCargoAllocator;

/**
 * DCP which tells the {@link BaseCargoAllocator} what the allocation limits are.
 * 
 * @author hinton
 * 
 */
public interface ICargoAllocationProvider<T> extends IDataComponentProvider {
	/**
	 * returns some kind of iterable containing pairs, whose first element is a
	 * capacity upper bound and whose second element is a collection of
	 * IPortSlots whose summed load and discharge quantities must be suitably
	 * bounded.
	 * 
	 * @return
	 */
	public Iterable<Pair<Long, Set<IPortSlot>>> getCargoAllocationLimits();
}
