package com.mmxlabs.lngdataserver.server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IProgressConstants2;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.mmxlabs.embeddedmongo.MongoProvider;
import com.mmxlabs.lngdataserver.server.endpoint.impl.DataServerEndPointExtensionUtil;

public class Activator implements BundleActivator {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.server"; //$NON-NLS-1$
	// The shared instance
	private static Activator plugin;

	private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

	private ConfigurableApplicationContext servletContext;

	private MongoDBService mongoService;

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
		plugin = this;

		final Job background = new Job("Start data server") {
			@Override
			public IStatus run(final IProgressMonitor monitor) {
				// Having a method called "main" in the stacktrace stops SpringBoot throwing an exception in the logging framework
				return main(monitor);
			}

			public IStatus main(final IProgressMonitor monitor) {
				monitor.beginTask("Starting data server", IProgressMonitor.UNKNOWN);
				try {
					monitor.subTask("Starting database");
					final String binariesPath = new MongoProvider().getStringPath();

					mongoService = new MongoDBService();
					mongoService.setEmbeddedBinariesLocation(binariesPath);

					final int port = mongoService.start();
					monitor.subTask("Starting internal webserver");

					final String[] args = {
							// "--db.embeddedBinaries=" + binariesPath,
							"--server.port=0", //
							"--db.embedded=false", //
							"--db.port=" + port, //
							"--db.diagnosticDataCollectionEnabled=false", //
							"--server.cors=true", //
							"--spring.mvc.async.request-timeout=-1", //
							// "--debug" //
					};
					monitor.subTask("Gathering end points");

					// Grab plugin contributed endpoints
					final Object[] endPoints = DataServerEndPointExtensionUtil.getEndPoints();
					// Extend endpoints array and add in out port discovery hook
					Object[] fullEndPoints = new Object[endPoints.length + 1];
					System.arraycopy(endPoints, 0, fullEndPoints, 0, endPoints.length);
					fullEndPoints[endPoints.length] = EmbdeddedWebServerPortDiscoveryProvider.class;
					monitor.worked(1);
					monitor.subTask("Starting webserver");
					servletContext = SpringApplication.run(fullEndPoints, args);

					monitor.subTask("Waiting....");
					while (!BackEndUrlProvider.INSTANCE.isAvailable()) {
						Thread.sleep(500);
					}
					monitor.worked(1);
				} catch (final Exception e) {
					setProperty(IProgressConstants2.KEEP_PROPERTY, Boolean.TRUE);
					return Status.CANCEL_STATUS;
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		// IRunnableWithProgress p = null;
		background.setSystem(false);
		background.setUser(true);
		background.setPriority(Job.LONG);
		// background.setProperty(IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY, true);
		// IProgressService service = PlatformUI.getWorkbench().getProgressService();
		// service.run(true, false, background);
		//

		background.schedule();
		System.out.println("starting in background...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}
