package com.mmxlabs.jobmanager.ipc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.ipc.impl.EquinoxRunner;
import com.mmxlabs.jobmanager.ipc.impl.IPCJobControlFactory;
import com.mmxlabs.jobmanager.ipc.impl.IPCJobControlFactory.IConnectionStateListener;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.impl.JobManager;
import com.mmxlabs.jobmanager.manager.impl.JobManagerDescriptor;

/**
 * Plugin activator for the IPC Job Manager; this registers the client side job manager service.
 * 
 * @author hinton
 * 
 */
@SuppressWarnings("restriction")
public class Activator implements BundleActivator {

	private final Logger log = LoggerFactory.getLogger(Activator.class);

	private JobManager localJobManager = null;
	private ServiceRegistration<IJobManager> localJobManagerService;
	private EquinoxRunner runner = new EquinoxRunner();

	private BundleContext context = null;

	/**
	 * Add all the dependencies of the given bundle into the given set, recursively.
	 * 
	 * Currently unused, instead we just add all bundles. Not so great.
	 * 
	 * @param bundle
	 * @param output
	 */
	private void collectRequiredBundles(final Bundle bundle, final Set<Bundle> output) {
		final BundleWiring wiring = bundle.adapt(BundleWiring.class);

		for (final BundleWire wire : wiring.getRequiredWires(null)) {
			final BundleWiring source = wire.getProviderWiring();
			final Bundle sourceBundle = source.getBundle();
			if (output.contains(sourceBundle))
				return;
			output.add(sourceBundle);
			collectRequiredBundles(sourceBundle, output);
		}
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		this.context = context;
		/**
		 * This is the bundle containing the runner, which we are going to send our jobs to.
		 */
		final Bundle runnerBundle = Platform.getBundle("com.mmxlabs.jobmanager.ipc.runner");

		localJobManager = new JobManager();
		final IPCJobControlFactory jobControlFactory = new IPCJobControlFactory();
		localJobManager.setJobControlFactory(jobControlFactory);
		localJobManager.setJobManagerDescriptor(new JobManagerDescriptor("IPC Job Manager"));
		jobControlFactory.addConnectionStateListener(new IConnectionStateListener() {
			@Override
			public void clientConnected() {
				log.debug("Registering IPC job manager");
				registerService();
			}

			@Override
			public void clientDisconnected() {
				unregisterService();
			}
		});

		runner.setExtraJavaArgs(Collections.singletonList("-Dcom.mmxlabs.jobmanager.ipc.runner.port=" + jobControlFactory.getLocalPort()));

		if (runnerBundle == null) {
			log.error("Cannot start IPC job manager plugin, as the reguired bundle com.mmxlabs.jobmanager.ipc.runner cannot be located in the current configuration");
		} else {
			final Thread runnerBootThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						runner.start();
						final HashSet<Bundle> requiredBundles = new HashSet<Bundle>();
						// Question : why does this method not capture the full dependency set?
						// Do we have to start the bundles or something to force full dependency loading?
						// collectRequiredBundles(runnerBundle, requiredBundles);
						requiredBundles.addAll(Arrays.asList(context.getBundles()));
						requiredBundles.remove(runnerBundle); // want to install this last
						for (final Bundle b : requiredBundles) {
							if (b.getLocation().startsWith("reference:file:")) {
								runner.installBundle(b.getLocation());
							}
						}
						runner.installBundle(runnerBundle.getLocation());
						runner.startBundle(runnerBundle.getSymbolicName());
					} catch (final IOException e) {
						log.error("Error starting equinox runner", e);
					}
				}
			});
			runnerBootThread.start();
		}
	}

	protected synchronized void unregisterService() {
		if (localJobManagerService != null) {
			localJobManagerService.unregister();
			localJobManagerService = null;
		}
	}

	protected synchronized void registerService() {
		unregisterService();
		if (context != null) {
			localJobManagerService = context.registerService(IJobManager.class, localJobManager, null);
		}
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		unregisterService();
		if (localJobManager != null) {
			localJobManager.dispose();
			localJobManager = null;
		}
		if (runner != null) {
			runner.shutdown();
			runner = null;
		}
	}
}
