package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.lso.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.lso.guided.IGuidedMoveHelper;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.LookupManager;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertDESPurchaseMove;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

/**
 * This move handler is designed to be given an unused DES Purchase and it will find an appropriate DES Sale to pair it up against. This may leave an open purchase which will be listed in the problem
 * elements of the returned hints.
 * 
 * @author Simon Goodall
 *
 */
public class InsertDESPurchaseMoveHandler implements IMoveHandler {

	@Inject
	private @NonNull IGuidedMoveHelper helper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull LookupManager state, final @NonNull ISequenceElement desPurchase) {
		final ISequences sequences = state.getSequences();

		final IResource desPurchaseResource = helper.getDESPurchaseResource(desPurchase);

		final InsertDESPurchaseMove.Builder builder = InsertDESPurchaseMove.Builder.newMove() //
				.withUnusedDESPurchase(desPurchaseResource, desPurchase);

		final Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidFollowers(desPurchase);
		final List<ISequenceElement> followers = Lists.newArrayList(validFollowers);
		Collections.shuffle(followers, helper.getSharedRandom());

		final Hints hints = new Hints();
		for (final ISequenceElement possibleFollower : followers) {
			// This should be implicit by virtue of being able to follow the DES Purchase
			assert helper.checkPermittedResource(possibleFollower, desPurchaseResource);

			// Where is this possible follower?
			final Pair<IResource, Integer> location = state.lookup(possibleFollower);
			assert location != null;
			if (location.getFirst() == null) {
				builder.withUnusedDESSale(possibleFollower);
			} else {
				builder.withUsedDESSale(location.getFirst(), possibleFollower);

				final ISequence desSaleSequence = sequences.getSequence(location.getFirst());
				final ISequenceElement prev = desSaleSequence.get(location.getSecond() - 1);
				if (helper.isLoadSlot(prev)) {
					hints.addProblemElement(prev);
				} else {
					hints.addShippingLength(location.getFirst(), location.getSecond() - 1);
				}
			}

			break;
		}

		return new Pair<IMove, Hints>(builder.create(), hints);
	}
}
