/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;

public class ParameterModesExtensionModule extends AbstractModule {

	@Override
	protected void configure() {

		// Extension points
		bind(iterable(ParameterModeExtension.class)).toProvider(service(ParameterModeExtension.class).multiple());
		bind(iterable(ParameterModeExtenderExtension.class)).toProvider(service(ParameterModeExtenderExtension.class).multiple());

		// Bind implementation as a singleton to our service interface
		bind(TypeLiterals.export(IParameterModesRegistry.class)).toProvider(service(ParameterModesRegistry.class).export());
		bind(ParameterModesRegistry.class).in(Singleton.class);
	}

}
