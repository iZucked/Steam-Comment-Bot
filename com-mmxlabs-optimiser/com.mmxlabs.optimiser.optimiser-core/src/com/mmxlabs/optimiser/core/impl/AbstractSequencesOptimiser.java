/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Triple;
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
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IReducingConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;

public abstract class AbstractSequencesOptimiser implements ISequencesOptimiser {

	private int numberOfIterations;

	private int numberOfIterationsCompleted;

	private List<@NonNull IConstraintChecker> constraintCheckers;

	private List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers;

	private List<@NonNull IEvaluationProcess> evaluationProcesses;

	private IFitnessEvaluator fitnessEvaluator;

	private ISequencesManipulator sequenceManipulator;

	private IOptimiserProgressMonitor progressMonitor;

	private int reportInterval = -1;

	private IOptimisationContext currentContext;

	private long startTime;

	private List<@NonNull IReducingConstraintChecker> reducingConstraintCheckers;

	private List<@NonNull IInitialSequencesConstraintChecker> initialSequencesConstraintCheckers;

	/**
	 * Initialise method checking the object has all the correct pieces of data to be able to perform the {@link #optimise(IOptimisationContext, Collection, Object)} method. Throws an
	 * {@link IllegalStateException} on error.
	 */
	public void init() {
		checkState(numberOfIterations > 0, "Number of iterations is less than 1");
		checkState(constraintCheckers != null, "Constraint Checkers list is not set");
		checkState(evaluatedStateConstraintCheckers != null, "Evaluated State Constraint Checkers list is not set");
		checkState(fitnessEvaluator != null, "Fitness Evaluator is not set");
		checkState(sequenceManipulator != null, "Sequence Manipulator is not set");
		checkState(progressMonitor != null, "Progress Monitor is not set");
		checkState(reportInterval > 0, "Report interval is not set");
	}

	/**
	 * A default optimisation loop, which calls start() and then step() until done, and notifies the progress monitor. Subclasses will need to implement these.
	 */
	@Override
	public final void optimise(@NonNull final IOptimisationContext optimiserContext) {
		final IAnnotatedSolution startSolution = start(optimiserContext, optimiserContext.getInputSequences(), optimiserContext.getInputSequences());
		getProgressMonitor().begin(this, fitnessEvaluator.getBestFitness(), startSolution);
		final int percentage = (100 * getReportInterval()) / getNumberOfIterations();

		numberOfIterationsCompleted = 0;

		while (getNumberOfIterationsCompleted() < getNumberOfIterations()) {
			step(percentage);
			getProgressMonitor().report(this, getNumberOfIterationsCompleted(), fitnessEvaluator.getCurrentFitness(), fitnessEvaluator.getBestFitness(), getCurrentSolution(), getBestSolution());
		}

		getProgressMonitor().done(this, fitnessEvaluator.getBestFitness(), getBestSolution());
	}

	@Override
	public ISequences getBestRawSequences() {
		final Triple<ISequences, ISequences, IEvaluationState> bestSequences = fitnessEvaluator.getBestSequences();
		if (bestSequences != null) {
			return bestSequences.getFirst();
		}
		return null;
	}

	@Override
	public final IAnnotatedSolution getBestSolution() {
		final IAnnotatedSolution annotatedSolution = fitnessEvaluator.getBestAnnotatedSolution();
		if (annotatedSolution == null) {
			return null;
		}

		final long clock = System.currentTimeMillis() - getStartTime();

		annotatedSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, getNumberOfIterationsCompleted());
		annotatedSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, clock);

		return annotatedSolution;
	}

	@Override
	public final IAnnotatedSolution getCurrentSolution() {
		final IAnnotatedSolution annotatedSolution = fitnessEvaluator.getCurrentAnnotatedSolution();

		if (annotatedSolution == null) {
			return null;
		}

		final long clock = System.currentTimeMillis() - getStartTime();

		annotatedSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, getNumberOfIterationsCompleted());
		annotatedSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, clock);

		return annotatedSolution;
	}

	@Override
	public final boolean isFinished() {
		return getNumberOfIterationsCompleted() >= getNumberOfIterations();
	}

	/**
	 * Copy the {@link ISequences} for the specified {@link IResource}s from the source sequences to the destination sequences.
	 * 
	 * @param source
	 * @param destination
	 * @param affectedResources
	 */
	protected final void updateSequences(@NonNull final ISequences source, @NonNull final IModifiableSequences destination, @NonNull final Collection<IResource> affectedResources) {

		for (final IResource resource : affectedResources) {
			assert resource != null;
			// Get source sequence
			final ISequence sourceSequence = source.getSequence(resource);
			assert sourceSequence != null;

			// Get destination sequence
			final IModifiableSequence destinationSequence = destination.getModifiableSequence(resource);
			assert destinationSequence != null;

			// Replace all entries in the destination with those in the source
			destinationSequence.replaceAll(sourceSequence);
		}
		// Update the unused elements array
		destination.getModifiableUnusedElements().clear();
		destination.getModifiableUnusedElements().addAll(source.getUnusedElements());
	}

	public final void setNumberOfIterations(final int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	@Override
	public final int getNumberOfIterations() {
		return numberOfIterations;
	}

	public final void setConstraintCheckers(@NonNull final List<@NonNull IConstraintChecker> constraintCheckers) {
		this.constraintCheckers = constraintCheckers;
		this.reducingConstraintCheckers = new ArrayList<>(constraintCheckers.size());
		this.initialSequencesConstraintCheckers = new ArrayList<>(constraintCheckers.size());
		for (final IConstraintChecker checker : constraintCheckers) {
			if (checker instanceof IReducingConstraintChecker) {
				reducingConstraintCheckers.add((IReducingConstraintChecker) checker);
			}
			if (checker instanceof IInitialSequencesConstraintChecker) {
				initialSequencesConstraintCheckers.add((IInitialSequencesConstraintChecker) checker);
			}
		}
	}

	public final void setEvaluatedStateConstraintCheckers(@NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers) {
		this.evaluatedStateConstraintCheckers = evaluatedStateConstraintCheckers;
	}

	public final void setEvaluationProcesses(@NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses) {
		this.evaluationProcesses = evaluationProcesses;
	}

	@Override
	@NonNull
	public final List<@NonNull IConstraintChecker> getConstraintCheckers() {
		return constraintCheckers;
	}

	@Override
	@NonNull
	public final List<@NonNull IEvaluatedStateConstraintChecker> getEvaluatedStateConstraintCheckers() {
		return evaluatedStateConstraintCheckers;
	}

	@Override
	@NonNull
	public final List<@NonNull IEvaluationProcess> getEvaluationProcesses() {
		return evaluationProcesses;
	}

	@Override
	@NonNull
	public final List<@NonNull IReducingConstraintChecker> getReducingConstraintCheckers() {
		return reducingConstraintCheckers;
	}

	@Override
	@NonNull
	public final List<@NonNull IInitialSequencesConstraintChecker> getInitialSequencesConstraintCheckers() {
		return initialSequencesConstraintCheckers;
	}

	public final void setFitnessEvaluator(final IFitnessEvaluator fitnessEvaluator) {
		this.fitnessEvaluator = fitnessEvaluator;
	}

	@Override
	@NonNull
	public final IFitnessEvaluator getFitnessEvaluator() {
		return fitnessEvaluator;
	}

	public final void setSequenceManipulator(final ISequencesManipulator sequenceManipulator) {
		this.sequenceManipulator = sequenceManipulator;
	}

	@Override
	@NonNull
	public final ISequencesManipulator getSequenceManipulator() {
		return sequenceManipulator;
	}

	public final IOptimiserProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	public final void setProgressMonitor(final IOptimiserProgressMonitor progressMonitor) {
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

	public final void setNumberOfIterationsCompleted(final int numberOfIterationsCompleted) {
		this.numberOfIterationsCompleted = numberOfIterationsCompleted;
	}

	protected final IOptimisationContext getCurrentContext() {
		return currentContext;
	}

	protected final void setCurrentContext(@NonNull final IOptimisationContext currentContext) {
		this.currentContext = currentContext;
	}

	protected final long getStartTime() {
		return startTime;
	}

	protected final void setStartTime(final long startTime) {
		this.startTime = startTime;
	}
}
