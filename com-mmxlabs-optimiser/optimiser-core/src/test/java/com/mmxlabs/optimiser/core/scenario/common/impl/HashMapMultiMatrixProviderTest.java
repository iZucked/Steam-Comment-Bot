package com.mmxlabs.optimiser.core.scenario.common.impl;



import org.junit.Assert;
import org.junit.Test;

public class HashMapMultiMatrixProviderTest {

	@Test
	public void testHashMapMatrixProviderString() {

		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		
		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name");
		Assert.assertEquals("Name", provider.getName());

		Assert.assertSame(provider.getDefaultValue(), provider.get(null, x, y));
	}

	@Test
	public void testHashMapMatrixProviderStringU() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name", d);
		Assert.assertEquals("Name", provider.getName());
		Assert.assertSame(d, provider.getDefaultValue());

		Assert.assertSame(d, provider.get(null, x, y));
	}

	@Test
	public void testGetSet() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(333);

		final Object key1 = new Object();
		final Object key2 = new Object();
		
		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name", d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(key1, x, y));
		Assert.assertSame(d, provider.get(key2, x, y));

		provider.set(key1, x, y, v);

		Assert.assertSame(v, provider.get(key1, x, y));
		Assert.assertSame(d, provider.get(key1, y, x));
		Assert.assertSame(d, provider.get(key2, x, y));
	}

	@Test
	public void testGetSet2() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer y2 = Integer.valueOf(3);
		final Integer v = Integer.valueOf(333);

		final Integer v2 = Integer.valueOf(334);

		final Object key1 = new Object();
		final Object key2 = new Object();
		
		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name", d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(key1, x, y));
		Assert.assertSame(d, provider.get(key2, x, y));

		provider.set(key1, x, y, v);
		provider.set(key1, x, y2, v2);

		Assert.assertSame(v, provider.get(key1, x, y));
		Assert.assertSame(v2, provider.get(key1, x, y2));

		Assert.assertSame(d, provider.get(key2, x, y));
		Assert.assertSame(d, provider.get(key2, x, y2));
		
		Assert.assertSame(d, provider.get(key1, y, x));
		Assert.assertSame(d, provider.get(key1, y2, x));

	}

	@Test
	public void testGetSet3() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer y2 = Integer.valueOf(3);
		final Integer v = Integer.valueOf(333);

		final Object key1 = new Object();
		final Object key2 = new Object();
		
		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name", d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(key1, x, y));
		Assert.assertSame(d, provider.get(key2, x, y));

		provider.set(key1, x, y, v);

		Assert.assertSame(v, provider.get(key1, x, y));
		Assert.assertSame(d, provider.get(key1, x, y2));

		Assert.assertSame(d, provider.get(key2, x, y));
		Assert.assertSame(d, provider.get(key2, x, y2));
		
		Assert.assertSame(d, provider.get(key1, y, x));
		Assert.assertSame(d, provider.get(key1, y2, x));

	}

	@Test
	public void testDispose() {
		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(333);

		final Object key1 = new Object();
		final Object key2 = new Object();
		
		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name", d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(key1, x, y));
		Assert.assertSame(d, provider.get(key2, x, y));

		provider.set(key1, x, y, v);

		Assert.assertEquals(v, provider.get(key1, x, y));
		Assert.assertEquals(d, provider.get(key1, y, x));
		Assert.assertEquals(d, provider.get(key2, x, y));

		provider.dispose();

		Assert.assertSame(d, provider.get(key1, x, y));
		Assert.assertSame(d, provider.get(key2, x, y));

	}

	@Test
	public void testDefaultValue() {

		final Integer d = Integer.valueOf(123);
		final Integer x = Integer.valueOf(1);
		final Integer y = Integer.valueOf(2);
		final Integer v = Integer.valueOf(321);

		final Object key1 = new Object();
		final Object key2 = new Object();
		
		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name", d);

		Assert.assertSame(d, provider.getDefaultValue());
		Assert.assertSame(d, provider.get(key1, x, y));
		Assert.assertSame(d, provider.get(key2, x, y));

		provider.setDefaultValue(v);

		Assert.assertSame(v, provider.getDefaultValue());
		Assert.assertSame(v, provider.get(key1, x, y));
		Assert.assertSame(v, provider.get(key2, x, y));

	}
}
