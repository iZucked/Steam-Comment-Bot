/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * @author hinton
 * 
 */
public class MarketPriceContract extends SimpleContract {

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
		final int shareOfPrice = Calculator.getShareOfPrice(multiplier, time);
		return offset + shareOfPrice;
	}
}
