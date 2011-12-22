/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
	public int getDownstreamTransferPrice(int dischargePricePerM3, int cvValue) {
		return (int) (dischargePricePerM3 - Calculator.multiply(cvValue, offsetPerMMBTU));
	}

	@Override
	public int getUpstreamTransferPrice(int loadPricePerM3, int cvValue) {
		return (int) (loadPricePerM3 + Calculator.multiply(cvValue, offsetPerMMBTU));
	}

	/**
	 * For this case, taxed profit is just pretax * ownership * taxrate(time)
	 */
	@Override
	public long getTaxedProfit(long pretax, int time) {
		final int taxRate = (int) taxCurve.getValueAtPoint(time);

		final int flip = Calculator.ScaleFactor - taxRate;

		return Calculator.multiply(pretax, Calculator.multiply(ownership, flip));
	}

}
