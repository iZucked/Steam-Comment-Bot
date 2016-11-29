package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.lso.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.IGuidedMoveHelper;
import com.mmxlabs.scheduler.optimiser.lso.guided.LookupManager;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.SwapElementsMove;

public class SwapSlotMoveHandler implements IMoveHandler {

	@Inject
	private @NonNull IGuidedMoveHelper helper;

	@Inject
	private @NonNull IFollowersAndPreceders followersAndPreceders;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull LookupManager state, final ISequenceElement slot) {
		final ISequences sequences = state.getSequences();

		final SwapElementsMove.Builder builder = SwapElementsMove.Builder.newMove();
		final Hints hints = new Hints();

		final Pair<IResource, Integer> slotLocation = state.lookup(slot);
		final IResource fromResource = slotLocation.getFirst();

		final Set<ISequenceElement> candidates = new LinkedHashSet<>();

		if (fromResource == null) {

			builder.withElementA(null, slot);

			// TODO: Find a way to populate the candidate set
			if (helper.isLoadSlot(slot)) {
				for (ISequenceElement e : followersAndPreceders.getValidFollowers(slot)) {
					Iterables.addAll(candidates, followersAndPreceders.getValidPreceders(e));
				}
				candidates.remove(slot);
			} else if (helper.isDischargeSlot(slot)) {
				for (ISequenceElement e : followersAndPreceders.getValidPreceders(slot)) {
					Iterables.addAll(candidates, followersAndPreceders.getValidFollowers(e));
				}
				candidates.remove(slot);
			}

//			return null;

		} else {
			final int index = slotLocation.getSecond().intValue();
			assert index != -1;

			final ISequence seq = sequences.getSequence(fromResource);
			final ISequenceElement slot_minus_1 = seq.get(slotLocation.getSecond() - 1);
			final ISequenceElement slot_plus_1 = seq.get(slotLocation.getSecond() + 1);

			Iterables.addAll(candidates, followersAndPreceders.getValidFollowers(slot_minus_1));
			candidates.retainAll(Lists.newLinkedList(followersAndPreceders.getValidPreceders(slot_plus_1)));
			candidates.remove(slot);
			if (candidates.isEmpty()) {
				// Nothing else can fit between these two elements....
				return null;
			}
			builder.withElementA(fromResource, slot);

			// Suggest we may want to consider the surrounding elements for further changes.
			hints.addSuggestedElements(slot_minus_1);
			hints.addSuggestedElements(slot_plus_1);
		}

		final List<Pair<IResource, ISequenceElement>> elementHints = new LinkedList<>();
		boolean foundElementB = false;
		final List<ISequenceElement> candidateList = new ArrayList<>(candidates);
		Collections.shuffle(candidateList, helper.getSharedRandom());
		for (final ISequenceElement candidate : candidateList) {
			foundElementB = false;

			final Pair<IResource, Integer> candidateLocation = state.lookup(candidate);
			final IResource candidateResource = candidateLocation.getFirst();

			if (candidateResource == null) {
				if (!helper.isOptional(slot)) {
					if (helper.isStrictOptional()) {
						continue;
					} else {
						hints.addProblemElement(slot);
					}
				}
				builder.withElementB(null, candidate);
				foundElementB = true;

			} else {
				if (!helper.checkPermittedResource(slot, candidateResource)) {
					continue;
				}

				final ISequence candidateSequence = sequences.getSequence(candidateLocation.getFirst());
				final ISequenceElement candidate_minus_1 = candidateSequence.get(candidateLocation.getSecond() - 1);
				final ISequenceElement candidate_plus_1 = candidateSequence.get(candidateLocation.getSecond() + 1);

				if (!followersAndPreceders.getValidFollowers(candidate_minus_1).contains(slot)) {
					continue;
				}
				if (!followersAndPreceders.getValidPreceders(candidate_plus_1).contains(slot)) {
					continue;
				}
				builder.withElementB(candidateResource, candidate);
				foundElementB = true;
				elementHints.add(new Pair<>(candidateResource, candidate_minus_1));
				elementHints.add(new Pair<>(candidateResource, candidate_plus_1));
			}

			if (fromResource == null) {
				if (candidateResource == null) {
					foundElementB = false;
					elementHints.clear();
					continue;
				}
				
				if (!helper.isOptional(candidate)) {
					if (helper.isStrictOptional()) {
						foundElementB = false;
						elementHints.clear();
						continue;
					} else {
						hints.addProblemElement(candidate);
					}
				}
			}
			break;
		}

		if (!foundElementB) {
			return null;
		}

		// Record hints
		elementHints.forEach(p -> hints.addSuggestedElements(p.getSecond()));

		return new Pair<IMove, Hints>(builder.create(), hints);
	}
}
