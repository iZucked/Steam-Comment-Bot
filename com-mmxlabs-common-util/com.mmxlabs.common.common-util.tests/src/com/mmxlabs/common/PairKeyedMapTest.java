/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.Assert;
import org.junit.Test;

public class PairKeyedMapTest {

	@Test
	public void testPairKeyMap() {

		final PairKeyedMap<String, String, Object> map = new PairKeyedMap<String, String, Object>();

		final String key1 = "key1";
		final String key2 = "key2";

		final String key3 = "key3";

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final Pair<String, String> p1 = new Pair<String, String>(key1, key2);
		final Pair<String, String> p2 = new Pair<String, String>(key1, key3);

		Assert.assertFalse(map.containsKey(p1));
		Assert.assertFalse(map.containsKey(p2));

		Assert.assertFalse(map.containsValue(obj1));
		Assert.assertFalse(map.containsValue(obj2));

		map.put(key1, key2, obj1);

		Assert.assertTrue(map.containsKey(p1));
		Assert.assertFalse(map.containsKey(p2));

		Assert.assertTrue(map.containsValue(obj1));
		Assert.assertFalse(map.containsValue(obj2));

		Assert.assertSame(obj1, map.get(key1, key2));
		Assert.assertSame(obj1, map.get(p1));

		map.put(p2, obj2);

		Assert.assertTrue(map.containsKey(p1));
		Assert.assertTrue(map.containsKey(p2));

		Assert.assertTrue(map.containsValue(obj1));
		Assert.assertTrue(map.containsValue(obj2));

		Assert.assertSame(obj2, map.get(key1, key3));
		Assert.assertSame(obj2, map.get(p2));

		map.clear();

		Assert.assertFalse(map.containsKey(p1));
		Assert.assertFalse(map.containsKey(p2));

		Assert.assertFalse(map.containsValue(obj1));
		Assert.assertFalse(map.containsValue(obj2));

	}
}
