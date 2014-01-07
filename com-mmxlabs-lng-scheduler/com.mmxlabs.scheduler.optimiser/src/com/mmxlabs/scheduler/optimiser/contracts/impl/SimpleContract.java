/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.List;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * @since 8.0
 * 
 */
public abstract class SimpleContract implements ILoadPriceCalculator, ISalesPriceCalculator, ICooldownPriceCalculator {

	/**
	 * @since 8.0
	 */
	@Override
	public void prepareEvaluation(final ISequences sequences, final ScheduledSequences scheduledSequences) {

	}

	/**
	 * @since 2.0
	 */
	protected abstract int calculateSimpleUnitPrice(final int loadTime);

	/**
	 * @since 8.0
	 */
	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot,  IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int dischargePricePerMMBTu,
			final long loadVolumeInM3, final long dischargeVolumeInM3, final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		final int loadPricingDate = loadSlot == null ? IPortSlot.NO_PRICING_DATE : loadSlot.getPricingDate();
		final int pricingDate = (loadPricingDate == IPortSlot.NO_PRICING_DATE ? loadTime : loadPricingDate);
		return calculateSimpleUnitPrice(pricingDate);
	}

	@Override
	public int calculateSalesUnitPrice(final IDischargeOption option, final int time) {
		final int dischargePricingDate = option == null ? IPortSlot.NO_PRICING_DATE : option.getPricingDate();
		final int pricingDate = (dischargePricingDate == IPortSlot.NO_PRICING_DATE ? time : dischargePricingDate);
		return calculateSimpleUnitPrice(pricingDate);
	}

	@Override
	public int calculateCooldownUnitPrice(final ILoadSlot slot, final int time) {
		return calculateSimpleUnitPrice(time);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateCooldownUnitPrice(final int time) {
		return calculateSimpleUnitPrice(time);
	}

	/**
	 * @since 8.0
	 */
	@Override
	public long calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final List<IPortSlot> slots, final int[] arrivalTimes, final long[] volumes, final int[] dischargePricesPerMMBTu,
			final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return 0;
	}

	/**
	 * @since 8.0
	 */
	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int transferTime, final int dischargePricePerMMBTu,
			final long transferVolumeInM3, final IDetailTree annotations) {
		return calculateSimpleUnitPrice(transferTime);
	}

	/**
	 * @since 8.0
	 */
	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int transferTime, final int dischargePricePerMMBTu,
			final long transferVolumeInM3, final IDetailTree annotations) {
		return calculateSimpleUnitPrice(transferTime);
	}
}
