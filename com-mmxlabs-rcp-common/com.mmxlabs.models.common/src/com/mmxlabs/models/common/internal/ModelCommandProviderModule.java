/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.common.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.common.commandservice.IModelCommandProvider;

public class ModelCommandProviderModule extends AbstractModule {

	private final BundleContext context;

	public ModelCommandProviderModule(BundleContext context) {
		this.context = context;
	}

	@Override
	protected void configure() {

		install(Peaberry.osgiModule(context, EclipseRegistry.eclipseRegistry()));

		// Extension points
		bind(iterable(IModelCommandProviderExtension.class)).toProvider(service(IModelCommandProviderExtension.class).multiple());
		// Services
		bind(iterable(IModelCommandProvider.class)).toProvider(service(IModelCommandProvider.class).multiple());
	}

}
