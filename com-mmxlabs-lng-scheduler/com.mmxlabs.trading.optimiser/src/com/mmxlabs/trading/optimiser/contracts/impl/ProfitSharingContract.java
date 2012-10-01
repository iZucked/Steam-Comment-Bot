/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.contracts.impl;

import java.util.Set;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
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
public class ProfitSharingContract implements ILoadPriceCalculator {
	private final ICurve market;
	private final ICurve referenceMarket;
	private final int marginScaled;
	private final int profitShareScaled;
	private final int salesMultiplier;
	private final Set<IPort> baseMarketPorts;

	/**
	 * @since 2.0
	 */
	public ProfitSharingContract(final ICurve market, final ICurve referenceMarket, final int marginScaled, final int profitShareScaled, final Set<IPort> baseMarketPorts, final int salesMultipler) {
		super();
		this.market = market;
		this.referenceMarket = referenceMarket;
		this.marginScaled = marginScaled;
		this.profitShareScaled = profitShareScaled;
		this.baseMarketPorts = baseMarketPorts;
		this.salesMultiplier = salesMultipler;
	}

	@Override
	public void prepareEvaluation(final ScheduledSequences sequences) {
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int actualSalesPrice, final int loadVolume,
			final IVessel vessel, final VoyagePlan plan, final IDetailTree annotation) {
		return calculateLoadUnitPrice(loadSlot, dischargeSlot, loadTime, dischargeTime, actualSalesPrice, annotation);
	}

	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargeTime, final int actualSalesPrice,
			final IDetailTree annotations) {
		final int marketPurchasePrice = (int) market.getValueAtPoint(loadTime);

		final int actualSalesPriceFraction = (int) Calculator.multiply(actualSalesPrice, salesMultiplier);
		final int basePrice = marketPurchasePrice - marginScaled - actualSalesPriceFraction;
		if (baseMarketPorts.contains(dischargeOption.getPort())) {
			return basePrice;
		} else {
			// Profit Share
			final int referenceSalesPrice = (int) referenceMarket.getValueAtPoint(dischargeTime);
			return (int) (basePrice - Calculator.multiply(profitShareScaled, actualSalesPrice - referenceSalesPrice));
		}
	}
}
