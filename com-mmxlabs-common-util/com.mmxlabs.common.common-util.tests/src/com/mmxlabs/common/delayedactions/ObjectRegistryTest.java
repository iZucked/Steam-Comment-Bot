/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions;

import org.junit.Assert;
import org.junit.Test;

public class ObjectRegistryTest {

	@Test
	public void testObjectRegistry() {
		final Class<Object> c1 = Object.class;
		final Class<String> c2 = String.class;

		final Object o1 = new Object();

		final Object k1 = new Object();
		final Object k2 = new Object();
		final Object k3 = new Object();

		final ObjectRegistry registry = new ObjectRegistry();

		Assert.assertFalse(registry.containsValue(c1, k1));
		Assert.assertFalse(registry.containsValue(c2, k1));
		Assert.assertNull(registry.getValue(c1, k1));

		Assert.assertFalse(registry.containsValue(c1, k2));
		Assert.assertFalse(registry.containsValue(c2, k2));
		Assert.assertNull(registry.getValue(c1, k2));

		registry.setValue(c1, k1, o1);
		registry.setValue(c1, k3, o1);

		Assert.assertTrue(registry.containsValue(c1, k1));
		Assert.assertFalse(registry.containsValue(c2, k1));
		Assert.assertSame(o1, registry.getValue(c1, k1));

		Assert.assertFalse(registry.containsValue(c1, k2));
		Assert.assertFalse(registry.containsValue(c2, k2));
		Assert.assertNull(registry.getValue(c1, k2));

		// Test second code branch - class already used.
		Assert.assertTrue(registry.containsValue(c1, k3));
		Assert.assertFalse(registry.containsValue(c2, k3));
		Assert.assertSame(o1, registry.getValue(c1, k3));

		registry.dispose();

		Assert.assertFalse(registry.containsValue(c1, k1));
		Assert.assertFalse(registry.containsValue(c2, k1));
		Assert.assertNull(registry.getValue(c1, k1));

		Assert.assertFalse(registry.containsValue(c1, k2));
		Assert.assertFalse(registry.containsValue(c2, k2));
		Assert.assertNull(registry.getValue(c1, k2));
	}
}
