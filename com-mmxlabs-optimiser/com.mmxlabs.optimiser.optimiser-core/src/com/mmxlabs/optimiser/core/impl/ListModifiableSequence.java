/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Implementation of {@link IModifiableSequence} which uses a {@link List} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class ListModifiableSequence implements IModifiableSequence {

	private final List<@NonNull ISequenceElement> list;

	public ListModifiableSequence(final List<@NonNull ISequenceElement> list) {
		this.list = list;
	}

	@Override
	public Iterator<@NonNull ISequenceElement> iterator() {
		return list.iterator();
	}

	@Override
	public void add(@NonNull final ISequenceElement element) {
		list.add(element);
	}

	@Override
	public void insert(final int index, @NonNull final ISequenceElement element) {
		list.add(index, element);
	}

	@Override
	public void insert(final int index, @NonNull final ISegment segment) {
		int idx = index;
		for (final ISequenceElement e : segment) {
			list.add(idx++, e);
		}
	}

	@Override
	public ISequenceElement remove(final int index) {
		return list.remove(index);
	}

	@Override
	public boolean remove(@NonNull final ISequenceElement element) {
		return list.remove(element);
	}

	@Override
	public void remove(@NonNull final ISegment segment) {
		for (final ISequenceElement e : segment) {
			list.remove(e);
		}
	}

	@Override
	public void remove(final int start, final int end) {
		list.subList(start, end).clear();
	}

	@Override
	public void set(final int index, @NonNull final ISequenceElement element) {
		list.set(index, element);

	}

	@Override
	@NonNull
	public ISequenceElement get(final int index) {
		final ISequenceElement element = list.get(index);
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
	public void replaceAll(@NonNull final ISequence sequence) {
		list.clear();

		for (final ISequenceElement t : sequence) {
			list.add(t);
		}
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
			final Iterator<@NonNull ISequenceElement> e1 = iterator();
			final Iterator<@NonNull ISequenceElement> e2 = ((ISequence) obj).iterator();
			while (e1.hasNext() && e2.hasNext()) {
				final ISequenceElement o1 = e1.next();
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
		final Iterator<@NonNull ISequenceElement> i = iterator();
		while (i.hasNext()) {
			final ISequenceElement obj = i.next();
			hashCode = (31 * hashCode) + (obj == null ? 0 : obj.hashCode());
		}
		return hashCode;
	}

	@Override
	public String toString() {
		return list.toString();
	}
}
