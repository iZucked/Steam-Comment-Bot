/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

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
public class ProfitSharingContract implements ILoadPriceCalculator {
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

	public int calculateLoadUnitPrice(final int loadTime,
			final long loadVolume, final int dischargeTime,
			final int actualSalesPrice, final int cvValue,
			final VoyageDetails ladenLeg, final VoyageDetails ballastLeg,
			final IVesselClass vesselClass) {

		final int marketPurchasePrice = (int) market.getValueAtPoint(loadTime);

		final int referenceSalesPrice = (int) referenceMarket
				.getValueAtPoint(dischargeTime);
		return (int) (marketPurchasePrice - alphaScaled
				- Calculator.multiply(betaScaled, actualSalesPrice) - Calculator
				.multiply(gammaScaled, actualSalesPrice - referenceSalesPrice));
	}
}
