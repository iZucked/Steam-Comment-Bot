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
	public static final String PLUGIN_ID = "com.mmxlabs.lngdataserver.distances.server"; //$NON-NLS-1$
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

					// this is a bit dangerous, should probably let the server choose it's own port with server.port=0
					final int randomPort = randomPort();
					final String[] args = {
							// "--db.embeddedBinaries=" + binariesPath,
							"--server.port=" + randomPort, //
							"--db.embedded=false", //
							"--db.port=" + port, //
							"--db.diagnosticDataCollectionEnabled=false", //
							"--server.cors=true", //
							"--spring.mvc.async.request-timeout=-1", //
							"--debug" //
					};
					monitor.subTask("Gathering end points");

					final Object[] endPoints = DataServerEndPointExtensionUtil.getEndPoints();
					monitor.worked(1);
					monitor.subTask("Starting webserver");
					servletContext = SpringApplication.run(endPoints, args);
					monitor.worked(1);
					BackEndUrlProvider.INSTANCE.setPort(randomPort);
					monitor.worked(1);
					BackEndUrlProvider.INSTANCE.setAvailable(true);
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

	private int randomPort() throws IOException {
		for (int i = 65534; i > 49152; i--) {
			if (portAvailable(i)) {
				return i;
			}
		}
		LOGGER.error("No free port available 49152-65534");
		throw new IOException("No free port available 49152-65534");
	}

	private static boolean portAvailable(final int port) {
		LOGGER.info("--------------Testing port " + port);
		Socket s = null;
		try {
			s = new Socket("localhost", port);

			// If the code makes it this far without an exception it means
			// something is using the port and has responded.
			LOGGER.info("--------------Port " + port + " is not available");
			return false;
		} catch (final IOException e) {
			LOGGER.info("--------------Port " + port + " is available");
			return true;
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (final IOException e) {
					LOGGER.error("Error closing probing socket", e);
					throw new RuntimeException("Error closing probing socket", e);
				}
			}
		}
	}

	private String getMongoDataPath() throws URISyntaxException, IOException {
		final Bundle bundle = FrameworkUtil.getBundle(Activator.class);
		final String result = new File(FileLocator.toFileURL(bundle.getResource("/mongo_data")).toURI()).getAbsolutePath();
		LOGGER.info("MongoDB directory: " + result);
		return result;
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
