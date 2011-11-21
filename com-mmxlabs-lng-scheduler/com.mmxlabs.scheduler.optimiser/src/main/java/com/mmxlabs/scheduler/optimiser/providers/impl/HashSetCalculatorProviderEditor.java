/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor;

/**
 * @author hinton
 *
 */
public class HashSetCalculatorProviderEditor<T> implements ICalculatorProviderEditor<T> {
	private final String name;
	private final LinkedHashSet<ILoadPriceCalculator2> loadPriceCalculators = new LinkedHashSet<ILoadPriceCalculator2>();
	private final LinkedHashSet<IShippingPriceCalculator<T>> shippingPriceCalculators = new LinkedHashSet<IShippingPriceCalculator<T>>();

	public HashSetCalculatorProviderEditor(final String name) {
		this.name = name;
	}
	
	
	@Override
	public Collection<ILoadPriceCalculator2> getLoadPriceCalculators() {
		return Collections.unmodifiableSet(loadPriceCalculators);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider#getShippingPriceCalculators()
	 */
	@Override
	public Collection<IShippingPriceCalculator<T>> getShippingPriceCalculators() {
		return Collections.unmodifiableSet(shippingPriceCalculators);
	}

	/* (non-Javadoc)
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


	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor#addLoadPriceCalculator(com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2)
	 */
	@Override
	public void addLoadPriceCalculator(ILoadPriceCalculator2 calculator) {
		loadPriceCalculators.add(calculator);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.providers.ICalculatorProviderEditor#addShippingPriceCalculator(com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator)
	 */
	@Override
	public void addShippingPriceCalculator(IShippingPriceCalculator<T> calculator) {
		shippingPriceCalculators.add(calculator);
	}

}
