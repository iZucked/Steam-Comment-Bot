/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

/**
 * Moved long summation and subtraction to a separate interface to clean up view a bit
 * @author Andrey Popov
 *
 */
public interface IDeltaDerivable {
	
	/**
	 *   Δ char
	 */
	public static final String DELTA_SYMBOL = "\u0394";
	
	/**
	 * No delta. All values set to zeroes
	 */
	void initZeroes();
	
	/**
	 * Sets emission values to first minus second
	 */
	void setDelta(final IDeltaDerivable first, final IDeltaDerivable second);
	
	/**
	 * Adds emission values to total model
	 */
	void addToTotal(final IDeltaDerivable summand);
	
	/**
	 * Checks if any difference is found. if At least one field is non zero returns true.
	 */
	boolean isNonZero();
}
