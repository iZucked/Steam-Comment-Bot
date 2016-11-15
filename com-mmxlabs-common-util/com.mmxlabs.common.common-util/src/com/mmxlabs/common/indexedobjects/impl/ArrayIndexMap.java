/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * An indexed map directly backed with an array, which doubles in size whenever its capacity is exhausted. Optimised for fast access rather than any other considerations.
 * 
 * @author hinton
 * 
 * @param <T>
 * @param <U>
 */
public final class ArrayIndexMap<T extends IIndexedObject, U> implements IIndexMap<T, U> {
	private U[] contents;
	private boolean[] isSet;

	/**
	 * The default capacity when not specified in constructor.
	 */
	private static final int defaultInitialCapacity = 128;

	@SuppressWarnings("unchecked")
	public ArrayIndexMap(final int initialCapacity) {
		contents = (U[]) new Object[initialCapacity];
		isSet = new boolean[initialCapacity];
	}

	public ArrayIndexMap() {
		this(defaultInitialCapacity);
	}

	private synchronized void ensure(final int index) {
		if (index < contents.length) {
			return;
		}
		final int newSize = Math.max(index + 1, contents.length * 2);
		@SuppressWarnings("unchecked")
		final U[] newContents = (U[]) new Object[newSize];
		final boolean[] newIsSet = new boolean[newSize];

		System.arraycopy(contents, 0, newContents, 0, contents.length);
		System.arraycopy(isSet, 0, newIsSet, 0, isSet.length);

		contents = newContents;
		isSet = newIsSet;
	}

	@SuppressWarnings("null")
	@Override
	public final U maybeGet(final T key) {
		// return get(key);
		final int index = key.getIndex();
		if ((index >= contents.length) || !isSet[index]) {
			return (U) null;
		} else {
			return contents[index];
		}
	}

	@Override
	public final U get(final @NonNull T key) {
		final int index = key.getIndex();
		if (index >= contents.length) {
			throw new NoSuchElementException("Index " + index + " exceeds maximum ever seen");
		} else if (!isSet[index]) {
			throw new NoSuchElementException("Index " + index + " has never been set!");
		} else {
			return contents[index];
		}
	}

	@Override
	public void set(final T key, final U value) {
		final int index = key.getIndex();
		ensure(index);
		contents[index] = value;
		isSet[index] = true;
	}

	@Override
	public Iterable<U> getValues() {
		return new Iterable<U>() {
			@Override
			public Iterator<U> iterator() {
				return new Iterator<U>() {
					private void advance() {
						index++;
						while ((index < isSet.length) && !isSet[index]) {
							index++;
						}
					}

					private int index = 0;

					@Override
					public boolean hasNext() {

						if ((index < isSet.length) && isSet[index]) {
							return true;
						} else {
							return false;
						}
					}

					@Override
					public U next() {
						if (!hasNext()) {
							throw new NoSuchElementException();
						}
						final U result = contents[index];
						advance();
						return result;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void clear() {
		contents = (U[]) new Object[defaultInitialCapacity];
		isSet = new boolean[defaultInitialCapacity];
	}
}
