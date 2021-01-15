/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

class SimpleIndexingContextTest {
	@Test
	void testObjectIndexAllocation() {
		final IIndexingContext index = new SimpleIndexingContext();
		final Object o = new Object();
		for (int i = 0; i < 10; i++) {
			Assertions.assertEquals(i, index.assignIndex(o));
		}
	}

	@Test
	void testSubclassIndexAllocation() {
		final IIndexingContext index = new SimpleIndexingContext();

		index.registerType(A.class);
		index.registerType(B.class);

		final A a1 = new A();
		final A a2 = new A();
		final B b1 = new B();
		final B b2 = new B();
		final C c1 = new C();
		final Object o1 = new Object();

		Assertions.assertEquals(0, index.assignIndex(a1));
		Assertions.assertEquals(0, index.assignIndex(b1));
		Assertions.assertEquals(1, index.assignIndex(c1));// C extends A but is not registered
		Assertions.assertEquals(0, index.assignIndex(o1));
		Assertions.assertEquals(2, index.assignIndex(a2));
		Assertions.assertEquals(1, index.assignIndex(b2));
	}

	class A {

	}

	class B extends A {

	}

	class C extends A {

	}
}
