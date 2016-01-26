/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

/**
 * An interface for a curve, which uses integer precision.
 * 
 * @author hinton
 * 
 */
public interface ICurve {

	/**
	 * @since 2.0
	 */
	int getValueAtPoint(final int point);
}
