/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * @author hinton
 * 
 */
public class MarketPriceContract extends SimpleContract {
	private final ICurve market;
	private int multiplier;
	private int offset;

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
	public int calculateSimpleUnitPrice(final int time) {
		int valueAtPoint = market.getValueAtPoint(time);
		int shareOfPrice = Calculator.getShareOfPrice(multiplier, valueAtPoint);
		return offset + shareOfPrice;
	}
}
