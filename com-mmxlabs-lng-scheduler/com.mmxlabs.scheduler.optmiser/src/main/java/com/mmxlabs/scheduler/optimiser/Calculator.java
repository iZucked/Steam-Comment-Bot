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

	public static long speedFromDistanceTime(long distance, long time) {

		return (distance * ScaleFactor) / time;
	}

	public static long getTimeFromSpeedDistance(long speed, long distance) {

		return (distance * ScaleFactor) / speed;
	}

	public static long quantityFromRateTime(long rate, long time) {

		return (rate * time);
	}

	public static int getTimeFromRateQuantity(long rate, long quantity) {
		
		return (int)(quantity / rate);
	}
}
