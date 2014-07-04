/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 */
public class PriceExpressionContract extends SimpleContract {

	private final ICurve expressionCurve;

	public PriceExpressionContract(final ICurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}

	@Override
	protected int calculateSimpleUnitPrice(final int time, final IPort port) {
		return expressionCurve.getValueAtPoint(time);
	}

}
