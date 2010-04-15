package com.mmxlabs.optimiser.impl;

import java.util.Iterator;

import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.ISegment;
import com.mmxlabs.optimiser.ISequence;

/**
 * Wraps an {@link ISequence} instance with this object. This class is intended
 * to be used with {@link IModifiableSequence} objects to force them to be
 * treated as read-only. As such there is a read-only iterator and a setter, but
 * no getter for the wrapped object.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
final public class UnmodifiableSequenceWrapper<T> implements ISequence<T> {

	private final ISequence<T> wrapped;

	public UnmodifiableSequenceWrapper(final ISequence<T> wrapped) {

		this.wrapped = wrapped;
	}

	@Override
	public Iterator<T> iterator() {

		return new Iterator<T>() {
			Iterator<? extends T> i = wrapped.iterator();

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
		return wrapped.get(index);
	}

	@Override
	public ISegment<T> getSegment(final int start, final int end) {
		return wrapped.getSegment(start, end);
	}

	@Override
	public int size() {
		return wrapped.size();
	}
}
