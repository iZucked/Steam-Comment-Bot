/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence;

import java.util.Iterator;
import java.util.stream.Stream;

public class SimpleLoadSequence implements ILoadSequence {
	private final int loadVolume;
	private final int totalLoadTime;
	private int loadTimePoint = 0;

	public SimpleLoadSequence(final int loadVolume, final int totalLoadTime) {
		this.loadVolume = loadVolume;
		this.totalLoadTime = Math.max(totalLoadTime, 0);
	}

	@Override
	public int stepForward() {
		if (loadTimePoint >= totalLoadTime) {
			return 0;
		}
		++loadTimePoint;
		return loadVolume;
	}

	@Override
	public boolean isComplete() {
		return loadTimePoint >= totalLoadTime;
	}

	@Override
	public Stream<Integer> stream() {
		if (isComplete()) {
			return Stream.empty();
		}
		final int count = totalLoadTime - loadTimePoint;
		return Stream.generate(() -> loadVolume).limit(count);
	}

	@Override
	public Iterator<Integer> createRemainingLoadIterator() {
		return stream().iterator();
	}

	@Override
	public int getUndoVolumeToRestore() {
		return loadTimePoint * loadVolume;
	}
}
