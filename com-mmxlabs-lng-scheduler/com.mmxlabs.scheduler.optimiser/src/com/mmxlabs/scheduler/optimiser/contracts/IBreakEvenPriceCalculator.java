/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

/**
 * Marker interface for {@link ISalesPriceCalculator} and {@link ILoadPriceCalculator} calculator instances to indicate the price is really missing and needs to be calculated.
 * 
 * @author Simon Goodall
 */
public interface IBreakEvenPriceCalculator {

	/** 
	 * Set the break even price.
	 */
	void setPrice(int newPrice);
	
}
