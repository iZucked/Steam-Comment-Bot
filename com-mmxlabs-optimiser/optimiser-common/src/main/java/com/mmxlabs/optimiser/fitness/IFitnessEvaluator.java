package com.mmxlabs.optimiser.fitness;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.ISequencesManipulator;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

/**
 * The {@link IFitnessEvaluator} interface defines objects which can be used in
 * an optimisation process to determine whether or not the current fitness is a
 * suitable improvement. The {@link #init(ISequences)} method needs to be called
 * at the start of the process and further invocations of this class should use
 * {@link #checkSequences(ISequences)}. The {@link IFitnessComponent} passed to
 * the {@link #setFitnessComponents(List)} will be used to evaluate fitnesses.
 * As such they should not be used while using this instance.
 * 
 * @param <T>
 *            Sequence element type
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessEvaluator<T> {

	/**
	 * Returns the {@link List} of {@link IFitnessComponent}s used by this
	 * {@link IFitnessEvaluator}
	 * 
	 * @return
	 */
	List<IFitnessComponent<T>> getFitnessComponents();

	/**
	 * Set the list of {@link IFitnessComponent}s to be used by this
	 * {@link IFitnessEvaluator}. Note these {@link IFitnessComponent}s
	 * instances should not be elsewhere while using this object.
	 * 
	 * @param fitnessComponents
	 */
	void setFitnessComponents(List<IFitnessComponent<T>> fitnessComponents);

	/**
	 * Returns the {@link IFitnessHelper} used to evaluate {@link ISequences}
	 * using the {@link IFitnessComponent}s passed into the
	 * {@link #setFitnessComponents(List)} method.
	 * 
	 * @return
	 */
	IFitnessHelper<T> getFitnessHelper();

	/**
	 * Set the {@link IFitnessHelper} used to evaluate {@link ISequences} using
	 * the {@link IFitnessComponent}s passed into the
	 * {@link #setFitnessComponents(List)} method.
	 * 
	 */
	void setFitnessHelper(IFitnessHelper<T> fitnessHelper);

	/**
	 * Initialise {@link IFitnessEvaluator} and ensure all the relevant setters
	 * have been called.
	 */
	void init();

	/**
	 * Initialise the {@link IFitnessEvaluator} with {@link IOptimisationData}
	 * object. This should be called once at the beginning of an optimisation
	 * process.
	 * 
	 * @param data
	 */
	void setOptimisationData(IOptimisationData<T> data);

	/**
	 * Set the initial sequences. These should have had any manipulation from
	 * {@link ISequencesManipulator} already applied.
	 * 
	 * @param sequences
	 */
	void setInitialSequences(ISequences<T> sequences);

	/**
	 * Evaluate the given {@link ISequences} to determine whether or not they
	 * are accepted as a better state than the previously accepted state. If
	 * accepted, then the {@link ISequences} "state" will be recorded and used
	 * as the basis for future checks. {@link #setInitialSequences(ISequences)}
	 * must have been called previously to setup the initial state.
	 * 
	 * @param sequences
	 * @return
	 */
	boolean checkSequences(ISequences<T> sequences,
			Collection<IResource> affectedResources);

	/**
	 * Returns the "best" {@link ISequences} instances seen by this
	 * {@link IFitnessEvaluator}. The value of best is determined by the
	 * implementation. However it is reasonable to assume the first item in the
	 * {@link Collection} has the best fitness value.
	 * 
	 * @return
	 */
	Collection<ISequences<T>> getBestSequences();
}
