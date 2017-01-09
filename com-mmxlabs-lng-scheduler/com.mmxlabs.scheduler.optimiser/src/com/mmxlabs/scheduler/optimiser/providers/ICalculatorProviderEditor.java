/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * The editor for {@link ICalculatorProvider}
 * 
 * @author hinton
 * 
 */
public interface ICalculatorProviderEditor extends ICalculatorProvider {
	void addLoadPriceCalculator(ILoadPriceCalculator calculator);

	void addSalesPriceCalculator(ISalesPriceCalculator calculator);

	void addCooldownCalculator(ICooldownCalculator calculator);
}
