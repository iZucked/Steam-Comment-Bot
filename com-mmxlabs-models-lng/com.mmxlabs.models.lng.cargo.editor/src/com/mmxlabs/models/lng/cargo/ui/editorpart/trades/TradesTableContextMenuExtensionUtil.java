/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.trades;

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
public class TradesTableContextMenuExtensionUtil {

	public static Iterable<ITradesTableContextMenuExtension> getContextMenuExtensions() {

		Injector injector = Guice.createInjector(new ContextMenuExtensionsModule());
		Iterable<TradesTableContextMenuExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<TradesTableContextMenuExtensionPoint>>() {
		}));
		if (extensions != null) {
			Collection<ITradesTableContextMenuExtension> result = new LinkedList<>();
			for (TradesTableContextMenuExtensionPoint ext : extensions) {
				ITradesTableContextMenuExtension instance = ext.createInstance();
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
			install(osgiModule(FrameworkUtil.getBundle(TradesTableContextMenuExtensionUtil.class).getBundleContext(), eclipseRegistry()));

			bind(iterable(TradesTableContextMenuExtensionPoint.class)).toProvider(service(TradesTableContextMenuExtensionPoint.class).multiple());
		}

	}
}
