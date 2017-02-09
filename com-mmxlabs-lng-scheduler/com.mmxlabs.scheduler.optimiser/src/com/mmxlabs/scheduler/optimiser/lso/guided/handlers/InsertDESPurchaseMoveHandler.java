package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertDESPurchaseMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

/**
 * This move handler is designed to be given an unused DES Purchase and it will find an appropriate DES Sale to pair it up against. This may leave an open purchase which will be listed in the problem
 * elements of the returned hints.
 * 
 * @author Simon Goodall
 *
 */
public class InsertDESPurchaseMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final @NonNull ISequenceElement desPurchase, @NonNull Random random, @NonNull GuideMoveGeneratorOptions options,
			@NonNull Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = lookupManager.getRawSequences();

		final IResource desPurchaseResource = helper.getDESPurchaseResource(desPurchase);

		final InsertDESPurchaseMove.Builder builder = InsertDESPurchaseMove.Builder.newMove() //
				.withUnusedDESPurchase(desPurchaseResource, desPurchase);

		final Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidFollowers(desPurchase);
		final List<ISequenceElement> followers = Lists.newArrayList(validFollowers);
		followers.removeAll(forbiddenElements);
		if (followers.isEmpty()) {
			return null;
		}
		Collections.shuffle(followers, random);
		final Hints hints = new Hints();
		for (final ISequenceElement possibleFollower : followers) {
			// This should be implicit by virtue of being able to follow the DES Purchase
			assert helper.checkResource(possibleFollower, desPurchaseResource);
			hints.getUsedElements().add(desPurchase);
			// Where is this possible follower?
			final Pair<IResource, Integer> location = lookupManager.lookup(possibleFollower);
			assert location != null;
			if (location.getFirst() == null) {
				builder.withUnusedDESSale(possibleFollower);
				hints.getUsedElements().add(possibleFollower);
			} else {
				builder.withUsedDESSale(location.getFirst(), possibleFollower);

				final ISequence desSaleSequence = sequences.getSequence(location.getFirst());
				final ISequenceElement prev = desSaleSequence.get(location.getSecond() - 1);
				if (helper.isOptional(prev)) {
					hints.getUsedElements().add(possibleFollower);
				} else {
					if (helper.isLoadSlot(prev)) {
						hints.addProblemElement(prev);
					} else {
						hints.addShippingLength(location.getFirst(), location.getSecond() - 1);
					}
				}
			}

			break;
		}

		return new Pair<IMove, Hints>(builder.create(), hints);
	}
}
