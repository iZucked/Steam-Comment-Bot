/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public class FixedPriceContract implements ILoadPriceCalculator, ISalesPriceCalculator, IPriceIntervalProvider {
	private final int pricePerMMBTU;

	public FixedPriceContract(final int pricePerMMBTU) {
		this.pricePerMMBTU = pricePerMMBTU;
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
	public long[] calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final IAllocationAnnotation allocationAnnotation, final int[] dischargePricesPerMMBTu,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		return EMPTY_ADDITIONAL_PNL_RESULT;
	}

	@Override
	public List<int[]> getPriceIntervals(IPortSlot slot, int startOfRange, int endOfRange, IPortTimeWindowsRecord portTimeWindowRecord) {
		List<int[]> fixedPriceIntervals = new LinkedList<int[]>();
		fixedPriceIntervals.add(new int[] {startOfRange, pricePerMMBTU});
		fixedPriceIntervals.add(new int[] {endOfRange, Integer.MIN_VALUE});
		return fixedPriceIntervals;
	}

	@Override
	public Pair<Integer, Integer> getHighestPriceInterval(int startOfRange, int endOfRange, ILoadOption loadOption, IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowRecord) {
		return new Pair<Integer, Integer>(startOfRange, endOfRange);
	}

	@Override
	public Pair<Integer, Integer> getLowestPriceInterval(int startOfRange, int endOfRange, ILoadOption loadOption, IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowRecord) {
		return new Pair<Integer, Integer>(startOfRange, endOfRange);
	}

	@Override
	public List<Integer> getPriceHourIntervals(IPortSlot slot, ILoadOption loadOption, IDischargeOption dischargeOption, int start, int end, IPortTimeWindowsRecord portTimeWindowsRecord) {
		List<Integer> priceIntervals = getFixedStartEndIntervals(start, end);
		return priceIntervals;
	}

	private List<Integer> getFixedStartEndIntervals(int start, int end) {
		List<Integer> priceIntervals = new LinkedList<Integer>();
		priceIntervals.add(start);
		priceIntervals.add(end);
		return priceIntervals;
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(ILoadOption loadOption, IDischargeOption dischargeOption) {
		return null;
	}

	@Override
	public int getEstimatedSalesPrice(IDischargeOption sell, int timeInHours) {
		return pricePerMMBTU;
	}

	@Override
	public int getEstimatedPurchasePrice(ILoadOption loadOption, IDischargeOption dischargeOption, int timeInHours) {
		return pricePerMMBTU;
	}

}
