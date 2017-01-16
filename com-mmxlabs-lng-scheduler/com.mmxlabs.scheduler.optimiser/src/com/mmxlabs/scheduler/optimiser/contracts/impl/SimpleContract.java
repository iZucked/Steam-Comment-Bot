/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public abstract class SimpleContract implements ILoadPriceCalculator, ISalesPriceCalculator {

	@Inject(optional = true)
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private PricingEventHelper pricingEventHelper;

	/**
	 */
	protected abstract int calculateSimpleUnitPrice(final int time, final IPort port);

	/**
	 */
	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, @Nullable VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadSlot)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadSlot);
		}

		final int pricingDate = pricingEventHelper.getLoadPricingDate(loadSlot, dischargeSlot, allocationAnnotation);
		final IPort port = loadSlot.getPort();
		return calculateSimpleUnitPrice(pricingDate, port);
	}

	@Override
	public int estimateSalesUnitPrice(final IDischargeOption dischargeOption, final IPortTimesRecord voyageRecord, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(dischargeOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(dischargeOption);
		}

		final int pricingDate = pricingEventHelper.getDischargePricingDate(dischargeOption, voyageRecord);
		final IPort port = dischargeOption.getPort();
		return calculateSimpleUnitPrice(pricingDate, port);
	}

	@Override
	public int getEstimatedPurchasePrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int timeInHours) {
		if (actualsDataProvider.hasActuals(loadOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadOption);
		} else {
			final IPort port = loadOption.getPort();
			return calculateSimpleUnitPrice(timeInHours, port);
		}
	}

	@Override
	public int getEstimatedSalesPrice(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int timeInHours) {
		if (actualsDataProvider.hasActuals(loadOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadOption);
		} else {
			final IPort port = dischargeOption.getPort();
			return calculateSimpleUnitPrice(timeInHours, port);
		}
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

	/**
	 */
	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			@Nullable VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadOption)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadOption);
		}
		final int pricingDate = pricingEventHelper.getLoadPricingDate(loadOption, dischargeSlot, allocationAnnotation);
		return calculateSimpleUnitPrice(pricingDate, dischargeSlot.getPort());
	}

	/**
	 */
	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			@Nullable VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {

		if (actualsDataProvider != null && actualsDataProvider.hasActuals(loadSlot)) {
			return actualsDataProvider.getLNGPricePerMMBTu(loadSlot);
		}

		final int pricingDate = pricingEventHelper.getLoadPricingDate(loadSlot, dischargeOption, allocationAnnotation);
		return calculateSimpleUnitPrice(pricingDate, loadSlot.getPort());
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(final ILoadOption loadOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		if (actualsDataProvider.hasActuals(loadOption)) {
			return PricingEventType.DATE_SPECIFIED;
		}
		return null;
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		if (actualsDataProvider.hasActuals(dischargeOption)) {
			return PricingEventType.DATE_SPECIFIED;
		}
		return null;
	}

	@Override
	public int getCalculatorPricingDate(final ILoadOption loadOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		if (actualsDataProvider.hasActuals(loadOption)) {
			return actualsDataProvider.getArrivalTime(loadOption);
		} else {
			return IPortSlot.NO_PRICING_DATE;
		}
	}

	@Override
	public int getCalculatorPricingDate(final IDischargeOption dischargeOption, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		if (actualsDataProvider.hasActuals(dischargeOption)) {
			return actualsDataProvider.getArrivalTime(dischargeOption);
		} else {
			return IPortSlot.NO_PRICING_DATE;
		}
	}
}
