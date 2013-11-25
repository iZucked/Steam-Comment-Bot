package com.mmxlabs.models.lng.transformer.extensions.redirection;

import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.BaseVolumeAllocator.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICustomVolumeAllocator;

public class RedirectionVolumeAllocator implements ICustomVolumeAllocator {

	@Override
	public boolean canHandle(final AllocationRecord constraint) {

		if (constraint.slots.length == 2) {
			if (constraint.slots[0] instanceof ILoadOption) {
				final ILoadOption iLoadOption = (ILoadOption) constraint.slots[0];
				if (iLoadOption instanceof ILoadSlot) {
					// Only handle DES Purchases
					return false;
				}
				if (iLoadOption.getLoadPriceCalculator() instanceof IRedirectionVolumeCalculator) {
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public void handle(final AllocationRecord constraint) {

		if (constraint.slots.length == 2) {
			if (constraint.slots[0] instanceof ILoadOption) {
				final ILoadOption iLoadOption = (ILoadOption) constraint.slots[0];
				if (iLoadOption instanceof ILoadSlot) {
					// Only handle DES Purchases
					return;
				}
				// Instance of
				if (iLoadOption.getLoadPriceCalculator() instanceof IRedirectionVolumeCalculator) {
					final IRedirectionVolumeCalculator calc = (IRedirectionVolumeCalculator) iLoadOption.getLoadPriceCalculator();
					calc.calculateVolumes(constraint);
				}
			}
		}
	}
}
