/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;

/**
 * @author hinton
 * 
 */
public class HashSetCalculatorProviderEditor implements ICalculatorProviderEditor {

	private final Set<ILoadPriceCalculator> loadPriceCalculators = new LinkedHashSet<>();
	private final Set<ISalesPriceCalculator> salesPriceCalculators = new LinkedHashSet<>();
	private final Set<ICooldownPriceCalculator> cooldownPriceCalculators = new LinkedHashSet<>();

	@Override
	public Collection<ILoadPriceCalculator> getLoadPriceCalculators() {
		return Collections.unmodifiableSet(loadPriceCalculators);
	}

	@Override
	public Collection<ISalesPriceCalculator> getSalesPriceCalculators() {
		return Collections.unmodifiableSet(salesPriceCalculators);
	}

	@Override
	public Collection<ICooldownPriceCalculator> getCooldownPriceCalculators() {
		return Collections.unmodifiableSet(cooldownPriceCalculators);
	}

	@Override
	public void addLoadPriceCalculator(final ILoadPriceCalculator calculator) {
		if (calculator != null) {
			loadPriceCalculators.add(calculator);
		}
	}

	@Override
	public void addSalesPriceCalculator(final ISalesPriceCalculator calculator) {
		if (calculator != null) {
			salesPriceCalculators.add(calculator);
		}
	}

	@Override
	public void addCooldownPriceCalculator(final ICooldownPriceCalculator calculator) {
		if (calculator != null) {
			cooldownPriceCalculators.add(calculator);
		}
	}

}
