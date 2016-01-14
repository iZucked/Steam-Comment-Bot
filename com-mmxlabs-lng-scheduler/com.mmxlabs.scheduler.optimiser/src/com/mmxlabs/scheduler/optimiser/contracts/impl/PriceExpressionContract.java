/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 */
public class PriceExpressionContract extends SimpleContract implements IPriceIntervalProvider{

	private final ICurve expressionCurve;
	private final IIntegerIntervalCurve priceChangeIntervalsInHours;
	
	@Inject
	private PriceIntervalProviderUtil priceIntervalProviderUtil;

	public PriceExpressionContract(final ICurve expressionCurve, final IIntegerIntervalCurve priceChangeIntervalsInHours) {
		this.expressionCurve = expressionCurve;
		this.priceChangeIntervalsInHours = priceChangeIntervalsInHours;
	}

	@Override
	protected int calculateSimpleUnitPrice(final int time, final IPort port) {
		System.out.println(time); // DO NOT COMMIT
		return expressionCurve.getValueAtPoint(time);
	}

	@Override
	public List<int[]> getPriceIntervals(IPortSlot slot, int startOfRange, int endOfRange, IPortTimeWindowsRecord portTimeWindowRecord) {
		if (slot instanceof ILoadOption) { //DON NOT COMMIT - make a load/discharge version
			//TODO: do we need this?
			return priceIntervalProviderUtil.getPriceIntervalsList((ILoadOption) slot, priceChangeIntervalsInHours, expressionCurve, startOfRange, endOfRange, 0, portTimeWindowRecord);
		} else if (slot instanceof IDischargeOption) {
			return priceIntervalProviderUtil.getPriceIntervalsList((IDischargeOption) slot, priceChangeIntervalsInHours, expressionCurve, startOfRange, endOfRange, 0, portTimeWindowRecord);
		} else {
			throw new IllegalStateException("getPriceIntervals() requires either an ILoadOption or IDischargeOption");
		}
	}

	@Override
	public Pair<Integer, Integer> getHighestPriceInterval(int startOfRange, int endOfRange, IPortSlot slot, IPortTimeWindowsRecord portTimeWindowRecord) {
		return priceIntervalProviderUtil.getHighestPriceInterval(getPriceIntervals(slot, startOfRange, endOfRange, portTimeWindowRecord));
	}

	@Override
	public Pair<Integer, Integer> getLowestPriceInterval(int startOfRange, int endOfRange, IPortSlot slot, IPortTimeWindowsRecord portTimeWindowRecord) {
		return priceIntervalProviderUtil.getLowestPriceInterval(getPriceIntervals(slot, startOfRange, endOfRange, portTimeWindowRecord));
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
