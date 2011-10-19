/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.Move3over2;

/**
 * Move generator which tries to remove charter out elements
 * 
 * @author hinton
 * 
 */
public class CounterCharterOutMoveGenerator<T> implements IMoveGenerator<T> {
	private ISequences<T> sequences;
	private Set<IResource> badResources;
	final List<Map.Entry<IResource, ISequence<T>>> nonEmptyBadSequences = new ArrayList<Map.Entry<IResource, ISequence<T>>>();
	final List<Map.Entry<IResource, ISequence<T>>> goodSequences = new ArrayList<Map.Entry<IResource, ISequence<T>>>();
	private Random random;
	
	private class RemoveCharterOut<T> extends Move3over2<T> {

	}

	@Override
	public IMove<T> generateMove() {
		Move3over2<T> result = null;
		for (final Map.Entry<IResource, ISequence<T>> entry : sequences.getSequences().entrySet()) {
			if (badResources.contains(entry.getKey())) {
				if (entry.getValue().size() > 2) {
					nonEmptyBadSequences.add(entry);
				}
			} else {
				goodSequences.add(entry);
			}
		}

		
		if (nonEmptyBadSequences.size() > 0) {
			final Map.Entry<IResource, ISequence<T>> bad = RandomHelper.chooseElementFrom(random, nonEmptyBadSequences);
			final Map.Entry<IResource, ISequence<T>> good = RandomHelper.chooseElementFrom(random, goodSequences);

			result = new RemoveCharterOut<T>();
			result.setResource1(bad.getKey());
			result.setResource2(good.getKey());
			result.setResource1Start(1);
			result.setResource1End(bad.getValue().size() - 1);
			result.setResource2Position(RandomHelper.nextIntBetween(random, 1, good.getValue().size() - 1));
		}
		
		nonEmptyBadSequences.clear();
		goodSequences.clear();

		return result;
	}

	@Override
	public ISequences<T> getSequences() {
		return sequences;
	}

	@Override
	public void setSequences(final ISequences<T> sequences) {
		this.sequences = sequences;
	}

	public void setRandom(final Random random) {
		this.random = random;
	}

	public void setBadResources(final Set<IResource> resources) {
		this.badResources = resources;
	}
}
