/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 */
public class PriceExpressionContract extends SimpleContract implements IPriceIntervalProvider {

	protected final @NonNull ICurve expressionCurve;
	protected final @NonNull IIntegerIntervalCurve priceChangeIntervalsInHours;

	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderUtil;

	public PriceExpressionContract(final @NonNull ICurve expressionCurve, final @NonNull IIntegerIntervalCurve priceChangeIntervalsInHours) {
		this.expressionCurve = expressionCurve;
		this.priceChangeIntervalsInHours = priceChangeIntervalsInHours;
	}

	@Override
	protected int calculateSimpleUnitPrice(final int time, final IPort port) {
		return expressionCurve.getValueAtPoint(time);
	}

	@Override
	public List<int @NonNull []> getPriceIntervals(@NonNull final IPortSlot slot, final int startOfRange, final int endOfRange, final IPortTimeWindowsRecord portTimeWindowRecord) {
		if (slot instanceof final ILoadOption option) {
			return priceIntervalProviderUtil.getPriceIntervalsList(option, priceChangeIntervalsInHours, startOfRange, endOfRange, portTimeWindowRecord);
		} else if (slot instanceof final IDischargeOption option) {
			return priceIntervalProviderUtil.getPriceIntervalsList(option, priceChangeIntervalsInHours, startOfRange, endOfRange, portTimeWindowRecord);
		} else {
			throw new IllegalStateException("getPriceIntervals() requires either an ILoadOption or IDischargeOption");
		}
	}

	@Override
	public @Nullable List<@NonNull Integer> getPriceHourIntervals(final IPortSlot slot, final int start, final int end, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		final int[] intervals = priceChangeIntervalsInHours.getIntervalsAs1dArray(start, end);
		if (slot instanceof ILoadOption) {
			return priceIntervalProviderUtil.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else if (slot instanceof IDischargeOption) {
			return priceIntervalProviderUtil.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else {
			return null;
		}
	}

	public ICurve getExpressionCurve() {
		return this.expressionCurve;
	}
}
