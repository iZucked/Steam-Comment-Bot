package com.mmxlabs.models.lng.analytics.ui.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.mmxlabs.models.lng.analytics.ui.views.SandboxViewCreatorService;

public class Activator implements BundleActivator {

	private SandboxViewCreatorService sandboxCreatorService = new SandboxViewCreatorService();

	@Override
	public void start(BundleContext context) throws Exception {
		sandboxCreatorService.start();

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		sandboxCreatorService.stop();
	}
}
