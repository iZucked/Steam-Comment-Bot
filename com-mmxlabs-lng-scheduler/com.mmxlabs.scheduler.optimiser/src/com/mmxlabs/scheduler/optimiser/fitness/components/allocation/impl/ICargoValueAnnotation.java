/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IElementAnnotation;
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
@NonNullByDefault
public interface ICargoValueAnnotation extends IAllocationAnnotation, IElementAnnotation {

	int getSlotPricePerMMBTu(IPortSlot slot);

	/**
	 * Value of the slot, equals to volume in mmbtu * slot price
	 * 
	 * @param portSlot
	 * @return
	 */
	long getSlotValue(IPortSlot portSlot);

	long getSlotAdditionalShippingPNL(IPortSlot slot);

	long getSlotAdditionalUpsidePNL(IPortSlot slot);

	long getSlotAdditionalOtherPNL(IPortSlot slot);

	long getSlotUpstreamPNL(IPortSlot slot);

	@Nullable
	IEntity getSlotEntity(IPortSlot slot);

	long getTotalProfitAndLoss();
}
