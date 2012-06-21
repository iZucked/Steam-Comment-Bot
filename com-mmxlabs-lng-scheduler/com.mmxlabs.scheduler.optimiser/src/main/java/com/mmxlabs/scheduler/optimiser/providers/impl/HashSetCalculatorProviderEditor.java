/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;

/**
 * @author hinton
 * 
 */
public class HashSetCalculatorProviderEditor implements ICalculatorProviderEditor {
	private final String name;
	private final LinkedHashSet<ILoadPriceCalculator> loadPriceCalculators = new LinkedHashSet<ILoadPriceCalculator>();
	private final LinkedHashSet<IShippingPriceCalculator> shippingPriceCalculators = new LinkedHashSet<IShippingPriceCalculator>();

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
	public Collection<IShippingPriceCalculator> getShippingPriceCalculators() {
		return Collections.unmodifiableSet(shippingPriceCalculators);
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
		shippingPriceCalculators.clear();
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
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor#addShippingPriceCalculator(com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator)
	 */
	@Override
	public void addShippingPriceCalculator(final IShippingPriceCalculator calculator) {
		if (calculator != null) {
			shippingPriceCalculators.add(calculator);
		}
	}

}
