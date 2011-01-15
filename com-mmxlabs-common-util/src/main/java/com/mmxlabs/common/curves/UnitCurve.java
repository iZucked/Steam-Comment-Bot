/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

public final class UnitCurve implements ICurve {
	protected UnitCurve() {
		
	}
	
	private static final UnitCurve INSTANCE = new UnitCurve();
	
	@Override
	public double getValueAtPoint(double point) {
		return 1;
	}

	public static ICurve getInstance() {
		return INSTANCE;
	}
}
