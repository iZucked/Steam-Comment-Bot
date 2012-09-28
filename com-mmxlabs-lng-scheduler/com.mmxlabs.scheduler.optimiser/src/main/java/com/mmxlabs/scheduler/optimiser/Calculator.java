/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

/**
 * Class to handle integer based maths. Assume, unless otherwise noted each input has been converted into a int/long by multiplying against the {@link #ScaleFactor}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Calculator {
	// If you change this, you will need to change the getHourlyCharterPrice operator
	// on AllocatedVessel in the ECore model.
	public static final int ScaleFactor = 1000;

	/**
	 * Simple multiplication between scaled integer based values
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long multiply(final long a, final long b) {
		return (a * b) / ScaleFactor;
	}

	/**
	 * Simple division between scaled integer based values
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long divide(final long a, final long b) {
		return (a * ScaleFactor) / b;
	}

	public static int speedFromDistanceTime(final long distance, final int time) {

		return (int) ((distance * ScaleFactor) / time);
	}

	public static int getTimeFromSpeedDistance(final int speed, final long distance) {

		return (int) ((distance * ScaleFactor) / speed);
	}

	public static long quantityFromRateTime(final long rate, final int time) {

		return (rate * time);
	}

	public static int getTimeFromRateQuantity(final long rate, final long quantity) {

		return (int) (quantity / rate);
	}

	public static long costFromConsumption(final long consumption, final long notionalFuelCost) {
		return (consumption * notionalFuelCost) / ScaleFactor;
	}

	public static long convertM3ToMMBTu(final long m3, final int factor) {
		return (m3 * factor) / ScaleFactor;
	}

	public static long convertMMBTuToM3(final long mmbtu, final int factor) {
		return (mmbtu * ScaleFactor) / factor;
	}

	public static long convertM3ToMT(final long m3, final int factor) {
		return (m3 * factor) / ScaleFactor;
	}

	public static long convertMTToM3(final long mt, final int factor) {
		return (mt * ScaleFactor) / factor;
	}

	/**
	 * Scale an integer value by the scaling factor
	 * 
	 * @param value
	 * @return scaled version of value
	 */
	public static long scale(final int value) {
		return ScaleFactor * value;
	}
	/**
	 * @since 2.0
	 */
	public static long scale(final long value) {
		return ScaleFactor * value;
	}

	public static long descale(final long value) {
		return value / ScaleFactor;
	}

	/**
	 * Scale a float
	 * 
	 * @param f
	 * @return scaled float
	 */
	public static long scale(final float f) {
		return (long) (ScaleFactor * f);
	}

	public static int scaleToInt(final double d) {
		return (int) (ScaleFactor * d);
	}

	/**
	 * Multiply a long by a double, returning the result as a long, rounded
	 * 
	 * @param multiply
	 * @param d
	 * @return
	 */
	public static long multiply(final long multiply, final double d) {
		return Math.round(multiply * d);
	}
}