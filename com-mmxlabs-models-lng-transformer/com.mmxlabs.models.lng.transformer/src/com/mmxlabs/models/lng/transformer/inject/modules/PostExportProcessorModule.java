/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.transformer.IPostExportProcessor;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IPostExportProcessorFactory;

/**
 * A {@link Module} implementation to instantiate {@link IExporterExtension}s from {@link IExporterExtensionFactory}
 * 
 * @author Simon Goodall
 * 
 */
public class PostExportProcessorModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	private List<IPostExportProcessor> provideExporterExtensions(final Injector injector, final Iterable<IPostExportProcessorFactory> extensionFactories) {
		final List<IPostExportProcessor> extensions = new ArrayList<IPostExportProcessor>();
		for (final IPostExportProcessorFactory factory : extensionFactories) {
			final IPostExportProcessor instance = factory.createInstance();
			injector.injectMembers(instance);
			extensions.add(instance);
		}
		return extensions;
	}
}
