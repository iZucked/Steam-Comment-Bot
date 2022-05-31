/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IElementAnnotation;
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
@NonNullByDefault
public interface IAllocationAnnotation extends IPortTimesRecord, IElementAnnotation {

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
	long getCommercialSlotVolumeInM3(IPortSlot slot);

	long getPhysicalSlotVolumeInM3(IPortSlot slot);

	long getCommercialSlotVolumeInMMBTu(IPortSlot slot);

	long getPhysicalSlotVolumeInMMBTu(IPortSlot slot);

	/**
	 * Returns the CV valid for this slot. Typically this will be the load CV, but for actualised cargoes discharge CV may be different to load CV
	 * 
	 * @param slot
	 * @return
	 */
	int getSlotCargoCV(IPortSlot slot);
	
	boolean isHeelCarrySource();
	
	boolean isHeelCarrySink();
}
