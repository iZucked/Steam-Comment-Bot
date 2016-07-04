/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.eclipse.jdt.annotation.NonNull;

public final class UnitLongCurve implements ILongCurve {

	protected UnitLongCurve() {

	}

	@NonNull
	private static final UnitLongCurve INSTANCE = new UnitLongCurve();

	@Override
	public long getValueAtPoint(final int point) {
		return 1L;
	}

	@NonNull
	public static UnitLongCurve getInstance() {
		return INSTANCE;
	}
}
