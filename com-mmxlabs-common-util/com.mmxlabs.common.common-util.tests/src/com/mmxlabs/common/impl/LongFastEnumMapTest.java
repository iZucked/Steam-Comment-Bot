/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongFastEnumMapTest {

	enum Enum1 {
		Enum1_A, Enum1_B;
	}

	@Test
	public void testHashCode() {

		final LongFastEnumMap<Enum1> map1 = new LongFastEnumMap<>(1);

		final LongFastEnumMap<Enum1> map2 = new LongFastEnumMap<>(1);

		Assertions.assertEquals(map1.hashCode(), map2.hashCode());

		map1.put(Enum1.Enum1_A, 5l);
		Assertions.assertFalse(map1.hashCode() == map2.hashCode());

		map2.put(Enum1.Enum1_A, 5l);
		Assertions.assertEquals(map1.hashCode(), map2.hashCode());
	}

	@Test
	public void testGetPut() {
		final LongFastEnumMap<Enum1> map = new LongFastEnumMap<>(Enum1.values().length);

		Assertions.assertEquals(0l, map.get(Enum1.Enum1_A));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_B));

		map.put(Enum1.Enum1_A, 5l);
		Assertions.assertEquals(5l, map.get(Enum1.Enum1_A));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_B));

		map.put(Enum1.Enum1_B, 10l);

		Assertions.assertEquals(5l, map.get(Enum1.Enum1_A));
		Assertions.assertEquals(10l, map.get(Enum1.Enum1_B));
	}

	@Test
	public void testGetPut2() {
		final LongFastEnumMap<Enum1> map = new LongFastEnumMap<>(1);

		map.get(Enum1.Enum1_A);
		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.get(Enum1.Enum1_B));
	}

	@Test
	public void testGetPutAll() {
		final LongFastEnumMap<Enum1> map = new LongFastEnumMap<>(Enum1.values().length);
		final LongFastEnumMap<Enum1> map2 = new LongFastEnumMap<>(Enum1.values().length);

		map.put(Enum1.Enum1_A, 5l);
		map2.put(Enum1.Enum1_B, 10l);

		Assertions.assertEquals(0l, map2.get(Enum1.Enum1_A));
		Assertions.assertEquals(10l, map2.get(Enum1.Enum1_B));

		map2.putAll(map);

		Assertions.assertEquals(5l, map2.get(Enum1.Enum1_A));
		Assertions.assertEquals(0l, map2.get(Enum1.Enum1_B));
	}

	@Test
	public void testEqualsObject() {
		final LongFastEnumMap<Enum1> map1 = new LongFastEnumMap<>(1);

		final LongFastEnumMap<Enum1> map2 = new LongFastEnumMap<>(1);

		Assertions.assertEquals(map1, map2);

		map1.put(Enum1.Enum1_A, 5l);
		Assertions.assertFalse(map1.equals(map2));

		map2.put(Enum1.Enum1_A, 5l);
		Assertions.assertFalse(map1.equals(new Object()));
		Assertions.assertFalse(map1.equals(null));
	}

}
