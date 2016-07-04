/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

/**
 * The {@link IEvaluationProcessRegistry} is a store for {@link IEvaluationProcessFactory} instances. The Registry is to be used to create new instances of {@link IEvaluationProcess} for use in
 * optimisations.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public interface IEvaluationProcessRegistry {
	/**
	 * Registers a {@link IEvaluationProcessFactory} instance.
	 * 
	 * @param factory
	 */
	void registerEvaluationProcessFactory(@NonNull IEvaluationProcessFactory factory);

	/**
	 * De-registers a {@link IEvaluationProcessFactory} instance.
	 * 
	 * @param factory
	 */
	void deregisterEvaluationProcessFactory(@NonNull IEvaluationProcessFactory factory);

	/**
	 * Return a {@link Collection} of the registered {@link IEvaluationProcessFactory} instances.
	 * 
	 * @return
	 */
	@NonNull
	Collection<IEvaluationProcessFactory> getEvaluationProcessFactories();

	/**
	 * Returns a {@link Collection} of the registered {@link IEvaluationProcess} names.
	 * 
	 * @return
	 */
	@NonNull
	Collection<String> getEvaluationProcessNames();

	/**
	 * Returns a {@link Collection} of {@link IEvaluationProcessFactory} instances based on the list of {@link IEvaluationProcess} names.
	 * 
	 * @param names
	 * @return
	 */
	@NonNull
	Collection<IEvaluationProcessFactory> getEvaluationProcessFactories(@NonNull Collection<String> names);
}
