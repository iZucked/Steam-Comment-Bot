/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;

public class CombinedCargoBlueprintIterator implements Iterator<Pair<Integer, CargoBlueprint>> {

	int nextIndex;
	Pair<Integer, CargoBlueprint> nextPair = null;
	int nextSelectedIterIndex;
	final List<Iterator<Pair<Integer, CargoBlueprint>>> iters;
	final List<Pair<Integer, CargoBlueprint>> nextPairs;

	public CombinedCargoBlueprintIterator(@NonNull final List<Pair<Integer, CargoBlueprint>>... lists) {
		iters = new ArrayList<>(lists.length);
		nextPairs = new ArrayList<>(lists.length);
		for (int i = 0; i < lists.length; ++i) {
			final Iterator<Pair<Integer, CargoBlueprint>> currentIter = lists[i].iterator();
			iters.add(currentIter);
			final Pair<Integer, CargoBlueprint> currentPair = currentIter.hasNext() ? currentIter.next() : null;
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
	public Pair<Integer, CargoBlueprint> next() {
		if (nextPair == null) {
			throw new NoSuchElementException();
		}
		final Pair<Integer, CargoBlueprint> pairToReturn = nextPair;
		if (iters.get(nextSelectedIterIndex).hasNext()) {
			nextPairs.set(nextSelectedIterIndex, iters.get(nextSelectedIterIndex).next());
		} else {
			nextPairs.set(nextSelectedIterIndex, null);
		}
		nextPair = null;
		for (int i = 0; i < nextPairs.size(); ++i) {
			final Pair<Integer, CargoBlueprint> currentPair = nextPairs.get(i);
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
