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
	public void insert(int index, final ISegment<T> segment) {
		for (final T e : segment) {
			list.add(index++, e);
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
	public boolean equals(final Object obj) {

		if (obj instanceof ListModifiableSequence) {
			return list.equals(((ListModifiableSequence) obj).list);
		} else if (obj instanceof ISequence) {
			final ISequence seq = (ISequence) obj;
			if (seq.size() != size()) {
				return false;
			}
			Iterator<T> it1, it2;
			it1 = iterator();
			it2 = seq.iterator();
			while (it1.hasNext()) {
				if (it1.next().equals(it2.next()) == false)
					return false;
			}
			return true;

		}
		return false;
	}

	@Override
	public final T last() {
		return get(size() - 1);
	}

	@Override
	public T first() {
		return get(0);
	}
}
