/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PairKeyedMapTest {

	@Test
	public void testPairKeyMap() {

		final PairKeyedMap<String, String, Object> map = new PairKeyedMap<>();

		final String key1 = "key1";
		final String key2 = "key2";

		final String key3 = "key3";

		final Object obj1 = new Object();
		final Object obj2 = new Object();

		final Pair<String, String> p1 = new Pair<>(key1, key2);
		final Pair<String, String> p2 = new Pair<>(key1, key3);

		Assertions.assertFalse(map.containsKey(p1));
		Assertions.assertFalse(map.containsKey(p2));

		Assertions.assertFalse(map.containsValue(obj1));
		Assertions.assertFalse(map.containsValue(obj2));

		map.put(key1, key2, obj1);

		Assertions.assertTrue(map.containsKey(p1));
		Assertions.assertFalse(map.containsKey(p2));

		Assertions.assertTrue(map.containsValue(obj1));
		Assertions.assertFalse(map.containsValue(obj2));

		Assertions.assertSame(obj1, map.get(key1, key2));
		Assertions.assertSame(obj1, map.get(p1));

		map.put(p2, obj2);

		Assertions.assertTrue(map.containsKey(p1));
		Assertions.assertTrue(map.containsKey(p2));

		Assertions.assertTrue(map.containsValue(obj1));
		Assertions.assertTrue(map.containsValue(obj2));

		Assertions.assertSame(obj2, map.get(key1, key3));
		Assertions.assertSame(obj2, map.get(p2));

		map.clear();

		Assertions.assertFalse(map.containsKey(p1));
		Assertions.assertFalse(map.containsKey(p2));

		Assertions.assertFalse(map.containsValue(obj1));
		Assertions.assertFalse(map.containsValue(obj2));

	}
}
