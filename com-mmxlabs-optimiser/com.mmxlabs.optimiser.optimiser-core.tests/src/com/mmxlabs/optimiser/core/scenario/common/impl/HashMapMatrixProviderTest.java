/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common.impl;

import org.junit.Assert;
import org.junit.Test;

public class HashMapMatrixProviderTest {

	@Test
	public void testHashMapMatrixProviderString() {

		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>();

		Assert.assertSame(provider.getDefaultValue(), provider.get(x, y));
	}

	@Test
	public void testHashMapMatrixProviderStringU() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(d);
		Assert.assertSame(d, provider.getDefaultValue());

		Assert.assertSame(d, provider.get(x, y));
	}

	@Test
	public void testGetSet() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(333);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(x, y));

		Assert.assertFalse(provider.has(x, y));

		provider.set(x, y, v);

		Assert.assertTrue(provider.has(x, y));

		Assert.assertSame(v, provider.get(x, y));
		Assert.assertSame(d, provider.get(y, x));
	}

	@Test
	public void testGetSet2() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer y2 = Integer.valueOf(3);
		final Integer v = Integer.valueOf(333);

		final Integer v2 = Integer.valueOf(334);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(x, y));

		Assert.assertFalse(provider.has(x, y));
		Assert.assertFalse(provider.has(x, y2));

		provider.set(x, y, v);
		provider.set(x, y2, v2);

		Assert.assertTrue(provider.has(x, y));
		Assert.assertTrue(provider.has(x, y2));

		Assert.assertSame(v, provider.get(x, y));
		Assert.assertSame(v2, provider.get(x, y2));

		Assert.assertSame(d, provider.get(y, x));
		Assert.assertSame(d, provider.get(y2, x));

	}

	@Test
	public void testGetSet3() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer y2 = Integer.valueOf(3);
		final Integer v = Integer.valueOf(333);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(x, y));

		provider.set(x, y, v);

		Assert.assertSame(v, provider.get(x, y));
		Assert.assertSame(d, provider.get(x, y2));

		Assert.assertSame(d, provider.get(y, x));
		Assert.assertSame(d, provider.get(y2, x));
	}

	@Test
	public void testDefaultValue() {

		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(321);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(x, y));

		provider.setDefaultValue(v);

		Assert.assertSame(v, provider.getDefaultValue());
		Assert.assertSame(v, provider.get(x, y));
	}

	@Test
	public void testHas() {

		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(321);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(d);

		Assert.assertFalse(provider.has(x, y));
		Assert.assertFalse(provider.has(y, x));
		provider.set(x, y, v);
		Assert.assertTrue(provider.has(x, y));
		Assert.assertFalse(provider.has(y, x));
	}
}
