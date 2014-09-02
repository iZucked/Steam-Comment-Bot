/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public abstract class SimpleContract implements ILoadPriceCalculator, ISalesPriceCalculator, ICooldownPriceCalculator {

	@Inject(optional = true)
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private PricingEventHelper pricingEventHelper;

	@Override
	public void prepareEvaluation(final ISequences sequences) {

	}

	/**
	 */
	protected abstract int calculateSimpleUnitPrice(final int time, final IPort port);

	/**
	 */
	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadSlot)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadSlot);
		}

		final int pricingDate = pricingEventHelper.getLoadPricingDate(loadSlot, dischargeSlot, allocationAnnotation);
		final IPort port = loadSlot.getPort();
		return calculateSimpleUnitPrice(pricingDate, port);
	}

	@Override
	public int estimateSalesUnitPrice(final IDischargeOption dischargeOption, IPortTimesRecord voyageRecord, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(dischargeOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(dischargeOption);
		}

		final int pricingDate = pricingEventHelper.getDischargePricingDate(dischargeOption, voyageRecord);
		final IPort port = dischargeOption.getPort();
		return calculateSimpleUnitPrice(pricingDate, port);
	}

	@Override
	public int calculateSalesUnitPrice(final IDischargeOption dischargeOption, final IAllocationAnnotation allocationAnnotation, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(dischargeOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(dischargeOption);
		}

		final int pricingDate = pricingEventHelper.getDischargePricingDate(dischargeOption, allocationAnnotation);
		final IPort port = dischargeOption.getPort();
		return calculateSimpleUnitPrice(pricingDate, port);
	}

	@Override
	public int calculateCooldownUnitPrice(final ILoadSlot slot, final int time) {
		return calculateSimpleUnitPrice(time, slot.getPort());
	}

	/**
	 */
	@Override
	public int calculateCooldownUnitPrice(final int time, final IPort port) {
		return calculateSimpleUnitPrice(time, port);
	}

	/**
	 */
	@Override
	public long calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final IAllocationAnnotation allocationAnnotation, final int[] dischargePricesPerMMBTu, final IVessel vessel,
			final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return 0;
	}

	/**
	 */
	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadOption);
		}
		final int pricingDate = pricingEventHelper.getDischargePricingDate(dischargeSlot, allocationAnnotation);
		return calculateSimpleUnitPrice(pricingDate, dischargeSlot.getPort());
	}

	/**
	 */
	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadSlot)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadSlot);
		}

		final int pricingDate = pricingEventHelper.getLoadPricingDate(loadSlot, dischargeOption, allocationAnnotation);
		return calculateSimpleUnitPrice(pricingDate, loadSlot.getPort());
	}

	@Override
	public void prepareRealPNL() {

	}
}
