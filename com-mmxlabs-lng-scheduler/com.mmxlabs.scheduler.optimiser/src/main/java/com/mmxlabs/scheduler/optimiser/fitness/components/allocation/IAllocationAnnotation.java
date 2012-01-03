/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

/**
 * An annotation which is appended to both load and discharge slots
 * indicating decisions made by the cargo allocator.
 * 
 * @author hinton
 *
 */
public interface IAllocationAnnotation {
	public ILoadSlot getLoadSlot();
	public IDischargeSlot getDischargeSlot();
	public long getLoadVolume();
	public long getFuelVolume();
	public long getDischargeVolume();
	/**
	 * @return
	 */
	int getLoadTime();
	/**
	 * @return
	 */
	int getDischargeTime();
	/**
	 * @return
	 */
	int getLoadM3Price();
	/**
	 * @return
	 */
	int getDischargeM3Price();
}
