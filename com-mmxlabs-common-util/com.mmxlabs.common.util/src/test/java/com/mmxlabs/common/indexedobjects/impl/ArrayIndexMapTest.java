/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

@RunWith(JMock.class)
public class ArrayIndexMapTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testMaybeGet() {

		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final IIndexedObject key = context.mock(IIndexedObject.class);

		context.checking(new Expectations() {
			{
				one(key).getIndex();
			}
		});

		Assert.assertNull(map.maybeGet(key));

		context.assertIsSatisfied();
	}

	@Test(expected = NoSuchElementException.class)
	public void testGet2() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final IIndexedObject key = context.mock(IIndexedObject.class);

		context.checking(new Expectations() {
			{
				one(key).getIndex();
			}
		});

		map.get(key);

		context.assertIsSatisfied();
	}

	@Test(expected = NoSuchElementException.class)
	public void testGet3() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final IIndexedObject key = context.mock(IIndexedObject.class);

		context.checking(new Expectations() {
			{
				one(key).getIndex();
				will(returnValue(Integer.MAX_VALUE));
			}
		});

		map.get(key);

		context.assertIsSatisfied();
	}

	@Test
	public void testGetSet() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final Object o1 = new Object();
		final IIndexedObject key = context.mock(IIndexedObject.class);

		context.checking(new Expectations() {
			{
				// One for put...
				one(key).getIndex();
				// ... and one for get
				one(key).getIndex();
			}
		});

		map.set(key, o1);
		final Object object = map.get(key);

		Assert.assertSame(o1, object);

		context.assertIsSatisfied();
	}

	@Test
	public void testGetValues1() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final Object value1 = new Object();
		final Object value2 = new Object();
		final IIndexedObject key1 = new MockIndexedObject(0);
		final IIndexedObject key2 = new MockIndexedObject(1);

		map.set(key1, value1);
		map.set(key2, value2);

		final Iterable<Object> values = map.getValues();

		Assert.assertNotNull(values);

		final Iterator<Object> iterator = values.iterator();

		Assert.assertNotNull(iterator);

		Assert.assertTrue(iterator.hasNext());
		Assert.assertSame(value1, iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertSame(value2, iterator.next());
		Assert.assertFalse(iterator.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetValues2() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final Object value1 = new Object();
		final Object value2 = new Object();
		final IIndexedObject key1 = new MockIndexedObject(0);
		final IIndexedObject key2 = new MockIndexedObject(1);

		map.set(key1, value1);
		map.set(key2, value2);

		final Iterable<Object> values = map.getValues();

		Assert.assertNotNull(values);

		final Iterator<Object> iterator = values.iterator();

		Assert.assertNotNull(iterator);

		Assert.assertTrue(iterator.hasNext());
		Assert.assertSame(value1, iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertSame(value2, iterator.next());
		Assert.assertFalse(iterator.hasNext());

		// Trigger no such element exception
		iterator.next();

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetValues3() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();

		final Iterable<Object> values = map.getValues();
		Assert.assertNotNull(values);

		final Iterator<Object> iterator = values.iterator();
		Assert.assertNotNull(iterator);

		iterator.remove();
	}

	@Test
	public void testClear() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final Object o1 = new Object();
		final IIndexedObject key = context.mock(IIndexedObject.class);

		context.checking(new Expectations() {
			{
				allowing(key).getIndex();
			}
		});

		map.set(key, o1);
		final Object object = map.get(key);

		Assert.assertSame(o1, object);

		map.clear();

		Assert.assertNull(map.maybeGet(key));

		context.assertIsSatisfied();

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
