/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public class FixedPriceContract implements ILoadPriceCalculator, ISalesPriceCalculator, ICooldownPriceCalculator {
	private final int pricePerMMBTU;

	public FixedPriceContract(final int pricePerMMBTU) {
		this.pricePerMMBTU = pricePerMMBTU;
	}

	@Override
	public int calculateCooldownUnitPrice(final ILoadSlot option, final int time) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateCooldownUnitPrice(final int time, final IPort port) {
		return pricePerMMBTU;
	}

	@Override
	public int estimateSalesUnitPrice(final IDischargeOption option, final IPortTimesRecord voyageRecord, @Nullable final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateSalesUnitPrice(final IDischargeOption option, final IAllocationAnnotation allocationAnnotation, @Nullable final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public void prepareRealPNL() {

	}

	@Override
	public void prepareEvaluation(final ISequences sequences) {

	}

	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public long calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final IAllocationAnnotation allocationAnnotation, final int[] dischargePricesPerMMBTu,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return 0;
	}

}
