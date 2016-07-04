/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

/**
 * Implementation of {@link ICurve} which returns a single constant value regardless of the input value.
 * 
 * @author Simon Goodall
 * 
 */
public final class ConstantValueLongCurve implements ILongCurve {

	private final long value;

	public ConstantValueLongCurve(final long value) {
		this.value = value;
	}

	@Override
	public final long getValueAtPoint(final int point) {
		return value;
	}
}
