package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * Extension of the {@link IFitnessComponent} interface for use with the
 * {@link CargoSchedulerFitnessCore} to provide fitnesses based on
 * {@link ISequence}s scheduled with an {@link ISequenceScheduler}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ICargoSchedulerFitnessComponent<T> extends
		IFitnessComponent<T> {

	/**
	 * Initialise the fitness component and obtain any data required from the
	 * {@link IOptimisationData} object.
	 * 
	 * @param data
	 */
	void init(IOptimisationData<T> data);

	/**
	 * Evaluate the given {@link ISequence}. The {@link IAnnotatedSequence} will
	 * have already been scheduled and can be queried. If newSequence is set to
	 * false, then this method is being invoked as part of a full evaluation. If
	 * it is true, then it is a partial/delta evaluation as part of a
	 * {@link IMove} evaluation.
	 * 
	 * @param resource
	 * @param sequence
	 * @param annotatedSequence
	 * @param newSequence
	 */
	void evaluateSequence(final IResource resource,
			final ISequence<T> sequence,
			final IAnnotatedSequence<T> annotatedSequence, boolean newSequence);

	/**
	 * Notify fitness component that the last evaluation has been accepted.
	 * 
	 * @param sequences
	 * @param affectedResources
	 */
	void accepted(ISequences<T> sequences,
			Collection<IResource> affectedResources);

	/**
	 * Notify fitness component that a full evaluation is about to begin. Expect
	 * {@link #evaluateSequence(IResource, ISequence, ISequenceScheduler, boolean)}
	 * with newSequence set to false.
	 */
	void prepare();

	/**
	 * Notify fitness component that a full evaluation is now complete. Ensure
	 * we are ready for partial/delta evaluations - where
	 * {@link #evaluateSequence(IResource, ISequence, ISequenceScheduler, boolean)}
	 * will be passed with newSequence set to true.
	 */
	void complete();

	/**
	 * Clean up references as this component is no longer required.
	 */
	void dispose();

	void prepareDelta();
}
