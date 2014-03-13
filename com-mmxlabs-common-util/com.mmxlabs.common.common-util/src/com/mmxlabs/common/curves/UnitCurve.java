/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

public final class UnitCurve implements ICurve {
	protected UnitCurve() {

	}

	private static final UnitCurve INSTANCE = new UnitCurve();

	@Override
	public int getValueAtPoint(final int point) {
		return 1;
	}

	public static UnitCurve getInstance() {
		return INSTANCE;
	}
}
