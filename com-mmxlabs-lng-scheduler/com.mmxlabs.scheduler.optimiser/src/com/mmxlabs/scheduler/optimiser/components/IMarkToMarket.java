/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

/**
 * An object representing a Mark-to-Market market representing either a DES Purchase and/or FOB Sale. This holds the pricing calculators and CV value for the market.
 * 
 */
public interface IMarkToMarket {

	/**
	 * Returns the {@link ILoadPriceCalculator} to use in DES Purchase calculations
	 * 
	 * @return
	 */
	@Nullable
	ILoadPriceCalculator getLoadPriceCalculator();

	/**
	 * Returns the {@link ISalesPriceCalculator} to use in FOB Sale calculations
	 * 
	 * @return
	 */
	@Nullable
	ISalesPriceCalculator getSalesPriceCalculator();

	/**
	 * The CV to use for a DES Purchase
	 * 
	 * @return
	 */
	int getCVValue();

	IEntity getEntity();
}
