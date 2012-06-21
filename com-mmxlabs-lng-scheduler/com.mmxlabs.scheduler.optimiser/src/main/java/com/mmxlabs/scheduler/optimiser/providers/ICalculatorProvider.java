/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;

/**
 * A data component provider which gives access to all the contracts in a scenario
 * 
 * @author hinton
 * 
 */
public interface ICalculatorProvider extends IDataComponentProvider {
	/**
	 * @return A collection of all the load price calculators used in this problem
	 */
	Collection<ILoadPriceCalculator> getLoadPriceCalculators();

	/**
	 * @return A collection of all shipping price calculators used in this scenario.
	 */
	Collection<IShippingPriceCalculator> getShippingPriceCalculators();
}
