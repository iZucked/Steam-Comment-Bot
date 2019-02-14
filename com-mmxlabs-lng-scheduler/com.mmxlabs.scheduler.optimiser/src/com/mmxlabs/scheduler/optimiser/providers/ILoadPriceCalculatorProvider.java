/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

/**
 */
public interface ILoadPriceCalculatorProvider extends IDataComponentProvider {

	boolean isPortfolioCalculator(ILoadPriceCalculator calculator);

}
