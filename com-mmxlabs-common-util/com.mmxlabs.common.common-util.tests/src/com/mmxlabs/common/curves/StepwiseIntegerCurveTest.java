/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author hinton
 * 
 */
public class StepwiseIntegerCurveTest {
	@Test
	public void testDefaultValue() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();
		c.setDefaultValue(12);

		Assert.assertEquals(12, c.getDefaultValue());

		Assert.assertEquals(12, c.getValueAtPoint(123));
		c.setValueAfter(10, 44);

		Assert.assertEquals(12, c.getValueAtPoint(9));

		Assert.assertEquals(44, c.getValueAtPoint(10));
	}

	@Test
	public void testSeveralSegments() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();

		c.setDefaultValue(0);

		for (int i = 0; i < 100; i++) {
			c.setValueAfter(i, i * 2);
		}

		for (int i = 0; i < 100; i++) {
			Assert.assertEquals(c.getValueAtPoint(i), i * 2, 0);
		}
	}
}
