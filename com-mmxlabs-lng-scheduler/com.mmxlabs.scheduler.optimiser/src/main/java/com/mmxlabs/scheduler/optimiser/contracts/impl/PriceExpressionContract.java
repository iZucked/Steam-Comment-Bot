package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;

public class PriceExpressionContract extends SimpleContract {

	private final ICurve expressionCurve;

	public PriceExpressionContract(ICurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}
	
	@Override
	protected int calculateSimpleUnitPrice(int loadTime) {
		return expressionCurve.getValueAtPoint(loadTime);
	}

}
