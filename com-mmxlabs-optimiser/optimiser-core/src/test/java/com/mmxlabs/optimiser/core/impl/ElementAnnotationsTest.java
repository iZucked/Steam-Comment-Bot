/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.core.impl.ElementAnnotations;

public class ElementAnnotationsTest {
	private class Class1 {

	};

	private class Class2 {

	};

	@Test
	public void testGetPut1() {

		final ElementAnnotations info = new ElementAnnotations();

		final String key = "key";
		final Class1 object1 = new Class1();

		info.put(key, object1);

		Assert.assertSame(object1, info.get(key, Class1.class));
		Assert.assertNull(info.get("unknown", Class1.class));
	}

	@Test(expected = ClassCastException.class)
	public void testGetPut2() {

		final ElementAnnotations info = new ElementAnnotations();

		final String key = "key";
		final Class1 object1 = new Class1();

		info.put(key, object1);
		info.get(key, Class2.class);
	}

	@Test
	public void testContainsKey() {

		final ElementAnnotations info = new ElementAnnotations();

		final String key = "key";
		final Class1 object1 = new Class1();

		Assert.assertFalse(info.containsKey(key));

		info.put(key, object1);

		Assert.assertTrue(info.containsKey(key));
	}

	@Test
	public void testDispose() {

		final ElementAnnotations info = new ElementAnnotations();

		final String key = "key";
		final Class1 object1 = new Class1();

		Assert.assertFalse(info.containsKey(key));

		info.put(key, object1);

		Assert.assertTrue(info.containsKey(key));

		Assert.assertSame(object1, info.get(key, Class1.class));
		Assert.assertNull(info.get("unknown", Class1.class));

		info.dispose();

		Assert.assertFalse(info.containsKey(key));
	}

}
