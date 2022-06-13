/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

public class SequentialLoadSequence implements ILoadSequence {

	private final Deque<@NonNull ILoadSequence> completedSequences = new LinkedList<>();
	private final Deque<@NonNull ILoadSequence> sequences;

	public SequentialLoadSequence(final List<@NonNull ILoadSequence> sequences) {
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
			completedSequences.add(this.sequences.removeFirst());
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

	@Override
	public int getUndoVolumeToRestore() {
		return completedSequences.stream().mapToInt(ILoadSequence::getUndoVolumeToRestore).sum() + sequences.getFirst().getUndoVolumeToRestore();
	}

}
