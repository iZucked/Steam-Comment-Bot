package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IConstraintChecker;
import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.ISequenceManipulator;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.ISolution;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.IMove;

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

		final IFitnessHelper<T> fitnessHelper = getFitnessHelper();

		// Get list of fitness components for this optimisation
		final List<IFitnessComponent<T>> fitnessComponents = optimiserContext
				.getFitnessComponents();

		// Get list of hard constraint checkers
		final List<IConstraintChecker<T>> constraintCheckers = getConstraintCheckers();

		final ISequenceManipulator<T> manipulator = getSequenceManipulator();

		// Setup the optimisation process
		int numberOfMovesTried = 0;
		int numberOfMovesAccepted = 0;

		// Create a modifiable working copy of the initial sequences.
		final ISequences<T> initialSequences = optimiserContext
				.getInitialSequences();
		final ModifiableSequences<T> currentRawSequences = new ModifiableSequences<T>(
				initialSequences);

		// Create a copy of the current sequences as the starting point for the
		// next state
		final ModifiableSequences<T> potentialRawSequences = new ModifiableSequences<T>(
				currentRawSequences.getResources());
		updateSequences(currentRawSequences, potentialRawSequences,
				currentRawSequences.getResources());

		// Evaluate initial sequences
		fitnessHelper.initFitnessComponents(fitnessComponents);

		// TODO: Run sequence manipulator
		{
			// ISequences<T> fullSequences =
			// manipulator.manipulate(currentRawSequences);

			final ISequences<T> fullSequences = currentRawSequences;
			// Prime fitness cores with initial sequences
			fitnessHelper.evaluateSequencesFromComponents(fullSequences,
					fitnessComponents, null);
		}

		// Perform the optimisation
		MAIN_LOOP: for (int iter = 0; iter < getNumberOfIterations(); ++iter) {

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
			// TODO: Define a better API
			// manipulator.manipulate(potentialFullSequences);
			final ISequences<T> potentialFullSequences = potentialRawSequences;

			// Apply hard constraint checkers
			for (final IConstraintChecker<T> checker : constraintCheckers) {
				if (checker.checkConstraints(potentialFullSequences) == false) {
					// Reject Move
					updateSequences(currentRawSequences, potentialRawSequences,
							move.getAffectedResources());
					// Break out
					continue MAIN_LOOP;
				}
			}

			if (evaluateSequences(potentialFullSequences, fitnessComponents,
					move.getAffectedResources())) {
				// Success update state for new sequences

				updateSequences(potentialRawSequences, currentRawSequences,
						move.getAffectedResources());

				// TODO: Update current fitness state
				assert false;

				++numberOfMovesAccepted;
			} else {
				// Failed, reset state for old sequences
				updateSequences(currentRawSequences, potentialRawSequences,
						move.getAffectedResources());
			}

		}

		// Finalise optimisation process
		// ... TODO ...
		assert false;

	}
}
