/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.ISeries;

public interface ILazyExpressionManager extends AutoCloseable {

	void setPriceCurve(@NonNull String name, @NonNull ISeries series);

	void initialiseAllPricingData();

	void clearData();

}
