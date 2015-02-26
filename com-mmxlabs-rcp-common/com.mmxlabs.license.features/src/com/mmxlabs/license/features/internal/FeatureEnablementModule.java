/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.license.features.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;

public class FeatureEnablementModule extends AbstractModule {

	@Override
	protected void configure() {

		install(Peaberry.osgiModule(FrameworkUtil.getBundle(FeatureEnablementModule.class).getBundleContext(), EclipseRegistry.eclipseRegistry()));

		bind(IExtensionRegistry.class).toProvider(Peaberry.service(IExtensionRegistry.class).single().direct());
		
		// Extension points
		bind(iterable(FeatureEnablementExtension.class)).toProvider(service(FeatureEnablementExtension.class).multiple());
		bind(iterable(PluginXMLEnablementExtension.class)).toProvider(service(PluginXMLEnablementExtension.class).multiple());
	}

}
