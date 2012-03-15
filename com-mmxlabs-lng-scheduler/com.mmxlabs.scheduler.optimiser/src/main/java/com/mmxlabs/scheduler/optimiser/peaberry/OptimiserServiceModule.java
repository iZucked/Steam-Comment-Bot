package com.mmxlabs.scheduler.optimiser.peaberry;

import com.google.inject.AbstractModule;

/**
 * Guice module for use with Peaberry Activation extension. Manifest should have a Bundle-Module entry referencing this class.
 * 
 * @author Simon Goodall
 * 
 * 
 */
public class OptimiserServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FitnessCoreServiceModule());
		install(new ConstraintCheckerServiceModule());
	}
}
