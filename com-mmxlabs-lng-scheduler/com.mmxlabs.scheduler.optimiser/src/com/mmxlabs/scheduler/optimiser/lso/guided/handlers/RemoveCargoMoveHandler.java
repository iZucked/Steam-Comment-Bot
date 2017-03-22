/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;

public class RemoveCargoMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IMoveHandlerHelper moveHelper;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final ISequenceElement element, @NonNull Random random, @NonNull GuideMoveGeneratorOptions options,
			@NonNull Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = lookupManager.getRawSequences();

		final Hints hints = new Hints();

		final Pair<IResource, Integer> slotLocation = lookupManager.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource == null) {
			// Already out of the solution.
			return null;

		}
		final ISequence fromSequence = sequences.getSequence(fromResource);

		final List<ISequenceElement> orderedCargoElements = moveHelper.extractSegment(fromSequence, element);

		if (orderedCargoElements.isEmpty()) {
			throw new IllegalStateException();
		}

		final RemoveElementsMove.Builder builder = RemoveElementsMove.Builder.newMove();
		for (final ISequenceElement e : orderedCargoElements) {
			if (!helper.isOptional(e)) {
				if (options.isStrictOptional()) {
					return null;
				} else {
					hints.addProblemElement(e);
				}
			} else {
				hints.usedElement(e);
			}
			builder.removeElement(fromResource, e);
		}

		return new Pair<IMove, Hints>(builder.create(), hints);
	}
}
