/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.junit.Assert;
import org.junit.Test;

public final class ConstantValueCurveTest {

	@Test
	public void testGetValueAtPoint() {
		final int v = 12345;
		final ConstantValueCurve curve = new ConstantValueCurve(v);
		Assert.assertEquals(v, curve.getValueAtPoint(-1000));
		Assert.assertEquals(v, curve.getValueAtPoint(1000));
		Assert.assertEquals(v, curve.getValueAtPoint(Integer.MAX_VALUE));
	}
}
