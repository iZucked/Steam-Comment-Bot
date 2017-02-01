package com.mmxlabs.scheduler.optimiser.lso.guided.handlers;

import java.util.Collection;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.Hints;
import com.mmxlabs.scheduler.optimiser.moves.util.LookupManager;

/**
 * A {@link IGuidedMoveHandler} is a part of the {@link GuidedMoveGenerator} used to construct an atomic change in the state of a portfolio.
 * 
 * @author Simon Goodall
 *
 */
public interface IGuidedMoveHandler {
	/**
	 * Attempt to create a move involving the given {@link ISequenceElement}. If successful, this returns {@link Pair} containing the {@link IMove} implementation and a {@link Hints} instance
	 * describing the effected elements that may need further attention.
	 * 
	 * @param state
	 * @param element
	 * @return
	 */
	@Nullable
	Pair<IMove, Hints> handleMove(@NonNull LookupManager state, @NonNull ISequenceElement element, @NonNull Random random, @NonNull GuideMoveGeneratorOptions options,
			@NonNull Collection<ISequenceElement> forbiddenElements);
}
