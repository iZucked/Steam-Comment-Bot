/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common;

import org.junit.Assert;
import org.junit.Test;

public class MatrixEntryTest {

	@Test
	public void testMatrixEntry() {

		final String key = "key";
		final String x = "x";
		final String y = "y";
		final Integer value = 1;

		final MatrixEntry<String, Integer> entry = new MatrixEntry<String, Integer>(key, x, y, value);

		Assert.assertSame(key, entry.getKey());
		Assert.assertSame(x, entry.getX());
		Assert.assertSame(y, entry.getY());
		Assert.assertSame(value, entry.getValue());
	}

	@Test
	public void testCompareTo() {
		final String key1 = "key1";
		final String x1 = "x1";
		final String y1 = "y1";
		final Integer value1 = 1;

		final MatrixEntry<String, Integer> entry1 = new MatrixEntry<String, Integer>(key1, x1, y1, value1);

		final String key2 = "key2";
		final String x2 = "x2";
		final String y2 = "y2";
		final Integer value2 = 2;

		final MatrixEntry<String, Integer> entry2 = new MatrixEntry<String, Integer>(key2, x2, y2, value2);

		Assert.assertEquals(0, entry1.compareTo(entry1));
		Assert.assertEquals(0, entry2.compareTo(entry2));

		Assert.assertEquals(value1.compareTo(value2), entry1.compareTo(entry2));
		Assert.assertEquals(value2.compareTo(value1), entry2.compareTo(entry1));

	}

	@Test(expected = NullPointerException.class)
	public void testCompareToNull() {
		final String key1 = "key1";
		final String x1 = "x1";
		final String y1 = "y1";
		final Integer value1 = 1;

		final MatrixEntry<String, Integer> entry1 = new MatrixEntry<String, Integer>(key1, x1, y1, value1);

		entry1.compareTo(null);
	}

}
