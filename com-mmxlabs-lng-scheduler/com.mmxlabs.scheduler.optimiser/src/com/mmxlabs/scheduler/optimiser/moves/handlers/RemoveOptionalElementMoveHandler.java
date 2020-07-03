/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;
import com.mmxlabs.scheduler.optimiser.lso.moves.RemoveAndFill;
import com.mmxlabs.scheduler.optimiser.lso.moves.RemoveOptionalElement;
import com.mmxlabs.scheduler.optimiser.lso.moves.SwapOptionalElements;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * A module for the {@link ConstrainedMoveGenerator} which handles moves around optional slots.
 * 
 * @author hinton
 * 
 */
public class RemoveOptionalElementMoveHandler implements IMoveGenerator {

	@Inject
	private IPhaseOptimisationData phaseOptimisationData;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private @NonNull IMoveHelper helper;

	@Override
	public IMove generateMove(@NonNull final ISequences rawSequences, @NonNull final ILookupManager lookupManager, @NonNull final Random random) {

		if (phaseOptimisationData.getOptionalElements().isEmpty()) {
			return new NullMove("RemoveOptionalElementMoveHandler", "No optional elements");
		}

		// select an optional element at random

		if (phaseOptimisationData.getOptionalElements().isEmpty()) {
			return new NullMove("RemoveOptionalElement", "No Optional Elements");
		}
		final ISequenceElement optional = RandomHelper.chooseElementFrom(random, phaseOptimisationData.getOptionalElements());
		final Pair<IResource, Integer> location = lookupManager.lookup(optional);

		if (location.getFirst() != null) {
			return generateRemovingMove(optional, location, lookupManager, random);
		}

		return new NullMove("RemoveOptionalElement", "Null First Element");
	}

	/**
	 * Attempt to remove the optional element at the given location, patching up the solution if possible.
	 * 
	 * @param unused
	 * @param location
	 * @return
	 */
	public IMove generateRemovingMove(final ISequenceElement element, final Pair<IResource, Integer> location, @NonNull final ILookupManager lookupManager, @NonNull final Random random) {
		final Integer locationIndex = location.getSecond();
		final ISequence locationSequence = lookupManager.getRawSequences().getSequence(location.getFirst());

		List<ISequenceElement> orderedElements = new ArrayList<>(1);
		orderedElements.add(element);

		IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		if (portSlot instanceof IVesselEventPortSlot) {
			IVesselEventPortSlot vesselEventPortSlot = (IVesselEventPortSlot) portSlot;
			orderedElements = vesselEventPortSlot.getEventSequenceElements();
		}

		final ISequenceElement beforeElement = locationSequence.get(lookupManager.lookup(orderedElements.get(0)).getSecond() - 1);
		final ISequenceElement afterElement = locationSequence.get(lookupManager.lookup(orderedElements.get(orderedElements.size() - 1)).getSecond() + 1);

		// check whether beforeElement can be before afterElement
		if (random.nextBoolean() && followersAndPreceders.getValidFollowers(beforeElement).contains(afterElement)) {
			// we can just cut out the optional elements

			final RemoveElementsMove.Builder builder = RemoveElementsMove.Builder.newMove();
			for (final ISequenceElement e : orderedElements) {
				if (!helper.isOptional(e)) {
					return null;
				}

				builder.removeElement(location.getFirst(), e);
			}

			return builder.create();
			// return new RemoveOptionalElement(location.getFirst(), orderedElementsIndices);
		} else {
			// we need to do something to make the solution valid after removing this element
			// either we can pop in another unused element, or we can patch something else in instead,
			// or we can remove another element as well.
			// for now let's assume that we need to patch an element from elsewhere
			// 1. try and find another optional element which is in use

			// first check whether either neighbour is optional, and if so whether we can
			// take it out as well and get a good answer

			if (phaseOptimisationData.isElementOptional(beforeElement)) {
				// check whether we can skip out both
				final ISequenceElement beforeBeforeElement = locationSequence.get(locationIndex - 2);
				if (followersAndPreceders.getValidFollowers(beforeBeforeElement).contains(afterElement)) {
					// remove both
					return new RemoveOptionalElement(location.getFirst(), locationIndex, locationIndex - 1);
				}
			}

			if (phaseOptimisationData.isElementOptional(afterElement)) {
				final ISequenceElement afterAfterElement = locationSequence.get(locationIndex + 2);
				if (followersAndPreceders.getValidFollowers(beforeElement).contains(afterAfterElement)) {
					// remove both
					return new RemoveOptionalElement(location.getFirst(), locationIndex + 1, locationIndex);
				}
			}

			final ISequenceElement another = RandomHelper.chooseElementFrom(random, phaseOptimisationData.getOptionalElements());
			final Pair<IResource, Integer> location2 = lookupManager.lookup(another);
			if (location2.getFirst() == null) {
				// this is a spare element, so we can rotate them
				return new SwapOptionalElements(location.getFirst(), locationIndex, location2.getSecond());
			} else {
				// try cutting them both out
				// TODO consider whether we can make this more efficient
				// as half the time we will pick L/L or D/D
				final ISequenceElement beforeAnother = lookupManager.getRawSequences().getSequence(location2.getFirst()).get(location2.getSecond() - 1);
				final ISequenceElement afterAnother = lookupManager.getRawSequences().getSequence(location2.getFirst()).get(location2.getSecond() + 1);
				// find whether we can move one of these into the gap
				if (followersAndPreceders.getValidFollowers(beforeElement).contains(afterAnother)) {
					// here afterAnother can go after before; resource2 is the thing whose guy gets moved.
					final IResource resource1 = location2.getFirst();
					final IResource resource2 = location.getFirst();
					return new RemoveAndFill(resource1, resource2, location2.getSecond(), locationIndex);
				} else if (followersAndPreceders.getValidFollowers(beforeAnother).contains(afterElement)) {
					// the converse of the above case.
					final IResource resource1 = location.getFirst();
					final IResource resource2 = location2.getFirst();
					return new RemoveAndFill(resource1, resource2, locationIndex, location2.getSecond());
				}
			}
		}

		return new NullMove("RemoveOptional", "Failure");
	}
}
