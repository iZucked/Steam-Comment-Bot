/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.google.inject.Inject;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * @since 8.0
 * 
 */
public abstract class SimpleContract implements ILoadPriceCalculator, ISalesPriceCalculator, ICooldownPriceCalculator {

	@Inject(optional = true)
	private IActualsDataProvider actualsDataProvider;

	@Override
	public void prepareEvaluation(final ISequences sequences) {

	}

	/**
	 * @since 2.0
	 */
	protected abstract int calculateSimpleUnitPrice(final int time, final IPort port);

	/**
	 * @since 8.0
	 */
	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadSlot)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadSlot);
		}

		final int loadPricingDate = loadSlot == null ? IPortSlot.NO_PRICING_DATE : loadSlot.getPricingDate();
		final int loadTime = allocationAnnotation.getSlotTime(loadSlot);
		final int pricingDate = (loadPricingDate == IPortSlot.NO_PRICING_DATE ? loadTime : loadPricingDate);
		final IPort port = loadSlot == null ? null : loadSlot.getPort();
		return calculateSimpleUnitPrice(pricingDate, port);
	}

	@Override
	public int estimateSalesUnitPrice(final IDischargeOption option, final int time, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(option)) {
			return actualsDataProvider.getLNGPricePerMMBTu(option);
		}

		final int dischargePricingDate = option == null ? IPortSlot.NO_PRICING_DATE : option.getPricingDate();
		final int pricingDate = (dischargePricingDate == IPortSlot.NO_PRICING_DATE ? time : dischargePricingDate);
		final IPort port = option == null ? null : option.getPort();
		return calculateSimpleUnitPrice(pricingDate, port);
	}

	@Override
	public int calculateSalesUnitPrice(final ILoadOption loadOption, final IDischargeOption option, final int loadTime, final int dischargeTime, final long discahrgeVolumeInMMBTu,
			final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(option)) {
			return actualsDataProvider.getLNGPricePerMMBTu(option);
		}

		final int dischargePricingDate = option == null ? IPortSlot.NO_PRICING_DATE : option.getPricingDate();
		final int pricingDate = (dischargePricingDate == IPortSlot.NO_PRICING_DATE ? dischargeTime : dischargePricingDate);
		final IPort port = option == null ? null : option.getPort();
		return calculateSimpleUnitPrice(pricingDate, port);
	}

	@Override
	public int calculateCooldownUnitPrice(final ILoadSlot slot, final int time) {
		return calculateSimpleUnitPrice(time, slot.getPort());
	}

	/**
	 * @since 2.0
	 */
	@Override
	public int calculateCooldownUnitPrice(final int time, final IPort port) {
		return calculateSimpleUnitPrice(time, port);
	}

	/**
	 * @since 8.0
	 */
	@Override
	public long calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final IAllocationAnnotation allocationAnnotation, final int[] dischargePricesPerMMBTu, final IVessel vessel,
			final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return 0;
	}

	/**
	 * @since 8.0
	 */
	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadOption);
		}
		final int transferTime = allocationAnnotation.getSlotTime(dischargeSlot);
		return calculateSimpleUnitPrice(transferTime, dischargeSlot.getPort());
	}

	/**
	 * @since 8.0
	 */
	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadSlot)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadSlot);
		}

		final int transferTime = allocationAnnotation.getSlotTime(loadSlot);
		return calculateSimpleUnitPrice(transferTime, loadSlot.getPort());
	}

	@Override
	public void prepareRealPNL() {

	}
}
