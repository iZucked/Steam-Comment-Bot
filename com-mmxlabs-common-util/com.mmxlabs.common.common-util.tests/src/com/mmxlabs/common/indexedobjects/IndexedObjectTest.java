/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

class IndexedObjectTest {
	@Test
	void testIndexedObject() {
		final IIndexingContext context = new SimpleIndexingContext();
		final IndexedObject o1 = new IndexedObject(context);
		final IndexedObject o2 = new IndexedObject(context);

		Assertions.assertNotEquals(o1.getIndex(), o2.getIndex());
		Assertions.assertEquals(0, o1.getIndex());
		Assertions.assertEquals(1, o2.getIndex());
	}

	@Test
	void testIndexedSubclasses() {
		final IIndexingContext context = new SimpleIndexingContext();
		context.registerType(A.class);
		final A a = new A(context);
		final B b = new B(context);
		final A a1 = new A(context);
		Assertions.assertEquals(a.getIndex(), b.getIndex());
		Assertions.assertNotEquals(a.getIndex(), a1.getIndex());
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
