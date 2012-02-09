/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.inject;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.lngscheduler.emf.extras.inject.extensions.ContractTransformer;
import com.mmxlabs.lngscheduler.emf.extras.plugin.ExtrasPlugin;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;

/**
 * Simple {@link Module} implementation to manage {@link ISchedulerBuilder} and {@link ContractTransformer} based extensions.
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduleBuilderModule extends AbstractModule {

	@Override
	protected void configure() {
		install(osgiModule(ExtrasPlugin.getDefault().getBundle().getBundleContext(), eclipseRegistry()));

		bind(iterable(ContractTransformer.class)).toProvider(service(ContractTransformer.class).multiple());

		bind(ISchedulerBuilder.class).to(SchedulerBuilder.class);
	}
}
