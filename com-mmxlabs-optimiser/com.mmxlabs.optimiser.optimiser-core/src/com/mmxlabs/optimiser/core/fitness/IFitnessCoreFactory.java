/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.Collection;

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
	String getFitnessCoreName();

	/**
	 * Returns a {@link Collection} of names for the {@link IFitnessComponent}s the {@link IFitnessCore} represents.
	 * 
	 * @return
	 */
	Collection<String> getFitnessComponentNames();

	/**
	 * Instantiate a new {@link IFitnessCore} instance.
	 * 
	 * @return
	 */
	IFitnessCore instantiate();
}
