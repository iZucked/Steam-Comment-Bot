/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Implementation of {@link ISequence} which uses a {@link List} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class ListSequence implements ISequence {

	private final List<@NonNull ISequenceElement> list;

	public ListSequence(final List<@NonNull ISequenceElement> list) {
		this.list = list;
	}

	/**
	 * Copy constructor cloning the contents of the input {@link ISequence}
	 * 
	 * @param sequence
	 */
	public ListSequence(final ISequence sequence) {
		list = new ArrayList<>(sequence.size());
		for (final ISequenceElement t : sequence) {
			list.add(t);
		}
	}

	@Override
	public Iterator<@NonNull ISequenceElement> iterator() {
		return new Iterator<@NonNull ISequenceElement>() {
			private final Iterator<@NonNull ISequenceElement> i = list.iterator();

			@Override
			public final boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public final ISequenceElement next() {
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
	public ISequenceElement get(final int index) {
		ISequenceElement element = list.get(index);
		if (element == null) {
			throw new IllegalArgumentException("Unknown element index");
		}
		return element;
	}

	@Override
	@NonNull
	public ISegment getSegment(final int start, final int end) {

		// Copy of the sublist to make segment independent from sequence.
		return new ListSegment(new ArrayList<>(list.subList(start, end)), this, start, end);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@Override
	public final ISequenceElement last() {
		return get(size() - 1);
	}

	@Override
	public ISequenceElement first() {
		return get(0);
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		} else if (!(obj instanceof ISequence)) {
			return false;
		} else {
			final Iterator<ISequenceElement> e1 = iterator();
			final Iterator<ISequenceElement> e2 = ((ISequence) obj).iterator();
			while (e1.hasNext() && e2.hasNext()) {
				final ISequenceElement o1 = e1.next();
				final ISequenceElement o2 = e2.next();
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
		final Iterator<ISequenceElement> i = iterator();
		while (i.hasNext()) {
			final ISequenceElement obj = i.next();
			hashCode = (31 * hashCode) + (obj == null ? 0 : obj.hashCode());
		}
		return hashCode;
	}
}
