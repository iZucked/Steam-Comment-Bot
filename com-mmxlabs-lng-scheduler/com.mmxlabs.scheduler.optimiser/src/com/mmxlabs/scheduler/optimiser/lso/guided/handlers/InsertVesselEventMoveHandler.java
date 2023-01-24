/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertSegmentMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class InsertVesselEventMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IPortSlotProvider portSlotProvider;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Inject
	private @NonNull IMoveHandlerHelper moveHandlerHelper;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final ISequenceElement element, @NonNull final Random random, @NonNull final GuideMoveGeneratorOptions options,
			@NonNull final Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = lookupManager.getRawSequences();

		final Hints hints = new Hints();

		// Loop up the element and ensure it is unused
		final Pair<IResource, Integer> slotLocation = lookupManager.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource != null) {
			// Invalid state
			return null;

		}
		//// Find possible element pairings which can be used to make a shipped cargo (FOB Purchase to DES Sale)

		assert helper.isVesselEvent(element);
		final IVesselEventPortSlot portSlot = (IVesselEventPortSlot) portSlotProvider.getPortSlot(element);
		final List<ISequenceElement> orderedElements = portSlot.getEventSequenceElements();

		// Assume we can, in principle, use any fleet vessel
		final List<IResource> validTargetResources = new LinkedList<>();

		validTargetResources.addAll(helper.getAllVesselResources(true));

		// Filter the valid target resource list to the common set.
		for (final ISequenceElement e : orderedElements) {
			final Iterator<IResource> itr = validTargetResources.iterator();
			while (itr.hasNext()) {
				if (!helper.checkResource(e, itr.next())) {
					itr.remove();
				}
			}
		}

		// Is this combination of elements permitted on any vessel?
		if (validTargetResources.isEmpty()) {
			return null;
		}

		// Get list of preceders for first element
		final Followers<ISequenceElement> preceders = followersAndPreceders.getValidPreceders(orderedElements.get(0));

		// Get list of followers for last element.
		final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(orderedElements.get(orderedElements.size() - 1));

		// Build up a list of all valid insertion points. For all valid elements that can go before the first element in the insertion list, see if it's following element could also follow the
		// last element in the insert sequence
		final List<Triple<ISequenceElement, ISequenceElement, Boolean>> validInsertionPairs = new LinkedList<>();
		LOOP_PRECEDER: for (final ISequenceElement preceder : preceders) {
			final Pair<IResource, Integer> precederLocation = lookupManager.lookup(preceder);
			// Element Unused? Skip as we can't insert after it.
			if (precederLocation.getFirst() == null) {
				continue LOOP_PRECEDER;
			}

			// Is the preceder on a valid resource?
			if (!validTargetResources.contains(precederLocation.getFirst())) {
				continue LOOP_PRECEDER;
			}

			final ISequence targetSequence = sequences.getSequence(precederLocation.getFirst());
			int followerIndex = precederLocation.getSecond() + 1;
			int segSize = 0;
			if (followerIndex < targetSequence.size()) {
				final ISequenceElement firstFollowerElement = targetSequence.get(followerIndex);
				// Seg size of 2 is can we insert before the next cargo (or event) or the one after.
				while (followerIndex < targetSequence.size() && segSize < 2) {
					final ISequenceElement followerElement = targetSequence.get(followerIndex);
					if (followers.contains(followerElement)) {
						// If this is not the next cargo, mark the next cargo as a problem
						validInsertionPairs.add(new Triple<>(preceder, firstFollowerElement, segSize != 0));
						break;
					}
					// Skip the whole segment
					final List<ISequenceElement> followerSegment = moveHandlerHelper.extractSegment(targetSequence, followerElement);
					followerIndex += followerSegment.size();
					++segSize;
				}
			}
		}

		// Find a pairing of preceders and followers on the same resource AND the ordered elements can be moved onto.
		if (validInsertionPairs.isEmpty()) {
			return null;
		}

		final InsertSegmentMove.Builder builder = InsertSegmentMove.Builder.newMove();

		// TODO: The hint manager could be used here to order by known shipping length
		// Pick the first random insertion point
		Collections.shuffle(validInsertionPairs, random);
		for (final Triple<ISequenceElement, ISequenceElement, Boolean> insertionPair : validInsertionPairs) {

			final Pair<IResource, Integer> location = lookupManager.lookup(insertionPair.getFirst());

			builder.withElements(null, orderedElements) //
					.withInsertAfter(location.getFirst(), insertionPair.getFirst()); //

			// Suggest element before and after as possible change targets
			hints.addSuggestedElements(insertionPair.getFirst(), insertionPair.getSecond());
			if (insertionPair.getThird()) {
				// This element was marked as a problem.
				hints.addProblemElement(insertionPair.getSecond());
			}
			break;
		}

		hints.getUsedElements().addAll(orderedElements);
		return new Pair<>(builder.create(), hints);

	}
}
