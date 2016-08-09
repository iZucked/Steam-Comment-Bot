/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.Move2over2;
import com.mmxlabs.optimiser.lso.impl.Move3over2;
import com.mmxlabs.optimiser.lso.impl.Move4over2;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.optimiser.lso.impl.NullMove2Over2;
import com.mmxlabs.optimiser.lso.impl.NullMove3Over2;
import com.mmxlabs.optimiser.lso.impl.NullMove4Over2;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

/**
 * Refactoring of the sequences-related CMG logic into a helper class.
 * 
 * This uses protected access to a lot of {@link ConstrainedMoveGenerator} fields, which isn't great, but if needs be they could all be made accessors. The code is very tightly connected anyway, so it
 * doesn't make that much difference.
 * 
 * @author hinton
 * 
 */
public class SequencesConstrainedLoopingMoveGeneratorUnit extends SequencesConstrainedMoveGeneratorUnit {

	private static final int MAX_LOOPS = 200;


	public SequencesConstrainedLoopingMoveGeneratorUnit(@NonNull final ConstrainedMoveGenerator owner) {
		super(owner);

	}

	@Override
	public Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> findEdge() {
		int currentLoops = 0;
		Pair<IResource, Integer> pos1 = null;
		Pair<IResource, Integer> pos2 = null;
		Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> positions = null;

		while (currentLoops < MAX_LOOPS) {

			final Pair<ISequenceElement, ISequenceElement> newPair = RandomHelper.chooseElementFrom(owner.getRandom(), owner.getValidBreaks());
			if (newPair != null) {
				Map<ISequenceElement, Pair<IResource, Integer>> reverseLookup = owner.getReverseLookup();
				pos1 = reverseLookup.get(newPair.getFirst());
				pos2 = reverseLookup.get(newPair.getSecond());

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
