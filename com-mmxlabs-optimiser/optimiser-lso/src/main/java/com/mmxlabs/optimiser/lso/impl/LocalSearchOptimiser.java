package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;

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
public abstract class LocalSearchOptimiser<T> implements
		ILocalSearchOptimiser<T> {

	private IMoveGenerator<T> moveGenerator;

	private int numberOfIterations;

	private List<IConstraintChecker<T>> constraintCheckers;

	private IFitnessEvaluator<T> fitnessEvaluator;

	private ISequencesManipulator<T> sequenceManipulator;

	private IOptimiserProgressMonitor<T> progressMonitor;

	private int reportInterval = -1;

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

		if (fitnessEvaluator == null) {
			throw new IllegalStateException("Fitness Evaluator is not set");
		}

		if (sequenceManipulator == null) {
			throw new IllegalStateException("Sequence Manipulator is not set");
		}

		if (progressMonitor == null) {
			throw new IllegalStateException("Progress Monitor is not set");
		}

		if (reportInterval < 1) {
			throw new IllegalStateException("Report interval is not set");
		}
	}

	/**
	 * Sub-classes of {@link LocalSearchOptimiser} should implement this method
	 * to perform the actual optimisation.
	 * 
	 * 
	 * // customisable module, default as below:
	 * 
	 * <pre>
	 * ISolutionBuilder solbuilder;
	 * 		// Convert Sequence to a Solution through the following steps (not necessarily in this order):
	 * 			// create Solution object
	 * 			// insert extra points
	 * 			// check hard constraints for early exit
	 * 			// loop over fitness functions to calculate fitness values/deltas, check hard constraints
	 * 			// populate Solution object as appropriate
	 * 		// evaluate fitnesses
	 * 		}
	 * </pre>
	 */
	@Override
	public abstract void optimise(IOptimisationContext<T> optimisationContext);

	/**
	 * Copy the {@link ISequences} for the specified {@link IResource}s from the
	 * source sequences to the destination sequences.
	 * 
	 * @param source
	 * @param destination
	 * @param affectedResources
	 */
	protected void updateSequences(final ISequences<T> source,
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

	@Override
	public final IMoveGenerator<T> getMoveGenerator() {
		return moveGenerator;
	}

	public final void setNumberOfIterations(final int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	@Override
	public final int getNumberOfIterations() {
		return numberOfIterations;
	}

	public void setConstraintCheckers(
			final List<IConstraintChecker<T>> constraintCheckers) {
		this.constraintCheckers = constraintCheckers;
	}

	@Override
	public List<IConstraintChecker<T>> getConstraintCheckers() {
		return constraintCheckers;
	}

	public void setFitnessEvaluator(final IFitnessEvaluator<T> fitnessEvaluator) {
		this.fitnessEvaluator = fitnessEvaluator;
	}

	@Override
	public IFitnessEvaluator<T> getFitnessEvaluator() {
		return fitnessEvaluator;
	}

	public void setSequenceManipulator(
			final ISequencesManipulator<T> sequenceManipulator) {
		this.sequenceManipulator = sequenceManipulator;
	}

	@Override
	public ISequencesManipulator<T> getSequenceManipulator() {
		return sequenceManipulator;
	}

	public IOptimiserProgressMonitor<T> getProgressMonitor() {
		return progressMonitor;
	}

	public void setProgressMonitor(IOptimiserProgressMonitor<T> progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	public int getReportInterval() {
		return reportInterval;
	}

	public void setReportInterval(int reportInterval) {
		this.reportInterval = reportInterval;
	}

	@Override
	public void dispose() {
		this.constraintCheckers = null;
		this.fitnessEvaluator = null;
		this.moveGenerator = null;
		this.progressMonitor = null;
		this.sequenceManipulator = null;
	}
}
