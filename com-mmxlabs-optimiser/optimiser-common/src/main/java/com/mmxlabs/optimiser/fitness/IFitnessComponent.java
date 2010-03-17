package com.mmxlabs.optimiser.fitness;

/**
 * The {@link IFitnessComponent} represents a single fitness value from a
 * {@link IFitnessCore} object. The {@link IFitnessComponent} does not perform
 * any processing of it's own, that is the responsibility of the
 * {@link IFitnessCore}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IFitnessComponent<T> {

	/**
	 * Returns the name of the {@link IFitnessComponent}
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns the last evaluated fitness value in the {@link IFitnessCore} for
	 * this component.
	 * 
	 * @return
	 */
	double getFitness();

	/**
	 * Returns the shared {@link IFitnessCore} instance for this component.
	 * 
	 * @return
	 */
	IFitnessCore<T> getFitnessCore();
}
