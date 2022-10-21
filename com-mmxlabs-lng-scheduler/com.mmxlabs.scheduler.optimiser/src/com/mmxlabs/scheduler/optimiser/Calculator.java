/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

/**
 * Class to handle integer based maths. Assume, unless otherwise noted each
 * input has been converted into a int/long by multiplying against the
 * {@link #ScaleFactor}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Calculator {
	// If you change this, you will need to change the getHourlyCharterPrice
	// operator
	// on AllocatedVessel in the ECore model.
	public static final int ScaleFactor = 1000;
	/**
	 */
	public static final long HighScaleFactor = 1000000;

	public static int speedFromDistanceTime(final long distance, final int time) {

		return (int) (Math.multiplyExact(distance, ScaleFactor) / time);
	}

	public static int getTimeFromSpeedDistance(final int speed, final long distance) {

		return (int) (Math.multiplyExact(distance, ScaleFactor) / speed);
	}

	public static long quantityFromRateTime(final long rate, final int time) {

		return Math.multiplyExact(rate, time);
	}

	public static int getTimeFromRateQuantity(final long rate, final long quantity) {

		return (int) (quantity / rate);
	}

	public static long costFromConsumption(final long consumption, final long notionalFuelCost) {
		return Math.multiplyExact(consumption, notionalFuelCost) / HighScaleFactor;
	}

	public static long costFromVolume(final long volume, final long price) {
		return Math.multiplyExact(volume, price) / HighScaleFactor;
	}

	public static long costFromDailyRateAndTimeInHours(final long rate, final int timeInHours) {

		return Math.multiplyExact(rate, timeInHours) / 24L;
	}

	public static long convertM3ToMMBTu(final long m3, final int factor) {

		return Math.multiplyExact(m3, factor) / HighScaleFactor;
	}

	public static long convertM3ToMMBTuWithOverflowProtection(final long volumeInM3, final int cv) {
		if (volumeInM3 == Long.MAX_VALUE) {
			return Long.MAX_VALUE;
		} else {
			return Calculator.convertM3ToMMBTu(volumeInM3, cv);
		}
	}

	/**
	 */
	public static long convertM3ToM3Price(final long m3, final int pricePerM3) {
		return Math.multiplyExact(m3, pricePerM3) / HighScaleFactor;
	}

	public static long convertMMBTuToM3(final long mmbtu, final int cv) {
		return Math.multiplyExact(mmbtu, HighScaleFactor) / (long) cv;
	}

	public static long convertMMBTuToM3WithOverflowProtection(final long volumeInMMBTu, final int cv) {
		if (volumeInMMBTu == Long.MAX_VALUE) {
			return Long.MAX_VALUE;
		} else {
			if (cv > 0) {
				return Calculator.convertMMBTuToM3(volumeInMMBTu, cv);
			} else {
				return 0L;
			}
		}
	}

	public static long convertM3ToMT(final long m3, final int cargoCV, final int equivalenceFactor) {
		return equivalenceFactor == 0 ? 0L : Math.multiplyExact((long) m3, cargoCV) / (long) equivalenceFactor;
	}

	public static long convertMTToM3(final long mt, final int cargoCV, final int equivalenceFactor) {
		return cargoCV == 0 ? 0 : Math.multiplyExact((long) mt, equivalenceFactor) / (long) cargoCV;
	}

	/**
	 * Convert a $/MMBTu price to a $/m3 price
	 * 
	 */
	public static int costPerM3FromMMBTu(final int costPerMMBTu, final int cvValue) {
		return (int) (Math.multiplyExact((long) costPerMMBTu, cvValue) / HighScaleFactor);
	}

	/**
	 * Convert a $/m3 price to a $/MMBTu price
	 * 
	 */
	public static int costPerMMBTuFromM3(final int costPerM3, final int cvValue) {

		return (int) (Math.multiplyExact((long) costPerM3, HighScaleFactor) / (long) cvValue);
	}

	/**
	 * 
	 */
	public static int getPerMMBTuFromTotalAndVolumeInMMBTu(final long totalCost, final long volumeInMMBTu) {
		// TODO: Be careful of overflows here
		return (int) (Math.multiplyExact((long) totalCost, HighScaleFactor) / (long) volumeInMMBTu);
	}

	/**
	 * 
	 */
	public static int getPerMMBTuFromTotalAndVolumeInM3(final long totalCost, final long volumeInM3, final int cvValue) {

		// TODO: Be careful of overflows here
		return (int) (Math.multiplyExact((long) totalCost, HighScaleFactor) / (long) convertM3ToMMBTu(volumeInM3, cvValue));
	}

	/**
	 * 
	 */
	public static int getPerM3FromTotalAndVolumeInMMBTu(final long totalCost, final long volumeInMMBTu, final int cvValue) {
		// TODO: Be careful of overflows here
		return (int) (Math.multiplyExact((long) totalCost, HighScaleFactor) / (long) volumeInMMBTu) * ScaleFactor / cvValue;

	}

	/**
	 * 
	 */
	public static int getPerM3FromTotalAndVolumeInM3(final long totalCost, final long volumeInM3) {

		// TODO: Be careful of overflows here
		return (int) (Math.multiplyExact((long) totalCost, HighScaleFactor) / (long) volumeInM3);
	}

	/**
	 * Take a price (in high scale factor) and multiply by a % share (range 0.0->1.0
	 * at high scale factor)
	 */
	public static int getShareOfPrice(final int hs_share, final int hs_price) {
		return (int) (Math.multiplyExact((long) hs_share, (long) hs_price) / HighScaleFactor);
	}

	/**
	 * Take a value (in low scale factor) and multiply by a % share (range 0.0->1.0
	 * at High scale factor)
	 */
	public static long getShareOfValue(final long hs_share, final long ls_value) {
		return Math.multiplyExact(ls_value, hs_share) / HighScaleFactor;
	}

	/**
	 * Scale an integer value by the scaling factor
	 * 
	 * @param value
	 * @return scaled version of value
	 */

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
	 * Multiply a int by a double, returning the result as a int, rounded
	 * 
	 * @param multiply
	 * @param d
	 * @return
	 */
	public static int multiply(final int multiply, final double d) {
		return (int) Math.round((multiply * d));
	}

	/**
	 * Simple multiplication between scaled integer based values
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long multiply(final long a, final long b) {
		return Math.multiplyExact(a, b) / ScaleFactor;
	}

	/**
	 * Simple multiplication between scaled integer based values
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long multiplyHigh(final long a, final long b) {
		return Math.multiplyExact(a, b) / HighScaleFactor;
	}

	/**
	 * Simple percentage (scaled by 1,000,000)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long percentageLow(final long percentage, final long input) {
		return Math.multiplyExact(input, percentage) / HighScaleFactor;
	}

	/**
	 * Converts from high scaling factor to low scaling factor unfortunately with
	 * loss of accuracy
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long convertFromHighToLow(final long input) {
		return input / (HighScaleFactor / ScaleFactor);
	}

}