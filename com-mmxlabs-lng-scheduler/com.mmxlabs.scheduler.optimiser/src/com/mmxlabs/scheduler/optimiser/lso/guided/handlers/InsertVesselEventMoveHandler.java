
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

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final ISequenceElement element, @NonNull Random random, @NonNull GuideMoveGeneratorOptions options,
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
		IVesselEventPortSlot portSlot = (IVesselEventPortSlot) portSlotProvider.getPortSlot(element);
		final List<ISequenceElement> orderedElements = portSlot.getEventSequenceElements();

		// Assume we can, in principle, use any fleet vessel
		final List<IResource> validTargetResources = new LinkedList<>();

		validTargetResources.addAll(helper.getAllVesselResources());

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
		final List<Pair<ISequenceElement, ISequenceElement>> validInsertionPairs = new LinkedList<>();
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
			final int followerIndex = precederLocation.getSecond() + 1;
			if (followerIndex < targetSequence.size()) {
				final ISequenceElement followerElement = targetSequence.get(followerIndex);
				if (followers.contains(followerElement)) {
					validInsertionPairs.add(new Pair<>(preceder, followerElement));
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
		for (final Pair<ISequenceElement, ISequenceElement> insertionPair : validInsertionPairs) {

			final Pair<IResource, Integer> location = lookupManager.lookup(insertionPair.getFirst());

			builder.withElements(null, orderedElements) //
					.withInsertAfter(location.getFirst(), insertionPair.getFirst()); //

			// Suggest element before and after as possible change targets
			hints.addSuggestedElements(insertionPair.getFirst(), insertionPair.getSecond());
			break;
		}

		hints.getUsedElements().addAll(orderedElements);
		return new Pair<IMove, Hints>(builder.create(), hints);

	}
}
