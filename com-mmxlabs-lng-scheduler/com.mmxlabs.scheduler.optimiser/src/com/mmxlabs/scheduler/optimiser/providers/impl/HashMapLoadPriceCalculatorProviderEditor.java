/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ILoadPriceCalculatorProviderEditor;

public class HashMapLoadPriceCalculatorProviderEditor implements ILoadPriceCalculatorProviderEditor {

	Set<ILoadPriceCalculator> loadCalculators = new HashSet<ILoadPriceCalculator>();
	@Override
	public boolean isPortfolioCalculator(ILoadPriceCalculator calculator) {
		return loadCalculators.contains(calculator);
	}

	@Override
	public void setPortfolioCalculator(ILoadPriceCalculator calculator) {
		loadCalculators.add(calculator);
	}

}
