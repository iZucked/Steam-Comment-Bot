/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EqualityTest {

	@Test
	public void testShallowEquals() {

		// test unequal lengths
		final Object[] twoElementArray = { 0, 1 };
		final Object[] fourElementArray = { 0, 1, 2, 3 };

		Assertions.assertFalse(Equality.shallowEquals(twoElementArray, fourElementArray));

		// Test null args are handled correctly.
		Assertions.assertFalse(Equality.shallowEquals(twoElementArray, null));
		Assertions.assertFalse(Equality.shallowEquals(null, twoElementArray));
		Assertions.assertTrue(Equality.shallowEquals(null, null));

		// Testing the same list returns true
		Assertions.assertTrue(Equality.shallowEquals(twoElementArray, twoElementArray));
		Assertions.assertTrue(Equality.shallowEquals(fourElementArray, fourElementArray));

		// Test two different lists for equality.
		final Object[] fourElementArrayAgain = { 0, 1, 2, 3 };
		final Object[] fourElementArrayDifferent = { 1, 1, 2, 3 };

		Assertions.assertTrue(Equality.shallowEquals(fourElementArray, fourElementArrayAgain));
		Assertions.assertFalse(Equality.shallowEquals(fourElementArray, fourElementArrayDifferent));
	}

	/**
	 * shallowEquals should fail when testing objects that are different, even if
	 * they have identical content. This is because it tests using == rather than
	 * the "correct" .equals method.
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
		Assertions.assertArrayEquals(array1, array2);
		// Methods that test equality using == will not return true.
		Assertions.assertFalse(Equality.shallowEquals(array1, array2));

	}

}
