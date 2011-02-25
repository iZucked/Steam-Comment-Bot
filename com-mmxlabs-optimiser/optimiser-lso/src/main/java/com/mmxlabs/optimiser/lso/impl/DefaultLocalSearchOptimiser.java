/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
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
			final IModifiableSequences<T> fullSequences = new ModifiableSequences<T>(
					currentRawSequences);
			manipulator.manipulate(fullSequences);

			// Prime fitness cores with initial sequences
			fitnessEvaluator.setOptimisationData(data);
			fitnessEvaluator.setInitialSequences(fullSequences);
		}

		// Set initial sequences
		getMoveGenerator().setSequences(potentialRawSequences);

		{
			final IAnnotatedSolution<T> annotatedBestSolution = fitnessEvaluator
					.getBestAnnotatedSolution(optimiserContext);
			// fitnessEvaluator.getBestSequences()
			
			annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, 0);
			annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, 0l);
			getProgressMonitor().begin(this, fitnessEvaluator.getBestFitness(),
					annotatedBestSolution);

		}
		
		final long startTime = System.currentTimeMillis();
		
		// Perform the optimisation
		MAIN_LOOP: for (int iter = 0; iter < getNumberOfIterations(); ++iter) {

			// Periodically issue a status report to the progress monitor
			if (iter % getReportInterval() == 0) {
				final IAnnotatedSolution<T> annotatedBestSolution = 
					fitnessEvaluator.getBestAnnotatedSolution(optimiserContext);
				final IAnnotatedSolution<T> annotatedCurrentSolution = 
					fitnessEvaluator.getCurrentAnnotatedSolution(optimiserContext);
				// fitnessEvaluator.getBestSequences()

				final long clock = System.currentTimeMillis() - startTime;
				
				annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, iter);
				annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, clock);

				annotatedCurrentSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, iter);
				annotatedCurrentSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, clock);
				
				getProgressMonitor().report(this, iter,
						fitnessEvaluator.getCurrentFitness(),
						fitnessEvaluator.getBestFitness(),
						annotatedCurrentSolution, annotatedBestSolution);
			}

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
			final IModifiableSequences<T> potentialFullSequences = new ModifiableSequences<T>(
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
			if (fitnessEvaluator.evaluateSequences(potentialFullSequences,
					move.getAffectedResources())) {

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

		final IAnnotatedSolution<T> bestSolution = fitnessEvaluator.getBestAnnotatedSolution(optimiserContext); 
		
		final long clock = System.currentTimeMillis() - startTime;
		
		bestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, getNumberOfIterations() - 1);
		bestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, clock);

		getProgressMonitor().done(this, fitnessEvaluator.getBestFitness(),
				bestSolution);

	}
}
