/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Interface defining a factory to generate new {@link IFitnessCore} instances.
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessCoreFactory {

	/**
	 * Returns the name associated with the {@link IFitnessCore}
	 * 
	 * @return
	 */
	@NonNull
	String getFitnessCoreName();

	/**
	 * Returns a {@link Collection} of names for the {@link IFitnessComponent}s the {@link IFitnessCore} represents.
	 * 
	 * @return
	 */
	@NonNull
	Collection<String> getFitnessComponentNames();

	/**
	 * Instantiate a new {@link IFitnessCore} instance.
	 * 
	 * @return
	 */
	@NonNull
	IFitnessCore instantiate();
}
