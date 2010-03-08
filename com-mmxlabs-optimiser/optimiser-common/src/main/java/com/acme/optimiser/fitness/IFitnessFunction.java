package com.acme.optimiser.fitness;

import java.util.List;

import com.acme.optimiser.IResource;
import com.acme.optimiser.ISequences;

/**
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IFitnessFunction<T> {

	/**
	 * Evaluates the fitness of the given sequence.
	 * 
	 * @param sequences
	 * @return
	 */
	double evaluate(ISequences<T> sequences);

	/**
	 * Evaluates the fitness of the given sequence. This method takes a list of
	 * affected resources used to indicate that only the given resources have
	 * changed since the previous evaluation. NOTE: This is to be used as part
	 * of a "delta" calculation. API still to be defined.
	 * 
	 * @param sequences
	 * @return
	 */
	double evaluate(ISequences<T> sequences, List<IResource> affectedResources);
}
