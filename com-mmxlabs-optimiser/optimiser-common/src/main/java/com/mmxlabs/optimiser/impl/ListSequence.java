package com.mmxlabs.optimiser.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mmxlabs.optimiser.ISegment;
import com.mmxlabs.optimiser.ISequence;

/**
 * Implementation of {@link ISequence} which uses a {@link List} as the backing
 * implementation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class ListSequence<T> implements ISequence<T> {

	private final List<T> list;

	public ListSequence(final List<T> list) {
		this.list = list;
	}

	/**
	 * Copy constructor cloning the contents of the input {@link ISequence}
	 * 
	 * @param sequence
	 */
	public ListSequence(final ISequence<T> sequence) {
		list = new ArrayList<T>(sequence.size());
		for (final T t : sequence) {
			list.add(t);
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Iterator<? extends T> i = list.iterator();

			public boolean hasNext() {
				return i.hasNext();
			}

			public T next() {
				return i.next();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
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
	public String toString() {
		return list.toString();
	}
}
