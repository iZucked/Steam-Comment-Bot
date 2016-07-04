/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

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
	void registerFitnessCoreFactory(@NonNull IFitnessCoreFactory factory);

	/**
	 * De-registers a {@link IFitnessCoreFactory} instance.
	 * 
	 * @param factory
	 */
	void deregisterFitnessCoreFactory(@NonNull IFitnessCoreFactory factory);

	/**
	 * Return a {@link Collection} of the registered {@link IFitnessCoreFactory} instances.
	 * 
	 * @return
	 */
	@NonNull
	Collection<IFitnessCoreFactory> getFitnessCoreFactories();

	/**
	 * Returns a {@link Collection} of the registered {@link IFitnessCoreFactory} names.
	 * 
	 * @return
	 */
	@NonNull
	Collection<String> getFitnessCoreFactoryNames();

	/**
	 * Returns a {@link Collection} of the {@link IFitnessComponent} names represented by the registered {@link IFitnessCoreFactory} objects.
	 * 
	 * @return
	 */
	@NonNull
	Collection<String> getFitnessComponentNames();

	/**
	 * Returns a {@link Collection} of {@link IFitnessCoreFactory} instances based on the list of {@link IFitnessComponent} names.
	 * 
	 * @param names
	 * @return
	 */
	@NonNull
	Collection<IFitnessCoreFactory> getFitnessCoreFactories(@NonNull Collection<String> names);

}
