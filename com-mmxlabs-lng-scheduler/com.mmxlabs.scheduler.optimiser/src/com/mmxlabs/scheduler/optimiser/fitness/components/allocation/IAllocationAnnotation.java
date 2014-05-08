/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.List;

import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * An annotation which is appended to both {@link ILoadOption} and {@link IDischargeOption} indicating decisions made by the {@link IVolumeAllocator}
 * 
 * @author hinton
 * 
 */
public interface IAllocationAnnotation extends IElementAnnotation {
	List<IPortSlot> getSlots();

	/**
	 * Returns the total LNG in m3 used as fuel during this cargo.
	 * 
	 * @return
	 */
	long getFuelVolumeInM3();

	long getStartHeelVolumeInM3();

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
	long getSlotVolumeInM3(IPortSlot slot);

	/**
	 * Returns the time a load or discharge began
	 * 
	 * @return
	 */
	int getSlotTime(IPortSlot slot);

	int getSlotPricePerMMBTu(IPortSlot slot);

	long getSlotVolumeInMMBTu(IPortSlot slot);

	/**
	 * Returns the CV valid for this slot. Typically this will be the load CV, but for actualised cargoes discharge CV may be different to load CV
	 * 
	 * @param slot
	 * @return
	 */
	int getSlotCargoCV(IPortSlot slot);
}
