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
		System.out.println(time);
		return expressionCurve.getValueAtPoint(time);
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(ILoadOption loadOption, IDischargeOption dischargeOption) {
		// not determined by this contract
		return null;
	}

	@Override
	public List<int[]> getPriceIntervals(int startOfRange, int endOfRange, ILoadOption loadOption, IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowRecord) {
		if (loadOption != null) { //DON NOT COMMIT
			//TODO: do we need this?
			return priceIntervalProviderUtil.getPriceIntervalsList(priceChangeIntervalsInHours, expressionCurve, startOfRange, endOfRange, 0, loadOption, portTimeWindowRecord);
		} else {
			return priceIntervalProviderUtil.getPriceIntervalsList(priceChangeIntervalsInHours, expressionCurve, startOfRange, endOfRange, 0, dischargeOption, portTimeWindowRecord);
		}
	}

	@Override
	public Pair<Integer, Integer> getHighestPriceInterval(int startOfRange, int endOfRange, ILoadOption loadOption, IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowRecord) {
		return priceIntervalProviderUtil.getHighestPriceInterval(getPriceIntervals(startOfRange, endOfRange, loadOption, dischargeOption, portTimeWindowRecord));
	}

	@Override
	public Pair<Integer, Integer> getLowestPriceInterval(int startOfRange, int endOfRange, ILoadOption loadOption, IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowRecord) {
		return priceIntervalProviderUtil.getLowestPriceInterval(getPriceIntervals(startOfRange, endOfRange, loadOption, dischargeOption, portTimeWindowRecord));
	}

	@Override
	public List<Integer> getPriceHourIntervals(IPortSlot slot, int start, int end, IPortTimeWindowsRecord portTimeWindowsRecord) {
		int[] intervals = priceChangeIntervalsInHours.getIntervalsAs1dArray(start, end);
		if (slot instanceof ILoadOption) {
			return priceIntervalProviderUtil.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord, PriceIntervalProviderUtil.getPriceEventFromSlotOrContract((ILoadOption) slot));
		} else if (slot instanceof IDischargeOption) {
			return priceIntervalProviderUtil.buildDateChangeCurveAsIntegerList(start, end, slot, intervals, portTimeWindowsRecord, PriceIntervalProviderUtil.getPriceEventFromSlotOrContract((IDischargeOption) slot));
		} else {
			return null;
		}
	}

}
