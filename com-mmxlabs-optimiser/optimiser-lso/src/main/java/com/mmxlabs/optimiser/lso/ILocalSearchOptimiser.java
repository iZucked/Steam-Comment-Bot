package com.mmxlabs.optimiser.lso;

import java.util.List;

import com.mmxlabs.optimiser.IOptimiser;
import com.mmxlabs.optimiser.ISequencesManipulator;
import com.mmxlabs.optimiser.constraints.IConstraintChecker;

/**
 * Extended {@link IOptimiser} interface for a Local Search Optimiser
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ILocalSearchOptimiser<T> extends IOptimiser<T> {

	IMoveGenerator<T> getMoveGenerator();

	int getNumberOfIterations();

	List<IConstraintChecker<T>> getConstraintCheckers();

	ISequencesManipulator<T> getSequenceManipulator();
}