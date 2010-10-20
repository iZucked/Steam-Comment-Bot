/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.lso;

import java.util.List;

import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;

/**
 * Extended {@link IOptimiser} interface for a Local Search Optimiser
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ILocalSearchOptimiser<T> extends IOptimiser<T> {

	/**
	 * Returns the {@link IMoveGenerator} used by this
	 * {@link ILocalSearchOptimiser} to generate a {@link IMove} each iteration.
	 * 
	 * @return
	 */
	IMoveGenerator<T> getMoveGenerator();

	/**
	 * Returns the number of iterations, moves to try, in this optimisation
	 * 
	 * @return
	 */
	int getNumberOfIterations();

	/**
	 * Returns the list of {@link IConstraintChecker} used to validate
	 * {@link ISequences} before evaluating them in each iteration.
	 * 
	 * @return
	 */
	List<IConstraintChecker<T>> getConstraintCheckers();

	/**
	 * Returns the {@link ISequencesManipulator} used to transform
	 * {@link ISequences} into a new {@link ISequences} object to validate and
	 * evaluate each iteration.
	 * 
	 * @return
	 */
	ISequencesManipulator<T> getSequenceManipulator();
}