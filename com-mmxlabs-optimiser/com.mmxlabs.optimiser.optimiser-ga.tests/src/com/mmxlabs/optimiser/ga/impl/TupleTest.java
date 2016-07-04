/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 * 
 */
package com.mmxlabs.optimiser.ga.impl;

import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Simon Goodall
 * 
 */
public class TupleTest {

	/**
	 * Test method for {@link com.mmxlabs.optimiser.ga.impl.Tuple#Tuple(java.lang.Object, int, long)} .
	 */
	@Test
	public void testTuple() {
		final Object o1 = new Object();
		final int idx1 = 1;
		final long f1 = 100l;
		final Tuple<Object> t1 = new Tuple<Object>(o1, idx1, f1);

		Assert.assertSame(o1, t1.i);
		Assert.assertEquals(idx1, t1.idx);
		Assert.assertEquals(f1, t1.f);
	}

	/**
	 * Test method for {@link com.mmxlabs.optimiser.ga.impl.Tuple#compareTo(com.mmxlabs.optimiser.ga.impl.Tuple)} .
	 */
	@Test
	public void testCompareTo() {

		final Object o1 = new Object();
		final int idx1 = 1;
		final long f1 = 100l;
		final Tuple<Object> t1 = new Tuple<Object>(o1, idx1, f1);

		final Object o2 = new Object();
		final int idx2 = 1;
		final long f2 = 100l;
		final Tuple<Object> t2 = new Tuple<Object>(o2, idx2, f2);

		final Object o3 = new Object();
		final int idx3 = 1;
		final long f3 = 200l;
		final Tuple<Object> t3 = new Tuple<Object>(o3, idx3, f3);

		final Object o4 = new Object();
		final int idx4 = 2;
		final long f4 = 100l;
		final Tuple<Object> t4 = new Tuple<Object>(o4, idx4, f4);

		Assert.assertEquals(0, t1.compareTo(t2));
		Assert.assertEquals(0, t2.compareTo(t1));

		Assert.assertEquals(-1, t1.compareTo(t3));
		Assert.assertEquals(1, t3.compareTo(t1));

		Assert.assertEquals(-1, t1.compareTo(t4));
		Assert.assertEquals(1, t4.compareTo(t1));
	}

	/**
	 * Test method for {@link com.mmxlabs.optimiser.ga.impl.Tuple#compareTo(com.mmxlabs.optimiser.ga.impl.Tuple)} .
	 */
	@Test
	public void testCompareToInTreeSet() {

		final Object o1 = new Object();
		final int idx1 = 1;
		final long f1 = 100l;
		final Tuple<Object> t1 = new Tuple<Object>(o1, idx1, f1);

		final Object o2 = new Object();
		final int idx2 = 1;
		final long f2 = 100l;
		final Tuple<Object> t2 = new Tuple<Object>(o2, idx2, f2);

		final Object o3 = new Object();
		final int idx3 = 1;
		final long f3 = 200l;
		final Tuple<Object> t3 = new Tuple<Object>(o3, idx3, f3);

		final Object o4 = new Object();
		final int idx4 = 2;
		final long f4 = 100l;
		final Tuple<Object> t4 = new Tuple<Object>(o4, idx4, f4);

		final TreeSet<Tuple<Object>> set = new TreeSet<Tuple<Object>>();
		set.add(t1);
		set.add(t2);
		set.add(t3);
		set.add(t4);

		// Expect to loose t2 as it is identical to t1
		Assert.assertEquals(3, set.size());

		final Object[] array = set.toArray();
		Assert.assertSame(t1, array[0]);
		Assert.assertSame(t4, array[1]);
		Assert.assertSame(t3, array[2]);
	}
}
