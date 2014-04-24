/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * @since 2.0
 */
public class PriceExpressionContract extends SimpleContract {

	private final ICurve expressionCurve;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	public PriceExpressionContract(final ICurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}

	@Override
	protected int calculateSimpleUnitPrice(final int time, final IPort port) {
		final int pricingTime = timeZoneToUtcOffsetProvider.UTC(time, port);

		return expressionCurve.getValueAtPoint(pricingTime);
	}

}
