/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

//	@Override
//	public boolean canHandle(final AllocationRecord constraint) {
//
////		if (constraint.slots.size() == 2) {
////			if (constraint.slots.get(0) instanceof ILoadOption) {
////				final ILoadOption iLoadOption = (ILoadOption) constraint.slots.get(0);
////				if (iLoadOption instanceof ILoadSlot) {
////					// Only handle DES Purchases
////					return false;
////				}
////				if (iLoadOption.getLoadPriceCalculator() instanceof IRedirectionVolumeCalculator) {
////					return true;
////				}
////			}
////
////		}
//
//		return true;
//	}

	@Override
	public void modifyAllocationRecord(final AllocationRecord constraint) {

//		if (constraint.slots.size() == 2) {
			if (constraint.slots.get(0) instanceof ILoadOption) {
				final ILoadOption iLoadOption = (ILoadOption) constraint.slots.get(0);
//				if (iLoadOption instanceof ILoadSlot) {
//					// Only handle DES Purchases
//					return;
//				}
				// Instance of
				if (iLoadOption.getLoadPriceCalculator() instanceof IRedirectionVolumeCalculator) {
					final IRedirectionVolumeCalculator calc = (IRedirectionVolumeCalculator) iLoadOption.getLoadPriceCalculator();
					calc.modifyAllocationRecord(constraint);
				}
			}
//		}
	}
}
