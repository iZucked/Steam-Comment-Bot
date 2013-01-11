/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
	 * @since 2.0
	 */
	public static final long HighScaleFactor = 1000000;

	public static int speedFromDistanceTime(final long distance, final int time) {

		return (int) ((distance * ScaleFactor) / time);
	}

	public static int getTimeFromSpeedDistance(final int speed, final long distance) {

		return (int) ((distance * ScaleFactor) / speed);
	}

	public static long quantityFromRateTime(final long rate, final int time) {

		return (rate * (long) time);
	}

	public static int getTimeFromRateQuantity(final long rate, final long quantity) {

		return (int) (quantity / rate);
	}

	public static long costFromConsumption(final long consumption, final long notionalFuelCost) {
		return (consumption * notionalFuelCost) / HighScaleFactor;
	}

	public static long convertM3ToMMBTu(final long m3, final int factor) {

		return (m3 * (long) factor) / HighScaleFactor;
	}

	/**
	 * @since 2.0
	 */
	public static long convertM3ToM3Price(final long m3, final int pricePerM3) {
		return (m3 * (long) pricePerM3) / HighScaleFactor;
	}

	public static long convertMMBTuToM3(final long mmbtu, final int factor) {
		return (mmbtu * HighScaleFactor) / (long) factor;
	}

	public static long convertM3ToMT(final long m3, final int factor) {
		return (m3 * factor) / HighScaleFactor;
	}

	public static long convertMTToM3(final long mt, final int factor) {
		return (mt * HighScaleFactor) / (long) factor;
	}

	/**
	 * Convert a $/MMBTu price to a $/m3 price
	 * 
	 * @since 2.0
	 */
	public static int costPerM3FromMMBTu(final int costPerMMBTu, final int cvValue) {
		return (int) (((long) costPerMMBTu * (long) cvValue) / HighScaleFactor);
	}

	/**
	 * Convert a $/m3 price to a $/MMBTu price
	 * 
	 * @since 2.0
	 */
	public static int costPerMMBTuFromM3(final int costPerM3, final int cvValue) {

		return (int) (((long) costPerM3 * HighScaleFactor) / (long) cvValue);
	}

	/**
	 * 
	 * @since 2.0
	 */
	public static int getPerMMBTuFromTotalAndVolumeInMMBTu(final long totalCost, final long volumeInMMBTu) {
		// TODO: Be careful of overflows here
		return (int) (((long) totalCost * HighScaleFactor) / (long) volumeInMMBTu);
	}

	/**
	 * 
	 * @since 2.0
	 */
	public static int getPerMMBTuFromTotalAndVolumeInM3(final long totalCost, final long volumeInM3, final int cvValue) {

		// TODO: Be careful of overflows here
		return (int) (((long) totalCost * HighScaleFactor) / (long) convertM3ToMMBTu(volumeInM3, cvValue));
	}

	/**
	 * 
	 * @since 2.0
	 */
	public static int getPerM3FromTotalAndVolumeInMMBTu(final long totalCost, final long volumeInMMBTu, final int cvValue) {
		// TODO: Be careful of overflows here
		return (int) (((long) totalCost * HighScaleFactor) / (long) volumeInMMBTu) * ScaleFactor / cvValue;

	}

	/**
	 * 
	 * @since 2.0
	 */
	public static int getPerM3FromTotalAndVolumeInM3(final long totalCost, final long volumeInM3) {

		// TODO: Be careful of overflows here
		return (int) (((long) totalCost * HighScaleFactor) / (long) volumeInM3);
	}

	/**
	 * Convert a $/m3 price to a $/MMBTu price
	 * 
	 * @since 2.0
	 */
	public static int getShareOfPrice(final int share, final int price) {
		return (int) (((long) share * (long) price) / HighScaleFactor);
	}

	/**
	 * Scale an integer value by the scaling factor
	 * 
	 * @param value
	 * @return scaled version of value
	 */
	private static long scale(final int value) {
		return ScaleFactor * value;
	}

	/**
	 * @since 2.0
	 */
	private static long scale(final long value) {
		return ScaleFactor * value;
	}

	private static long descale(final long value) {
		return value / ScaleFactor;
	}

	/**
	 * Scale a float
	 * 
	 * @param f
	 * @return scaled float
	 */
	private static long scale(final float f) {
		return (long) (ScaleFactor * f);
	}

	private static int scaleToInt(final double d) {
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
	private static long divide(final long a, final long b) {
		return (a * ScaleFactor) / b;
	}

}