package com.mmxlabs.lngdataserver.browser.ui;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends Plugin{
	
private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
	
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.browser.ui"; //$NON-NLS-1$
	// The shared instance
	private static Activator plugin;
	
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
}
