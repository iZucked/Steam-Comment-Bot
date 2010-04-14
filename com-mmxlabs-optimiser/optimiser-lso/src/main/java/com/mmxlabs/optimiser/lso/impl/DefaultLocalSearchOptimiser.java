package com.mmxlabs.optimiser.lso.impl;

import java.util.List;

import com.mmxlabs.optimiser.IConstraintChecker;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.ISequencesManipulator;
import com.mmxlabs.optimiser.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

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
	public void optimise(final IOptimisationContext<T> optimiserContext) {

		final IOptimisationData<T> data = optimiserContext
				.getOptimisationData();

		final IFitnessEvaluator<T> fitnessEvaluator = getFitnessEvaluator();

		// Get list of hard constraint checkers
		final List<IConstraintChecker<T>> constraintCheckers = getConstraintCheckers();

		final ISequencesManipulator<T> manipulator = getSequenceManipulator();

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
		{
			// Apply sequence manipulators
			IModifiableSequences<T> fullSequences = new ModifiableSequences<T>(
					currentRawSequences);
			manipulator.manipulate(fullSequences);

			// Prime fitness cores with initial sequences
			fitnessEvaluator.setOptimisationData(data);
			fitnessEvaluator.setInitialSequences(fullSequences);
		}

		// Set initial sequences
		getMoveGenerator().setSequences(potentialRawSequences);

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
			IModifiableSequences<T> potentialFullSequences = new ModifiableSequences<T>(
					potentialRawSequences);
			manipulator.manipulate(potentialFullSequences);

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

			// Test move and update state if accepted
			if (fitnessEvaluator.checkSequences(potentialFullSequences, move
					.getAffectedResources())) {

				// Success update state for new sequences
				updateSequences(potentialRawSequences, currentRawSequences,
						move.getAffectedResources());

				// Update move sequences.
				getMoveGenerator().setSequences(potentialRawSequences);

				++numberOfMovesAccepted;
			} else {
				// Failed, reset state for old sequences
				updateSequences(currentRawSequences, potentialRawSequences,
						move.getAffectedResources());
			}
		}
	}
}
