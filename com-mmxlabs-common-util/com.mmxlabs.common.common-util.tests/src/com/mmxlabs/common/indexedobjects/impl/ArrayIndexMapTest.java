/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

public class ArrayIndexMapTest {

	@Test
	public void testMaybeGet() {

		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<>();
		final IIndexedObject key = Mockito.mock(IIndexedObject.class);

		Assertions.assertNull(map.maybeGet(key));
		Mockito.verify(key).getIndex();
	}

	@Test
	public void testGet2() {

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<>();
			final IIndexedObject key = Mockito.mock(IIndexedObject.class);

			map.get(key);

			Mockito.verify(key).getIndex();
		});
	}

	@Test
	public void testGet3() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {

			final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<>();
			final IIndexedObject key = Mockito.mock(IIndexedObject.class);

			Mockito.when(key.getIndex()).thenReturn(Integer.MAX_VALUE);
			map.get(key);
		});
	}

	@Test
	public void testGetSet() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<>();
		final Object o1 = new Object();
		final IIndexedObject key = Mockito.mock(IIndexedObject.class);

		map.set(key, o1);
		final Object object = map.get(key);

		Assertions.assertSame(o1, object);

	}

	@Test
	public void testGetValues1() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<>();
		final Object value1 = new Object();
		final Object value2 = new Object();
		final IIndexedObject key1 = new MockIndexedObject(0);
		final IIndexedObject key2 = new MockIndexedObject(1);

		map.set(key1, value1);
		map.set(key2, value2);

		final Iterable<Object> values = map.getValues();

		Assertions.assertNotNull(values);

		final Iterator<Object> iterator = values.iterator();

		Assertions.assertNotNull(iterator);

		Assertions.assertTrue(iterator.hasNext());
		Assertions.assertSame(value1, iterator.next());
		Assertions.assertTrue(iterator.hasNext());
		Assertions.assertSame(value2, iterator.next());
		Assertions.assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetValues2() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<>();
		final Object value1 = new Object();
		final Object value2 = new Object();
		final IIndexedObject key1 = new MockIndexedObject(0);
		final IIndexedObject key2 = new MockIndexedObject(1);

		map.set(key1, value1);
		map.set(key2, value2);

		final Iterable<Object> values = map.getValues();

		Assertions.assertNotNull(values);

		final Iterator<Object> iterator = values.iterator();

		Assertions.assertNotNull(iterator);

		Assertions.assertTrue(iterator.hasNext());
		Assertions.assertSame(value1, iterator.next());
		Assertions.assertTrue(iterator.hasNext());
		Assertions.assertSame(value2, iterator.next());
		Assertions.assertFalse(iterator.hasNext());

		// Trigger no such element exception
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			iterator.next();
		});
	}

	@Test
	public void testGetValues3() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<>();

		final Iterable<Object> values = map.getValues();
		Assertions.assertNotNull(values);

		final Iterator<Object> iterator = values.iterator();
		Assertions.assertNotNull(iterator);

		Assertions.assertThrows(UnsupportedOperationException.class, () -> {

			iterator.remove();
		});
	}

	@Test
	public void testClear() {
		final ArrayIndexMap<IIndexedObject, Object> map = new ArrayIndexMap<>();
		final Object o1 = new Object();
		final IIndexedObject key = Mockito.mock(IIndexedObject.class);

		map.set(key, o1);
		final Object object = map.get(key);

		Assertions.assertSame(o1, object);

		map.clear();

		Assertions.assertNull(map.maybeGet(key));

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
