package com.mmxlabs.lngscheduler.emf.extras.inject;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.lngscheduler.emf.extras.inject.extensions.ContractTransformer;
import com.mmxlabs.lngscheduler.emf.extras.plugin.ExtrasPlugin;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;

public class ScheduleBuilderModule extends AbstractModule {

	@Override
	protected void configure() {
		install(osgiModule(ExtrasPlugin.getDefault().getBundle().getBundleContext(), eclipseRegistry()));

		bind(iterable(ContractTransformer.class)).toProvider(service(ContractTransformer.class).multiple());

		bind(ISchedulerBuilder.class).to(SchedulerBuilder.class);
	}

}
