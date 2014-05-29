/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

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
	public int calculateCooldownUnitPrice(ILoadSlot option, int time) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateCooldownUnitPrice(int time, IPort port) {
		return pricePerMMBTU;
	}

	@Override
	public int estimateSalesUnitPrice(IDischargeOption option, int time, IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateSalesUnitPrice(ILoadOption loadOption, IDischargeOption option, int loadTime, int dischargeTime, long dischargeVolumeInMMBTu, IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public void prepareRealPNL() {

	}

	@Override
	public void prepareEvaluation(ISequences sequences) {

	}

	@Override
	public int calculateFOBPricePerMMBTu(ILoadSlot loadSlot, IDischargeSlot dischargeSlot, int dischargePricePerMMBTu, IAllocationAnnotation allocationAnnotation, IVessel vessel, int vesselStartTime,
			VoyagePlan plan, IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculateDESPurchasePricePerMMBTu(ILoadOption loadOption, IDischargeSlot dischargeSlot, int dischargePricePerMMBTu, IAllocationAnnotation allocationAnnotation, IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public int calculatePriceForFOBSalePerMMBTu(ILoadSlot loadSlot, IDischargeOption dischargeOption, int dischargePricePerMMBTu, IAllocationAnnotation allocationAnnotation, IDetailTree annotations) {
		return pricePerMMBTU;
	}

	@Override
	public long calculateAdditionalProfitAndLoss(ILoadOption loadOption, IAllocationAnnotation allocationAnnotation, int[] dischargePricesPerMMBTu, IVessel vessel, int vesselStartTime,
			VoyagePlan plan, IDetailTree annotations) {
		return 0;
	}

}
