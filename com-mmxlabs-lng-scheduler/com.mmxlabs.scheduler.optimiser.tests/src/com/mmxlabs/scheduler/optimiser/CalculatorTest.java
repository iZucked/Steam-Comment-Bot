/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

	@Test
	public void testSpeedFromDistanceTimeForDivideByZero() {

		// Testing for divide by zero error for a non-zero distance.
		Assertions.assertThrows(ArithmeticException.class, () -> Calculator.speedFromDistanceTime(1, 0));
	}

	/**
	 * A division by zero should throw an exception even if the distance is invalid.
	 */
	@Test
	public void testSpeedFromDistanceTimeForNegDistanceAndDivideByZero() {

		Assertions.assertThrows(ArithmeticException.class, () -> Calculator.speedFromDistanceTime(-1, 0));
	}

	@Test
	public void testGetTimeFromSpeedDistance() {

		final int limit = 1000;

		for (int distance = 1; distance < limit; distance++) {
			for (int speed = 1; speed < limit; speed++) {

				final int expected = (int) ((1000L * distance) / speed);
				final int actual = Calculator.getTimeFromSpeedDistance(speed, distance);

				Assertions.assertEquals(expected, actual, "Distance: " + distance + ", speed: " + speed);
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

				Assertions.assertEquals(expectedQuantity, actualQuantity, "Rate: " + rate + ", time: " + time);
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

				Assertions.assertEquals(expectedTime, actualTime, "Quantity: " + quantity + ", rate: " + rate);
			}
		}

	}
}
