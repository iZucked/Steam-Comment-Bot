/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.mmxlabs.models.lng.analytics.ui.views.SandboxViewCreatorService;
import com.mmxlabs.models.lng.analytics.ui.views.valuematrix.ValueMatrixViewCreatorService;

public class Activator implements BundleActivator {

	private SandboxViewCreatorService sandboxCreatorService = new SandboxViewCreatorService();
	private ValueMatrixViewCreatorService valueMatrixViewCreatorService = new ValueMatrixViewCreatorService();

	@Override
	public void start(BundleContext context) throws Exception {
		sandboxCreatorService.start();
		valueMatrixViewCreatorService.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		sandboxCreatorService.stop();
		valueMatrixViewCreatorService.stop();
	}
}
