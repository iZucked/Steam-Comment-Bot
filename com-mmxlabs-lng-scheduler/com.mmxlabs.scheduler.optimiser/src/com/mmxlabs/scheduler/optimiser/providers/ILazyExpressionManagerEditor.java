/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.curves.ILazyCurve;
import com.mmxlabs.common.parser.series.ILazyExpressionContainer;
import com.mmxlabs.scheduler.optimiser.curves.LazyIntegerIntervalCurve;

@NonNullByDefault
public interface ILazyExpressionManagerEditor {

	void addPriceCurve(String name, ILazyExpressionContainer lazyNamedSeriesContainer);

	void addLazyCurve(ILazyCurve lazyCurve);

	void addLazyIntervalCurve(LazyIntegerIntervalCurve lazyIntervalCurve);
}
