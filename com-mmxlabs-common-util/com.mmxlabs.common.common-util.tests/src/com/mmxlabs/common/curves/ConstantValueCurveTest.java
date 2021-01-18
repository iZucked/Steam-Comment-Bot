/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class ConstantValueCurveTest {

	@Test
	public void testGetValueAtPoint() {
		final int v = 12345;
		final ConstantValueCurve curve = new ConstantValueCurve(v);
		Assertions.assertEquals(v, curve.getValueAtPoint(-1000));
		Assertions.assertEquals(v, curve.getValueAtPoint(1000));
		Assertions.assertEquals(v, curve.getValueAtPoint(Integer.MAX_VALUE));
	}
}
