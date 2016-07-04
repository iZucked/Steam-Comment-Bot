/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Extended implementation of {@link AbstractUIPlugin} added API to access the {@link LogService}
 * 
 * @author Simon Goodall
 */
public class LoggingActivator extends AbstractUIPlugin {

	private ServiceTracker<ExtendedLogService, ExtendedLogService> logServiceTracker;
	private BundleContext context;

	@Override
	public void start(final BundleContext context) throws Exception {

		super.start(context);
		this.context = context;

		// create a tracker and track the log service
		logServiceTracker = new ServiceTracker<ExtendedLogService, ExtendedLogService>(context, ExtendedLogService.class.getName(), null);
		logServiceTracker.open();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

		// close the service tracker
		logServiceTracker.close();
		logServiceTracker = null;

		this.context = null;

		super.stop(context);
	}

	/**
	 * This method invokes {@link ExtendedLogService#log(Object, int, String, Throwable)}. This will fail silently if there is no {@link ExtendedLogService} present.
	 * 
	 * @param logLevel
	 *            One of {@link LogService#LOG_INFO}, {@link LogService#LOG_WARNING}, {@link LogService#LOG_ERROR}, or s{@link LogService#LOG_DEBUG}
	 * @param message
	 * @param exception
	 */
	public void log(final int logLevel, final String message, final Throwable exception) {
		// grab the service
		final ExtendedLogService logService = logServiceTracker.getService();
		if (logService != null) {
			logService.log(context, logLevel, message, exception);
		}
	}

	/**
	 * Log an error message. Calls {@link #log(int, String, Throwable)} with {@link LogService#LOG_ERROR}
	 * 
	 * @param message
	 * @param exception
	 */
	public void logError(final String message, final Throwable exception) {
		this.log(LogService.LOG_ERROR, message, exception);
	}

	/**
	 * Log a warning message. Calls {@link #log(int, String, Throwable)} with {@link LogService#LOG_WARNING}
	 * 
	 * @param message
	 * @param exception
	 */
	public void logWarning(final String message, final Throwable exception) {
		this.log(LogService.LOG_WARNING, message, exception);
	}

	/**
	 * Log an information message. Calls {@link #log(int, String, Throwable)} with {@link LogService#LOG_INFO}
	 * 
	 * @param message
	 * @param exception
	 */
	public void logInfo(final String message, final Throwable exception) {
		this.log(LogService.LOG_INFO, message, exception);
	}

	/**
	 * Log a debug message. Calls {@link #log(int, String, Throwable)} with {@link LogService#LOG_DEBUG}
	 * 
	 * @param message
	 * @param exception
	 */
	public void logDebug(final String message, final Throwable exception) {
		this.log(LogService.LOG_DEBUG, message, exception);
	}
}
