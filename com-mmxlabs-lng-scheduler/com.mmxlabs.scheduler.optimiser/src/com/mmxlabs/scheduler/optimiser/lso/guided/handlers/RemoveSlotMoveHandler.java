/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;

public class RemoveSlotMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final @NonNull ISequenceElement element, @NonNull Random random, @NonNull GuideMoveGeneratorOptions options,
			@NonNull Collection<ISequenceElement> forbiddenElements) {

		final Hints hints = new Hints();

		final Pair<IResource, Integer> slotLocation = lookupManager.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource == null) {
			// Already out of the solution.
			return null;

		}
		final RemoveElementsMove.Builder builder = RemoveElementsMove.Builder.newMove();
		if (!helper.isOptional(element)) {
			if (options.isStrictOptional()) {
				return null;
			} else {
				hints.addProblemElement(element);
			}
		} else {
			hints.getUsedElements().add(element);
		}
		builder.removeElement(fromResource, element);

		return new Pair<>(builder.create(), hints);
	}
}
