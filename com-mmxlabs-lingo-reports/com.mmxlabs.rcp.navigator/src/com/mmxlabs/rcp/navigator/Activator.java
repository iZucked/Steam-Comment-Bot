/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.rcp.navigator;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.jobcontroller.core.IJobManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements ServiceListener {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.rcp.navigator"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceTracker jobManagerServiceTracker;
	private BundleContext fContext;

	private IJobManager jobManager;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		fContext = context;

		jobManagerServiceTracker = new ServiceTracker(context,
				IJobManager.class.getName(), null);
		jobManagerServiceTracker.open();

		context.addServiceListener(this,
				"(objectclass=" + IJobManager.class.getName() + ")");

		jobManager = (IJobManager) jobManagerServiceTracker.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

		// close the service tracker
		jobManagerServiceTracker.close();
		jobManagerServiceTracker = null;

		fContext = null;

		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public IJobManager getJobManager() {
		return jobManager;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		ServiceReference sr = event.getServiceReference();
		switch (event.getType()) {
		case ServiceEvent.REGISTERED: {
			IJobManager jobManager = (IJobManager) fContext.getService(sr);
			this.jobManager = jobManager;
		}
			break;
		case ServiceEvent.UNREGISTERING: {
			// IJobManager manager = (IJobManager) fContext.getService(sr);
			this.jobManager = null;
		}
			break;
		}

	}}
