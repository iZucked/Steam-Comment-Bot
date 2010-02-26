package com.acme.optimiser.lso.impl;

import java.util.Collection;

import com.acme.optimiser.IOptimisationContext;
import com.acme.optimiser.ISequences;
import com.acme.optimiser.ISolution;
import com.acme.optimiser.impl.ModifiableSequences;
import com.acme.optimiser.lso.IMove;

/**
 * A sub-class of {@link LocalSearchOptimiser} implementing a default main loop.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public class DefaultLocalSearchOptimiser<T> extends LocalSearchOptimiser<T> {
	@Override
	public void optimise(final IOptimisationContext<T> optimiserContext,
			final Collection<ISolution> initialSolutions,
			final Object archiverCallback) {

		// Setup the optimisation process

		int numberOfMovesTried = 0;
		int numberOfMovesAccepted = 0;

		// Create a modifiable working copy of the initial sequences.
		final ISequences<T> initialSequences = optimiserContext.getInitialSequences();
		final ModifiableSequences<T> currentRawSequences = new ModifiableSequences<T>(initialSequences);

		// Create a copy of the current sequences as the starting point for the next state
		final ModifiableSequences<T> potentialRawSequences = new ModifiableSequences<T>(
				currentRawSequences.getResources());
		updateSequences(currentRawSequences, potentialRawSequences,
				currentRawSequences.getResources());

		// Perform the optimisation
		for (int iter = 0; iter < getNumberOfIterations(); ++iter) {

			++numberOfMovesTried;

			// Generate a new move
			final IMove<T> move = getMoveGenerator().generateMove();

			// Make sure the generator was able to generate a move
			if (move == null) {
				continue;
			}

			// Test move is valid against data.
			if (!move.validate(potentialRawSequences)) {
				continue;
			}

			// Update potential sequences
			move.apply(potentialRawSequences);

			// Apply sequence manipulators
			// ... TODO ...
			// ISequences<T> fullSequenes =
			// manipulator.manipulate(potentialRawSquences);

			// Apply hard constraint checkers
			// ... TODO ...

			if (evaluateSequences(potentialRawSequences)) {
				// Success update state for new sequences

				updateSequences(potentialRawSequences, currentRawSequences,
						move.getAffectedResources());

				// TODO: Update current fitness state

				++numberOfMovesAccepted;
			} else {
				// Failed, reset state for old sequences

				updateSequences(currentRawSequences, potentialRawSequences,
						move.getAffectedResources());
			}

		}

		// Finalise optimisation process

	}
}
