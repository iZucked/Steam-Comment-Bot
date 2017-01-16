/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

/**
 */
public interface ILoadPriceCalculatorProviderEditor extends ILoadPriceCalculatorProvider {

	void setPortfolioCalculator(ILoadPriceCalculator calculator);

}
