/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;

/**
 * The {@link IFitnessFunctionRegistry} is a store for {@link IFitnessCoreFactory} instances. The Registry is to be used to create new instances of {@link IFitnessCore} for use in optimisations.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessFunctionRegistry {

	/**
	 * Registers a {@link IFitnessCoreFactory} instance.
	 * 
	 * @param factory
	 */
	void registerFitnessCoreFactory(IFitnessCoreFactory factory);

	/**
	 * De-registers a {@link IFitnessCoreFactory} instance.
	 * 
	 * @param factory
	 */
	void deregisterFitnessCoreFactory(IFitnessCoreFactory factory);

	/**
	 * Return a {@link Collection} of the registered {@link IFitnessCoreFactory} instances.
	 * 
	 * @return
	 */

	Collection<IFitnessCoreFactory> getFitnessCoreFactories();

	/**
	 * Returns a {@link Collection} of the registered {@link IFitnessCoreFactory} names.
	 * 
	 * @return
	 */
	Collection<String> getFitnessCoreFactoryNames();

	/**
	 * Returns a {@link Collection} of the {@link IFitnessComponent} names represented by the registered {@link IFitnessCoreFactory} objects.
	 * 
	 * @return
	 */
	Collection<String> getFitnessComponentNames();

	/**
	 * Returns a {@link Collection} of {@link IFitnessCoreFactory} instances based on the list of {@link IFitnessComponent} names.
	 * 
	 * @param names
	 * @return
	 */
	Collection<IFitnessCoreFactory> getFitnessCoreFactories(Collection<String> names);

}
