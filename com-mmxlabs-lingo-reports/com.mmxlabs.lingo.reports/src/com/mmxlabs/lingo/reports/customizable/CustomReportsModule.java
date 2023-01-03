/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;

public class CustomReportsModule extends AbstractModule {

	@Override
	protected void configure() {

		final Bundle bundle = FrameworkUtil.getBundle(CustomReportsModule.class);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			if (bundleContext != null) {
				install(Peaberry.osgiModule(bundleContext, EclipseRegistry.eclipseRegistry()));

				bind(IExtensionRegistry.class).toProvider(Peaberry.service(IExtensionRegistry.class).single().direct());
			}
		}
	}

}
