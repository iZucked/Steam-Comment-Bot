/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import java.util.Set;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Computes the price for a profit-sharing contract; this is
 * 
 * market purchase price - marginScaled - salesMultiplierScaled * (actual sales price) - profitShareScaled * (actual sales price - reference sales price)
 * 
 * TODO this may be incorrect as it's per unit and maybe ought not to be.
 * 
 * @author hinton
 * 
 */
public class ProfitSharingContract implements ILoadPriceCalculator2 {
	private final ICurve market;
	private final ICurve referenceMarket;
	private final int marginScaled;
	private final int profitShareScaled;
	private final Set<IPort> baseMarketPorts;

	public ProfitSharingContract(final ICurve market, final ICurve referenceMarket, final int marginScaled, final int profitShareScaled, final Set<IPort> baseMarketPorts) {
		super();
		this.market = market;
		this.referenceMarket = referenceMarket;
		this.marginScaled = marginScaled;
		this.profitShareScaled = profitShareScaled;
		this.baseMarketPorts = baseMarketPorts;
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

		int basePrice = marketPurchasePrice - marginScaled - actualSalesPrice;
		if (baseMarketPorts.contains(dischargeOption.getPort())) {
			return basePrice;
		} else {
			// Profit Share
			final int referenceSalesPrice = (int) referenceMarket.getValueAtPoint(dischargeTime);
			return (int) (basePrice - Calculator.multiply(profitShareScaled, actualSalesPrice - referenceSalesPrice));
		}
	}
}
