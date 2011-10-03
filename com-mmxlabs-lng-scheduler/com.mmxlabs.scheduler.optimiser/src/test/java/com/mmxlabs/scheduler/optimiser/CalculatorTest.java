/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import static org.junit.Assert.fail;

import org.junit.Test;

public class CalculatorTest {

	@Test(expected = ArithmeticException.class)
	public void testSpeedFromDistanceTimeForDivideByZero() {

		// Testing for divide by zero error for a non-zero distance.
		Calculator.speedFromDistanceTime(1, 0);
	}

	/**
	 * A division by zero should throw an exception even if the distance is
	 * invalid.
	 */
	@Test(expected = ArithmeticException.class)
	public void testSpeedFromDistanceTimeForNegDistanceAndDivideByZero() {

		Calculator.speedFromDistanceTime(-1, 0);
	}

	@Test
	public void testGetTimeFromSpeedDistance() {
		fail("Not yet implemented");
	}

	@Test
	public void testQuantityFromRateTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTimeFromRateQuantity() {
		fail("Not yet implemented");
	}
}
