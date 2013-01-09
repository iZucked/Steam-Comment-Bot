/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
	final ICurve taxCurve;

	final int ownership;
	final int offsetPerMMBTU;

	public SimpleEntity(final String name, final int ownership, final ICurve taxCurve, final int offset) {
		super(name);
		this.ownership = ownership;
		this.taxCurve = taxCurve;
		this.offsetPerMMBTU = offset;
	}

	@Override
	public int getDownstreamTransferPrice(final int dischargePricePerM3, final int cvValue) {
		return dischargePricePerM3 - Calculator.costPerM3FromMMBTu(offsetPerMMBTU, cvValue);
	}

	@Override
	public int getUpstreamTransferPrice(final int loadPricePerM3, final int cvValue) {
		return loadPricePerM3 + Calculator.costPerM3FromMMBTu(offsetPerMMBTU, cvValue);
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
