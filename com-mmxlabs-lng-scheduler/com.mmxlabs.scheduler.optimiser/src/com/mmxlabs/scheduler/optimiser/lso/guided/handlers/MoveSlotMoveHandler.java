package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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

/**
 * This move handler is designed to move a slot between resources. Specifically this is intended to move slots between non-shipped resources, but could be used to handle complex cargoes in future.
 * 
 * @author Simon Goodall
 *
 */
public class MoveSlotMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull MoveHandlerHelper moveHandlerHelper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final @NonNull ISequenceElement element, @NonNull final Random random,
			@NonNull final GuideMoveGeneratorOptions options, @NonNull final Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = lookupManager.getRawSequences();

		@Nullable
		Pair<@Nullable IResource, @NonNull Integer> elementPosition = lookupManager.lookup(element);
		@Nullable
		IResource elementResource = elementPosition.getFirst();
		if (elementResource == null) {
			return null;
		}

		@NonNull
		final List<ISequenceElement> cargoSegment = moveHandlerHelper.extractSegment(lookupManager.getRawSequences().getSequence(elementResource), element);

		// If we are paired to a non-optional element, we cannot continue.
		if (options.isStrictOptional()) {
			// Check optionality status
			for (final ISequenceElement e : cargoSegment) {
				if (e == element) {
					continue;
				}
				if (!helper.isOptional(e)) {
					return null;
				}
			}
		}

		final List<ISequenceElement> candidates = generateCandidates(lookupManager, element, options, forbiddenElements);

		if (candidates.isEmpty()) {
			return null;
		}
		Collections.shuffle(candidates, random);

		final Hints hints = new Hints();

		LOOP_CANDIDATES: for (final ISequenceElement candidate : candidates) {

			final IMove insertionMove;
			final IResource candidateResource;
			if (helper.isDESSale(element)) {
				candidateResource = helper.getDESPurchaseResource(candidate);

				final InsertDESPurchaseMove.Builder builder = InsertDESPurchaseMove.Builder.newMove() //
						.withUsedDESPurchase(candidateResource, candidate) //
						.withUsedDESSale(elementResource, element) //
				;
				insertionMove = builder.create();
			} else if (helper.isFOBPurchase(element)) {
				candidateResource = helper.getFOBSaleResource(candidate);
				final InsertFOBSaleMove.Builder builder = InsertFOBSaleMove.Builder.newMove() //
						.withUsedFOBSale(candidateResource, candidate) //
						.withUsedFOBPurchase(elementResource, element) //
				;
				insertionMove = builder.create();
			} else {
				throw new IllegalArgumentException();
			}

			final ISequence candidateSequence = sequences.getSequence(candidateResource);
			IMove removeElementsMove = createRemoveElementsMove(hints, candidate, candidateResource, candidateSequence);

			hints.getUsedElements().add(candidate);
			hints.getUsedElements().add(element);

			// Finally generate the DES Purchase pairing move and a second step to remove the other cargo elements.
			final List<IMove> moveComponents = new LinkedList<>();
			moveComponents.add(insertionMove);
			// Note - this should work equally well for both shipped and unshipped cargoes.
			moveComponents.add(removeElementsMove);

			final CompoundMove finalMove = new CompoundMove(moveComponents);
			return new Pair<IMove, Hints>(finalMove, hints);
		}

		return null;

	}

	private List<ISequenceElement> generateCandidates(final ILookupManager lookupManager, final ISequenceElement element, final GuideMoveGeneratorOptions options,
			final Collection<ISequenceElement> forbiddenElements) {

		boolean isFOBPurchase = helper.isFOBPurchase(element);
		boolean isDESSale = helper.isDESSale(element);

		assert isFOBPurchase || isDESSale;

		final Followers<ISequenceElement> validCandidates = isFOBPurchase ? followersAndPreceders.getValidFollowers(element) : followersAndPreceders.getValidPreceders(element);
		final List<ISequenceElement> candidates = Lists.newArrayList(validCandidates);
		candidates.removeAll(forbiddenElements);
		if (candidates.isEmpty()) {
			return null;
		}
		// Filter candidates
		Iterator<ISequenceElement> itr = candidates.iterator();
		while (itr.hasNext()) {
			ISequenceElement candidate = itr.next();
			if (isFOBPurchase && !helper.isFOBSale(candidate)) {
				itr.remove();
				continue;
			}
			if (isDESSale && !helper.isDESPurchase(candidate)) {
				itr.remove();
				continue;
			}

			Pair<@Nullable IResource, @NonNull Integer> candidatePosition = lookupManager.lookup(candidate);
			if (candidatePosition.getFirst() == null) {
				itr.remove();
				continue;
			}
			// Check for optional restrictions
			if (options.isStrictOptional() && !helper.isOptional(candidate)) {
				itr.remove();
				continue;
			}
		}
		return candidates;
	}

	private IMove createRemoveElementsMove(final Hints hints, final ISequenceElement candidate, final IResource candidateResource, final ISequence candidateSequence) {
		@NonNull
		final List<ISequenceElement> candidateSegment = moveHandlerHelper.extractSegment(candidateSequence, candidate);

		// Record problem elements and elements to remove
		final List<Pair<IResource, ISequenceElement>> elementsToRemove = new LinkedList<>();
		for (final ISequenceElement e : candidateSegment) {
			if (e == candidate) {
				continue;
			}
			if (!helper.isOptional(e)) {
				hints.addProblemElement(e);
			}
			elementsToRemove.add(new Pair<>(candidateResource, e));
		}
		return new RemoveElementsMove(elementsToRemove);
	}
}
