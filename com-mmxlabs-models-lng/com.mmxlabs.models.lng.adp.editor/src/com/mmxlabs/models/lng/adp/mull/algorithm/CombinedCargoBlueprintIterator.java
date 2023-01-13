/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;

@NonNullByDefault
public class CombinedCargoBlueprintIterator implements Iterator<Pair<Integer, ICargoBlueprint>> {

	private int nextIndex;
	@Nullable
	private Pair<Integer, ICargoBlueprint> nextPair = null;
	private int nextSelectedIterIndex;
	private final List<Iterator<Pair<Integer, ICargoBlueprint>>> iters;
	private final List<@Nullable Pair<Integer, ICargoBlueprint>> nextPairs;

	public CombinedCargoBlueprintIterator(final List<Pair<Integer, ICargoBlueprint>>... lists) {
		iters = new ArrayList<>(lists.length);
		nextPairs = new ArrayList<>(lists.length);
		for (int i = 0; i < lists.length; ++i) {
			final Iterator<Pair<Integer, ICargoBlueprint>> currentIter = lists[i].iterator();
			iters.add(currentIter);
			final Pair<Integer, ICargoBlueprint> currentPair = currentIter.hasNext() ? currentIter.next() : null;
			nextPairs.add(currentPair);
			if (nextPair == null) {
				if (currentPair != null) {
					nextPair = currentPair;
					nextSelectedIterIndex = i;
				}
			} else {
				if (currentPair != null) {
					if (currentPair.getFirst() < nextPair.getFirst()) {
						nextPair = currentPair;
						nextSelectedIterIndex = i;
					}
				}
			}
		}
	}

	@Override
	public boolean hasNext() {
		return nextPair != null;
	}

	@Override
	public Pair<Integer, ICargoBlueprint> next() {
		final Pair<Integer, ICargoBlueprint> pNextPair = this.nextPair;
		if (pNextPair == null) {
			throw new NoSuchElementException();
		}
		final Pair<Integer, ICargoBlueprint> pairToReturn = pNextPair;
		if (iters.get(nextSelectedIterIndex).hasNext()) {
			nextPairs.set(nextSelectedIterIndex, iters.get(nextSelectedIterIndex).next());
		} else {
			nextPairs.set(nextSelectedIterIndex, null);
		}
		nextPair = null;
		for (int i = 0; i < nextPairs.size(); ++i) {
			final Pair<Integer, ICargoBlueprint> currentPair = nextPairs.get(i);
			if (nextPair == null) {
				if (currentPair != null) {
					nextPair = currentPair;
					nextSelectedIterIndex = i;
				}
			} else {
				if (currentPair != null) {
					if (currentPair.getFirst() < nextPair.getFirst()) {
						nextPair = currentPair;
						nextSelectedIterIndex = i;
					}
				}
			}
		}
		return pairToReturn;
	}

}
