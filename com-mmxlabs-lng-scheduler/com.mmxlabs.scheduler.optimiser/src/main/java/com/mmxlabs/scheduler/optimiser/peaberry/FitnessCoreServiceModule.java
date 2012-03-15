package com.mmxlabs.scheduler.optimiser.peaberry;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

/**
 * Guice module using Peaberry to register {@link IFitnessCoreFactory} instances as services.
 * 
 * @author Simon Goodall
 * 
 */
public class FitnessCoreServiceModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(TypeLiterals.export(IFitnessCoreFactory.class)).annotatedWith(Names.named(CargoSchedulerFitnessCoreFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new CargoSchedulerFitnessCoreFactory()).export());
	}
}
