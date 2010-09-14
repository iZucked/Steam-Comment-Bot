package com.mmxlabs.common;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

public class RandomHelperTest {
	@Test
	public void testNextDifferentInt() {
		final Random random = new Random(1);
		Assert.assertEquals(1, RandomHelper.nextDifferentInt(random, 2, 0));
		Assert.assertEquals(0, RandomHelper.nextDifferentInt(random, 2, 1));
		for (int i = 0; i<10000; i++) {
			Assert.assertFalse(
					5 == RandomHelper.nextDifferentInt(random, 10, 5));
		}
	}
	
	@Test
	public void testChooseElementFrom() {
		Random random = new Random(1);
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		final List<Object> list = CollectionsUtil.makeArrayList(
				o1, o2, o3);
		int [] hits = new int[3];
		final int tests = 10000;
		for (int i = 0; i<tests; i++) {
			Object o = RandomHelper.chooseElementFrom(random, list);
			Assert.assertTrue(list.contains(o));
			hits[list.indexOf(o)]++;
		}
		
		Assert.assertEquals(1/3.0, hits[0]/(double)tests, tests/50.0);
		Assert.assertEquals(1/3.0, hits[1]/(double)tests, tests/50.0);
		Assert.assertEquals(1/3.0, hits[2]/(double)tests, tests/50.0);
	}
}
