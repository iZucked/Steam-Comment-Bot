/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.parser.series.ISeries;

@NonNullByDefault
public interface IPriceExpressionProvider {

	ISeries getExpression(PriceCurveKey key);
}
