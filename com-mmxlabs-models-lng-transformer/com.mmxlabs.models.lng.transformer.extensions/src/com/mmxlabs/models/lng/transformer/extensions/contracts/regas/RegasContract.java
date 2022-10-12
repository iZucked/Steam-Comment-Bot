/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.regas;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class RegasContract implements ISalesPriceCalculator, IPriceIntervalProvider {

	public static final String ANNOTATION_KEY = "regas.annotation";

	private final PriceExpressionContract basePriceDelegate;
	private final int numberOfPricingDays;

	public RegasContract(final PriceExpressionContract basePriceDelegate, final int numberOfPricingDays) {
		this.basePriceDelegate = basePriceDelegate;
		this.numberOfPricingDays = numberOfPricingDays;
	}

	@Override
	public @NonNull List<int @NonNull []> getPriceIntervals(@NonNull IPortSlot slot, int startOfRange, int endOfRange, @NonNull IPortTimeWindowsRecord portTimeWindowRecord) {
		return basePriceDelegate.getPriceIntervals(slot, startOfRange, endOfRange, portTimeWindowRecord);
	}

	@Override
	public @Nullable List<@NonNull Integer> getPriceHourIntervals(@NonNull IPortSlot slot, int start, int end, @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		return basePriceDelegate.getPriceHourIntervals(slot, start, end, portTimeWindowsRecord);
	}

	@Override
	public int estimateSalesUnitPrice(IDischargeOption option, IPortTimesRecord voyageRecord, @Nullable IDetailTree annotations) {
		return 0;
	}

	@Override
	public int calculateSalesUnitPrice(IDischargeOption option, IAllocationAnnotation allocationAnnotation, @Nullable IDetailTree annotations) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowsRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEstimatedSalesPrice(ILoadOption loadOption, IDischargeOption dischargeOption, int timeInHours) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCalculatorPricingDate(IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowsRecord) {
		// TODO Auto-generated method stub
		return 0;
	}
}
