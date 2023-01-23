/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

	int getValueAtPoint(int point);
}
