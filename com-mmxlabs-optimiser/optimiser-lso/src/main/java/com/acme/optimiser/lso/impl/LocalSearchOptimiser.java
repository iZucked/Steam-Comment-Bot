package com.acme.optimiser.lso.impl;

import java.util.Collection;

import com.acme.optimiser.IModifiableSequence;
import com.acme.optimiser.IModifiableSequences;
import com.acme.optimiser.IOptimisationContext;
import com.acme.optimiser.IOptimiser;
import com.acme.optimiser.IResource;
import com.acme.optimiser.ISequence;
import com.acme.optimiser.ISequences;
import com.acme.optimiser.ISolution;
import com.acme.optimiser.impl.ModifiableSequences;
import com.acme.optimiser.lso.IMove;
import com.acme.optimiser.lso.IMoveGenerator;

/**
 * Main class implementing a Local Search Optimiser.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence Element Type
 */
public class LocalSearchOptimiser<T> implements IOptimiser<T> {

	private IMoveGenerator<T> moveGenerator;

	private int numberOfIterations;

	@Override
	public void optimise(final IOptimisationContext optimiserContext,
			final Collection<ISolution> initialSolutions,
			final Object archiverCallback) {

		// TODO: Some or all of this method should be abstract to allow custom
		// loops to be provided. Appropriate accessors should be generated for
		// internal data.

		// Setup the optimisation process

		int numberOfMovesTried = 0;
		int numberOfMovesAccepted = 0;

		final ModifiableSequences<T> currentRawSequences;

		// Create a copy of the current sequences
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

	/**
	 * Evaluate the current sequences and return whether or not they are
	 * acceptable for the next solution state.
	 * 
	 * @param sequences
	 * @return
	 */
	public boolean evaluateSequences(final ISequences<T> sequences) {
		// TODO: Delegate to a FitnessUtility/Helper class?

		return true;
	}

	/**
	 * Copy the {@link ISequences} for the specified {@link IResource}s from the
	 * source sequences to the destination sequences.
	 * 
	 * @param source
	 * @param destination
	 * @param affectedResources
	 */
	public final void updateSequences(final ISequences<T> source,
			final IModifiableSequences<T> destination,
			final Collection<IResource> affectedResources) {

		for (final IResource resource : affectedResources) {
			// Get source sequence
			final ISequence<T> sourceSequence = source.getSequence(resource);
			assert sourceSequence != null;

			// Get destination sequence
			final IModifiableSequence<T> destinationSequence = destination
					.getModifiableSequence(resource);
			assert destinationSequence != null;

			// Replace all entries in the destination with those in the source
			destinationSequence.replaceAll(sourceSequence);
		}
	}

	public final void setMoveGenerator(IMoveGenerator<T> moveGenerator) {
		this.moveGenerator = moveGenerator;
	}

	public final IMoveGenerator<T> getMoveGenerator() {
		return moveGenerator;
	}

	public final void setNumberOfIterations(int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	public final int getNumberOfIterations() {
		return numberOfIterations;
	}
}
