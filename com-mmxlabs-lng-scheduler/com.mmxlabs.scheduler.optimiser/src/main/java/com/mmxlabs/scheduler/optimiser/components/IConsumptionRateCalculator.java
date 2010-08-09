package com.mmxlabs.scheduler.optimiser.components;

/**
 * Implementations of {@link IConsumptionRateCalculator} calculate vessel fuel
 * consumption rates based on an input speed.
 * 
 * @author Simon Goodall
 * 
 */
public interface IConsumptionRateCalculator {

	/**
	 * Return the consumption rate for this speed.
	 * 
	 * @param speed
	 * @return
	 */
	long getRate(int speed);
}
