/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * An annotation which is appended to both {@link ILoadOption} and {@link IDischargeOption} indicating decisions made by the {@link IVolumeAllocator}
 * 
 * @author hinton
 * 
 */
public interface IAllocationAnnotation extends IPortTimesRecord {

	@Override
	@NonNull
	List<@NonNull IPortSlot> getSlots();

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
	 * Returns the quantity of LNG in m3 that has been purchased or sold.
	 * 
	 * @return
	 */
	long getCommercialSlotVolumeInM3(@NonNull IPortSlot slot);
	long getPhysicalSlotVolumeInM3(@NonNull IPortSlot slot);

	/**
	 * Returns the time a load or discharge began
	 * 
	 * @return
	 */
	@Override
	int getSlotTime(@NonNull IPortSlot slot);

	@Override
	int getSlotDuration(@NonNull IPortSlot slot);

	long getCommercialSlotVolumeInMMBTu(@NonNull IPortSlot slot);
	long getPhysicalSlotVolumeInMMBTu(@NonNull IPortSlot slot);

	/**
	 * Returns the CV valid for this slot. Typically this will be the load CV, but for actualised cargoes discharge CV may be different to load CV
	 * 
	 * @param slot
	 * @return
	 */
	int getSlotCargoCV(@NonNull IPortSlot slot);
	
	
}
