package com.mmxlabs.optimiser.fitness;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;

/**
 * The {@link IFitnessCore} is the main fitness function object. A single
 * {@link IFitnessCore} can represent multiple {@link IFitnessComponent}s. The
 * {@link IFitnessCore} is used to evaluate {@link ISequences} instances either
 * in whole, or progressively throughout an optimisation process.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IFitnessCore<T> {

	/**
	 * Initialise or re-initialise the fitness core. This will reset the state
	 * associated with the {@link #evaluate(ISequences, List)} method.
	 */
	void init();

	/**
	 * Return a {@link Collection} of {@link IFitnessComponent} instances that
	 * are represented by this {@link IFitnessCore}.
	 * 
	 * @return
	 */
	Collection<IFitnessComponent<T>> getFitnessComponents();

	/**
	 * Evaluates the full fitness of the given sequence.
	 * 
	 * @param sequences
	 * @return Fitness of sequences
	 */
	double evaluate(ISequences<T> sequences);

	/**
	 * Evaluates the fitness of the given sequence. This method takes a list of
	 * affected resources used to indicate that only the given resources have
	 * changed since the previous evaluation. If this is the first invocation of
	 * this method since the last {@link #init()} call, then a full evaluation
	 * must be performed and subsequent calls can take advantage of the affected
	 * list.
	 * 
	 * @param sequences
	 * @return Fitness of sequences
	 */
	double evaluate(ISequences<T> sequences,
			Collection<IResource> affectedResources);
}
