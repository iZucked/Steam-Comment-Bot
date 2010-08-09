package com.mmxlabs.optimiser.core.fitness;

import java.util.List;

public interface IFitnessComponentInstantiator {

	/**
	 * Return new instances of all {@link IFitnessComponent}s the
	 * {@link IFitnessFunctionRegistry} knows about.
	 * 
	 * @param <T>
	 * @param registry
	 * @return
	 */
	public <T> List<IFitnessComponent<T>> instantiateFitnesses(
			final IFitnessFunctionRegistry registry);

	/**
	 * Return new instances of all {@link IFitnessComponent}s the
	 * {@link IFitnessFunctionRegistry} knows about. The returned list will
	 * contain components in the same order as the input list. Missing
	 * {@link IFitnessComponent}s will be replaced with a null entry in the
	 * list.
	 * 
	 * @param <T>
	 * @param registry
	 * @param componentNames
	 * @return
	 */
	public <T> List<IFitnessComponent<T>> instantiateFitnesses(
			final IFitnessFunctionRegistry registry,
			final List<String> componentNames);

}