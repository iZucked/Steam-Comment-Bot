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
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class RemoveVesselEventMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IPortSlotProvider portSlotProvider;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final ISequenceElement element, @NonNull final Random random, @NonNull final GuideMoveGeneratorOptions options,
			@NonNull final Collection<ISequenceElement> forbiddenElements) {

		final Hints hints = new Hints();

		// Loop up the element and ensure it is unused
		final Pair<IResource, Integer> slotLocation = lookupManager.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource == null) {
			// Invalid state
			return null;

		}

		assert helper.isVesselEvent(element);
		final IVesselEventPortSlot portSlot = (IVesselEventPortSlot) portSlotProvider.getPortSlot(element);
		final List<ISequenceElement> orderedElements = portSlot.getEventSequenceElements();

		final RemoveElementsMove.Builder builder = RemoveElementsMove.Builder.newMove();
		for (final ISequenceElement e : orderedElements) {
			if (!helper.isOptional(e)) {
				return null;
			}
			builder.removeElement(fromResource, e);
		}

		hints.getUsedElements().addAll(orderedElements);
		return new Pair<IMove, Hints>(builder.create(), hints);

	}
}
