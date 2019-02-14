/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.headline.extensions;

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
public class HeadlineValueExtenderExtensionUtil {

	public static Iterable<IHeadlineValueExtender> getColumnExtendeders() {

		Injector injector = Guice.createInjector(new ExtensionsModule());
		Iterable<HeadlineValueExtenderExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<HeadlineValueExtenderExtensionPoint>>() {
		}));
		if (extensions != null) {
			Collection<IHeadlineValueExtender> result = new LinkedList<>();
			for (HeadlineValueExtenderExtensionPoint ext : extensions) {
				IHeadlineValueExtender instance = ext.getExtender();
				if (instance != null) {
					result.add(instance);
				}
			}
			return result;
		}
		return null;

	}

	private static class ExtensionsModule extends AbstractModule {

		@Override
		protected void configure() {
			install(osgiModule(FrameworkUtil.getBundle(HeadlineValueExtenderExtensionUtil.class).getBundleContext(), eclipseRegistry()));

			bind(iterable(HeadlineValueExtenderExtensionPoint.class)).toProvider(service(HeadlineValueExtenderExtensionPoint.class).multiple());
		}

	}
}
