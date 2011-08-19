/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
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
		for (int i = 0; i < 10000; i++) {
			Assert.assertFalse(5 == RandomHelper
					.nextDifferentInt(random, 10, 5));
		}
	}

	@Test
	public void testChooseElementFrom() {
		Random random = new Random(1);
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		final List<Object> list = CollectionsUtil.makeArrayList(o1, o2, o3);
		int[] hits = new int[3];
		final int tests = 10000;
		for (int i = 0; i < tests; i++) {
			Object o = RandomHelper.chooseElementFrom(random, list);
			Assert.assertTrue(list.contains(o));
			hits[list.indexOf(o)]++;
		}

		Assert.assertEquals(1 / 3.0, hits[0] / (double) tests, tests / 50.0);
		Assert.assertEquals(1 / 3.0, hits[1] / (double) tests, tests / 50.0);
		Assert.assertEquals(1 / 3.0, hits[2] / (double) tests, tests / 50.0);
	}

	@Test
	public void testNextIntBetween() {

		Random random = new Random(1);

		// number of time to perform tests in each case.
		final int numOfTests = 1000;
		// the maximum the random number can be.
		final int max = 100;

		// Test normal cases are within range.
		
		// TODO max/2 is not the middle.
		for (int i = 0; i < numOfTests; i++)
			Assert.assertEquals(max / 2,
					RandomHelper.nextIntBetween(random, 0, max), max / 2);

		// No difference between min and max, should return the minimum.
		for (int i = 0; i < numOfTests; i++)
			Assert.assertEquals(i, RandomHelper.nextIntBetween(random, i, i));

		// Test the distribution is the same.
		int[] distribution = new int[max + 1]; // add one because max of nextIntBetween is inclusive of max
		for (int i = 0; i < numOfTests; i++)
			distribution[RandomHelper.nextIntBetween(random, 0, max)]++;

		// get the sum of all the hits
		int numOfHits = 0;
		for (int i = 0; i < distribution.length; i++)
			numOfHits += distribution[i];
		// this should equal the number of tests.
		Assert.assertEquals(numOfTests, numOfHits);
		
		// TODO test each one has been hit equally.

	}

	/**
	 * Test to see if the method handles an invalid range correctly.
	 * 
	 * e.g. Max: 0, Min: 10
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testNextIntBetweenInvalidRange() {

		Random random = new Random(1);

		// number of time to perform test
		final int numOfTests = 1000;
		// the wrong limits
		final int wrongMin = 100;
		final int wrongMax = 0;

		for (int i = 0; i < numOfTests; i++)
			RandomHelper.nextIntBetween(random, wrongMin, wrongMax);
	}

	/**
	 * Test to see if the method handles negative maxs and mins.
	 */
	public void testNextIntBetweenNegativeRange() {

		Random random = new Random(1);

		// number of time to perform test
		final int numOfTests = 1000;
		
		final int[] maxes = {100, 0, -50};
		final int negMin = -100;

		// Test cases are within range.
		for (int i = 0; i < numOfTests; i++) {
			
			for (int j = 0; j < maxes.length; j++) {
				
				final float halfDifference = ((float)maxes[j] - (float)negMin) / 2f;
				final float midPoint = (float)maxes[j] - halfDifference;
				
				Assert.assertEquals(midPoint, RandomHelper.nextIntBetween(random, negMin, maxes[j]), halfDifference);
			}
		}
			
	}
}
