/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * This interface defines a BaseFuel
 * 
 * @author achurchill
 * 
 */
public interface IBaseFuel {

	/**
	 * The name of the base fuel.
	 * 
	 * @return the base fuel's name
	 */
	String getName();

	/**
	 * returns the conversion factor to use to convert 1 {@link FuelUnit#M3} of LNG to {@link FuelUnit#MT} equivalence.
	 * @return
	 */
	int getEquivalenceFactor();

	/**
	 * Sets returns the conversion factor to use to convert 1 {@link FuelUnit#M3} of LNG to {@link FuelUnit#MT} equivalence.
	 * @return
	 */
	void setEquivalenceFactor(int value);
}
