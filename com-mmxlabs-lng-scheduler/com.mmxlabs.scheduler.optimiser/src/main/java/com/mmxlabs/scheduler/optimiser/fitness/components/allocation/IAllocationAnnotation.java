/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * An annotation which is appended to both {@link ILoadOption} and {@link IDischargeOption} indicating decisions made by the {@link IVolumeAllocator}
 * 
 * @author hinton
 * 
 */
public interface IAllocationAnnotation {
	/*
	ILoadOption getLoadOption();

	IDischargeOption getDischargeOption();
	*/
	
	ILoadOption getFirstLoadSlot();
	List<IPortSlot> getSlots();

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
	 * Returns the total volume of LNG (in m^3) loaded or discharged at a particular slot.
	 * @param slot
	 * @return
	 */
	long getSlotVolumeInM3(IPortSlot slot);

	/**
	 * Returns the total load volume in M3. This is the sum of the discharge volume, fuel volume and the remaining heel.
	 * 
	 */
	//long getLoadVolumeInM3();

	/**
	 * Returns the quantity of LNG in m3 that has been discharged.
	 * 
	 * @return
	 */
	//long getDischargeVolumeInM3();

	/**
	 * Returns the time at which loading or discharging began for a particular port slot
	 * @param slot
	 * @return
	 */
	int getSlotTime(IPortSlot slot);
	
	/**
	 * Returns the time loading began
	 * 
	 * @return
	 */
	//int getLoadTime();

	/**
	 * Returns the time discharging began
	 * 
	 * @return
	 */
	//int getDischargeTime();

	/**
	 * Returns the price/m3 at a load or discharge slot
	 * 
	 * @return
	 */
	int getSlotPricePerM3(IPortSlot slot);
	
	/**
	 * Returns the cargo CV value, which is based on the first load port 
	 */
	
	
	/**
	 * Returns the price/m3 at load
	 * 
	 * @return
	 */
	//int getLoadPricePerM3();

	/**
	 * Returns the price/m3 at discharge
	 * 
	 * @return
	 */
	//int getDischargePricePerM3();
}
