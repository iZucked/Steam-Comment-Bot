/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.export.IPortSlotEventProvider;
import com.mmxlabs.models.lng.transformer.export.PortSlotEventProvider;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;

/**
 * A {@link Module} implementation to instantiate {@link IExporterExtension}s from {@link IExporterExtensionFactory}
 * 
 * @author Simon Goodall
 * 
 */
public class ExporterExtensionsModule extends AbstractModule {

	@Override
	protected void configure() {
		IPortSlotEventProvider portSlotEventProvider = new PortSlotEventProvider();
		bind(IPortSlotEventProvider.class).toInstance(portSlotEventProvider);
	}

	@Provides
	private List<IExporterExtension> provideExporterExtensions(final Injector injector, final Iterable<IExporterExtensionFactory> extensionFactories) {
		final List<IExporterExtension> extensions = new ArrayList<IExporterExtension>();
		for (final IExporterExtensionFactory factory : extensionFactories) {
			final IExporterExtension instance = factory.createInstance();
			injector.injectMembers(instance);
			extensions.add(instance);
		}
		return extensions;
	}
}
