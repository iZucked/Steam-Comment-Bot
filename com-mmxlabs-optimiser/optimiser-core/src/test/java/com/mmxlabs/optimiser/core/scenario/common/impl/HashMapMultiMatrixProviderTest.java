/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common.impl;

import java.util.Set;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;

@RunWith(JMock.class)
public class HashMapMultiMatrixProviderTest {

	private final Mockery context = new JUnit4Mockery();

	@Test
	public void testHashMapMatrixProviderString() {

		final String name = "Name";
		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				name);
		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testHashMapMatrixGetSet() {

		final String key = "key";

		@SuppressWarnings("unchecked")
		final IMatrixProvider<Integer, Integer> p = context
				.mock(IMatrixProvider.class);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name");

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
		final IMatrixProvider<Integer, Integer> p = context
				.mock(IMatrixProvider.class);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name");

		Assert.assertFalse(provider.containsKey(null));
		Assert.assertFalse(provider.containsKey(key));
		Assert.assertFalse(provider.containsKey(key2));

		provider.set(key, p);

		Assert.assertFalse(provider.containsKey(null));
		Assert.assertTrue(provider.containsKey(key));
		Assert.assertFalse(provider.containsKey(key2));
	}

	@Test
	public void testHashMapMatrixGetKeySet() {

		final String key = "key";
		final String key2 = "key2";

		@SuppressWarnings("unchecked")
		final IMatrixProvider<Integer, Integer> p = context
				.mock(IMatrixProvider.class);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name");

		Set<String> keySet = provider.getKeySet();
		Assert.assertTrue(keySet.isEmpty());

		provider.set(key, p);

		keySet = provider.getKeySet();
		Assert.assertEquals(1, keySet.size());

		Assert.assertTrue(keySet.contains(key));
		Assert.assertFalse(keySet.contains(key2));
	}

	@Test
	public void testHashMapMatrixGetKeys() {

		final String key = "key";
		final String key2 = "key2";

		@SuppressWarnings("unchecked")
		final IMatrixProvider<Integer, Integer> p = context
				.mock(IMatrixProvider.class);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name");

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
		Assert.assertTrue(key == keys[0] || key == keys[1]);
		Assert.assertTrue(key2 == keys[0] || key2 == keys[1]);
		Assert.assertTrue(keys[0] != keys[1]);
	}

	@Test
	public void testHashMapMatrixDispose() {

		final String key = "key";

		@SuppressWarnings("unchecked")
		final IMatrixProvider<Integer, Integer> p = context
				.mock(IMatrixProvider.class);

		final HashMapMultiMatrixProvider<Integer, Integer> provider = new HashMapMultiMatrixProvider<Integer, Integer>(
				"Name");

		String[] keys = provider.getKeys();
		Assert.assertNotNull(keys);
		Assert.assertEquals(0, keys.length);

		Set<String> keySet = provider.getKeySet();
		Assert.assertTrue(keySet.isEmpty());

		Assert.assertNull(provider.get(key));

		Assert.assertFalse(provider.containsKey(key));

		provider.set(key, p);

		keys = provider.getKeys();
		Assert.assertNotNull(keys);
		Assert.assertEquals(1, keys.length);
		Assert.assertSame(key, keys[0]);

		keySet = provider.getKeySet();
		Assert.assertTrue(keySet.contains(key));

		Assert.assertTrue(provider.containsKey(key));

		Assert.assertSame(p, provider.get(key));

		provider.dispose();

		keys = provider.getKeys();
		Assert.assertNotNull(keys);
		Assert.assertEquals(0, keys.length);

		keySet = provider.getKeySet();
		Assert.assertTrue(keySet.isEmpty());

		Assert.assertNull(provider.get(key));

		Assert.assertFalse(provider.containsKey(key));

	}

}
