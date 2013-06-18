/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.models.migration.internal;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.GuiceExtensionFactory;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.migration.IMigrationRegistry;

/**
 * Module used by the {@link GuiceExtensionFactory} when a class is loaded via an extension point (see plugin.xml!). Ensure class name is prefixed with
 * "org.ops4j.peaberry.eclipse.GuiceExtensionFactory:" to enable dependency injection via plugin.xml
 * 
 * @author Simon Goodall
 * 
 */
public class MigratorInjectionModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(IMigrationRegistry.class).toProvider(Peaberry.service(IMigrationRegistry.class).single());
	}

}
