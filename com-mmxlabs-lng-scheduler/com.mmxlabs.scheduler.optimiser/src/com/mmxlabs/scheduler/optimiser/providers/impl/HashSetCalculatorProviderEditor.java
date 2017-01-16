/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
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
	private final Set<ICooldownCalculator> cooldownCalculators = new LinkedHashSet<>();

	@Override
	public Collection<ILoadPriceCalculator> getLoadPriceCalculators() {
		return Collections.unmodifiableSet(loadPriceCalculators);
	}

	@Override
	public Collection<ISalesPriceCalculator> getSalesPriceCalculators() {
		return Collections.unmodifiableSet(salesPriceCalculators);
	}

	@Override
	public Collection<ICooldownCalculator> getCooldownCalculators() {
		return Collections.unmodifiableSet(cooldownCalculators);
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
	public void addCooldownCalculator(final ICooldownCalculator calculator) {
		if (calculator != null) {
			cooldownCalculators.add(calculator);
		}
	}

}
