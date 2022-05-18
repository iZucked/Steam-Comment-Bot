/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence;

import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LoadSequence implements ILoadSequence {
	private final int headLoadVolume;
	private final int tailLoadVolume;
	private final int totalLoadTime;
	private final int preLoadSetupTime;
	int loadTimePoint = 0;

	public LoadSequence(final int allocationDrop, final int duration, final int preLoadSetupTime) {
		this.tailLoadVolume = allocationDrop/duration;
		this.headLoadVolume = this.tailLoadVolume + (allocationDrop % duration);
		this.totalLoadTime = duration;
		this.preLoadSetupTime = preLoadSetupTime;
	}

//	public int getHeadLoadVolume() {
//		return headLoadVolume;
//	}
//
//	public int getTailLoadVolume() {
//		return tailLoadVolume;
//	}

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
		final Stream<Integer> preLoadStream = Stream.generate(() -> 0).limit(preLoadSetupTime);
		final Stream<Integer> tailStream = Stream.generate(() -> tailLoadVolume).limit(tailCount);
		final Stream<Integer> fullStream;
		
		Stream<Integer> currentStream = Stream.empty();
		if (loadTimePoint < preLoadSetupTime) {
			currentStream = Stream.concat(currentStream, Stream.generate(() -> 0).limit(preLoadSetupTime - loadTimePoint));
		}
		if (loadTimePoint == preLoadSetupTime) {
			currentStream = Stream.concat(currentStream, Stream.of(headLoadVolume));
		}

		currentStream = Stream.concat(currentStream, Stream.generate(() -> tailLoadVolume).limit(tailCount));
		
		if (loadTimePoint == 0) {
			fullStream = Stream.concat(Stream.of(headLoadVolume), tailStream);
		} else {
			fullStream = tailStream;
		}
		return tailStream.iterator();
	}
}
