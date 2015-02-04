/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 */
public class BreakEvenSalesPriceCalculator implements ISalesPriceCalculator, IBreakEvenPriceCalculator {

	private int price;

	/**
	 */
	@Override
	public void prepareEvaluation(final ISequences sequences) {
		price = 0;
	}

	@Override
	public int estimateSalesUnitPrice(final IDischargeOption option, IPortTimesRecord voyageRecord, final IDetailTree annotations) {
		return price;
	}

	@Override
	public void setPrice(final int newPrice) {
		this.price = newPrice;
	}

	@Override
	public int calculateSalesUnitPrice(IDischargeOption option, final IAllocationAnnotation allocationAnnotation, IDetailTree annotations) {
		return price;
	}

	@Override
	public void prepareRealPNL() {

	}
}
