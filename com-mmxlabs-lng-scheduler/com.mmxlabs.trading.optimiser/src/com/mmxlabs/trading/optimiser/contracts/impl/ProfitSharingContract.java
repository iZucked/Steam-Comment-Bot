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

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateLoadUnitPrice(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int actualSalesPricePerMMBTu,
			final long loadVolumeInM3, final long dischargeVolumeInM3, final IVessel vessel, final VoyagePlan plan, final IDetailTree annotation) {
		return calculateLoadUnitPrice(loadSlot, dischargeSlot, loadTime, actualSalesPricePerMMBTu, loadVolumeInM3, annotation);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateLoadUnitPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int transferTime, final int actualSalesPricePerMMBTu, long transferVolumeInM3,
			final IDetailTree annotations) {
		final int marketPurchasePricePerMMBTu = market.getValueAtPoint(transferTime);

		final int actualSalesPriceFractionPerMMBTu = Calculator.getShareOfPrice(salesMultiplier, actualSalesPricePerMMBTu);
		final int basePricePerMMBTu = marketPurchasePricePerMMBTu - marginScaled - actualSalesPriceFractionPerMMBTu;
		if (baseMarketPorts.contains(dischargeOption.getPort())) {
			return basePricePerMMBTu;
		} else {
			// Profit Share
			final int referenceSalesPricePerMMBtu = referenceMarket.getValueAtPoint(transferTime);
			return (basePricePerMMBTu - Calculator.getShareOfPrice(profitShareScaled, actualSalesPricePerMMBTu - referenceSalesPricePerMMBtu));
		}
	}
}
