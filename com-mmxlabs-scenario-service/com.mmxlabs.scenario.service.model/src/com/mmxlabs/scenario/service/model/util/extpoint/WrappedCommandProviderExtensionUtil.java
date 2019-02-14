/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.extpoint;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.Collection;
import java.util.LinkedList;

import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 */
public class WrappedCommandProviderExtensionUtil {

	public static Iterable<IWrappedCommandProvider> getExtensions() {

		Injector injector = Guice.createInjector(new ContextMenuExtensionsModule());
		Iterable<WrappedCommandProviderExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<WrappedCommandProviderExtensionPoint>>() {
		}));
		if (extensions != null) {
			Collection<IWrappedCommandProvider> result = new LinkedList<>();
			for (WrappedCommandProviderExtensionPoint ext : extensions) {
				IWrappedCommandProvider instance = ext.createInstance();
				if (instance != null) {
					result.add(instance);
				}
			}
			return result;
		}
		return null;

	}

	private static class ContextMenuExtensionsModule extends AbstractModule {

		@Override
		protected void configure() {
			install(osgiModule(FrameworkUtil.getBundle(WrappedCommandProviderExtensionUtil.class).getBundleContext(), eclipseRegistry()));

			bind(iterable(WrappedCommandProviderExtensionPoint.class)).toProvider(service(WrappedCommandProviderExtensionPoint.class).multiple());
		}

	}
}
