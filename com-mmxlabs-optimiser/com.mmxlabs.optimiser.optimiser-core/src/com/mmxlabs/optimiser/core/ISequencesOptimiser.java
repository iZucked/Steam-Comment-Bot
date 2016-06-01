/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IReducingConstraintChecker;
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
	List<@NonNull IConstraintChecker> getConstraintCheckers();

	/**
	 * Returns the list of {@link #getConstraintCheckers()} which also implement @link {IReducingContraintChecker}
	 * 
	 * @return
	 */
	@NonNull
	List<@NonNull IReducingConstraintChecker> getReducingConstraintCheckers();

	/**
	 * Returns the list of {@link #getConstraintCheckers()} which also implement @link {IInitialSequencesConstraintChecker}
	 * 
	 * @return
	 */
	@NonNull
	List<@NonNull IInitialSequencesConstraintChecker> getInitialSequencesConstraintCheckers();

	@NonNull
	List<@NonNull IEvaluationProcess> getEvaluationProcesses();

	/**
	 * Returns the {@link ISequencesManipulator} used to transform {@link ISequences} into a new {@link ISequences} object to validate and evaluate each iteration.
	 * 
	 * @return
	 */
	@NonNull
	ISequencesManipulator getSequenceManipulator();

	/**
	 * Prepare for optimisation on the given inputs
	 * 
	 * @param context
	 * @param initialRawSequences
	 *            This is the earliest starting point. In a multiple stage optimisation this is the starting point
	 * @param inputRawSequences
	 *            This the solution to optimise. In a multiple stage optimisation this is the result of the previous stage and may be different to the initialRawSequences
	 * @return
	 */
	@Nullable
	IAnnotatedSolution start(@NonNull IOptimisationContext context, @NonNull final ISequences initialRawSequences, @NonNull final ISequences inputRawSequences);

	@Nullable
	IAnnotatedSolution getBestSolution();

	@Nullable
	IAnnotatedSolution getCurrentSolution();

	int step(int percentage);

	boolean isFinished();

	ISequences getBestRawSequences();

	@NonNull
	List<@NonNull IEvaluatedStateConstraintChecker> getEvaluatedStateConstraintCheckers();

}