package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.IGuidedMoveHelper;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertCargoMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.LookupManager;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public class InsertCargoVesselMoveHandler implements IGuidedMoveHandler {

	
	
	@Inject
	private @NonNull IGuidedMoveHelper helper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull LookupManager state, final ISequenceElement element, @NonNull Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = state.getSequences();

		final Hints hints = new Hints();

		// Loop up the element and ensure it is unused
		final Pair<IResource, Integer> slotLocation = state.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource != null) {
			// Invalid state
			return null;

		}
		//// Find possible element pairings which can be used to make a shipped cargo (FOB Purchase to DES Sale)

		// List of possible pairing elements
		final List<ISequenceElement> filteredUnusedSequenceElements;

		// This is the LD style cargo we will create
		final List<ISequenceElement> orderedCargoElements = new ArrayList<>(2);
		// The index into the orderedCargoElements list for which we need the pairing (should be 0 or 1)
		final int insertIndex;
		{
			final List<ISequenceElement> unusedSequenceElements = new ArrayList<>(sequences.getUnusedElements());
			if (helper.isDESSale(element)) {
				// Find a valid set of FOB Purchases
				insertIndex = 0;
				orderedCargoElements.add(null);
				orderedCargoElements.add(element);
				final Followers<ISequenceElement> preceders = followersAndPreceders.getValidPreceders(element);

				filteredUnusedSequenceElements = unusedSequenceElements.stream()//
						.filter(e -> helper.isLoadSlot(e))//
						.filter(e -> preceders.contains(e))//
						.filter(e -> !forbiddenElements.contains(e)) //
						.collect(Collectors.toList());
			} else if (helper.isFOBPurchase(element)) {
				// Find a valid set of DES Sales
				insertIndex = 1;
				orderedCargoElements.add(element);
				orderedCargoElements.add(null);
				final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(element);

				filteredUnusedSequenceElements = unusedSequenceElements.stream() //
						.filter(e -> helper.isDischargeSlot(e))//
						.filter(e -> followers.contains(e))//
						.filter(e -> !forbiddenElements.contains(e)) //
						.collect(Collectors.toList());
			} else {
				// Invalid element type passed to method
				throw new IllegalStateException();
			}
		}
		// Randomise search order, find the first valid pairing
		Collections.shuffle(filteredUnusedSequenceElements, helper.getSharedRandom());
		LOOP_UNUSED: for (final ISequenceElement other : filteredUnusedSequenceElements) {

			// Construct the sequence
			orderedCargoElements.set(insertIndex, other);

			// Assume we can, in principle, use and fleet vessel
			final List<IResource> validTargetResources = new LinkedList<>();
			if (helper.isDESPurchase(other)) {
				validTargetResources.add(helper.getDESPurchaseResource(other));
			} else if (helper.isFOBSale(other)) {
				validTargetResources.add(helper.getFOBSaleResource(other));
			} else {
				validTargetResources.addAll(helper.getAllVesselResources());
			}

			// Filter the valid target resource list to the common set.
			for (final ISequenceElement e : orderedCargoElements) {
				final Iterator<IResource> itr = validTargetResources.iterator();
				while (itr.hasNext()) {
					if (!helper.checkPermittedResource(e, itr.next())) {
						itr.remove();
					}
				}
			}

			// Is this combination of elements permitted on any vessel?
			if (validTargetResources.isEmpty()) {
				continue;
			}

			// Get list of preceders for first element
			final Followers<ISequenceElement> preceders = followersAndPreceders.getValidPreceders(orderedCargoElements.get(0));

			// Get list of followers for last element.
			final Followers<ISequenceElement> followers = followersAndPreceders.getValidFollowers(orderedCargoElements.get(orderedCargoElements.size() - 1));

			// Build up a list of all valid insertion points. For all valid elements that can go before the first element in the insertion list, see if it's following element could also follow the
			// last element in the insert sequence
			final List<Pair<ISequenceElement, ISequenceElement>> validInsertionPairs = new LinkedList<>();
			LOOP_PRECEDER: for (final ISequenceElement preceder : preceders) {
				final Pair<IResource, Integer> precederLocation = state.lookup(preceder);
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
				if (followerIndex < targetSequence.size()) {
					final ISequenceElement followerElement = targetSequence.get(followerIndex);
					if (followers.contains(followerElement)) {
						validInsertionPairs.add(new Pair<>(preceder, followerElement));
					}
				}
			}

			// Find a pairing of preceders and followers on the same resource AND the ordered elements can be moved onto.
			if (validInsertionPairs.isEmpty()) {
				continue LOOP_UNUSED;
			}

			final InsertCargoMove.Builder builder = InsertCargoMove.Builder.newMove();

			// TODO: The hint manager could be used here to order by known shipping length
			// Pick the first random insertion point
			Collections.shuffle(validInsertionPairs, helper.getSharedRandom());
			for (final Pair<ISequenceElement, ISequenceElement> insertionPair : validInsertionPairs) {

				final Pair<IResource, Integer> location = state.lookup(insertionPair.getFirst());

				builder.withElements(null, orderedCargoElements) //
						.withInsertAfter(location.getFirst(), insertionPair.getFirst()); //

				// Suggest element before and after as possible change targets
				hints.addSuggestedElements(insertionPair.getFirst(), insertionPair.getSecond());
				break;
			}

			return new Pair<IMove, Hints>(builder.create(), hints);
		}

		return null;
	}
}
