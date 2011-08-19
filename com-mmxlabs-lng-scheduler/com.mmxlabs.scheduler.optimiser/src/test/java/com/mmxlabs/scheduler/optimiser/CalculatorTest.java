/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Test;

public class CalculatorTest {

	/**
	 * A division by zero should throw an exception, except for the case where
	 * the distance is zero (so that 0 / 0 equals 0).
	 * @author Adam Semenenko
	 */
	@Test(expected=ArithmeticException.class)
	public void testSpeedFromDistanceTimeForDivideByZero() {
		
		// Testing for divide by zero error for a non-zero distance.
		Calculator.speedFromDistanceTime(1, 0);
		Calculator.speedFromDistanceTime(-1, 0);
		
		// A zero distance should return zero iff the time is zero.
		Assert.assertEquals(0, Calculator.speedFromDistanceTime(0, 0)); 
	}

	/**
	 * Should you expect an Exception if a negative time is 
	 * input, because it gives a negative speed?
	 * @author Adam Semenenko
	 */
	@Test(expected=ArithmeticException.class)
	public void testSpeedFromDistanceTimeForNegTimeInput() {

		// Negative time?
		Calculator.speedFromDistanceTime(1, -1);
	}

	/**
	 * Should you expect an Exception if a negative distance is 
	 * input, because it gives a negative speed?
	 * @author Adam Semenenko
	 */
	@Test(expected=ArithmeticException.class)
	public void testSpeedFromDistanceTimeForNegDistanceInput() {

		// Negative distance?
		Calculator.speedFromDistanceTime(-1, 1);
	}
	
	/**
	 * If both inputs are negative then they will cancel out to give a
	 * positive result. This doesn't make sense though, so an exception
	 * should be thrown for either the negative distance or time.
	 * @author Adam Semenenko
	 */
	@Test(expected=ArithmeticException.class)
	public void testSpeedFromDistanceTimeForNegInput(){
		
		Calculator.speedFromDistanceTime(-1, -1);
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
