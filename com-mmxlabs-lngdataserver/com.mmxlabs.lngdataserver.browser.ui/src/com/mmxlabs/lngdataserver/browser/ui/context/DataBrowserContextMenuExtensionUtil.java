/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui.context;

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
public class DataBrowserContextMenuExtensionUtil {

	public static Iterable<IDataBrowserContextMenuExtension> getContextMenuExtensions() {

		Injector injector = Guice.createInjector(new ContextMenuExtensionsModule());
		Iterable<DataBrowserContextMenuExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<DataBrowserContextMenuExtensionPoint>>() {
		}));
		if (extensions != null) {
			Collection<IDataBrowserContextMenuExtension> result = new LinkedList<>();
			for (DataBrowserContextMenuExtensionPoint ext : extensions) {
				IDataBrowserContextMenuExtension instance = ext.createInstance();
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
			install(osgiModule(FrameworkUtil.getBundle(DataBrowserContextMenuExtensionUtil.class).getBundleContext(), eclipseRegistry()));

			bind(iterable(DataBrowserContextMenuExtensionPoint.class)).toProvider(service(DataBrowserContextMenuExtensionPoint.class).multiple());
		}

	}
}
