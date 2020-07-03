/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PairTest {
	@Test
	public void testEmptyPair() {
		final Pair<Object, Object> pair = new Pair<>();
		Assertions.assertNull(pair.getFirst());
		Assertions.assertNull(pair.getSecond());
	}

	@Test
	public void testFullPair() {
		final Object o = new Object();
		final Object m = new Object();
		final Pair<Object, Object> p = new Pair<>(o, m);
		Assertions.assertSame(o, p.getFirst());
		Assertions.assertSame(m, p.getSecond());
	}

	@Test
	public void testPairCopy() {
		final Object o = new Object();
		final Object m = new Object();
		final Pair<Object, Object> p = new Pair<>(o, m);
		Assertions.assertSame(o, p.getFirst());
		Assertions.assertSame(m, p.getSecond());

		final Pair<Object, Object> p2 = new Pair<>(p);
		Assertions.assertSame(o, p2.getFirst());
		Assertions.assertSame(m, p2.getSecond());

		Assertions.assertEquals(p, p2);
		Assertions.assertEquals(p.hashCode(), p2.hashCode());
	}

	@Test
	public void testEquals() {
		final Object o1 = new Object();
		final Object m1 = new Object();

		final Object o2 = new Object();
		final Object m2 = new Object();

		final Pair<Object, Object> p0 = new Pair<>(o1, m1);
		final Pair<Object, Object> p1 = new Pair<>(o1, m1);
		final Pair<Object, Object> p2 = new Pair<>(o2, m2);

		final Pair<Object, Object> p3 = new Pair<>(null, m1);
		final Pair<Object, Object> p4 = new Pair<>(o1, null);
		final Pair<Object, Object> p5 = new Pair<>(null, null);
		final Pair<Object, Object> p6 = new Pair<>(null, null);

		Assertions.assertEquals(p0, p0);
		Assertions.assertEquals(p0, p1);
		Assertions.assertEquals(p1, p0);
		Assertions.assertFalse(p2.equals(p0));
		Assertions.assertFalse(p0.equals(p2));

		Assertions.assertFalse(p0.equals(null));

		Assertions.assertFalse(p0.equals(p3));
		Assertions.assertFalse(p0.equals(p4));
		Assertions.assertFalse(p0.equals(p5));

		Assertions.assertFalse(p3.equals(p0));
		Assertions.assertFalse(p4.equals(p0));
		Assertions.assertFalse(p5.equals(p0));

		Assertions.assertEquals(p5, p6);
	}

	@Test
	public void testAccessors() {
		final Object o = new Object();
		final Object m = new Object();
		final Pair<Object, Object> pair = new Pair<>();
		pair.setFirst(o);
		Assertions.assertSame(o, pair.getFirst());
		pair.setSecond(o);
		Assertions.assertSame(o, pair.getSecond());
		Assertions.assertSame(pair.getFirst(), pair.getSecond());

		pair.setBoth(m, o);
		Assertions.assertSame(o, pair.getSecond());
		Assertions.assertSame(m, pair.getFirst());
	}

	/**
	 * Test to make sure toString returns a string. A pair with objects should return a non-null non-empty string.
	 */
	@Test
	public void testToString() {

		// initialise a variety of non-null & null pairs.
		final Object o = new Object();
		final Object m = new Object();

		// Neither object null
		final Pair<Object, Object> pairNonNull = new Pair<>(o, m);
		Assertions.assertNotNull(pairNonNull.toString());
		Assertions.assertTrue(pairNonNull.toString().length() > 0);

		// first object null
		final Pair<Object, Object> pairFirstNull = new Pair<>(null, m);
		Assertions.assertNotNull(pairFirstNull.toString());
		Assertions.assertTrue(pairFirstNull.toString().length() > 0);

		// second object null
		final Pair<Object, Object> pairSecondNull = new Pair<>(o, null);
		Assertions.assertNotNull(pairSecondNull.toString());
		Assertions.assertTrue(pairSecondNull.toString().length() > 0);

		// both objects null
		final Pair<Object, Object> pairBothNull = new Pair<>(null, null);
		Assertions.assertNotNull(pairBothNull.toString());
		Assertions.assertTrue(pairBothNull.toString().length() > 0);

		// blank pair null
		final Pair<Object, Object> pairBlank = new Pair<>();
		Assertions.assertNotNull(pairBlank.toString());
		Assertions.assertTrue(pairBlank.toString().length() > 0);
	}
}
