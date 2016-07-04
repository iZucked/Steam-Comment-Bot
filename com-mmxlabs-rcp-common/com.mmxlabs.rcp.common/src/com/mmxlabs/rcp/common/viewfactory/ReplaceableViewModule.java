/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.viewfactory;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;

public class ReplaceableViewModule extends AbstractModule {

	@Override
	protected void configure() {

		install(Peaberry.osgiModule(FrameworkUtil.getBundle(ReplaceableViewModule.class).getBundleContext(), EclipseRegistry.eclipseRegistry()));

		bind(IExtensionRegistry.class).toProvider(Peaberry.service(IExtensionRegistry.class).single().direct());
		
		// Extension points
		bind(iterable(ReplaceableViewExtension.class)).toProvider(service(ReplaceableViewExtension.class).multiple());
		bind(iterable(ReplaceableViewFactoryExtension.class)).toProvider(service(ReplaceableViewFactoryExtension.class).multiple());
	}

}
