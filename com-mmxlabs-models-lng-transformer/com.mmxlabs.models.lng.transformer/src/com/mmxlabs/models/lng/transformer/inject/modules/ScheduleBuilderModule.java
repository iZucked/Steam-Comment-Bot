/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IPostExportProcessorFactory;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;

/**
 * Simple {@link Module} implementation to manage {@link ISchedulerBuilder} and {@link ContractTransformer} based extensions.
 * 
 * @author Simon Goodall
 */
public class ScheduleBuilderModule extends AbstractModule {

	@Override
	protected void configure() {

		if (Platform.isRunning()) {
			install(osgiModule(FrameworkUtil.getBundle(ScheduleBuilderModule.class).getBundleContext(), eclipseRegistry()));

			bind(iterable(IBuilderExtensionFactory.class)).toProvider(service(IBuilderExtensionFactory.class).multiple());
			bind(iterable(ITransformerExtensionFactory.class)).toProvider(service(ITransformerExtensionFactory.class).multiple());
			bind(iterable(IExporterExtensionFactory.class)).toProvider(service(IExporterExtensionFactory.class).multiple());
			bind(iterable(IPostExportProcessorFactory.class)).toProvider(service(IPostExportProcessorFactory.class).multiple());
		}
	}

	@Provides
	@Singleton
	private ISchedulerBuilder provideSchedulerBuilder(@NonNull final Injector injector, @NonNull final Iterable<IBuilderExtensionFactory> builderExtensionFactories) {
		final SchedulerBuilder builder = new SchedulerBuilder();
		for (final IBuilderExtensionFactory factory : builderExtensionFactories) {
			final IBuilderExtension instance = factory.createInstance();
			if (instance != null) {
				injector.injectMembers(instance);
				builder.addBuilderExtension(instance);
			}
		}
		injector.injectMembers(builder);
		return builder;
	}

	@Provides
	@Singleton
	private List<IBuilderExtension> provideBuilderExtensions(final Injector injector, final Iterable<IBuilderExtensionFactory> extensionFactories) {
		final List<IBuilderExtension> extensions = new ArrayList<IBuilderExtension>();
		for (final IBuilderExtensionFactory factory : extensionFactories) {
			final IBuilderExtension instance = factory.createInstance();
			injector.injectMembers(instance);
			extensions.add(instance);
		}
		return extensions;
	}

	@Provides
	@Singleton
	private List<ITransformerExtension> provideTransformerExtensions(final Injector injector, final Iterable<ITransformerExtensionFactory> extensionFactories) {
		final List<ITransformerExtension> extensions = new ArrayList<ITransformerExtension>();
		for (final ITransformerExtensionFactory factory : extensionFactories) {
			final ITransformerExtension instance = factory.createInstance();
			injector.injectMembers(instance);
			extensions.add(instance);
		}
		return extensions;
	}
}
