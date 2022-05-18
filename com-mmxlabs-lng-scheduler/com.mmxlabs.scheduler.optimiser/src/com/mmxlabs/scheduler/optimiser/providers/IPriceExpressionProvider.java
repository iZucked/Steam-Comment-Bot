/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.series.ISeries;

public interface IPriceExpressionProvider {
	@NonNull
	ISeries getExpression(@NonNull PriceCurveKey key);

	@NonNull
	ISeries getExpression(@NonNull String indexName, @Nullable String curveName);
}
