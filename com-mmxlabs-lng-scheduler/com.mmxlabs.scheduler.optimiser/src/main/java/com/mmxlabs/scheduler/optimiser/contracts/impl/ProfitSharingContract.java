/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Computes the price for a profit-sharing contract; this is
 * 
 * market purchase price - alpha - beta * (actual sales price) - gamma * (actual sales price - reference sales price)
 * 
 * TODO this may be incorrect as it's per unit and maybe ought not to be.
 * 
 * @author hinton
 * 
 */
public class ProfitSharingContract implements ILoadPriceCalculator2 {
	private final ICurve market;
	private final ICurve referenceMarket;
	private final int alphaScaled;
	private final int betaScaled;
	private final int gammaScaled;

	public ProfitSharingContract(final ICurve market, final ICurve referenceMarket, final int alpha, final int betaScaled, final int gammaScaled) {
		super();
		this.market = market;
		this.referenceMarket = referenceMarket;
		this.alphaScaled = alpha;
		this.betaScaled = betaScaled;
		this.gammaScaled = gammaScaled;
	}

	@Override
	public void prepareEvaluation(final ScheduledSequences sequences) {
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int actualSalesPrice, final int loadVolume,
			final IVessel vessel, final VoyagePlan plan) {
		return calculateLoadUnitPrice(loadSlot, dischargeSlot, loadTime, dischargeTime, actualSalesPrice);
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargeTime, final int actualSalesPrice) {
		final int marketPurchasePrice = (int) market.getValueAtPoint(loadTime);

		final int referenceSalesPrice = (int) referenceMarket.getValueAtPoint(dischargeTime);
		return (int) (marketPurchasePrice - alphaScaled - Calculator.multiply(betaScaled, actualSalesPrice) - Calculator.multiply(gammaScaled, actualSalesPrice - referenceSalesPrice));
	}
}
