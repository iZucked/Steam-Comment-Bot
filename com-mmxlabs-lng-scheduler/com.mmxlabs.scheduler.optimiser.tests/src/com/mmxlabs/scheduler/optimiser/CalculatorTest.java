/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {

	@Test(expected = ArithmeticException.class)
	public void testSpeedFromDistanceTimeForDivideByZero() {

		// Testing for divide by zero error for a non-zero distance.
		Calculator.speedFromDistanceTime(1, 0);
	}

	/**
	 * A division by zero should throw an exception even if the distance is invalid.
	 */
	@Test(expected = ArithmeticException.class)
	public void testSpeedFromDistanceTimeForNegDistanceAndDivideByZero() {

		Calculator.speedFromDistanceTime(-1, 0);
	}

	@Test
	public void testGetTimeFromSpeedDistance() {

		final int limit = 1000;

		for (int distance = 1; distance < limit; distance++) {
			for (int speed = 1; speed < limit; speed++) {

				final int expected = (int) ((1000L * distance) / speed);
				final int actual = Calculator.getTimeFromSpeedDistance(speed, distance);

				Assert.assertEquals("Distance: " + distance + ", speed: " + speed, expected, actual);
			}
		}
	}

	@Test
	public void testQuantityFromRateTime() {

		final int limit = 1000;

		for (long rate = 1; rate < limit; rate++) {
			for (int time = 1; time < limit; time++) {

				final long expectedQuantity = rate * time;
				final long actualQuantity = Calculator.quantityFromRateTime(rate, time);

				Assert.assertEquals("Rate: " + rate + ", time: " + time, expectedQuantity, actualQuantity);
			}
		}
	}

	@Test
	public void testGetTimeFromRateQuantity() {

		final long limit = 1000;

		for (long quantity = 1; quantity < limit; quantity++) {
			for (long rate = 1; rate < limit; rate++) {

				final int expectedTime = (int) (quantity / rate);
				final int actualTime = Calculator.getTimeFromRateQuantity(rate, quantity);

				Assert.assertEquals("Quantity: " + quantity + ", rate: " + rate, expectedTime, actualTime);
			}
		}

	}
}
