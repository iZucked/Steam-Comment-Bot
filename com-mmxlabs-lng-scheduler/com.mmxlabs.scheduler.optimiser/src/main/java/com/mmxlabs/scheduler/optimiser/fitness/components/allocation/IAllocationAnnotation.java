/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 * An annotation which is appended to both {@link ILoadOption} and {@link IDischargeOption} indicating decisions made by the {@link ICargoAllocator}
 * 
 * @author hinton
 * 
 */
public interface IAllocationAnnotation {
	public ILoadOption getLoadOption();

	public IDischargeOption getDischargeOption();

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
