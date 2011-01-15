/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;

/**
 * Implementation of {@link IModifiableSequence} which uses a {@link List} as
 * the backing implementation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class ListModifiableSequence<T> implements IModifiableSequence<T> {

	private final List<T> list;

	public ListModifiableSequence(final List<T> list) {
		this.list = list;
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public void add(final T element) {
		list.add(element);
	}

	@Override
	public void insert(final int index, final T element) {
		list.add(index, element);
	}

	@Override
	public void insert(final int index, final ISegment<T> segment) {
		int idx = index;
		for (final T e : segment) {
			list.add(idx++, e);
		}
	}

	@Override
	public void remove(final int index) {
		list.remove(index);
	}

	@Override
	public boolean remove(final T element) {
		return list.remove(element);
	}

	@Override
	public void remove(final ISegment<T> segment) {
		for (final T e : segment) {
			list.remove(e);
		}
	}

	@Override
	public void remove(final int start, final int end) {
		list.subList(start, end).clear();
	}

	@Override
	public void set(final int index, final T element) {
		list.set(index, element);

	}

	@Override
	public T get(final int index) {
		return list.get(index);
	}

	@Override
	public ISegment<T> getSegment(final int start, final int end) {

		// Copy of the sublist to make segment independent from sequence.
		return new ListSegment<T>(new ArrayList<T>(list.subList(start, end)),
				this, start, end);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void replaceAll(final ISequence<T> sequence) {
		list.clear();

		for (final T t : sequence) {
			list.add(t);
		}
	}

	@Override
	public final T last() {
		return get(size() - 1);
	}

	@Override
	public T first() {
		return get(0);
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		} else if (!(obj instanceof ISequence)) {
			return false;
		} else {
			final Iterator<T> e1 = iterator();
			final Iterator e2 = ((ISequence) obj).iterator();
			while (e1.hasNext() && e2.hasNext()) {
				final T o1 = e1.next();
				final Object o2 = e2.next();
				if (!(o1 == null ? o2 == null : o1.equals(o2))) {
					return false;
				}
			}
			return !(e1.hasNext() || e2.hasNext());
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		final Iterator<T> i = iterator();
		while (i.hasNext()) {
			final T obj = i.next();
			hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
		}
		return hashCode;
	}
}
