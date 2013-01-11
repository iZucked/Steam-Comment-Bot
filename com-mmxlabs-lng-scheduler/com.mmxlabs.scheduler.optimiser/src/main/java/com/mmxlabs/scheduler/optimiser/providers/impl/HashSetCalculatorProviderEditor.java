/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;

/**
 * @author hinton
 * 
 */
public class HashSetCalculatorProviderEditor implements ICalculatorProviderEditor {
	private final String name;
	private final LinkedHashSet<ILoadPriceCalculator> loadPriceCalculators = new LinkedHashSet<ILoadPriceCalculator>();
	private final LinkedHashSet<ISalesPriceCalculator> salesPriceCalculators = new LinkedHashSet<ISalesPriceCalculator>();
	private final LinkedHashSet<ICooldownPriceCalculator> cooldownPriceCalculators = new LinkedHashSet<ICooldownPriceCalculator>();

	public HashSetCalculatorProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	public Collection<ILoadPriceCalculator> getLoadPriceCalculators() {
		return Collections.unmodifiableSet(loadPriceCalculators);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider#getShippingPriceCalculators()
	 */
	@Override
	public Collection<ISalesPriceCalculator> getSalesPriceCalculators() {
		return Collections.unmodifiableSet(salesPriceCalculators);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider#getCooldownPriceCalculators()
	 */
	@Override
	public Collection<ICooldownPriceCalculator> getCooldownPriceCalculators() {
		return Collections.unmodifiableSet(cooldownPriceCalculators);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.core.scenario.IDataComponentProvider#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		loadPriceCalculators.clear();
		salesPriceCalculators.clear();
		cooldownPriceCalculators.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor#addLoadPriceCalculator(com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2)
	 */
	@Override
	public void addLoadPriceCalculator(final ILoadPriceCalculator calculator) {
		if (calculator != null) {
			loadPriceCalculators.add(calculator);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor#addSalesPriceCalculator(com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator)
	 */
	@Override
	public void addSalesPriceCalculator(final ISalesPriceCalculator calculator) {
		if (calculator != null) {
			salesPriceCalculators.add(calculator);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor#addCooldownPriceCalculator(com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator)
	 */
	@Override
	public void addCooldownPriceCalculator(final ICooldownPriceCalculator calculator) {
		if (calculator != null) {
			cooldownPriceCalculators.add(calculator);
		}
	}

}
