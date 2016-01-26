/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.junit.Assert;
import org.junit.Test;

public class UnitCurveTest {

	@Test
	public void testGetValueAtPoint() {

		final UnitCurve c = UnitCurve.getInstance();

		Assert.assertNotNull(c);

		for (int i = 0; i < 100; ++i) {
			Assert.assertEquals(1, c.getValueAtPoint(i));
		}
	}
}
