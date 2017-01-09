/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Iterator;
import java.util.List;

import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Default implementation of {@link ISegment} which uses a {@link List} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class ListSegment implements ISegment {

	/**
	 * List of segment items
	 */
	private final List<ISequenceElement> l;

	/**
	 * {@link ISegment} instance this segment originates from
	 */
	private final ISequence sequence;

	/**
	 * Start index of segment in parent sequence
	 */
	private final int start;
	/**
	 * End index of segment in parent sequence
	 */
	private final int end;

	public ListSegment(final List<ISequenceElement> segment, final ISequence sequence, final int start, final int end) {
		this.l = segment;
		this.sequence = sequence;
		this.start = start;
		this.end = end;
	}

	@Override
	public ISequenceElement get(final int index) {
		return l.get(index);
	}

	@Override
	public ISequence getSequence() {
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
	public Iterator<ISequenceElement> iterator() {
		return l.iterator();
	}
}
