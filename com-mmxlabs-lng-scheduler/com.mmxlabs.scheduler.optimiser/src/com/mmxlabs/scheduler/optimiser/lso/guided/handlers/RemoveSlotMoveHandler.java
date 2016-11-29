package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.IGuidedMoveHelper;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.lso.guided.LookupManager;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.RemoveElementsMove;

public class RemoveSlotMoveHandler implements IMoveHandler {

	@Inject
	private @NonNull IGuidedMoveHelper helper;

	@Override
	public Pair<IMove, Hints> handleMove(final @NonNull LookupManager state, final @NonNull ISequenceElement element, @NonNull Collection<ISequenceElement> forbiddenElements) {
		final ISequences sequences = state.getSequences();

		final Hints hints = new Hints();

		final Pair<IResource, Integer> slotLocation = state.lookup(element);
		final IResource fromResource = slotLocation.getFirst();

		if (fromResource == null) {
			// Already out of the solution.
			return null;

		}
		final RemoveElementsMove.Builder builder = RemoveElementsMove.Builder.newMove();
		if (!helper.isOptional(element)) {
			if (helper.isStrictOptional()) {
				return null;
			} else {
				hints.addProblemElement(element);
			}
		}
		builder.removeElement(fromResource, element);

		return new Pair<IMove, Hints>(builder.create(), hints);
	}
}
