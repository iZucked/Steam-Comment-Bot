package com.acme.optimiser.lso.impl;

import java.util.Collection;
import java.util.List;

import com.acme.optimiser.IConstraintChecker;
import com.acme.optimiser.IModifiableSequence;
import com.acme.optimiser.IModifiableSequences;
import com.acme.optimiser.IOptimisationContext;
import com.acme.optimiser.IOptimiser;
import com.acme.optimiser.IResource;
import com.acme.optimiser.ISequence;
import com.acme.optimiser.ISequences;
import com.acme.optimiser.ISolution;
import com.acme.optimiser.fitness.IFitnessHelper;
import com.acme.optimiser.lso.IMoveGenerator;
import com.sun.xml.internal.txw2.IllegalSignatureException;

/**
 * Main class implementing a Local Search Optimiser. While the actual
 * optimisation loop is left to sub-classes, this class provides the bulk of the
 * implementation to simplify such loops.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence Element Type
 */
public abstract class LocalSearchOptimiser<T> implements IOptimiser<T> {

	private IMoveGenerator<T> moveGenerator;

	private int numberOfIterations;

	private List<IConstraintChecker<T>> constraintCheckers;

	private IFitnessHelper<T> fitnessHelper;

	/**
	 * Initialise method checking the object has all the correct pieces of data
	 * to be able to perform the
	 * {@link #optimise(IOptimisationContext, Collection, Object)} method.
	 * Throws an {@link IllegalStateException} on error.
	 */
	public void init() {
		if (moveGenerator == null) {
			throw new IllegalStateException("Move Generator is not set");
		}

		if (numberOfIterations < 1) {
			throw new IllegalStateException(
					"Number of iterations is less than 1");
		}

		if (constraintCheckers == null) {
			throw new IllegalStateException(
					"Constraint Checkers list is not set");
		}

		if (fitnessHelper == null) {
			throw new IllegalSignatureException("Fitness Helper is not set");
		}
	}

	/**
	 * Sub-classes of {@link LocalSearchOptimiser} should implement this method
	 * to perform the actual optimisation.
	 */
	@Override
	public abstract void optimise(IOptimisationContext<T> optimiserContext,
			Collection<ISolution> initialSolutions, Object archiverCallback);

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

	public final void setMoveGenerator(final IMoveGenerator<T> moveGenerator) {
		this.moveGenerator = moveGenerator;
	}

	public final IMoveGenerator<T> getMoveGenerator() {
		return moveGenerator;
	}

	public final void setNumberOfIterations(final int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	public final int getNumberOfIterations() {
		return numberOfIterations;
	}

	public void setConstraintCheckers(
			final List<IConstraintChecker<T>> constraintCheckers) {
		this.constraintCheckers = constraintCheckers;
	}

	public List<IConstraintChecker<T>> getConstraintCheckers() {
		return constraintCheckers;
	}

	public void setFitnessHelper(final IFitnessHelper<T> fitnessHelper) {
		this.fitnessHelper = fitnessHelper;
	}

	public IFitnessHelper<T> getFitnessHelper() {
		return fitnessHelper;
	}
}
