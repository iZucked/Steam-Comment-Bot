package com.mmxlabs.optimiser.impl;

import java.util.Iterator;
import java.util.List;

import com.mmxlabs.optimiser.ISegment;
import com.mmxlabs.optimiser.ISequence;

/**
 * Default implementation of {@link ISegment} which uses a {@link List} as the
 * backing implementation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 */
public final class ListSegment<T> implements ISegment<T> {

	/**
	 * List of segment items
	 */
	private final List<T> l;

	/**
	 * {@link ISegment} instance this segment originates from
	 */
	private final ISequence<T> sequence;

	/**
	 * Start index of segment in parent sequence
	 */
	private final int start;
	/**
	 * End index of segment in parent sequence
	 */
	private final int end;

	public ListSegment(final List<T> segment, final ISequence<T> sequence,
			final int start, final int end) {
		this.l = segment;
		this.sequence = sequence;
		this.start = start;
		this.end = end;
	}

	@Override
	public T get(final int index) {
		return l.get(index);
	}

	@Override
	public ISequence<T> getSequence() {
		return sequence;
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
}
