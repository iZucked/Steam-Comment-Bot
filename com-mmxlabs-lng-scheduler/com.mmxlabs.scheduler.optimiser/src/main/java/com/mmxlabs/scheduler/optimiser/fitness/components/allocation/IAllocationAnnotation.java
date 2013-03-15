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

	public long getLoadVolumeInM3();

	public long getFuelVolumeInM3();

	public long getDischargeVolumeInM3();

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
	int getLoadPricePerM3();

	/**
	 * @return
	 */
	int getDischargePricePerM3();
}
