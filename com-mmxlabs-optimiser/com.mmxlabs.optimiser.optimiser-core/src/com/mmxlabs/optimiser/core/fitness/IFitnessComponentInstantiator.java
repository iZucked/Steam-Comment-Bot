/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public interface IFitnessComponentInstantiator {

	/**
	 * Return new instances of all {@link IFitnessComponent}s the {@link IFitnessFunctionRegistry} knows about.
	 * 
	 * @param registry
	 * @return
	 */
	@NonNull
	List<IFitnessComponent> instantiateFitnesses(@NonNull IFitnessFunctionRegistry registry);

	/**
	 * Return new instances of all {@link IFitnessComponent}s the {@link IFitnessFunctionRegistry} knows about. The returned list will contain components in the same order as the input list. Missing
	 * {@link IFitnessComponent}s will be replaced with a null entry in the list.
	 * 
	 * @param registry
	 * @param componentNames
	 * @return
	 */
	@NonNull
	List<IFitnessComponent> instantiateFitnesses(@NonNull IFitnessFunctionRegistry registry, @NonNull List<String> componentNames);

}