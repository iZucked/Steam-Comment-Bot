/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;

/**
 * Extended {@link IOptimiser} interface for optimising {@link ISequences}.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequencesOptimiser extends IOptimiser {

	/**
	 * Returns the number of iterations, moves to try, in this optimisation
	 * 
	 * @return
	 */
	int getNumberOfIterations();

	/**
	 * Returns the list of {@link IConstraintChecker} used to validate {@link ISequences} before evaluating them in each iteration.
	 * 
	 * @return
	 */
	List<IConstraintChecker> getConstraintCheckers();

	/**
	 * Returns the {@link ISequencesManipulator} used to transform {@link ISequences} into a new {@link ISequences} object to validate and evaluate each iteration.
	 * 
	 * @return
	 */
	ISequencesManipulator getSequenceManipulator();

	IAnnotatedSolution start(IOptimisationContext context);

	IAnnotatedSolution getBestSolution(final boolean b);

	IAnnotatedSolution getCurrentSolution(final boolean b);

	int step(int percentage);

	boolean isFinished();
}