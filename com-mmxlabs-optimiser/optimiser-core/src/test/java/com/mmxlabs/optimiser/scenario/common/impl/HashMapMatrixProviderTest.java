package com.mmxlabs.optimiser.scenario.common.impl;

import junit.framework.Assert;

import org.junit.Test;

public class HashMapMatrixProviderTest {

	@Test
	public void testHashMapMatrixProviderString() {

		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(
				"Name");
		Assert.assertEquals("Name", provider.getName());

		Assert.assertSame(provider.getDefaultValue(), provider.get(x, y));
	}

	@Test
	public void testHashMapMatrixProviderStringU() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(
				"Name", d);
		Assert.assertEquals("Name", provider.getName());
		Assert.assertSame(d, provider.getDefaultValue());

		Assert.assertSame(d, provider.get(x, y));
	}

	@Test
	public void testGetSet() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(333);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(
				"Name", d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(x, y));

		provider.set(x, y, v);

		Assert.assertSame(v, provider.get(x, y));
		Assert.assertSame(d, provider.get(y, x));

	}

	@Test
	public void testDispose() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(333);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(
				"Name", d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(x, y));

		provider.set(x, y, v);

		Assert.assertEquals(v, provider.get(x, y));
		Assert.assertEquals(d, provider.get(y, x));

		provider.dispose();

		Assert.assertSame(d, provider.get(x, y));
		Assert.assertSame(d, provider.get(x, y));

	}

	@Test
	public void testDefaultValue() {

		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(321);

		final HashMapMatrixProvider<Integer, Integer> provider = new HashMapMatrixProvider<Integer, Integer>(
				"Name", d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(x, y));

		provider.setDefaultValue(v);

		Assert.assertSame(v, provider.getDefaultValue());
		Assert.assertSame(v, provider.get(x, y));

	}
}
