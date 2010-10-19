/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import java.util.Iterator;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;

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
public final class UnmodifiableSequenceWrapper<T> implements ISequence<T> {

	private final ISequence<T> wrapped;

	public UnmodifiableSequenceWrapper(final ISequence<T> wrapped) {

		this.wrapped = wrapped;
	}

	@Override
	public final Iterator<T> iterator() {

		/**
		 * A new anonymous Iterator wrapping iterator from the 'wrapped'
		 * ISequence to forbid calls to #remove()
		 */
		return new Iterator<T>() {
			private final Iterator<? extends T> i = wrapped.iterator();

			public final boolean hasNext() {
				return i.hasNext();
			}

			public final T next() {
				return i.next();
			}

			public final void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public final T get(final int index) {
		return wrapped.get(index);
	}

	@Override
	public final ISegment<T> getSegment(final int start, final int end) {
		return wrapped.getSegment(start, end);
	}

	@Override
	public final int size() {
		return wrapped.size();
	}

	@Override
	public final T last() {
		return wrapped.last();
	}

	@Override
	public final T first() {
		return wrapped.first();
	}
}
