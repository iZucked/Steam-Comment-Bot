/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.moves.util.IBreakPointHelper;

/**
 * 
 */
public class SequencesConstrainedLoopingMoveGeneratorUnit extends SequencesConstrainedMoveGeneratorUnit {

	private static final int MAX_LOOPS = 200;

	@Inject
	private IBreakPointHelper breakPointHelper;

	@Override
	public Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> findEdge(@NonNull ILookupManager stateManager, @NonNull Random random) {
		int currentLoops = 0;
		Pair<IResource, Integer> pos1 = null;
		Pair<IResource, Integer> pos2 = null;
		Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> positions = null;

		while (currentLoops < MAX_LOOPS) {

			final Pair<ISequenceElement, ISequenceElement> newPair = RandomHelper.chooseElementFrom(random, breakPointHelper.getValidBreaks());
			if (newPair != null) {
				pos1 = stateManager.lookup(newPair.getFirst());
				pos2 = stateManager.lookup(newPair.getSecond());

				if (pos1 != null && pos2 != null && (pos1.getFirst() != null) && (pos2.getFirst() != null)) {
					positions = new Pair<>(pos1, pos2);
					return positions;
				}
			}
			currentLoops += 1;
		}

		return positions;
	}

	public int getMaxLoops() {

		return MAX_LOOPS;
	}
}
