/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.ISequencesOptimiser;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;

public abstract class AbstractSequencesOptimiser implements ISequencesOptimiser {

	private int numberOfIterations;

	private int numberOfIterationsCompleted;

	private List<IConstraintChecker> constraintCheckers;

	private IFitnessEvaluator fitnessEvaluator;

	private ISequencesManipulator sequenceManipulator;

	private IOptimiserProgressMonitor progressMonitor;

	private int reportInterval = -1;

	private IOptimisationContext currentContext;

	private long startTime;

	/**
	 * Initialise method checking the object has all the correct pieces of data
	 * to be able to perform the
	 * {@link #optimise(IOptimisationContext, Collection, Object)} method.
	 * Throws an {@link IllegalStateException} on error.
	 */
	public void init() {

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
	 * A default optimisation loop, which calls start() and then step() until
	 * done, and notifies the progress monitor. Subclasses will need to
	 * implement these.
	 */
	@Override
	public final void optimise(final IOptimisationContext optimiserContext) {
		final IAnnotatedSolution startSolution = start(optimiserContext);
		getProgressMonitor().begin(this, fitnessEvaluator.getBestFitness(),
				startSolution);
		final int percentage = (100 * getReportInterval())
				/ getNumberOfIterations();

		numberOfIterationsCompleted = 0;

		while (getNumberOfIterationsCompleted() < getNumberOfIterations()) {
			step(percentage);
			getProgressMonitor().report(this, getNumberOfIterationsCompleted(),
					fitnessEvaluator.getCurrentFitness(),
					fitnessEvaluator.getBestFitness(),
					getCurrentSolution(false), getBestSolution(false));
		}

		getProgressMonitor().done(this, fitnessEvaluator.getBestFitness(),
				getBestSolution(true));
	}

	@Override
	public final IAnnotatedSolution getBestSolution(final boolean forExport) {
		final IAnnotatedSolution annotatedSolution = fitnessEvaluator
				.getBestAnnotatedSolution(currentContext, forExport);
		final long clock = System.currentTimeMillis() - getStartTime();

		annotatedSolution.setGeneralAnnotation(
				OptimiserConstants.G_AI_iterations,
				getNumberOfIterationsCompleted());
		annotatedSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime,
				clock);

		return annotatedSolution;
	}

	@Override
	public final IAnnotatedSolution getCurrentSolution(final boolean forExport) {
		final IAnnotatedSolution annotatedSolution = fitnessEvaluator
				.getCurrentAnnotatedSolution(currentContext, forExport);
		final long clock = System.currentTimeMillis() - getStartTime();

		annotatedSolution.setGeneralAnnotation(
				OptimiserConstants.G_AI_iterations,
				getNumberOfIterationsCompleted());
		annotatedSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime,
				clock);

		return annotatedSolution;
	}

	@Override
	public final boolean isFinished() {
		return getNumberOfIterationsCompleted() >= getNumberOfIterations();
	}

	/**
	 * Copy the {@link ISequences} for the specified {@link IResource}s from the
	 * source sequences to the destination sequences.
	 * 
	 * @param source
	 * @param destination
	 * @param affectedResources
	 */
	protected final void updateSequences(final ISequences source,
			final IModifiableSequences destination,
			final Collection<IResource> affectedResources) {

		for (final IResource resource : affectedResources) {
			// Get source sequence
			final ISequence sourceSequence = source.getSequence(resource);
			assert sourceSequence != null;

			// Get destination sequence
			final IModifiableSequence destinationSequence = destination
					.getModifiableSequence(resource);
			assert destinationSequence != null;

			// Replace all entries in the destination with those in the source
			destinationSequence.replaceAll(sourceSequence);
		}
	}

	public final void setNumberOfIterations(final int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	@Override
	public final int getNumberOfIterations() {
		return numberOfIterations;
	}

	public final void setConstraintCheckers(
			final List<IConstraintChecker> constraintCheckers) {
		this.constraintCheckers = constraintCheckers;
	}

	@Override
	public final List<IConstraintChecker> getConstraintCheckers() {
		return constraintCheckers;
	}

	public final void setFitnessEvaluator(
			final IFitnessEvaluator fitnessEvaluator) {
		this.fitnessEvaluator = fitnessEvaluator;
	}

	@Override
	public final IFitnessEvaluator getFitnessEvaluator() {
		return fitnessEvaluator;
	}

	public final void setSequenceManipulator(
			final ISequencesManipulator sequenceManipulator) {
		this.sequenceManipulator = sequenceManipulator;
	}

	@Override
	public final ISequencesManipulator getSequenceManipulator() {
		return sequenceManipulator;
	}

	public final IOptimiserProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	public final void setProgressMonitor(
			final IOptimiserProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	public final int getReportInterval() {
		return reportInterval;
	}

	public final void setReportInterval(final int reportInterval) {
		this.reportInterval = reportInterval;
	}

	public final int getNumberOfIterationsCompleted() {
		return numberOfIterationsCompleted;
	}

	public final void setNumberOfIterationsCompleted(
			final int numberOfIterationsCompleted) {
		this.numberOfIterationsCompleted = numberOfIterationsCompleted;
	}

	protected final IOptimisationContext getCurrentContext() {
		return currentContext;
	}

	protected final void setCurrentContext(
			final IOptimisationContext currentContext) {
		this.currentContext = currentContext;
	}

	protected final long getStartTime() {
		return startTime;
	}

	
	protected final void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	@Override
	public void dispose() {
		this.constraintCheckers = null;
		this.fitnessEvaluator = null;
		this.progressMonitor = null;
		this.sequenceManipulator = null;
	}
}
