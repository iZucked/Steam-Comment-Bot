/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

/**
 * An interface for a curve, which uses long precision.
 * 
 */
public interface ILongCurve {
	
	static final int zeroChangePoints[] = new int[0];
	
	/**
	 * Get the first point where the values in the curve change.
	 * @return empty array, if constant value, array times in hours otherwise.
	 */
	default int[] getChangePoints() { return zeroChangePoints; }
	
	/**
	 * Get the value at a particular point in time in hours.
	 * @param point in hours.
	 * @return a value.
	 */
	long getValueAtPoint(int point);
}
