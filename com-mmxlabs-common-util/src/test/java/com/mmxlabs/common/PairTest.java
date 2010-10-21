/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.common;

import junit.framework.Assert;

import org.junit.Test;

public class PairTest {
	@Test
	public void testEmptyPair() {
		Pair<Object, Object> pair = new Pair<Object, Object>();
		Assert.assertNull(pair.getFirst());
		Assert.assertNull(pair.getSecond());
	}
	
	@Test
	public void testFullPair() {
		Object o = new Object();
		Object m = new Object();
		Pair<Object, Object> p = new Pair<Object, Object>(o, m);
		Assert.assertSame(o, p.getFirst());
		Assert.assertSame(m, p.getSecond());
	}
	
	@Test
	public void testAccessors() {
		Object o = new Object();
		Object m = new Object();
		Pair<Object, Object> pair = new Pair<Object, Object>();
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
