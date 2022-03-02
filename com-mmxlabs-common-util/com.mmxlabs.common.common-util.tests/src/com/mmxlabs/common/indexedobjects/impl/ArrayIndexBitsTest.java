/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

public class ArrayIndexBitsTest {

	@Test
	public void testIsSet() {

		final ArrayIndexBits<MockIndexedObject> bits = new ArrayIndexBits<>();

		final MockIndexedObject obj1 = new MockIndexedObject(1);
		final MockIndexedObject obj2 = new MockIndexedObject(2);
		final MockIndexedObject obj3 = new MockIndexedObject(3);

		Assertions.assertFalse(bits.isSet(obj1));
		Assertions.assertFalse(bits.isSet(obj2));
		Assertions.assertFalse(bits.isSet(obj3));

		bits.set(obj1);

		Assertions.assertTrue(bits.isSet(obj1));
		Assertions.assertFalse(bits.isSet(obj2));
		Assertions.assertFalse(bits.isSet(obj3));

		bits.set(obj2);
		Assertions.assertTrue(bits.isSet(obj1));
		Assertions.assertTrue(bits.isSet(obj2));
		Assertions.assertFalse(bits.isSet(obj3));

		bits.clear(obj2);
		bits.set(obj3);

		Assertions.assertTrue(bits.isSet(obj1));
		Assertions.assertFalse(bits.isSet(obj2));
		Assertions.assertTrue(bits.isSet(obj3));
	}

	private static class MockIndexedObject implements IIndexedObject {

		private final int index;

		public MockIndexedObject(final int index) {
			this.index = index;
		}

		@Override
		public int getIndex() {
			return index;
		}

	}
}
