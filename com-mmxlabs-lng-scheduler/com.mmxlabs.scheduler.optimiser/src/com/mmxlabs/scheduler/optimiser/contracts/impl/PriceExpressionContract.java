/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.List;

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

	private final ICurve expressionCurve;
	private final IIntegerIntervalCurve priceChangeIntervalsInHours;
	
	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderUtil;

	public PriceExpressionContract(final ICurve expressionCurve, final IIntegerIntervalCurve priceChangeIntervalsInHours) {
		this.expressionCurve = expressionCurve;
		this.priceChangeIntervalsInHours = priceChangeIntervalsInHours;
	}

	@Override
	protected int calculateSimpleUnitPrice(final int time, final IPort port) {
		return expressionCurve.getValueAtPoint(time);
	}

	@Override
	public List<int[]> getPriceIntervals(IPortSlot slot, int startOfRange, int endOfRange, IPortTimeWindowsRecord portTimeWindowRecord) {
		if (slot instanceof ILoadOption) {
			return priceIntervalProviderUtil.getPriceIntervalsList((ILoadOption) slot, priceChangeIntervalsInHours, expressionCurve, startOfRange, endOfRange, 0, portTimeWindowRecord);
		} else if (slot instanceof IDischargeOption) {
			return priceIntervalProviderUtil.getPriceIntervalsList((IDischargeOption) slot, priceChangeIntervalsInHours, expressionCurve, startOfRange, endOfRange, 0, portTimeWindowRecord);
		} else {
			throw new IllegalStateException("getPriceIntervals() requires either an ILoadOption or IDischargeOption");
		}
	}

	@Override
	public List<Integer> getPriceHourIntervals(IPortSlot slot, int start, int end, IPortTimeWindowsRecord portTimeWindowsRecord) {
		int[] intervals = priceChangeIntervalsInHours.getIntervalsAs1dArray(start, end);
		if (slot instanceof ILoadOption) {
			return priceIntervalProviderUtil.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else if (slot instanceof IDischargeOption) {
			return priceIntervalProviderUtil.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord);
		} else {
			return null;
		}
	}

}
