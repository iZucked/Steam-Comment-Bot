/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 2.0
 */
public class BreakEvenLoadPriceCalculator implements ILoadPriceCalculator, IBreakEvenPriceCalculator {

	private int price;

	@Override
	public void prepareEvaluation(final ISequences sequences) {
		price = 0;
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int dischargePricePerM3,
			final long loadVolumeInM3, final long dischargeVolumeInM3, final IVessel vessel, final VoyagePlan plan, final IDetailTree annotations) {
		return price;
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int transferTime, final int salesPricePerM3, final long transferVolumeInM3,
			final IDetailTree annotations) {
		return price;
	}

	@Override
	public void setPrice(final int newPrice) {
		this.price = newPrice;
	}
}
