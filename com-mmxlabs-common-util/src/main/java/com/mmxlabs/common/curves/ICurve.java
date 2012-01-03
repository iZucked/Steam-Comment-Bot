/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

/**
 * An interface for a curve, which uses double precision.
 * 
 * @author hinton
 *
 */
public interface ICurve {
	double getValueAtPoint(final double point);
}
