/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.impl;

import org.junit.Assert;
import org.junit.Test;

public class LongFastEnumEnumMapTest {

	enum Enum1 {
		Enum1_A, Enum1_B;
	}

	enum Enum2 {
		Enum2_A, Enum2_B;
	}

	@Test
	public void testHashCode() {

		final LongFastEnumEnumMap<Enum1, Enum2> map1 = new LongFastEnumEnumMap<Enum1, Enum2>(Enum1.values().length, Enum2.values().length);
		final LongFastEnumEnumMap<Enum1, Enum2> map2 = new LongFastEnumEnumMap<Enum1, Enum2>(Enum1.values().length, Enum2.values().length);

		Assert.assertEquals(map1.hashCode(), map2.hashCode());

		map1.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assert.assertFalse(map1.hashCode() == map2.hashCode());

		map2.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assert.assertEquals(map1.hashCode(), map2.hashCode());
	}

	@Test
	public void testGetPut() {
		final LongFastEnumEnumMap<Enum1, Enum2> map = new LongFastEnumEnumMap<Enum1, Enum2>(Enum1.values().length, Enum2.values().length);

		Assert.assertEquals(0l, map.get(Enum1.Enum1_A, Enum2.Enum2_A));
		Assert.assertEquals(0l, map.get(Enum1.Enum1_A, Enum2.Enum2_B));
		Assert.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_A));
		Assert.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_B));

		map.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assert.assertEquals(5l, map.get(Enum1.Enum1_A, Enum2.Enum2_A));
		Assert.assertEquals(0l, map.get(Enum1.Enum1_A, Enum2.Enum2_B));
		Assert.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_A));
		Assert.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_B));

		map.put(Enum1.Enum1_A, Enum2.Enum2_B, 10l);

		Assert.assertEquals(5l, map.get(Enum1.Enum1_A, Enum2.Enum2_A));
		Assert.assertEquals(10l, map.get(Enum1.Enum1_A, Enum2.Enum2_B));
		Assert.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_A));
		Assert.assertEquals(0l, map.get(Enum1.Enum1_B, Enum2.Enum2_B));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetPut2() {
		final LongFastEnumEnumMap<Enum1, Enum2> map = new LongFastEnumEnumMap<Enum1, Enum2>(1, 1);

		map.get(Enum1.Enum1_A, Enum2.Enum2_B);
	}

	@Test
	public void testEqualsObject() {
		final LongFastEnumEnumMap<Enum1, Enum2> map1 = new LongFastEnumEnumMap<Enum1, Enum2>(Enum1.values().length, Enum2.values().length);
		final LongFastEnumEnumMap<Enum1, Enum2> map2 = new LongFastEnumEnumMap<Enum1, Enum2>(Enum1.values().length, Enum2.values().length);

		Assert.assertEquals(map1, map2);

		map1.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assert.assertFalse(map1.equals(map2));

		map2.put(Enum1.Enum1_A, Enum2.Enum2_A, 5l);
		Assert.assertFalse(map1.equals(new Object()));
		Assert.assertFalse(map1.equals(null));
	}

}
