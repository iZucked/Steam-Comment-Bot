/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;

public class HashMapMultiMatrixProviderTest {

	@Test
	public void testHashMapMatrixGetSet() {

		final String key = "key";

		@SuppressWarnings("unchecked")
		final IMatrixProvider<Integer, Integer> p = Mockito.mock(IMatrixProvider.class);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>();

		Assert.assertNull(provider.get(null));
		Assert.assertNull(provider.get(key));

		provider.set(key, p);

		Assert.assertNull(provider.get(null));
		Assert.assertSame(p, provider.get(key));
	}

	@Test
	public void testHashMapMatrixContainsKey() {

		final String key = "key";
		final String key2 = "key2";

		@SuppressWarnings("unchecked")
		final IMatrixProvider<Integer, Integer> p = Mockito.mock(IMatrixProvider.class);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>();

		Assert.assertFalse(provider.containsKey(null));
		Assert.assertFalse(provider.containsKey(key));
		Assert.assertFalse(provider.containsKey(key2));

		provider.set(key, p);

		Assert.assertFalse(provider.containsKey(null));
		Assert.assertTrue(provider.containsKey(key));
		Assert.assertFalse(provider.containsKey(key2));
	}

	@Test
	public void testHashMapMatrixGetKeys() {

		final String key = "key";
		final String key2 = "key2";

		@SuppressWarnings("unchecked")
		final IMatrixProvider<Integer, Integer> p = Mockito.mock(IMatrixProvider.class);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>();

		String[] keys = provider.getKeys();
		Assert.assertNotNull(keys);
		Assert.assertEquals(0, keys.length);

		provider.set(key, p);

		keys = provider.getKeys();
		Assert.assertNotNull(keys);
		Assert.assertEquals(1, keys.length);

		Assert.assertSame(key, keys[0]);

		provider.set(key2, p);

		keys = provider.getKeys();
		Assert.assertNotNull(keys);
		Assert.assertEquals(2, keys.length);

		// Due to set, we cannot be sure of the order.
		Assert.assertTrue((key == keys[0]) || (key == keys[1]));
		Assert.assertTrue((key2 == keys[0]) || (key2 == keys[1]));
		Assert.assertTrue(keys[0] != keys[1]);
	}
}
