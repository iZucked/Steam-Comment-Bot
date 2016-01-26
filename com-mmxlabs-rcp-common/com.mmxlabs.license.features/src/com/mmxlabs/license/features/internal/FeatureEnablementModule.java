/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.license.features.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;

public class FeatureEnablementModule extends AbstractModule {

	@Override
	protected void configure() {

		final Bundle bundle = FrameworkUtil.getBundle(FeatureEnablementModule.class);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			if (bundleContext != null) {
				install(Peaberry.osgiModule(bundleContext, EclipseRegistry.eclipseRegistry()));

				bind(IExtensionRegistry.class).toProvider(Peaberry.service(IExtensionRegistry.class).single().direct());

				// Extension points
				bind(iterable(FeatureEnablementExtension.class)).toProvider(service(FeatureEnablementExtension.class).multiple());
				bind(iterable(PluginXMLEnablementExtension.class)).toProvider(service(PluginXMLEnablementExtension.class).multiple());
			}
		}
	}

}
