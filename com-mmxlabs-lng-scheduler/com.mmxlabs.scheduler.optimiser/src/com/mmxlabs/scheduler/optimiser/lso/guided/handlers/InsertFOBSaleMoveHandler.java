package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertFOBSaleMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.LookupManager;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public class InsertFOBSaleMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull LookupManager state, final ISequenceElement fobSale, @NonNull Random random, @NonNull GuideMoveGeneratorOptions options,
			@NonNull Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = state.getSequences();
		final IResource fobSaleResource = helper.getFOBSaleResource(fobSale);

		final InsertFOBSaleMove.Builder builder = InsertFOBSaleMove.Builder.newMove() //
				.withUnusedFOBSale(fobSaleResource, fobSale);

		final Followers<ISequenceElement> validFollowers = followersAndPreceders.getValidPreceders(fobSale);
		final List<ISequenceElement> preceders = Lists.newArrayList(validFollowers);
		preceders.removeAll(forbiddenElements);
		Collections.shuffle(preceders, random);

		final Hints hints = new Hints();
		for (final ISequenceElement possiblePreceder : preceders) {
			// This should be implicit by virtue of being able to follow the DES Purchase
			assert helper.checkResource(possiblePreceder, fobSaleResource);

			// TODO: Where is this possible follower?
			final Pair<IResource, Integer> location = state.lookup(possiblePreceder);
			assert location != null;
			if (location.getFirst() == null) {
				builder.withUnusedFOBPurchase(possiblePreceder);
			} else {
				builder.withUsedFOBPurchase(location.getFirst(), possiblePreceder);

				final ISequence fobPurchaseSequence = sequences.getSequence(location.getFirst());
				final ISequenceElement next = fobPurchaseSequence.get(location.getSecond() + 1);
				if (helper.isDischargeSlot(next)) {
					hints.addProblemElement(next);
				} else {
					hints.addShippingLength(location.getFirst(), location.getSecond());
				}
			}

			break;
		}

		return new Pair<com.mmxlabs.optimiser.core.moves.IMove, Hints>(builder.create(), hints);
	}
}
