/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author hinton
 * 
 */
public class PreGeneratedIntegerCurveTest {
	@Test
	public void testDefaultValue() {
		final PreGeneratedIntegerCurve c = new PreGeneratedIntegerCurve();
		c.setDefaultValue(12);

		Assertions.assertEquals(12, c.getDefaultValue());

		Assertions.assertEquals(12, c.getValueAtPoint(123));
		c.setValueAfter(10, 44);

		Assertions.assertEquals(12, c.getValueAtPoint(9));

		Assertions.assertEquals(44, c.getValueAtPoint(10));
	}

	@Test
	public void testSeveralSegments() {
		final PreGeneratedIntegerCurve c = new PreGeneratedIntegerCurve();

		c.setDefaultValue(0);

		for (int i = 0; i < 100; i++) {
			c.setValueAfter(i, i * 2);
		}

		for (int i = 0; i < 100; i++) {
			Assertions.assertEquals(c.getValueAtPoint(i), i * 2L);
		}
	}
}
