/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LngUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.models.lng.ui"; //$NON-NLS-1$

	// The shared instance
	private static LngUIActivator plugin;

	/**
	 * The constructor
	 */
	public LngUIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);

		final ImageDescriptor duplicateImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/fastview_restore.gif");
		getImageRegistry().put(ImageConstants.IMAGE_DUPLICATE, duplicateImageDescriptor);
		getImageRegistry().put(ImageConstants.IMAGE_DUPLICATE_DISABLED, ImageDescriptor.createWithFlags(duplicateImageDescriptor, SWT.IMAGE_DISABLE));

		final ImageDescriptor importImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif");
		getImageRegistry().put(ImageConstants.IMAGE_IMPORT, importImageDescriptor);
		getImageRegistry().put(ImageConstants.IMAGE_IMPORT_DISABLED, ImageDescriptor.createWithFlags(importImageDescriptor, SWT.IMAGE_DISABLE));

		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;

		getImageRegistry().dispose();

		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static LngUIActivator getDefault() {
		return plugin;
	}

}
