/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnitCurveTest {

	@Test
	public void testGetValueAtPoint() {

		final UnitCurve c = UnitCurve.getInstance();

		Assertions.assertNotNull(c);

		for (int i = 0; i < 100; ++i) {
			Assertions.assertEquals(1, c.getValueAtPoint(i));
		}
	}
}
