package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

/**
 * An extended interface to {@link IAllocationAnnotation} adding in extra information regarding P&L details for a set of slots. It is expected a {@link IEntityValueCalculator} will populate instances
 * of these objects.
 * 
 * @author Simon Goodall.
 * 
 */
public interface ICargoValueAnnotation extends IAllocationAnnotation {

	int getSlotPricePerMMBTu(IPortSlot slot);

	/**
	 * Value of the slot, equals to volume in mmbtu * slot price
	 * 
	 * @param portSlot
	 * @return
	 */
	long getSlotValue(IPortSlot portSlot);

	long getSlotAdditionalPNL(IPortSlot slot);

	IEntity getSlotEntity(IPortSlot slot);

}
