/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.alternatives;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.ops4j.peaberry.util.TypeLiterals;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;

public class TradesTableEditorProviderModule extends AbstractModule {

	@Override
	protected void configure() {

		install(Peaberry.osgiModule(FrameworkUtil.getBundle(TradesTableEditorProviderModule.class).getBundleContext(), EclipseRegistry.eclipseRegistry()));

		bind(IExtensionRegistry.class).toProvider(Peaberry.service(IExtensionRegistry.class).single().direct());

		// Extension points
		bind(TypeLiterals.iterable(TradesTableEditorExtension.class)).toProvider(Peaberry.service(TradesTableEditorExtension.class).multiple());
	}

}