package com.mmxlabs.scheduler.optimiser;

/**
 * Class to handle integer based maths. Assume, unless otherwise noted each
 * input has been converted into a int/long by multiplying against the
 * {@link #ScaleFactor}.
 * 
 * @author Simon Goodall
 * 
 */
public class Calculator {

	public static final int ScaleFactor = 1000;

	public static int speedFromDistanceTime(long distance, int time) {

		return (int)((distance * ScaleFactor) / time);
	}

	public static int getTimeFromSpeedDistance(int speed, long distance) {

		return (int)((distance * ScaleFactor) / speed);
	}

	public static long quantityFromRateTime(long rate, int time) {

		return (rate * time);
	}

	public static int getTimeFromRateQuantity(long rate, long quantity) {
		
		return (int)(quantity / rate);
	}

	public static long costFromConsumption(long consumption, int unitPrice) {
		return (consumption * unitPrice) / ScaleFactor;
	}
}
