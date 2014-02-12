/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * @since 2.0
 */
public class BreakEvenSalesPriceCalculator implements ISalesPriceCalculator, IBreakEvenPriceCalculator {

	private int price;

	/**
	 * @since 8.0
	 */
	@Override
	public void prepareEvaluation(final ISequences sequences) {
		price = 0;
	}

	@Override
	public int estimateSalesUnitPrice(final IDischargeOption option, final int time, final IDetailTree annotations) {
		return price;
	}

	@Override
	public void setPrice(final int newPrice) {
		this.price = newPrice;
	}

	@Override
	public int calculateSalesUnitPrice(ILoadOption loadOption, IDischargeOption option, int loadTime, int dischargeTime, long dischargeVolumeInMMBTu, IDetailTree annotations) {
		return price;
	}

	@Override
	public void prepareRealPNL() {

	}
}
