/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;

/**
 * A data component provider which gives access to all the contracts in a scenario
 * 
 * @author hinton
 * 
 */
public interface ICalculatorProvider<T> extends IDataComponentProvider {
	/**
	 * @return A collection of all the load price calculators used in this problem
	 */
	Collection<ILoadPriceCalculator2> getLoadPriceCalculators();

	/**
	 * @return A collection of all shipping price calculators used in this scenario.
	 */
	Collection<IShippingPriceCalculator<T>> getShippingPriceCalculators();
}
