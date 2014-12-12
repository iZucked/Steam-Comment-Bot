/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IReducingContraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;

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
	@NonNull
	List<IConstraintChecker> getConstraintCheckers();

	/**
	 * Returns the list of {@link #getConstraintCheckers()} which also implement @link {IReducingContraintChecker}
	 * 
	 * @return
	 */
	@NonNull
	List<IReducingContraintChecker> getReducingConstraintCheckers();

	@NonNull
	List<IEvaluationProcess> getEvaluationProcesses();

	/**
	 * Returns the {@link ISequencesManipulator} used to transform {@link ISequences} into a new {@link ISequences} object to validate and evaluate each iteration.
	 * 
	 * @return
	 */
	@NonNull
	ISequencesManipulator getSequenceManipulator();

	@Nullable
	IAnnotatedSolution start(@NonNull IOptimisationContext context);

	@Nullable
	IAnnotatedSolution getBestSolution();

	@Nullable
	IAnnotatedSolution getCurrentSolution();

	int step(int percentage);

	boolean isFinished();

}