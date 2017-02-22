package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Iterables;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;

public class RemoveLinkedSlotMoveHandler implements IGuidedMoveHandler {

	@Inject
	private @NonNull IMoveHelper helper;

	@Inject
	private @NonNull IMoveHandlerHelper moveHandlerHelper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull ILookupManager lookupManager, final @NonNull ISequenceElement element, @NonNull Random random, @NonNull GuideMoveGeneratorOptions options,
			@NonNull Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = lookupManager.getRawSequences();

		final Hints hints = new Hints();

		final Pair<IResource, Integer> slotLocation = lookupManager.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource != null) {
			// Already out of the solution.
			return null;

		}
		final RemoveElementsMove.Builder builder = RemoveElementsMove.Builder.newMove();

		final Set<ISequenceElement> candidates = new LinkedHashSet<>();

		if (helper.isLoadSlot(element)) {
			for (ISequenceElement e : followersAndPreceders.getValidFollowers(element)) {
				Iterables.addAll(candidates, followersAndPreceders.getValidPreceders(e));
			}
			candidates.remove(element);
		} else if (helper.isDischargeSlot(element)) {
			for (ISequenceElement e : followersAndPreceders.getValidPreceders(element)) {
				Iterables.addAll(candidates, followersAndPreceders.getValidFollowers(e));
			}
			candidates.remove(element);
		}

		List<ISequenceElement> candidateList = new ArrayList<>(candidates);
		Collections.shuffle(candidateList, random);

		Iterator<ISequenceElement> itr = candidateList.iterator();
		while (itr.hasNext()) {
			ISequenceElement candidate = itr.next();

			final Pair<IResource, Integer> candidateLocation = lookupManager.lookup(candidate);
			final IResource candidateResource = candidateLocation.getFirst();

			if (candidateResource == null) {
				continue;
			}

			// Check valid chain
			final ISequence candidateSequence = sequences.getSequence(candidateLocation.getFirst());

			hints.addProblemElement(element);
			if (!helper.isOptional(candidate)) {
//				continue;
//				hints.addProblemElement(candidate);
				
			}
			moveHandlerHelper.extractSegment(candidateSequence, candidate).forEach(e -> {
				builder.removeElement(candidateResource, e);	
				if (!helper.isOptional(e)) {
//					continue;
					hints.addProblemElement(e);
					
				}
			});

			return new Pair<IMove, Hints>(builder.create(), hints);
		}

		return null;
	}
}
