/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

public class ArrayIndexMapTest {

	@Test
	public void testMaybeGet() {

		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final IIndexedObject key = Mockito.mock(IIndexedObject.class);

		Assert.assertNull(map.maybeGet(key));
		Mockito.verify(key).getIndex();
	}

	@Test(expected = NoSuchElementException.class)
	public void testGet2() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final IIndexedObject key = Mockito.mock(IIndexedObject.class);

		map.get(key);

		Mockito.verify(key).getIndex();
	}

	@Test(expected = NoSuchElementException.class)
	public void testGet3() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final IIndexedObject key = Mockito.mock(IIndexedObject.class);

		Mockito.when(key.getIndex()).thenReturn(Integer.MAX_VALUE);
		map.get(key);
	}

	@Test
	public void testGetSet() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<IIndexedObject, Object>();
		final Object o1 = new Object();
		final IIndexedObject key = Mockito.mock(IIndexedObject.class);

		map.set(key, o1);
		final Object object = map.get(key);

		Assert.assertSame(o1, object);

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
		final IIndexedObject key = Mockito.mock(IIndexedObject.class);

		map.set(key, o1);
		final Object object = map.get(key);

		Assert.assertSame(o1, object);

		map.clear();

		Assert.assertNull(map.maybeGet(key));

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
