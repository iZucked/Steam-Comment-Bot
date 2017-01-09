/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.peaberry;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.scheduler.optimiser.fitness.ICargoFitnessComponentProvider;

/**
 * Guice module for use with Peaberry Activation extension. Manifest should have a Bundle-Module entry referencing this class.
 * 
 * @author Simon Goodall
 * 
 * 
 */
public class OptimiserServiceModule extends PeaberryActivationModule {
	@Override
	protected void configure() {
		bindService(ICargoFitnessComponentProvider.class).multiple();

		install(new FitnessCoreServiceModule());
		install(new ConstraintCheckerServiceModule());
		install(new EvaluatedStateConstraintCheckerServiceModule());
		install(new EvaluationProcessServiceModule());
	}
}
