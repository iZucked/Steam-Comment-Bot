/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;

/**
 * @author hinton
 * 
 */
public class MarketPriceContract extends SimpleContract {
	private final ICurve market;

	public MarketPriceContract(final ICurve market) {
		super();
		this.market = market;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.contracts.ISimpleLoadPriceCalculator#calculateSimpleLoadUnitPrice(int)
	 */
	@Override
	public int calculateSimpleLoadUnitPrice(final int loadTime) {
		return (int) market.getValueAtPoint(loadTime);
	}
}
