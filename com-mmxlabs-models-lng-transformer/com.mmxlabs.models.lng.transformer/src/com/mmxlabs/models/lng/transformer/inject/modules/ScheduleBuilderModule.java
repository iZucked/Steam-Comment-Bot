/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformer;
import com.mmxlabs.models.lng.transformer.internal.Activator;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
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

		final Activator plugin = Activator.getDefault();
		if (plugin != null) {
			install(osgiModule(plugin.getBundle().getBundleContext(), eclipseRegistry()));

			bind(iterable(ContractTransformer.class)).toProvider(service(ContractTransformer.class).multiple());
			bind(iterable(IBuilderExtensionFactory.class)).toProvider(service(IBuilderExtensionFactory.class).multiple());
			bind(iterable(ITransformerExtensionFactory.class)).toProvider(service(ITransformerExtensionFactory.class).multiple());
		}
	}

	@Provides
	ISchedulerBuilder provideSchedulerBuilder(final Injector injector, final Iterable<IBuilderExtensionFactory> builderExtensionFactories) {
		final SchedulerBuilder builder = new SchedulerBuilder();
		for (final IBuilderExtensionFactory factory : builderExtensionFactories) {
			final IBuilderExtension instance = factory.createInstance();
			injector.injectMembers(instance);
			builder.addBuilderExtension(instance);
		}
		injector.injectMembers(builder);
		return builder;
	}
}
