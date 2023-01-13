/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.intarray;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntArrayIndividualTest {

	@Test
	public void testClone() {
		final int[] ints = new int[] { 0xFE, 0xED };
		final IntArrayIndividual original = new IntArrayIndividual(ints);

		final IntArrayIndividual clone = original.clone();
		Assertions.assertNotNull(clone);

		// Ensure equals & hashcode are the same
		Assertions.assertEquals(original, clone);
		Assertions.assertArrayEquals(original.ints, clone.ints);
		Assertions.assertEquals(original.hashCode(), clone.hashCode());
		Assertions.assertEquals(original.hashBytes(), clone.hashBytes());

		// However, arrays should be different instances
		Assertions.assertNotSame(original.ints, clone.ints);
	}
}
