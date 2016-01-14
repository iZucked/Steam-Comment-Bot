/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public class BreakEvenLoadPriceCalculator implements ILoadPriceCalculator, IBreakEvenPriceCalculator {

	private int price;

	@Override
	public void prepareEvaluation(final ISequences sequences) {
		price = 0;
	}

	/**
	 */
	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerM3, final IAllocationAnnotation allocationAnnotation,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return price;
	}

	/**
	 */
	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {
		return price;
	}

	/**
	 */
	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {
		return price;
	}

	/**
	 */
	@Override
	public long[] calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final IAllocationAnnotation allocationAnnotation, final int[] dischargePricesPerMMBTu,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return EMPTY_ADDITIONAL_PNL_RESULT;
	}

	@Override
	public void setPrice(final int newPrice) {
		this.price = newPrice;
	}

	@Override
	public void prepareRealPNL() {

	}

	@Override
	public int getEstimatedPurchasePrice(ILoadOption loadOption, IDischargeOption dischargeOption, int timeInHours) {
		throw new IllegalStateException("BreakEvenLoadPriceCalculator should not use getEstimatedPurchasePrice()");
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(ILoadOption loadOption, IPortTimeWindowsRecord portTimeWindowsRecord) {
		return null;
	}

	@Override
	public int getCalculatorPricingDate(ILoadOption loadOption, IPortTimeWindowsRecord portTimeWindowsRecord) {
		return IPortSlot.NO_PRICING_DATE;
	}
}
