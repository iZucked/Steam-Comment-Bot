package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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
	 * Returns false if the given sequence is so bad it's not worth proceeding with,
	 * and the caller must ensure that accepted() is never called for a sequences
	 * which has had this outcome (if it is accepted, the internal state will be invalid).
	 * 
	 * @param resource
	 * @param sequence
	 * @param annotatedSequence
	 * @param newSequence
	 */
	boolean evaluateSequence(IResource resource,
			ISequence<T> sequence,
			List<VoyagePlan> plans, boolean newSequence, final int startTime);

	boolean shouldIterate();
	
	void beginIterating(IResource resource);
	void evaluateNextObject(final Object object, final int startTime);
	void endIterating();
	
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
	 * Prepare for a new partial evaluation.
	 */
	void prepareDelta();

	/**
	 * Evaluate the given {@link ISequence}. The {@link IAnnotatedSequence} will
	 * have already been scheduled and can be queried. This method does not
	 * update internal state.
	 * 
	 * @param resource
	 * @param sequence
	 * @param annotatedSequence
	 * @return
	 */
	long rawEvaluateSequence(IResource resource, ISequence<T> sequence,
			List<VoyagePlan> plans, int startTime);

	/**
	 * Clean up references as this component is no longer required.
	 */
	void dispose();
}
