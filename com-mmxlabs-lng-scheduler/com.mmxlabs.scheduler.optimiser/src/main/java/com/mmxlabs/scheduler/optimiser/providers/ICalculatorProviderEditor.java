/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;

/**
 * The editor for {@link ICalculatorProvider}
 * 
 * @author hinton
 * 
 */
public interface ICalculatorProviderEditor extends ICalculatorProvider {
	void addLoadPriceCalculator(ILoadPriceCalculator calculator);

	void addShippingPriceCalculator(IShippingPriceCalculator calculator);
}
