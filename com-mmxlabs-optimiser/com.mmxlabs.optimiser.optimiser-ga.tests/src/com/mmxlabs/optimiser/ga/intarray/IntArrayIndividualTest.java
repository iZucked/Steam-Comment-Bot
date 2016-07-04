/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.intarray;

import org.junit.Assert;
import org.junit.Test;

public class IntArrayIndividualTest {

	@Test
	public void testClone() {
		final int[] ints = new int[] { 0xFE, 0xED };
		final IntArrayIndividual original = new IntArrayIndividual(ints);

		final IntArrayIndividual clone = original.clone();
		Assert.assertNotNull(clone);

		// Ensure equals & hashcode are the same
		Assert.assertEquals(original, clone);
		Assert.assertArrayEquals(original.ints, clone.ints);
		Assert.assertEquals(original.hashCode(), clone.hashCode());
		Assert.assertEquals(original.hashBytes(), clone.hashBytes());

		// However, arrays should be different instances
		Assert.assertNotSame(original.ints, clone.ints);
	}
}
