package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
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
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.CompoundMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertDESPurchaseMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
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
	private @NonNull IMoveHandlerHelper moveHandlerHelper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final @NonNull ISequenceElement desPurchase, @NonNull final Random random,
			@NonNull final GuideMoveGeneratorOptions options, @NonNull final Collection<ISequenceElement> forbiddenElements) {
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
		hints.getUsedElements().add(desPurchase);

		LOOP_CANDIDATES: for (final ISequenceElement possibleDESSale : followers) {
			// This should be implicit by virtue of being able to follow the DES Purchase
			assert helper.checkResource(possibleDESSale, desPurchaseResource);

			// Locate DES Sale
			final Pair<IResource, Integer> location = lookupManager.lookup(possibleDESSale);
			assert location != null;
			final IResource desSaleResouce = location.getFirst();
			if (desSaleResouce == null) {
				builder.withUnusedDESSale(possibleDESSale);
				hints.getUsedElements().add(possibleDESSale);
				return new Pair<IMove, Hints>(builder.create(), hints);

			} else {
				if (!options.isInsertCanRemove()) {
					continue;
				}

				final ISequence desSaleSequence = sequences.getSequence(desSaleResouce);

				@NonNull
				final List<ISequenceElement> cargoSegment = moveHandlerHelper.extractSegment(desSaleSequence, possibleDESSale);
				// Check optionality status
				for (final ISequenceElement e : cargoSegment) {
					if (e == possibleDESSale) {
						continue;
					}
					if (!helper.isOptional(e) && options.isStrictOptional()) {
						continue LOOP_CANDIDATES;
					}
				}
				// TODO
				// hints.addShippingLength(location.getFirst(), location.getSecond() - 1);

				// Record problem elements and elements to remove
				final List<Pair<IResource, ISequenceElement>> elementsToRemove = new LinkedList<>();
				for (final ISequenceElement e : cargoSegment) {
					if (e == possibleDESSale) {
						continue;
					}
					if (!helper.isOptional(e)) {
						hints.addProblemElement(e);
					}
					elementsToRemove.add(new Pair<>(desSaleResouce, e));
				}

				hints.getUsedElements().add(possibleDESSale);
				builder.withUsedDESSale(desSaleResouce, possibleDESSale);

				final InsertDESPurchaseMove insertionMove = builder.create();

				if (options.isPermitPartialSegments()) {
					return new Pair<IMove, Hints>(insertionMove, hints);
				} else {
					// Finally generate the DES Purchase pairing move and a second step to remove the other cargo elements.
					final List<IMove> moveComponents = new LinkedList<>();
					moveComponents.add(insertionMove);
					// Note - this should work equally well for both shipped and unshipped cargoes.
					moveComponents.add(new RemoveElementsMove(elementsToRemove));

					final CompoundMove finalMove = new CompoundMove(moveComponents);
					return new Pair<IMove, Hints>(finalMove, hints);
				}
			}
		}
		return null;
	}
}
