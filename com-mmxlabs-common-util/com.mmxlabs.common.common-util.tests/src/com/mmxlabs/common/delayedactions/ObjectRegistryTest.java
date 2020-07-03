/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

		Assertions.assertFalse(registry.containsValue(c1, k1));
		Assertions.assertFalse(registry.containsValue(c2, k1));
		Assertions.assertNull(registry.getValue(c1, k1));

		Assertions.assertFalse(registry.containsValue(c1, k2));
		Assertions.assertFalse(registry.containsValue(c2, k2));
		Assertions.assertNull(registry.getValue(c1, k2));

		registry.setValue(c1, k1, o1);
		registry.setValue(c1, k3, o1);

		Assertions.assertTrue(registry.containsValue(c1, k1));
		Assertions.assertFalse(registry.containsValue(c2, k1));
		Assertions.assertSame(o1, registry.getValue(c1, k1));

		Assertions.assertFalse(registry.containsValue(c1, k2));
		Assertions.assertFalse(registry.containsValue(c2, k2));
		Assertions.assertNull(registry.getValue(c1, k2));

		// Test second code branch - class already used.
		Assertions.assertTrue(registry.containsValue(c1, k3));
		Assertions.assertFalse(registry.containsValue(c2, k3));
		Assertions.assertSame(o1, registry.getValue(c1, k3));

		registry.dispose();

		Assertions.assertFalse(registry.containsValue(c1, k1));
		Assertions.assertFalse(registry.containsValue(c2, k1));
		Assertions.assertNull(registry.getValue(c1, k1));

		Assertions.assertFalse(registry.containsValue(c1, k2));
		Assertions.assertFalse(registry.containsValue(c2, k2));
		Assertions.assertNull(registry.getValue(c1, k2));
	}
}
