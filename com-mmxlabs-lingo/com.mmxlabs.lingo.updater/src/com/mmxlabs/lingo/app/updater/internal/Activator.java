/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.updater.internal;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lingo.updater"; //$NON-NLS-1$

	private LiNGOUpdaterConsoleCommand cc = new LiNGOUpdaterConsoleCommand();

	private ServiceRegistration<CommandProvider> ccService;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		ccService = context.registerService(CommandProvider.class, cc, null);

	}


	@Override
	public void stop(final BundleContext context) throws Exception {

		if (ccService != null) {
			ccService.unregister();
			ccService = null;
		}
		super.stop(context);
	}

}
