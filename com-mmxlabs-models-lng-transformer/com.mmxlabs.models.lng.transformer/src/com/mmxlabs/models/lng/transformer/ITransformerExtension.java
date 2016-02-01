/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;

/**
 * Interface for simple extensions to the LNGScenarioTransformer.
 * 
 * @author hinton
 * 
 */
public interface ITransformerExtension {
	/**
	 * This method will be called by the {@link LNGScenarioTransformer} before any transformation has happened. The contract transformer should take this opportunity to install any
	 * {@link IBuilderExtension}s it needs in the builder, and take a note of the {@link ModelEntityMap} and scenario if it needs them later on
	 * 
	 * This will be called *after* basic port and curve data has been configured .
	 * 
	 * @param scenario
	 * @param modelEntityMap
	 * @param builder
	 */
	public void startTransforming(LNGScenarioModel rootObject, ModelEntityMap modelEntityMap, ISchedulerBuilder builder);

	/**
	 * This method will be called just before the transformation is completed and the builder's {@link ISchedulerBuilder#getOptimisationData()} method is called.
	 */
	public void finishTransforming();
}
