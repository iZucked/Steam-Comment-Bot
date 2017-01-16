/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.peaberry;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

/**
 * {@link Guice} module using {@link Peaberry} to register {@link IFitnessCoreFactory} instances as services.
 * 
 * @author Simon Goodall
 * 
 */
public class FitnessCoreServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TypeLiterals.export(IFitnessCoreFactory.class)).annotatedWith(Names.named(CargoSchedulerFitnessCoreFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(CargoSchedulerFitnessCoreFactory.class).export());

		bind(TypeLiterals.export(IFitnessCoreFactory.class)).annotatedWith(Names.named(NonOptionalSlotFitnessCoreFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(NonOptionalSlotFitnessCoreFactory.class).export());

		bind(TypeLiterals.export(IFitnessCoreFactory.class)).annotatedWith(Names.named(SimilarityFitnessCoreFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(SimilarityFitnessCoreFactory.class).export());
	}
}
