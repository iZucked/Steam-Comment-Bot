/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common;

import junit.framework.Assert;

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

		Assert.assertEquals(p0, p1);
		Assert.assertEquals(p1, p0);
		Assert.assertFalse(p2.equals(p0));
		Assert.assertFalse(p0.equals(p2));
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
}
