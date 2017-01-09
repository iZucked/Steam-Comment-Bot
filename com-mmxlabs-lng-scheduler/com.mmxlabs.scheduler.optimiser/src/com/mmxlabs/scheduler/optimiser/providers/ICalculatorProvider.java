/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * A data component provider which gives access to all the price calculators in a scenario
 * 
 * @author hinton
 * 
 */
public interface ICalculatorProvider extends IDataComponentProvider {
	/**
	 * @return A collection of all the {@link ILoadPriceCalculator} used in this problem
	 */
	Collection<ILoadPriceCalculator> getLoadPriceCalculators();

	/**
	 * @return A collection of all {@link ISalesPriceCalculator} used in this scenario.
	 */
	Collection<ISalesPriceCalculator> getSalesPriceCalculators();

	/**
	 * @return A collection of all {@link ICooldownCalculator} used in this scenario.
	 */
	Collection<ICooldownCalculator> getCooldownCalculators();
}
