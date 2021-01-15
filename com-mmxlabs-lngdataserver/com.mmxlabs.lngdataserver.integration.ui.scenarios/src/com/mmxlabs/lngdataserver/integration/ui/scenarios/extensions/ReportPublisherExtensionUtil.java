/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

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

/**
 */
public class ReportPublisherExtensionUtil {

	public static Iterable<IReportPublisherExtension> getReportPublishers() {

		Injector injector = Guice.createInjector(new ContextMenuExtensionsModule());
		Iterable<ReportPublisherExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<ReportPublisherExtensionPoint>>() {
		}));
		if (extensions != null) {
			Collection<IReportPublisherExtension> result = new LinkedList<>();
			for (ReportPublisherExtensionPoint ext : extensions) {
				IReportPublisherExtension instance = ext.createInstance();
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
			install(osgiModule(FrameworkUtil.getBundle(ReportPublisherExtensionUtil.class).getBundleContext(), EclipseRegistry.eclipseRegistry()));

			bind(iterable(ReportPublisherExtensionPoint.class)).toProvider(service(ReportPublisherExtensionPoint.class).multiple());
		}

	}
}
