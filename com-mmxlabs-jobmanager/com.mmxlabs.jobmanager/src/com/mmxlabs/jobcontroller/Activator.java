/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.jobcontroller.core.IJobManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements ServiceListener {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.jobmanager"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceTracker<IJobManager, IJobManager> jobManagerServiceTracker;

	private IJobManager jobManager;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		jobManagerServiceTracker = new ServiceTracker<IJobManager, IJobManager>(context, IJobManager.class.getName(), null);
		jobManagerServiceTracker.open();

		context.addServiceListener(this, "(objectclass=" + IJobManager.class.getName() + ")");

		jobManager = (IJobManager) jobManagerServiceTracker.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {

		// close the service tracker
		jobManagerServiceTracker.close();
		jobManagerServiceTracker = null;

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
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public IJobManager getJobManager() {
		return jobManager;
	}

	@Override
	public void serviceChanged(final ServiceEvent event) {
		jobManager = jobManagerServiceTracker.getService();
	}

	public static void warning(final String message) {

		final Status status = new Status(IStatus.WARNING, PLUGIN_ID, message);
		getDefault().getLog().log(status);
	}

	public static void warning(final String message, final Throwable t) {

		final Status status = new Status(IStatus.WARNING, PLUGIN_ID, message, t);
		getDefault().getLog().log(status);
	}

	public static void error(final String message) {

		final Status status = new Status(IStatus.ERROR, PLUGIN_ID, message);
		getDefault().getLog().log(status);
	}

	public static void error(final String message, final Throwable t) {

		final Status status = new Status(IStatus.ERROR, PLUGIN_ID, message, t);
		getDefault().getLog().log(status);
	}
}
