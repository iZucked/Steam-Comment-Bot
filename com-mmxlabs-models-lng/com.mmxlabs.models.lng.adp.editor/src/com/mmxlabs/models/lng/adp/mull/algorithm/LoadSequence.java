/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.Iterator;
import java.util.stream.Stream;

public class LoadSequence implements ILoadSequence {
	private final int headLoadVolume;
	private final int tailLoadVolume;
	private final int totalLoadTime;
	int loadTimePoint = 0;

	public LoadSequence(final int allocationDrop, final int duration) {
		this.tailLoadVolume = allocationDrop/duration;
		this.headLoadVolume = this.tailLoadVolume + (allocationDrop % duration);
		this.totalLoadTime = duration;
	}

	public int getHeadLoadVolume() {
		return headLoadVolume;
	}

	public int getTailLoadVolume() {
		return tailLoadVolume;
	}

	public int stepForward() {
		if (loadTimePoint >= totalLoadTime) {
			return 0;
		}
		final int volumeToReturn = loadTimePoint == 0 ? headLoadVolume : tailLoadVolume;
		++loadTimePoint;
		return volumeToReturn;
	}

	public boolean isComplete() {
		return loadTimePoint >= totalLoadTime;
	}

	public Iterator<Integer> createRemainingLoadIterator() {
		if (isComplete()) {
			return Stream.<Integer>empty().iterator();
		}
		final int tailCount = totalLoadTime - Math.max(loadTimePoint, 1);
		final Stream<Integer> tailStream = Stream.generate(() -> tailLoadVolume).limit(tailCount);
		final Stream<Integer> fullStream;
		if (loadTimePoint == 0) {
			fullStream = Stream.concat(Stream.of(headLoadVolume), tailStream);
		} else {
			fullStream = tailStream;
		}
		return tailStream.iterator();
	}
}
