package com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class SequentialLoadSequence implements ILoadSequence {
	
	private final Deque<ILoadSequence> sequences;
	
	public SequentialLoadSequence(final List<ILoadSequence> sequences) {
		this.sequences = new LinkedList<>(sequences);
	}

	@Override
	public int stepForward() {
		if (this.sequences.isEmpty()) {
			return 0;
		}
		final ILoadSequence firstSequence = sequences.getFirst();
		final int amountToReturn = firstSequence.stepForward();
		if (firstSequence.isComplete()) {
			this.sequences.removeFirst();
		}
		return amountToReturn;
	}

	@Override
	public boolean isComplete() {
		return this.sequences.isEmpty();
	}

	public Stream<Integer> stream() {
		return this.sequences.stream().flatMap(ILoadSequence::stream);
	}

	@Override
	public Iterator<Integer> createRemainingLoadIterator() {
		return stream().iterator();
	}

	
}
