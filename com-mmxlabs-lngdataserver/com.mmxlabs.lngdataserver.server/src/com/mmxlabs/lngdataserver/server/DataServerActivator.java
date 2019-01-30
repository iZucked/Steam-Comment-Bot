/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.mmxlabs.rcp.common.user.IUserNameProvider;

public class DataServerActivator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.server"; //$NON-NLS-1$

	private static DataServerActivator plugin;

	private ServiceRegistration<@NonNull IUserNameProvider> usernameServiceRegistration;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// Trigger class load and property change hooks
		UpstreamUrlProvider.INSTANCE.isAvailable();

		usernameServiceRegistration = context.registerService(IUserNameProvider.class, UpstreamUrlProvider.INSTANCE, null);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		usernameServiceRegistration.unregister();

		plugin = null;
		super.stop(context);
	}

	public static DataServerActivator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
