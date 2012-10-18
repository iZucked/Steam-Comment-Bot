package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * @since 2.0
 */
public class BreakEvenSalesPriceCalculator implements ISalesPriceCalculator, IBreakEvenPriceCalculator {

	private int price;

	@Override
	public void prepareEvaluation(ISequences sequences) {
		price = 0;
	}

	@Override
	public int calculateSalesUnitPrice(IDischargeOption option, int time) {
		return price;
	}

	@Override
	public void setPrice(int newPrice) {
		// TODO Auto-generated method stub
		this.price = newPrice;
	}
}
