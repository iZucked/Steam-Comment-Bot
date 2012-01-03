/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

/**
 * Implementation of {@link ICurve} which returns a single constant value
 * regardless of the input value.
 * 
 * @author Simon Goodall
 * 
 */
public final class ConstantValueCurve implements ICurve {

	private final double value;

	public ConstantValueCurve(final double value) {
		this.value = value;
	}

	@Override
	public final double getValueAtPoint(final double point) {
		return value;
	}
}
