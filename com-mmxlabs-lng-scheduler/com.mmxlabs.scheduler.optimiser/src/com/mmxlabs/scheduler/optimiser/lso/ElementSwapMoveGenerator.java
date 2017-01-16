/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.impl.Move4over2;
import com.mmxlabs.optimiser.lso.impl.NullMove;

/**
 * Move generator to swap a single element in one sequence with a single element in another sequence.
 * 
 * @author Simon Goodall
 *
 */
public class ElementSwapMoveGenerator implements IConstrainedMoveGeneratorUnit {

	public static final class NullElementSwapMove extends NullMove {

	}

	@Inject
	@NonNull
	private IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider;

	@Inject
	private IFollowersAndPreceders followersAndPreceders;

	private final @NonNull ConstrainedMoveGenerator owner;

	public ElementSwapMoveGenerator(@NonNull final ConstrainedMoveGenerator owner) {
		this.owner = owner;
	}

	@Override
	public void setSequences(final ISequences sequences) {
		// Do nothing - data stored in owner
	}

	@Override
	public IMove generateMove() {
		// Retry a few times...
		for (int i = 0; i < 10; ++i) {

			// Find a target pair (pos1 -> pos2)
			final Pair<ISequenceElement, ISequenceElement> newPair = chooseRandomElementPair();

			final Pair<IResource, Integer> pos1Pair = getReverseLookup().get(newPair.getFirst());
			final Pair<IResource, Integer> pos2Pair = getReverseLookup().get(newPair.getSecond());

			// Are either element unused? If so, skip
			final IResource resource1 = pos1Pair.getFirst();
			final IResource resource2 = pos2Pair.getFirst();
			if ((resource1 == null) || (resource2 == null)) {
				continue;
			}

			final int position1Index = pos1Pair.getSecond();
			final int position2Index = pos2Pair.getSecond();

			// Element index 0 should always be a Start location, and nothing can come before a Start
			assert position2Index > 0;

			// We only expect -1 if the resource is null. This is really an assert!
			if (position1Index == -1 || position2Index == -1) {
				continue;
			}
			// If elements are on the same route, skip
			if (resource1 == resource2) {
				continue;
			}

			final ISequence seq1 = getCurrentSequences().getSequence(resource1);
			final ISequence seq2 = getCurrentSequences().getSequence(resource2);

			// Is pos2 at the end of the sequence? Cannot move it
			if (position2Index == seq2.size() - 1) {
				continue;
			}
			if (position1Index + 1 == seq1.size() - 1) {
				continue;
			}

			// final ISequenceElement pos1 = seq1.get(position1Index);
			final ISequenceElement pos2 = seq2.get(position2Index);

			// Can element be moved to new resources?
			if (!checkResource(pos2, resource1)) {
				continue;
			}

			// Check new sequencing conditions
			// ISequenceElement pos1_minus1 = seq1.get(position1 - 1);
			final ISequenceElement pos1_plus1 = seq1.get(position1Index + 1);
			// Can element be moved to new resources?
			if (!checkResource(pos1_plus1, resource2)) {
				continue;
			}

			final ISequenceElement pos1_plus2 = seq1.get(position1Index + 2);

			final ISequenceElement pos2_minus1 = seq2.get(position2Index - 1);
			final ISequenceElement pos2_plus1 = seq2.get(position2Index + 1);

			// Can we have p1, p2, p1+2 ? (p1 -> p2 should be fine as this is the input pairing)
			if (!followersAndPreceders.getValidFollowers(pos2).contains(pos1_plus2)) {
				continue;
			}
			// Can we have p2-1,p1+1,p2+1?
			if (!followersAndPreceders.getValidFollowers(pos2_minus1).contains(pos1_plus1)) {
				continue;
			}
			if (!followersAndPreceders.getValidFollowers(pos1_plus1).contains(pos2_plus1)) {
				continue;
			}

			return new Move4over2(resource1, position1Index + 1, position1Index + 1 + 1, resource2, position2Index, position2Index + 1);
		}
		return new NullElementSwapMove();
	}

	protected ISequences getCurrentSequences() {
		return owner.sequences;
	}

	protected Pair<ISequenceElement, ISequenceElement> chooseRandomElementPair() {
		return RandomHelper.chooseElementFrom(owner.random, owner.validBreaks);
	}

	protected Map<ISequenceElement, Pair<IResource, Integer>> getReverseLookup() {
		return owner.reverseLookup;
	}

	private boolean checkResource(@NonNull final ISequenceElement element, @NonNull final IResource resource) {

		final Collection<IResource> allowedResources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(element);
		if (allowedResources == null) {
			return true;
		}
		return allowedResources.contains(resource);
	}

}
