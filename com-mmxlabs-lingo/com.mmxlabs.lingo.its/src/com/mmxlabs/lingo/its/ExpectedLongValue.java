/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its;

import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;

public class ExpectedLongValue {

	private final int externalValue;
	private final long internalValue;

	public ExpectedLongValue(final int externalValue, final long internalValue) {
		this.externalValue = externalValue;
		this.internalValue = internalValue;

	}

	/**
	 * Returns the data model value used as input data
	 * 
	 * @return
	 */
	public int input() {
		return externalValue;
	}

	/**
	 * Returns the transformed data equivalent value used for comparisions in output
	 * 
	 * @return
	 */
	public long output() {
		return internalValue;
	}

	public String expression() {
		return Integer.toString(externalValue);
	}

	public static ExpectedLongValue forFixedCost(int fixedCost) {
		return new ExpectedLongValue(fixedCost, OptimiserUnitConvertor.convertToInternalFixedCost(fixedCost));
	}

	public static ExpectedLongValue forRoundedFixedCost(double fixedCost) {
		return new ExpectedLongValue((int) Math.round(fixedCost), OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(fixedCost)));
	}
}
