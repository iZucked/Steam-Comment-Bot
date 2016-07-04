/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import org.eclipse.jdt.annotation.NonNull;

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

	int getSlotPricePerMMBTu(@NonNull IPortSlot slot);

	/**
	 * Value of the slot, equals to volume in mmbtu * slot price
	 * 
	 * @param portSlot
	 * @return
	 */
	long getSlotValue(@NonNull IPortSlot portSlot);

	long getSlotAdditionalShippingPNL(@NonNull IPortSlot slot);

	long getSlotAdditionalUpsidePNL(@NonNull IPortSlot slot);

	long getSlotAdditionalOtherPNL(@NonNull IPortSlot slot);

	long getSlotUpstreamPNL(@NonNull IPortSlot slot);

	IEntity getSlotEntity(@NonNull IPortSlot slot);
}
