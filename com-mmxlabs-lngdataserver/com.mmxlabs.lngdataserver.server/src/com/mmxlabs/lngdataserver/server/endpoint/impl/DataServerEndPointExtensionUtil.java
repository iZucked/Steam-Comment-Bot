/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.endpoint.impl;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 */
public class DataServerEndPointExtensionUtil {

	public static Object[] getEndPoints() {

		Injector injector = Guice.createInjector(new ContextMenuExtensionsModule());
		Iterable<DataServerEndPointExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<DataServerEndPointExtensionPoint>>() {
		}));
		if (extensions != null) {
			List<Class<?>> result = new LinkedList<>();
			for (DataServerEndPointExtensionPoint ext : extensions) {
				Class<?> instance = ext.getEndpointClass();
				if (instance != null) {
					result.add(instance);
				}
			}
			return result.toArray();
		}
		return null;

	}

	private static class ContextMenuExtensionsModule extends AbstractModule {

		@Override
		protected void configure() {
			install(osgiModule(FrameworkUtil.getBundle(DataServerEndPointExtensionUtil.class).getBundleContext(), eclipseRegistry()));

			bind(iterable(DataServerEndPointExtensionPoint.class)).toProvider(service(DataServerEndPointExtensionPoint.class).multiple());
		}

	}
}
