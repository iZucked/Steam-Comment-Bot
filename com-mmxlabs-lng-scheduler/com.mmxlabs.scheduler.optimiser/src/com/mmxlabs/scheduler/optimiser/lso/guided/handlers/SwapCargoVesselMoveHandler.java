/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertSegmentMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public class SwapCargoVesselMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IMoveHandlerHelper moveHelper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final ISequenceElement element, @NonNull final Random random, @NonNull final GuideMoveGeneratorOptions options,
			@NonNull final Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = lookupManager.getRawSequences();

		final Hints hints = new Hints();

		final Pair<IResource, Integer> slotLocation = lookupManager.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource == null) {
			// Invalid state
			return null;
		}
		
		final ISequence fromSequence = sequences.getSequence(fromResource);

		final List<ISequenceElement> orderedCargoElements = moveHelper.extractSegment(fromSequence, element);

		if (orderedCargoElements.isEmpty()) {
			moveHelper.extractSegment(fromSequence, element);
			throw new IllegalStateException();
		}

		final int slotIndex = slotLocation.getSecond();
		final int offset = orderedCargoElements.indexOf(element);
		final int segmentStart = slotIndex - offset;
		final int segmentEnd = segmentStart + orderedCargoElements.size();

		final ISequenceElement elementBeforeSegment = fromSequence.get(segmentStart - 1);
		final ISequenceElement elementAfterSegment = fromSequence.get(segmentEnd);

		// TODO: THis should really use the shipping length hint
		// TODO: Exclude self

		// Find all possible elements that could be inserted into the gap

		final List<IResource> validTargetResources = new LinkedList<>();
		validTargetResources.addAll(helper.getAllVesselResources(true));

		// Filter to the common set of valid resources.
		for (final ISequenceElement e : orderedCargoElements) {
			validTargetResources.retainAll(helper.getAllowedResources(e));
		}

		// Get list of preceders for first element
		final Followers<ISequenceElement> preceders = followersAndPreceders.getValidPreceders(orderedCargoElements.get(0));

		// Get list of followers for last element.
		final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(orderedCargoElements.get(orderedCargoElements.size() - 1));

		// Build up a list of all valid insertion points
		final List<Pair<ISequenceElement, ISequenceElement>> validInsertionPairs = new LinkedList<>();
		LOOP_PRECEDER: for (final ISequenceElement preceder : preceders) {
			final Pair<IResource, Integer> precederLocation = lookupManager.lookup(preceder);
			// Unused? skip
			if (precederLocation.getFirst() == null) {
				continue LOOP_PRECEDER;
			}

			// Do not move into same resource
			if (precederLocation.getFirst() == fromResource) {
				// continue LOOP_PRECEDER;
			}

			if (preceder == elementBeforeSegment) {
				continue LOOP_PRECEDER;
			}

			// Is the preceder on a useable resource?
			if (!validTargetResources.contains(precederLocation.getFirst())) {
				continue LOOP_PRECEDER;
			}
			LOOP_FOLLOWER: for (final ISequenceElement follower : followers) {
				final Pair<IResource, Integer> followerLocation = lookupManager.lookup(follower);
				// Unused? skip
				if (followerLocation.getFirst() == null) {
					continue LOOP_FOLLOWER;
				}

				if (follower == elementAfterSegment) {
					continue LOOP_FOLLOWER;
				}

				// The following check is redundant by the next check, preceder resource equality.
				// // Is the follower on a useable resource?
				// if (!validTargetResources.contains(followerLocation.getFirst())) {
				// continue LOOP_FOLLOWER;
				// }

				// They are on different resources, so skip
				if (precederLocation.getFirst() != followerLocation.getFirst()) {
					continue LOOP_FOLLOWER;
				}

				// Consecutive elements?
				if (precederLocation.getSecond() + 1 == followerLocation.getSecond().intValue()) {
					// Valid insertion index!
					validInsertionPairs.add(new Pair<>(preceder, follower));
				}
			}
		}

		// Find a pairing of preceders and followers on the same resource AND the ordered elements can be moved onto.
		if (validInsertionPairs.isEmpty()) {
			return null;
		}

		final LinkedHashSet<ISequenceElement> suggestedElements = new LinkedHashSet<>();
		// We may have left a gap, what can we do to fill it?
		{
			// TODO: THis should really use the shipping length hint
			// TODO: Exclude self

			// Find all possible elements that could be inserted into the gap
			followersAndPreceders.getValidFollowers(elementBeforeSegment).forEach(suggestedElements::add);
			followersAndPreceders.getValidPreceders(elementAfterSegment).forEach(suggestedElements::add);

			// Add all elements in the sequence to be considered for further change
			// TODO: Do we really want to do this? Consider issues with long ballast legs -
			// Maybe just +/- 1 cargo / event rather than whole sequence (which could be quite large?)?
			// e.g. [segment start - 2 -> segment end + 2)
			fromSequence.forEach(suggestedElements::add);
		}

		final InsertSegmentMove.Builder builder = InsertSegmentMove.Builder.newMove();

		// TODO: The hint manager could be used here to order by known shipping length
		// Pick the first random insertion point
		Collections.shuffle(validInsertionPairs, random);
		for (final Pair<ISequenceElement, ISequenceElement> insertionPair : validInsertionPairs) {

			final Pair<IResource, Integer> location = lookupManager.lookup(insertionPair.getFirst());

			builder.withElements(fromResource, orderedCargoElements) //
					.withInsertAfter(location.getFirst(), insertionPair.getFirst()); //

			sequences.getSequence(location.getFirst()).forEach(suggestedElements::add);

			break;
		}

		// Filter out the elements we have just moved
		suggestedElements.removeAll(orderedCargoElements);
		hints.addSuggestedElements(suggestedElements);
		hints.getUsedElements().addAll(orderedCargoElements);

		return new Pair<>(builder.create(), hints);
	}
}
