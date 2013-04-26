/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 * An annotation which is appended to both {@link ILoadOption} and {@link IDischargeOption} indicating decisions made by the {@link IVolumeAllocator}
 * 
 * @author hinton
 * 
 */
public interface IAllocationAnnotation {
	ILoadOption getLoadOption();

	IDischargeOption getDischargeOption();

	/**
	 * Returns the total load volume in M3. This is the sum of the discharge volume, fuel volume and the remaining heel.
	 * 
	 */
	long getLoadVolumeInM3();

	/**
	 * Returns the total LNG in m3 used as fuel during this cargo.
	 * 
	 * @return
	 */
	long getFuelVolumeInM3();

	/**
	 * Returns the quantity of LNG left as heel in m3 with no where to go (i.e. lost).
	 * 
	 * @return
	 */
	long getRemainingHeelVolumeInM3();

	/**
	 * Returns the quantity of LNG in m3 that has been discharged.
	 * 
	 * @return
	 */
	long getDischargeVolumeInM3();

	/**
	 * Returns the time loading began
	 * 
	 * @return
	 */
	int getLoadTime();

	/**
	 * Returns the time discharging began
	 * 
	 * @return
	 */
	int getDischargeTime();

	/**
	 * Returns the price/m3 at load
	 * 
	 * @return
	 */
	int getLoadPricePerM3();

	/**
	 * Returns the price/m3 at discharge
	 * 
	 * @return
	 */
	int getDischargePricePerM3();
}
