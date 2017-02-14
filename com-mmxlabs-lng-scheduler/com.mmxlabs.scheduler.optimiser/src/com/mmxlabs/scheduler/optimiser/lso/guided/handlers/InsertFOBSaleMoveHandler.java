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
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertFOBSaleMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.providers.Followers;

public class InsertFOBSaleMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull MoveHandlerHelper moveHandlerHelper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final ISequenceElement fobSale, @NonNull Random random, @NonNull GuideMoveGeneratorOptions options,
			@NonNull Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = lookupManager.getRawSequences();
		final IResource fobSaleResource = helper.getFOBSaleResource(fobSale);

		final InsertFOBSaleMove.Builder builder = InsertFOBSaleMove.Builder.newMove() //
				.withUnusedFOBSale(fobSaleResource, fobSale);

		final Followers<ISequenceElement> validPreceders = followersAndPreceders.getValidPreceders(fobSale);
		final List<ISequenceElement> preceders = Lists.newArrayList(validPreceders);
		preceders.removeAll(forbiddenElements);

		if (preceders.isEmpty()) {
			return null;
		}

		Collections.shuffle(preceders, random);

		final Hints hints = new Hints();
		hints.getUsedElements().add(fobSale);

		LOOP_CANDIDATES: for (final ISequenceElement possibleFOBPurchase : preceders) {
			// This should be implicit by virtue of being able to follow the DES Purchase
			assert helper.checkResource(possibleFOBPurchase, fobSaleResource);

			// Locate FOB purchase
			final Pair<IResource, Integer> location = lookupManager.lookup(possibleFOBPurchase);
			assert location != null;
			IResource fobPurchaseResource = location.getFirst();
			if (fobPurchaseResource == null) {
				builder.withUnusedFOBPurchase(possibleFOBPurchase);
				hints.usedElement(possibleFOBPurchase);
				return new Pair<IMove, Hints>(builder.create(), hints);
			} else {
				final ISequence fobPurchaseSequence = sequences.getSequence(fobPurchaseResource);
				@NonNull
				final List<ISequenceElement> cargoSegment = moveHandlerHelper.extractSegment(fobPurchaseSequence, possibleFOBPurchase);
				// Check optionality status
				for (final ISequenceElement e : cargoSegment) {
					if (e == possibleFOBPurchase) {
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
					if (e == possibleFOBPurchase) {
						continue;
					}
					if (!helper.isOptional(e)) {
						hints.addProblemElement(e);
					}
					elementsToRemove.add(new Pair<>(fobPurchaseResource, e));
				}

				hints.getUsedElements().add(possibleFOBPurchase);
				builder.withUsedFOBPurchase(fobPurchaseResource, possibleFOBPurchase);

				final InsertFOBSaleMove insertionMove = builder.create();

				if (options.isPermitPartialSegments()) {
					return new Pair<IMove, Hints>(insertionMove, hints);
				} else {
					// Finally generate the FOB Sale pairing move and a second step to remove the other cargo elements.
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
