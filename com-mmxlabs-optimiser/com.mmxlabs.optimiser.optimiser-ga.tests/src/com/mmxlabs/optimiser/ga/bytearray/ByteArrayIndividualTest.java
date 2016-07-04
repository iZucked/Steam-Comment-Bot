/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.bytearray;

import org.junit.Assert;
import org.junit.Test;

public class ByteArrayIndividualTest {

	@Test
	public void testClone() {
		final byte[] bytes = new byte[] { (byte) 0xFE, (byte) 0xED };
		final ByteArrayIndividual original = new ByteArrayIndividual(bytes);

		final ByteArrayIndividual clone = original.clone();
		Assert.assertNotNull(clone);

		// Ensure equals & hashcode are the same
		Assert.assertEquals(original, clone);
		Assert.assertArrayEquals(original.bytes, clone.bytes);
		Assert.assertEquals(original.hashCode(), clone.hashCode());
		Assert.assertEquals(original.hashBytes(), clone.hashBytes());

		// However, arrays should be different instances
		Assert.assertNotSame(original.bytes, clone.bytes);
	}
}
