package com.mmxlabs.scheduler.optimiser.contracts;

/**
 * Marker interface for {@link ISalesPriceCalculator} and {@link ILoadPriceCalculator} calculator instances to indicate the price is really missing and needs to be calculated.
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public interface IBreakEvenPriceCalculator {

	/** 
	 * Set the break even price.
	 */
	void setPrice(int newPrice);
	
}
