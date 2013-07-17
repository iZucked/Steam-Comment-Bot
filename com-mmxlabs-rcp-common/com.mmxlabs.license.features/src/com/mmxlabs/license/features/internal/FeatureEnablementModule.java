/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.license.features.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;

public class FeatureEnablementModule extends AbstractModule {

	@Override
	protected void configure() {

		install(Peaberry.osgiModule(FrameworkUtil.getBundle(FeatureEnablementModule.class).getBundleContext(), EclipseRegistry.eclipseRegistry()));

		// Extension points
		bind(iterable(FeatureEnablementExtension.class)).toProvider(service(FeatureEnablementExtension.class).multiple());
	}

}
