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
 * An implementation of {@link ISegment} that contains {@link ISequenceElement}s that are not contained anywhere else.
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public class DisconnectedSegment implements ISegment {

	private final List<ISequenceElement> elements;

	public DisconnectedSegment(final List<ISequenceElement> elements) {
		this.elements = elements;
	}

	@Override
	public Iterator<ISequenceElement> iterator() {
		return elements.iterator();
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public ISequenceElement get(final int index) {
		return elements.get(index);
	}

	@Override
	public int getSequenceStart() {
		return -1;
	}

	@Override
	public int getSequenceEnd() {
		return -1;
	}

	@Override
	public ISequence getSequence() {
		return null;
	}

}
