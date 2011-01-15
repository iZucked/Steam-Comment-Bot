/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ITotalVolumeLimit;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.BaseCargoAllocator;

/**
 * DCP which tells the {@link BaseCargoAllocator} what the allocation limits are.
 * 
 * @author hinton
 * 
 */
public interface ITotalVolumeLimitProvider<T> extends IDataComponentProvider {
	/**
	 * @return an iterable over the total volume limits in this problem
	 */
	public Iterable<ITotalVolumeLimit> getTotalVolumeLimits();
	
	public boolean isEmpty();
}
