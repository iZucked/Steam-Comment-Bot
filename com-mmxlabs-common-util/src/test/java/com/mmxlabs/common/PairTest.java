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
