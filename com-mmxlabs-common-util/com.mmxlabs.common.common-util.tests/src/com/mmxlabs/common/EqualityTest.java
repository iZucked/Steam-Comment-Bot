/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

	@Test
	public void testIsEqual() {

		final Object a = new Object();
		final Object b = new Object();

		Assert.assertTrue(Equality.isEqual(null, null));
		Assert.assertFalse(Equality.isEqual(a, null));
		Assert.assertFalse(Equality.isEqual(null, a));
		Assert.assertFalse(Equality.isEqual(a, b));

		Assert.assertTrue(Equality.isEqual(a, a));
		Assert.assertTrue(Equality.isEqual(b, b));
	}

	@Test
	public void testShallowEquals() {

		// test unequal lengths
		final Object[] twoElementArray = { 0, 1 };
		final Object[] fourElementArray = { 0, 1, 2, 3 };

		Assert.assertFalse(Equality.shallowEquals(twoElementArray, fourElementArray));

		// Test null args are handled correctly.
		Assert.assertFalse(Equality.shallowEquals(twoElementArray, null));
		Assert.assertFalse(Equality.shallowEquals(null, twoElementArray));
		Assert.assertTrue(Equality.shallowEquals(null, null));

		// Testing the same list returns true
		Assert.assertTrue(Equality.shallowEquals(twoElementArray, twoElementArray));
		Assert.assertTrue(Equality.shallowEquals(fourElementArray, fourElementArray));

		// Test two different lists for equality.
		final Object[] fourElementArrayAgain = { 0, 1, 2, 3 };
		final Object[] fourElementArrayDifferent = { 1, 1, 2, 3 };

		Assert.assertTrue(Equality.shallowEquals(fourElementArray, fourElementArrayAgain));
		Assert.assertFalse(Equality.shallowEquals(fourElementArray, fourElementArrayDifferent));
	}

	/**
	 * shallowEquals should fail when testing objects that are different, even if they have identical content. This is because it tests using == rather than the "correct" .equals method.
	 */
	@Test
	public void testShallowEqualsStringEquality() {

		final String a1 = new String("a");
		final String b1 = new String("b");
		final String c1 = new String("c");

		final String a2 = new String("a");
		final String b2 = new String("b");
		final String c2 = new String("c");

		final String[] array1 = { a1, b1, c1 };
		final String[] array2 = { a2, b2, c2 };

		// Methods that use .equals will work.
		Assert.assertArrayEquals(array1, array2);
		// Methods that test equality using == will not return true.
		Assert.assertFalse(Equality.shallowEquals(array1, array2));

	}

}
