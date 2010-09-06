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

		return (int) ((distance * ScaleFactor) / time);
	}

	public static int getTimeFromSpeedDistance(int speed, long distance) {

		return (int) ((distance * ScaleFactor) / speed);
	}

	public static long quantityFromRateTime(long rate, int time) {

		return (rate * time);
	}

	public static int getTimeFromRateQuantity(long rate, long quantity) {

		return (int) (quantity / rate);
	}

	public static long costFromConsumption(long consumption, int unitPrice) {
		return (consumption * unitPrice) / ScaleFactor;
	}

	public static long convertM3ToMMBTu(long m3, int factor) {
		return (m3 * factor) / ScaleFactor;
	}

	public static long convertMMBTuToM3(long mmbtu, int factor) {
		return (mmbtu * ScaleFactor) / factor;
	}

	public static long convertM3ToMT(long m3, int factor) {
		return (m3 * factor) / ScaleFactor;
	}

	public static long convertMTToM3(long mt, int factor) {
		return (mt * ScaleFactor) / factor;
	}
	
	/**
	 * Scale an integer value by the scaling factor
	 * @param value
	 * @return scaled version of value
	 */
	public static int scale(int value) {
		return ScaleFactor * value;	
	}

	/**
	 * Scale a float
	 * @param f
	 * @return scaled float
	 */
	public static long scale(float f) {
		return (long) (ScaleFactor * f);
	}
	
	public static int scaleToInt(double d) {
		return (int) (ScaleFactor * d);
	}
}