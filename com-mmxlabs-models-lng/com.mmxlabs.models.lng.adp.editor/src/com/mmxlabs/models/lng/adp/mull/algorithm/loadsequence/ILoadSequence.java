/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm.loadsequence;

import java.util.Iterator;
import java.util.stream.Stream;

public interface ILoadSequence {

	int getUndoVolumeToRestore();

	int stepForward();

	boolean isComplete();

	Stream<Integer> stream();

	Iterator<Integer> createRemainingLoadIterator();
}
