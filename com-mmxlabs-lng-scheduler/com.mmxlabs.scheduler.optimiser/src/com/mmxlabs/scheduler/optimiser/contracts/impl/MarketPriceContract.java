/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * @author hinton
 * 
 */
public class MarketPriceContract extends SimpleContract {
	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	private final ICurve market;
	private final int multiplier;
	private final int offset;

	public MarketPriceContract(final ICurve market, final int offset, final int multiplier) {
		super();
		this.market = market;
		this.offset = offset;
		this.multiplier = multiplier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.contracts.ISimpleLoadPriceCalculator#calculateSimpleLoadUnitPrice(int)
	 */
	@Override
	public int calculateSimpleUnitPrice(final int time, final IPort port) {
		final int valueAtPoint = market.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(time, port));
		final int shareOfPrice = Calculator.getShareOfPrice(multiplier, valueAtPoint);
		return offset + shareOfPrice;
	}
}
