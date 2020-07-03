/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class IndexedObjectTest {
	@Test
	public void testIndexedObject() {
		final IIndexingContext context = new SimpleIndexingContext();
		final IndexedObject o1 = new IndexedObject(context);
		final IndexedObject o2 = new IndexedObject(context);

		Assertions.assertFalse(o1.getIndex() == o2.getIndex());
		Assertions.assertEquals(o1.getIndex(), 0);
		Assertions.assertEquals(o2.getIndex(), 1);
	}

	@Test
	public void testIndexedSubclasses() {
		final IIndexingContext context = new SimpleIndexingContext();
		context.registerType(A.class);
		final A a = new A(context);
		final B b = new B(context);
		final A a1 = new A(context);
		Assertions.assertTrue(a.getIndex() == b.getIndex());
		Assertions.assertFalse(a.getIndex() == a1.getIndex());
	}

	static class A extends IndexedObject {
		public A(final IIndexingContext provider) {
			super(provider);
		}
	}

	static class B extends IndexedObject {
		public B(final IIndexingContext provider) {
			super(provider);
		}
	}
}
