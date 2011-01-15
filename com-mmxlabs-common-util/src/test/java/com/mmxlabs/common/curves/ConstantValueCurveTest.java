/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.junit.Assert;
import org.junit.Test;

public final class ConstantValueCurveTest {

	@Test
	public void testGetValueAtPoint() {
		final double v = 1.2345;
		final ConstantValueCurve curve = new ConstantValueCurve(v);
		Assert.assertEquals(v, curve.getValueAtPoint(-1000.0), 0);
		Assert.assertEquals(v, curve.getValueAtPoint(1000.0), 0);
		Assert.assertEquals(v, curve.getValueAtPoint(Double.MAX_VALUE), 0);
	}
}
