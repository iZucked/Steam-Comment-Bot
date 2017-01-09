/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class SimpleIndexingContextTest {
	@Test
	public void testObjectIndexAllocation() {
		final IIndexingContext index = new SimpleIndexingContext();
		final Object o = new Object();
		for (int i = 0; i < 10; i++) {
			Assert.assertTrue(index.assignIndex(o) == i);
		}
	}

	@Test
	public void testSubclassIndexAllocation() {
		final IIndexingContext index = new SimpleIndexingContext();

		index.registerType(A.class);
		index.registerType(B.class);

		final A a1 = new A();
		final A a2 = new A();
		final B b1 = new B();
		final B b2 = new B();
		final C c1 = new C();
		final Object o1 = new Object();

		Assert.assertEquals(index.assignIndex(a1), 0);
		Assert.assertEquals(index.assignIndex(b1), 0);
		Assert.assertEquals(index.assignIndex(c1), 1);// C extends A but is not registered
		Assert.assertEquals(index.assignIndex(o1), 0);
		Assert.assertEquals(index.assignIndex(a2), 2);
		Assert.assertEquals(index.assignIndex(b2), 1);
	}

	class A {

	}

	class B extends A {

	}

	class C extends A {

	}
}
