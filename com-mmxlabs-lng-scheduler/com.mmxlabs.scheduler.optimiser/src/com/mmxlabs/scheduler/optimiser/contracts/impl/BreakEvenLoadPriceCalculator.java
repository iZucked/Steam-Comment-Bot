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
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public class BreakEvenLoadPriceCalculator implements ILoadPriceCalculator, IBreakEvenPriceCalculator {

	private int price;

	@Override
	public void prepareEvaluation(final ISequences sequences) {
		price = 0;
	}

	/**
	 */
	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerM3, final IAllocationAnnotation allocationAnnotation,
			final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return price;
	}

	/**
	 */
	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {
		return price;
	}

	/**
	 */
	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {
		return price;
	}

	/**
	 */
	@Override
	public long calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final IAllocationAnnotation allocationAnnotation, final int[] dischargePricesPerMMBTu, final IVessel vessel,
			final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return 0;
	}

	@Override
	public void setPrice(final int newPrice) {
		this.price = newPrice;
	}

	@Override
	public void prepareRealPNL() {

	}
}
