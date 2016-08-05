/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

/**
 * A utility class to convert to/from internal/external optimiser units. The optimiser uses integer arithmetic internally and so external floating point numbers need to be scaled appropriately for use
 * within the optimiser and values being extracted back out will need the reverse conversion.
 * 
 * @author Simon Goodall
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class OptimiserUnitConvertor {

	/**
	 * Utility class, no instantiation permitted.
	 */
	private OptimiserUnitConvertor() {

	}

	/**
	 * Convert to internal price units. Such prices are expected to be in the form $/MMBTu or $/MT
	 * 
	 * @param price
	 * @return
	 */
	public static int convertToInternalPrice(final double price) {
		final long value = Math.round(price * (double) Calculator.HighScaleFactor);
		return (int) value;
	}

	public static double convertToExternalPrice(final int price) {
		return (double) price / (double) Calculator.HighScaleFactor;
	}

	/**
	 * Convert to internal rate units. Such rates are expected to be e.g. m3/day of boiloff or MT/day of base fuel.
	 * 
	 * @param rate
	 * @return
	 */
	public static int convertToInternalDailyRate(final double dailyRate) {
		return (int) ((double) Calculator.ScaleFactor * dailyRate);
	}

	public static double convertToExternalDailyRate(final int dailyRate) {
		return (double) dailyRate / (double) Calculator.ScaleFactor;
	}

	/**
	 * Convert to internal conversion factor. Such factors are expected to be small numbers e.g. conversion factor between LNG and base fuel or a scale factor for a price curve or tax rate.
	 * 
	 * @param factor
	 * @return
	 */
	public static int convertToInternalConversionFactor(final double factor) {
		return (int) (Calculator.HighScaleFactor * factor);
	}

	public static double convertToExternalConversionFactor(final int factor) {
		return (double) factor / (double) Calculator.HighScaleFactor;
	}

	public static double convertToExternalConversionFactor(final long factor) {
		return (double) factor / (double) Calculator.HighScaleFactor;
	}

	/**
	 * Convert to internal speed units.
	 * 
	 * @param speed
	 * @return
	 */
	public static int convertToInternalSpeed(final double speed) {
		return (int) (Calculator.ScaleFactor * speed);
	}

	public static double convertToExternalSpeed(final int speed) {
		return (double) speed / (double) Calculator.ScaleFactor;
	}

	/**
	 * Convert to internal volume. This is expected to be a larger number with no floating point. E.g. Vessel capacity, cargo load or discharge limits etc.
	 * 
	 * @param volume
	 * @return
	 */
	public static long convertToInternalVolume(final int volume) {
		return (long) volume * (long) Calculator.ScaleFactor;
	}

	public static long convertToInternalVolume(final double volume) {
		return (long) Math.round(volume * (double) Calculator.ScaleFactor);
	}

	public static int convertToExternalVolume(final long volume) {
		return (int) (volume / (long) Calculator.ScaleFactor);
	}

	/**
	 * Convert to internal daily rate. This is expected to be larger numbers such as daily charter costs
	 * 
	 * @param dailyCost
	 * @return
	 */
	public static long convertToInternalDailyCost(final int dailyCost) {
		return (long) dailyCost * (long) Calculator.ScaleFactor;
	}

	public static long convertToInternalDailyCost(final double dailyCost) {
		return (long) Math.round(dailyCost * (double) Calculator.ScaleFactor);
	}

	public static int convertToExternalDailyCost(final long dailyCost) {
		return (int) (dailyCost / (long) Calculator.ScaleFactor);
	}

	/**
	 * Convert to internal hourly rate. This is expected to be larger numbers such as daily charter costs
	 * 
	 * @param dailyCost
	 * @return
	 */
	public static long convertToInternalHourlyCost(final int dailyCost) {
		return (long) dailyCost * (long) Calculator.ScaleFactor / 24L;
	}

	public static int convertToExernalHourlyCost(final long hourlyCost) {
		return (int) (hourlyCost * 24L / (long) Calculator.ScaleFactor);
	}

	/**
	 * Convert to internal fixed costs units. Such costs are expected to be large but a fixed quantity per use. For example port costs or canal costs.
	 * 
	 * @param cost
	 * @return
	 */
	public static long convertToInternalFixedCost(final int cost) {
		return (long) cost * (long) Calculator.ScaleFactor;
	}

	public static long convertToInternalFixedCost(final long cost) {
		return cost * (long) Calculator.ScaleFactor;
	}

	public static int convertToExternalFixedCost(final long cost) {
		return (int) (cost / (long) Calculator.ScaleFactor);
	}

	public static long convertToInternalPercentage(final double percentage) {
		return (long) (percentage * ((double) Calculator.HighScaleFactor));
	}
}
