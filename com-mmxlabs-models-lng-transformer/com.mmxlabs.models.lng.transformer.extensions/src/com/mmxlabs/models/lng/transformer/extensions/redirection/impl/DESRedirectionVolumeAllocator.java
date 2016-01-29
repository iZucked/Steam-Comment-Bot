/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import com.mmxlabs.models.lng.transformer.extensions.redirection.IRedirectionVolumeCalculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICustomVolumeAllocator;

/**
 * Implementation of {@link ICustomVolumeAllocator} which delegates to contract implementations which implement {@link IRedirectionVolumeCalculator} *AND* when the load is a DES Purchase
 * 
 * @author Simon Goodall
 * 
 */
public class DESRedirectionVolumeAllocator implements ICustomVolumeAllocator {

	@Override
	public void modifyAllocationRecord(final AllocationRecord constraint) {

		if (constraint.slots.get(0) instanceof ILoadOption) {
			final ILoadOption loadOption = (ILoadOption) constraint.slots.get(0);
			if (loadOption instanceof ILoadSlot) {
				// Only handle DES Purchases
				return;
			}
			if (loadOption.getLoadPriceCalculator() instanceof IRedirectionVolumeCalculator) {
				final IRedirectionVolumeCalculator calc = (IRedirectionVolumeCalculator) loadOption.getLoadPriceCalculator();
				calc.modifyAllocationRecord(constraint);
			}
		}
	}
}
