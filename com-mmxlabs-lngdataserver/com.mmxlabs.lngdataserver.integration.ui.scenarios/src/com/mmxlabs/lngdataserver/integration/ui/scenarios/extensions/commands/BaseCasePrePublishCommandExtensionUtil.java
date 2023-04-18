/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.commands;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.Collection;
import java.util.LinkedList;

import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class BaseCasePrePublishCommandExtensionUtil {

	public static Iterable<IBaseCasePrePublishCommandExtension> getCommandExtensions() {

		Injector injector = Guice.createInjector(new ContextMenuExtensionsModule());
		Iterable<BaseCasePrePublishCommandExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<BaseCasePrePublishCommandExtensionPoint>>() {
		}));
		if (extensions != null) {
			Collection<IBaseCasePrePublishCommandExtension> result = new LinkedList<>();
			for (BaseCasePrePublishCommandExtensionPoint ext : extensions) {
				IBaseCasePrePublishCommandExtension instance = ext.createInstance();
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
			install(osgiModule(FrameworkUtil.getBundle(BaseCasePrePublishCommandExtensionUtil.class).getBundleContext(), EclipseRegistry.eclipseRegistry()));

			bind(iterable(BaseCasePrePublishCommandExtensionPoint.class)).toProvider(service(BaseCasePrePublishCommandExtensionPoint.class).multiple());
		}

	}
}
