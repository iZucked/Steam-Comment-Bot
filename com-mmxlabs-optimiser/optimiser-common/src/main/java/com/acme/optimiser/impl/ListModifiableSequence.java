package com.acme.optimiser.impl;

import java.util.Iterator;
import java.util.List;

import com.acme.optimiser.IModifiableSequence;
import com.acme.optimiser.ISegment;
import com.acme.optimiser.ISequence;

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
	public void remove(final T element) {
		list.remove(element);
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

		return new ISegment<T>() {

			List<T> l = list.subList(start, end);

			@Override
			public T get(final int index) {
				return l.get(index);
			}

			@Override
			public ISequence<T> getSequence() {
				return ListModifiableSequence.this;
			}

			@Override
			public int getSequenceEnd() {
				return end;
			}

			@Override
			public int getSequenceStart() {
				return start;
			}

			@Override
			public int size() {
				return l.size();
			}

			@Override
			public Iterator<T> iterator() {
				return l.iterator();
			}
		};

	}

	@Override
	public int size() {
		return list.size();
	}
}
