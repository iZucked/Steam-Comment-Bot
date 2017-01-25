package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.IGuidedMoveHelper;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;
import com.mmxlabs.scheduler.optimiser.moves.util.LookupManager;

public class RemoveCargoMoveHandler implements IMoveHandler {

	@Inject
	private @NonNull IGuidedMoveHelper helper;

	@Inject
	private @NonNull MoveHandlerHelper moveHelper;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull LookupManager state, final ISequenceElement element, @NonNull Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = state.getSequences();

		final Hints hints = new Hints();

		final Pair<IResource, Integer> slotLocation = state.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource == null) {
			// Already out of the solution.
			return null;

		}
		final ISequence fromSequence = sequences.getSequence(fromResource);

		final List<ISequenceElement> orderedCargoElements = moveHelper.extractSegment(fromSequence, element);

		if (orderedCargoElements.isEmpty()) {
			throw new IllegalStateException();
		}

		final RemoveElementsMove.Builder builder = RemoveElementsMove.Builder.newMove();
		for (final ISequenceElement e : orderedCargoElements) {
			if (!helper.isOptional(e)) {
				if (helper.isStrictOptional()) {
					return null;
				} else {
					hints.addProblemElement(e);
				}
			}
			builder.removeElement(fromResource, e);
		}

		return new Pair<IMove, Hints>(builder.create(), hints);
	}
}
