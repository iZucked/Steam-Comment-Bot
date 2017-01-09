/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {

	@Test
	public void testIsEqual_null_null() {
		Assert.assertTrue(Equality.isEqual(null, null));
	}

	@Test
	public void testIsEqual_a_null() {
		final Object a = new Object();
		Assert.assertFalse(Equality.isEqual(a, null));
	}

	@Test
	public void testIsEqual_null_a() {
		final Object a = new Object();
		Assert.assertFalse(Equality.isEqual(null, a));

	}

	@Test
	public void testIsEqual_a_b() {
		final Object a = new Object();
		final Object b = new Object();

		Assert.assertFalse(Equality.isEqual(a, b));
	}

	@Test
	public void testIsEqual_b_a() {
		final Object a = new Object();
		final Object b = new Object();
		Assert.assertFalse(Equality.isEqual(b, a));

	}

	@Test
	public void testIsEqual_a_a() {
		final Object a = new Object();
		Assert.assertTrue(Equality.isEqual(a, a));
	}

	@Test
	public void testIsEqual_s2_s1() {
		String s1 = "string";
		String s2 = "string";
		Assert.assertTrue(Equality.isEqual(s2, s1));
	}

	@Test
	public void testIsEqual_s1_s2() {
		String s1 = "string";
		String s2 = "string";
		Assert.assertTrue(Equality.isEqual(s1, s2));
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
