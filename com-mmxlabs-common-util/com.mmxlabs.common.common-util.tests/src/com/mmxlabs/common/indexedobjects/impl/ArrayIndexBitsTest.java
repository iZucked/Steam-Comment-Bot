/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

public class ArrayIndexBitsTest {

	@Test
	public void testIsSet() {

		final ArrayIndexBits<MockIndexedObject> bits = new ArrayIndexBits<MockIndexedObject>();

		final MockIndexedObject obj1 = new MockIndexedObject(1);
		final MockIndexedObject obj2 = new MockIndexedObject(2);
		final MockIndexedObject obj3 = new MockIndexedObject(3);

		Assert.assertFalse(bits.isSet(obj1));
		Assert.assertFalse(bits.isSet(obj2));
		Assert.assertFalse(bits.isSet(obj3));

		bits.set(obj1);

		Assert.assertTrue(bits.isSet(obj1));
		Assert.assertFalse(bits.isSet(obj2));
		Assert.assertFalse(bits.isSet(obj3));

		bits.set(obj2);
		Assert.assertTrue(bits.isSet(obj1));
		Assert.assertTrue(bits.isSet(obj2));
		Assert.assertFalse(bits.isSet(obj3));

		bits.clear(obj2);
		bits.set(obj3);

		Assert.assertTrue(bits.isSet(obj1));
		Assert.assertFalse(bits.isSet(obj2));
		Assert.assertTrue(bits.isSet(obj3));
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
