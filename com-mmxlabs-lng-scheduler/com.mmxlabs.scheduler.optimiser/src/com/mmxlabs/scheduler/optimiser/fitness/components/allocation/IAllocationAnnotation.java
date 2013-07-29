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
	/**
	 * @since 5.0
	 */
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
	 * Returns the quantity of LNG in m3 that has been discharged.
	 * 
	 * @return
	 * @since 6.0
	 */
	long getSlotVolumeInM3(IPortSlot slot);
	
	/**
	 * Returns the time a load or discharge began
	 * 
	 * @return
	 * @since 5.0
	 */
	int getSlotTime(IPortSlot slot);

	/**
	 * Returns the price per M3 for LNG bought or sold at this slot
	 * 
	 * @param slot
	 * @return
	 * @since 5.0
	 */
	int getSlotPricePerM3(IPortSlot slot);
}
