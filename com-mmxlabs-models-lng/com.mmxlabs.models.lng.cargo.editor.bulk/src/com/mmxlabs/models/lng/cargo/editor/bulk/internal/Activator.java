/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	// The shared instance
	private static Activator plugin;
	
	public Activator() {
	}
	
	public void start(final BundleContext context) throws Exception{
		super.start(context);
		plugin = this;
	}
	
	public void stop(final BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}
	
	public static Activator getDefault() {
		return plugin;
	}
	
}
