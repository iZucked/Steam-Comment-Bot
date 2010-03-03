package com.acme.optimiser.fitness;

import java.util.Collection;
import java.util.List;

/**
 * The {@link IFitnessFunctionRegistery} is a store for
 * {@link IFitnessFunctionFactory} instances. The Registery is to be used to
 * create new instances of fitness functions for use in optimisations.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessFunctionRegistery {

	/**
	 * Registers a {@link IFitnessFunctionFactory} instance.
	 * 
	 * @param name
	 * @param factory
	 */
	void registerFitnessFunction(String name, IFitnessFunctionFactory factory);

	/**
	 * Return a {@link Collection} of the registered
	 * {@link IFitnessFunctionFactory} instances.
	 * 
	 * @return
	 */

	Collection<IFitnessFunctionFactory> getFitnessFunctionFactories();

	/**
	 * Returns a {@link Collection} of the registered
	 * {@link IFitnessFunctionFactory} names.
	 * 
	 * @return
	 */
	Collection<String> getFitnessFunctionFactoryNames();

	/**
	 * Returns a {@link List} of {@link IFitnessFunctionFactory} instances
	 * ordered by the given list of names.
	 * 
	 * @param names
	 * @return
	 */
	List<IFitnessFunctionFactory> getFitnessFunctionFactories(List<String> names);
}
