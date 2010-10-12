package com.mmxlabs.common.indexedobjects;

import org.junit.Assert;
import org.junit.Test;

public class IndexedObjectTest {
	@Test
	public void testIndexedObject() {
		final IIndexingContext context = new SimpleIndexingContext();
		final IndexedObject o1 = new IndexedObject(context);
		final IndexedObject o2 = new IndexedObject(context);
		
		Assert.assertFalse(o1.getIndex() == o2.getIndex());
		Assert.assertEquals(o1.getIndex(), 0);
		Assert.assertEquals(o2.getIndex(), 1);
	}
	
	@Test
	public void testIndexedSubclasses() {
		final IIndexingContext context = new SimpleIndexingContext();
		context.registerType(A.class);
		final A a = new A(context);
		final B b = new B(context);
		final A a1 = new A(context);
		Assert.assertTrue(a.getIndex() == b.getIndex());
		Assert.assertFalse(a.getIndex() == a1.getIndex());
	}
	
	class A extends IndexedObject {
		public A(IIndexingContext provider) {
			super(provider);
		}		
	}
	
	class B extends IndexedObject {
		public B(IIndexingContext provider) {
			super(provider);
		}
	}
}
