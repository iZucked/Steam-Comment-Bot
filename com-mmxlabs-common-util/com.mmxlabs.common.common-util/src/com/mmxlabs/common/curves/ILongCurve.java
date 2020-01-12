/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.Set;

/**
 * An interface for a curve, which uses long precision.
 * 
 */
public interface ILongCurve {
	
	/**
	 * Get the first point where the values in the curve change.
	 * @return empty set, if constant value, set of change points in hours otherwise.
	 */
	default NavigableSet<Integer> getChangePoints() { return Collections.emptyNavigableSet(); }
	
	/**
	 * Get the value at a particular point in time in hours.
	 * @param point in hours.
	 * @return a value.
	 */
	long getValueAtPoint(int point);
}
