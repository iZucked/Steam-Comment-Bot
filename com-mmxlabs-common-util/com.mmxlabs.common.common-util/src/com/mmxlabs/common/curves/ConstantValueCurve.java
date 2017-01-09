/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

/**
 * Implementation of {@link ICurve} which returns a single constant value regardless of the input value.
 * 
 * @author Simon Goodall
 * 
 */
public final class ConstantValueCurve implements ICurve {

	private final int value;

	public ConstantValueCurve(final int value) {
		this.value = value;
	}

	@Override
	public final int getValueAtPoint(final int point) {
		return value;
	}
}
