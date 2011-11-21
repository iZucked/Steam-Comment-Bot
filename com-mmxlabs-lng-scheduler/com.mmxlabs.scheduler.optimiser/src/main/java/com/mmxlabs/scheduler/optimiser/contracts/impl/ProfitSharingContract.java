/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Computes the price for a profit-sharing contract; this is
 * 
 * market purchase price - alpha - beta * (actual sales price) - gamma * (actual
 * sales price - reference sales price)
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

	public ProfitSharingContract(ICurve market, ICurve referenceMarket,
			int alpha, int betaScaled, int gammaScaled) {
		super();
		this.market = market;
		this.referenceMarket = referenceMarket;
		this.alphaScaled = alpha;
		this.betaScaled = betaScaled;
		this.gammaScaled = gammaScaled;
	}

	@Override
	public void prepareEvaluation(ScheduledSequences sequences) {
	}


	@Override
	public int calculateLoadUnitPrice(ILoadSlot loadSlot, IDischargeSlot dischargeSlot, int loadTime, int dischargeTime, int actualSalesPrice, int loadVolume, IVesselClass vesselClass, VoyagePlan plan) {
		final int marketPurchasePrice = (int) market.getValueAtPoint(loadTime);

		final int referenceSalesPrice = (int) referenceMarket.getValueAtPoint(dischargeTime);
		return (int) (marketPurchasePrice - alphaScaled - Calculator.multiply(betaScaled, actualSalesPrice) - Calculator.multiply(gammaScaled, actualSalesPrice - referenceSalesPrice));
	}
}
