/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components.impl;

import org.junit.Assert;
import org.junit.Test;

public class TimeWindowTest {

	@Test
	public void testTimeWindow() {

		final int start = 10;
		final int end = 15;

		final TimeWindow tw = new TimeWindow(start, end);

		Assert.assertEquals(start, tw.getInclusiveStart());
		Assert.assertEquals(end, tw.getExclusiveEnd());
	}

	@Test
	public void testEquals() {
		final TimeWindow tw1 = new TimeWindow(10, 20);
		final TimeWindow tw2 = new TimeWindow(10, 20);
		final TimeWindow tw3 = new TimeWindow(20, 20);
		final TimeWindow tw4 = new TimeWindow(20, 10);
		final TimeWindow tw5 = new TimeWindow(10, 10);

		Assert.assertTrue(tw1.equals(tw1));

		Assert.assertTrue(tw1.equals(tw2));
		Assert.assertTrue(tw2.equals(tw1));

		Assert.assertFalse(tw1.equals(tw3));
		Assert.assertFalse(tw1.equals(tw4));
		Assert.assertFalse(tw1.equals(tw5));

		Assert.assertFalse(tw3.equals(tw1));
		Assert.assertFalse(tw4.equals(tw1));
		Assert.assertFalse(tw5.equals(tw1));

		Assert.assertFalse(tw1.equals(new Object()));
	}

	@Test
	public void testOverlaps() {
		final TimeWindow tw1 = new TimeWindow(10, 20);
		final TimeWindow tw2 = new TimeWindow(10, 20);
		final TimeWindow tw3 = new TimeWindow(20, 21);
		final TimeWindow tw4 = new TimeWindow(1, 10);
		final TimeWindow tw5 = new TimeWindow(10, 11);
		final TimeWindow tw6 = new TimeWindow(11, 12);

		Assert.assertTrue(tw1.overlaps(tw1)); // Identity
		Assert.assertTrue(tw1.overlaps(tw2)); // Equivalence
		Assert.assertFalse(tw1.overlaps(tw3)); // Out of bounds
		Assert.assertFalse(tw1.overlaps(tw4)); // Out of bounds
		Assert.assertTrue(tw1.overlaps(tw5)); // Intersects
		Assert.assertTrue(tw1.overlaps(tw5)); // Intersects
	}
}
