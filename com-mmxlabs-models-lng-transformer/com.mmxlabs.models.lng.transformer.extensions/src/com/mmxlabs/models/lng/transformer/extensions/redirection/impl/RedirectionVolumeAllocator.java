/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import com.mmxlabs.models.lng.transformer.extensions.redirection.IRedirectionVolumeCalculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICustomVolumeAllocator;

/**
 * Implementation of {@link ICustomVolumeAllocator} which delegates to contract implementations which implement {@link IRedirectionVolumeCalculator} for both FOB and DES purchases
 * 
 * @author Simon Goodall
 * 
 */
public class RedirectionVolumeAllocator implements ICustomVolumeAllocator {

	@Override
	public void modifyAllocationRecord(final AllocationRecord constraint) {

		if (constraint.slots.get(0) instanceof ILoadOption) {
			final ILoadOption iLoadOption = (ILoadOption) constraint.slots.get(0);
			if (iLoadOption.getLoadPriceCalculator() instanceof IRedirectionVolumeCalculator) {
				final IRedirectionVolumeCalculator calc = (IRedirectionVolumeCalculator) iLoadOption.getLoadPriceCalculator();
				calc.modifyAllocationRecord(constraint);
			}
		}
	}
}
