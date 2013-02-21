/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * Basic entity which has a tax curve and and does transfer pricing by adding a fixed offset per MMBTU
 * 
 * @author hinton
 * 
 */
public class SimpleEntity extends Entity {
	/**
	 * @since 3.0
	 */
	final protected ICurve taxCurve;

	/**
	 * @since 3.0
	 */
	final protected int ownership;

	/**
	 * @since 3.0
	 */
	public SimpleEntity(final String name, final int ownership, final ICurve taxCurve) {
		super(name);
		this.ownership = ownership;
		this.taxCurve = taxCurve;
	}

	
	@Override
	public int getDownstreamTransferPrice(final int dischargePricePerM3, final int cvValue) {
		return dischargePricePerM3;
	}

	@Override
	public int getUpstreamTransferPrice(final int loadPricePerM3, final int cvValue) {
		return loadPricePerM3;
	}

	/**
	 * For this case, taxed profit is just pretax * ownership * taxrate(time)
	 */
	@Override
	public long getTaxedProfit(final long pretax, final int time) {
		final int taxRate = taxCurve.getValueAtPoint(time);

		final int flip = Calculator.ScaleFactor - taxRate;

		final long ownershipValue = ((long) ownership * (long) flip) / Calculator.HighScaleFactor;
		final long taxedValue = Calculator.multiply(pretax, ownershipValue);
		return taxedValue;
	}

}
