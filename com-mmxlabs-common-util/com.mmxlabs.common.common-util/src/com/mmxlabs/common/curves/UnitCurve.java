/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.eclipse.jdt.annotation.NonNull;

public final class UnitCurve implements ICurve {
	protected UnitCurve() {

	}

	@NonNull
	private static final UnitCurve INSTANCE = new UnitCurve();

	@Override
	public int getValueAtPoint(final int point) {
		return 1;
	}

	@NonNull
	public static UnitCurve getInstance() {
		return INSTANCE;
	}
}
