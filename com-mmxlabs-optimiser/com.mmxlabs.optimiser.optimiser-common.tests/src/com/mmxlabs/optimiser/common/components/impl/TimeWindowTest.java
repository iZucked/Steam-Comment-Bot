/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeWindowTest {

	@Test
	public void testTimeWindow() {

		final int start = 10;
		final int end = 15;

		final TimeWindow tw = new TimeWindow(start, end);

		Assertions.assertEquals(start, tw.getInclusiveStart());
		Assertions.assertEquals(end, tw.getExclusiveEnd());
	}

	@Test
	public void testEquals() {
		final TimeWindow tw1 = new TimeWindow(10, 20);
		final TimeWindow tw2 = new TimeWindow(10, 20);
		final TimeWindow tw3 = new TimeWindow(20, 20);
		final TimeWindow tw4 = new TimeWindow(20, 10);
		final TimeWindow tw5 = new TimeWindow(10, 10);

		Assertions.assertTrue(tw1.equals(tw1));

		Assertions.assertTrue(tw1.equals(tw2));
		Assertions.assertTrue(tw2.equals(tw1));

		Assertions.assertFalse(tw1.equals(tw3));
		Assertions.assertFalse(tw1.equals(tw4));
		Assertions.assertFalse(tw1.equals(tw5));

		Assertions.assertFalse(tw3.equals(tw1));
		Assertions.assertFalse(tw4.equals(tw1));
		Assertions.assertFalse(tw5.equals(tw1));

		Assertions.assertFalse(tw1.equals(new Object()));
	}

	@Test
	public void testOverlaps() {
		final TimeWindow tw1 = new TimeWindow(10, 20);
		final TimeWindow tw2 = new TimeWindow(10, 20);
		final TimeWindow tw3 = new TimeWindow(20, 21);
		final TimeWindow tw4 = new TimeWindow(1, 10);
		final TimeWindow tw5 = new TimeWindow(10, 11);

		Assertions.assertTrue(tw1.overlaps(tw1)); // Identity
		Assertions.assertTrue(tw1.overlaps(tw2)); // Equivalence
		Assertions.assertFalse(tw1.overlaps(tw3)); // Out of bounds
		Assertions.assertFalse(tw1.overlaps(tw4)); // Out of bounds
		Assertions.assertTrue(tw1.overlaps(tw5)); // Intersects
		Assertions.assertTrue(tw1.overlaps(tw5)); // Intersects
	}
}
