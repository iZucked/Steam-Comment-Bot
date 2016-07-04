/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.ops4j.peaberry.Import;
import org.ops4j.peaberry.util.AbstractWatcher;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;

public class ParameterModesExtensionModule extends AbstractModule {

	private final class ParameterModeExtensionWatcher extends AbstractWatcher<ParameterModeExtension> {

		@Inject
		private ParameterModesRegistry registry;

		@Override
		protected ParameterModeExtension adding(final Import<ParameterModeExtension> service) {
			// the returned object is used in the modified and removed calls
			final ParameterModeExtension instance = service.get();
			if (instance != null) {
				registry.registerParameterMode(instance);
			}
			return instance;
		}

		@Override
		protected void modified(final ParameterModeExtension instance, final Map<String, ?> attributes) {
		}

		@Override
		protected void removed(final ParameterModeExtension instance) {
			// FIXME: Implement
		}
	}

	private final class ParameterModeExtenderExtensionWatcher extends AbstractWatcher<ParameterModeExtenderExtension> {
		@Inject
		private ParameterModesRegistry registry;

		@Override
		protected ParameterModeExtenderExtension adding(final Import<ParameterModeExtenderExtension> service) {
			// the returned object is used in the modified and removed calls
			final ParameterModeExtenderExtension instance = service.get();
			if (instance != null) {
				registry.registerExtender(instance);
			}
			return instance;
		}

		@Override
		protected void modified(final ParameterModeExtenderExtension instance, final Map<String, ?> attributes) {
		}

		@Override
		protected void removed(final ParameterModeExtenderExtension instance) {
			// FIXME: Implement
		}
	}

	@Override
	protected void configure() {

		// Extension points
		// bind(iterable(ParameterModeExtension.class)).toProvider(service(ParameterModeExtension.class).multiple());
		// bind(iterable(ParameterModeExtenderExtension.class)).toProvider(service(ParameterModeExtenderExtension.class).multiple());

		// Bind the extension points and register a watcher to pick up any further changes (e.g. from a plugin.xml enablement)
		bind(iterable(ParameterModeExtenderExtension.class)).toProvider(service(ParameterModeExtenderExtension.class).out(new ParameterModeExtenderExtensionWatcher()).multiple());
		bind(iterable(ParameterModeExtension.class)).toProvider(service(ParameterModeExtension.class).out(new ParameterModeExtensionWatcher()).multiple());

		// Bind implementation as a singleton to our service interface
		bind(ParameterModesRegistry.class).in(Singleton.class);
		bind(TypeLiterals.export(IParameterModesRegistry.class)).toProvider(service(ParameterModesRegistry.class).export());
	}

}
