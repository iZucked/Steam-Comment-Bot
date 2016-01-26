/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.Assert;
import org.junit.Test;

public class PairTest {
	@Test
	public void testEmptyPair() {
		final Pair<Object, Object> pair = new Pair<Object, Object>();
		Assert.assertNull(pair.getFirst());
		Assert.assertNull(pair.getSecond());
	}

	@Test
	public void testFullPair() {
		final Object o = new Object();
		final Object m = new Object();
		final Pair<Object, Object> p = new Pair<Object, Object>(o, m);
		Assert.assertSame(o, p.getFirst());
		Assert.assertSame(m, p.getSecond());
	}

	@Test
	public void testPairCopy() {
		final Object o = new Object();
		final Object m = new Object();
		final Pair<Object, Object> p = new Pair<Object, Object>(o, m);
		Assert.assertSame(o, p.getFirst());
		Assert.assertSame(m, p.getSecond());

		final Pair<Object, Object> p2 = new Pair<Object, Object>(p);
		Assert.assertSame(o, p2.getFirst());
		Assert.assertSame(m, p2.getSecond());

		Assert.assertEquals(p, p2);
		Assert.assertEquals(p.hashCode(), p2.hashCode());
	}

	@Test
	public void testEquals() {
		final Object o1 = new Object();
		final Object m1 = new Object();

		final Object o2 = new Object();
		final Object m2 = new Object();

		final Pair<Object, Object> p0 = new Pair<Object, Object>(o1, m1);
		final Pair<Object, Object> p1 = new Pair<Object, Object>(o1, m1);
		final Pair<Object, Object> p2 = new Pair<Object, Object>(o2, m2);

		final Pair<Object, Object> p3 = new Pair<Object, Object>(null, m1);
		final Pair<Object, Object> p4 = new Pair<Object, Object>(o1, null);
		final Pair<Object, Object> p5 = new Pair<Object, Object>(null, null);
		final Pair<Object, Object> p6 = new Pair<Object, Object>(null, null);

		Assert.assertEquals(p0, p0);
		Assert.assertEquals(p0, p1);
		Assert.assertEquals(p1, p0);
		Assert.assertFalse(p2.equals(p0));
		Assert.assertFalse(p0.equals(p2));

		Assert.assertFalse(p0.equals(null));

		Assert.assertFalse(p0.equals(p3));
		Assert.assertFalse(p0.equals(p4));
		Assert.assertFalse(p0.equals(p5));

		Assert.assertFalse(p3.equals(p0));
		Assert.assertFalse(p4.equals(p0));
		Assert.assertFalse(p5.equals(p0));

		Assert.assertEquals(p5, p6);
	}

	@Test
	public void testAccessors() {
		final Object o = new Object();
		final Object m = new Object();
		final Pair<Object, Object> pair = new Pair<Object, Object>();
		pair.setFirst(o);
		Assert.assertSame(o, pair.getFirst());
		pair.setSecond(o);
		Assert.assertSame(o, pair.getSecond());
		Assert.assertSame(pair.getFirst(), pair.getSecond());

		pair.setBoth(m, o);
		Assert.assertSame(o, pair.getSecond());
		Assert.assertSame(m, pair.getFirst());
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
		final Pair<Object, Object> pairNonNull = new Pair<Object, Object>(o, m);
		Assert.assertNotNull(pairNonNull.toString());
		Assert.assertTrue(pairNonNull.toString().length() > 0);

		// first object null
		final Pair<Object, Object> pairFirstNull = new Pair<Object, Object>(null, m);
		Assert.assertNotNull(pairFirstNull.toString());
		Assert.assertTrue(pairFirstNull.toString().length() > 0);

		// second object null
		final Pair<Object, Object> pairSecondNull = new Pair<Object, Object>(o, null);
		Assert.assertNotNull(pairSecondNull.toString());
		Assert.assertTrue(pairSecondNull.toString().length() > 0);

		// both objects null
		final Pair<Object, Object> pairBothNull = new Pair<Object, Object>(null, null);
		Assert.assertNotNull(pairBothNull.toString());
		Assert.assertTrue(pairBothNull.toString().length() > 0);

		// blank pair null
		final Pair<Object, Object> pairBlank = new Pair<Object, Object>();
		Assert.assertNotNull(pairBlank.toString());
		Assert.assertTrue(pairBlank.toString().length() > 0);
	}
}
