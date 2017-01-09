/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Iterator;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Wraps an {@link ISequence} instance with this object. This class is intended to be used with {@link IModifiableSequence} objects to force them to be treated as read-only. As such there is a
 * read-only iterator and a setter, but no getter for the wrapped object.
 * 
 * @author Simon Goodall
 * 
 */
public final class UnmodifiableSequenceWrapper implements ISequence {

	private final ISequence wrapped;

	public UnmodifiableSequenceWrapper(final ISequence wrapped) {

		this.wrapped = wrapped;
	}

	@Override
	public final Iterator<@NonNull ISequenceElement> iterator() {

		/**
		 * A new anonymous Iterator wrapping iterator from the 'wrapped' ISequence to forbid calls to #remove()
		 */
		return new Iterator<@NonNull ISequenceElement>() {
			private final Iterator<@NonNull ISequenceElement> i = wrapped.iterator();

			@Override
			public final boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public final @NonNull ISequenceElement next() {
				return i.next();
			}

			@Override
			public final void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	@NonNull
	public final ISequenceElement get(final int index) {
		return wrapped.get(index);
	}

	@Override
	@NonNull
	public final ISegment getSegment(final int start, final int end) {
		return wrapped.getSegment(start, end);
	}

	@Override
	public final int size() {
		return wrapped.size();
	}

	@Override
	public final ISequenceElement last() {
		return wrapped.last();
	}

	@Override
	public final ISequenceElement first() {
		return wrapped.first();
	}
}
