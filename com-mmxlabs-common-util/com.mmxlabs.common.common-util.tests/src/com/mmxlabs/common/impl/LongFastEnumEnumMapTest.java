/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongFastEnumEnumMapTest {

	enum Enum1 {
		Enum1_A, Enum1_B;
	}

	enum Enum2 {
		Enum2_A, Enum2_B;
	}

	@Test
	public void testHashCode() {

		final LongFastEnumEnumMap<Enum1, Enum2> map1 = new LongFastEnumEnumMap<>(Enum1.values().length, Enum2.values().length);
		final LongFastEnumEnumMap<Enum1, Enum2> map2 = new LongFastEnumEnumMap<>(Enum1.values().length, Enum2.values().length);

		Assertions.assertEquals(map1.hashCode(), map2.hashCode());

		map1.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assertions.assertFalse(map1.hashCode() == map2.hashCode());

		map2.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assertions.assertEquals(map1.hashCode(), map2.hashCode());
	}

	@Test
	public void testGetPut() {
		final LongFastEnumEnumMap<Enum1, Enum2> map = new LongFastEnumEnumMap<>(Enum1.values().length, Enum2.values().length);

		Assertions.assertEquals(0l, map.get(Enum1.Enum1_A, Enum2.Enum2_A));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_A, Enum2.Enum2_B));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_A));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_B));

		map.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assertions.assertEquals(5l, map.get(Enum1.Enum1_A, Enum2.Enum2_A));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_A, Enum2.Enum2_B));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_A));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_B));

		map.put(Enum1.Enum1_A, Enum2.Enum2_B, 10l);

		Assertions.assertEquals(5l, map.get(Enum1.Enum1_A, Enum2.Enum2_A));
		Assertions.assertEquals(10l, map.get(Enum1.Enum1_A, Enum2.Enum2_B));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_A));
		Assertions.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_B));
	}

	@Test
	public void testGetPut2() {
		final LongFastEnumEnumMap<Enum1, Enum2> map = new LongFastEnumEnumMap<>(1, 1);

		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> map.get(Enum1.Enum1_A, Enum2.Enum2_B));
	}

	@Test
	public void testEqualsObject() {
		final LongFastEnumEnumMap<Enum1, Enum2> map1 = new LongFastEnumEnumMap<>(Enum1.values().length, Enum2.values().length);
		final LongFastEnumEnumMap<Enum1, Enum2> map2 = new LongFastEnumEnumMap<>(Enum1.values().length, Enum2.values().length);

		Assertions.assertEquals(map1, map2);

		map1.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assertions.assertFalse(map1.equals(map2));

		map2.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assertions.assertFalse(map1.equals(new Object()));
		Assertions.assertFalse(map1.equals(null));
	}

}
