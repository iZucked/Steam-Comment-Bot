/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.bytearray;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteArrayIndividualTest {

	@Test
	public void testClone() {
		final byte[] bytes = new byte[] { (byte) 0xFE, (byte) 0xED };
		final ByteArrayIndividual original = new ByteArrayIndividual(bytes);

		final ByteArrayIndividual clone = original.clone();
		Assertions.assertNotNull(clone);

		// Ensure equals & hashcode are the same
		Assertions.assertEquals(original, clone);
		Assertions.assertArrayEquals(original.bytes, clone.bytes);
		Assertions.assertEquals(original.hashCode(), clone.hashCode());
		Assertions.assertEquals(original.hashBytes(), clone.hashBytes());

		// However, arrays should be different instances
		Assertions.assertNotSame(original.bytes, clone.bytes);
	}
}
