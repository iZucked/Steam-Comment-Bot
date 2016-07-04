/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.application;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;

public class ViewPartRewriteModule extends AbstractModule {

	@Override
	protected void configure() {

		install(Peaberry.osgiModule(FrameworkUtil.getBundle(ViewPartRewriteModule.class).getBundleContext(), EclipseRegistry.eclipseRegistry()));

		// Extension points
		bind(iterable(ViewPartRewriteExtension.class)).toProvider(service(ViewPartRewriteExtension.class).multiple());
	}

}
