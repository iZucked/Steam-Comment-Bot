/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.intro;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.internal.p2.garbagecollector.GarbageCollector;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.features.pluginxml.PluginRegistryHook;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseChecker.LicenseState;
import com.mmxlabs.rcp.common.application.DelayedOpenFileProcessor;
import com.mmxlabs.rcp.common.application.WorkbenchStateManager;
import com.mmxlabs.rcp.common.viewfactory.ReplaceableViewManager;

/**
 * This class controls all aspects of the application's execution
 */
@SuppressWarnings("restriction")
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(final IApplicationContext context) {
		final String[] appLineArgs = Platform.getApplicationArgs();
		System.out.println(Arrays.toString(appLineArgs));
		if (appLineArgs != null && appLineArgs.length > 0) {
			// Look for the no-auto-mem command first and skip auto-mem code if so (e.g. could get here through a relaunch)
			boolean skipAutoMemory = false;
			for (final String arg : appLineArgs) {
				if (arg.equalsIgnoreCase(CMD_NO_AUTO_MEM)) {
					skipAutoMemory = true;
					break;
				}
			}
			if (!skipAutoMemory) {
				for (final String arg : appLineArgs) {
					if (arg.equals("-auto-mem")) {

						final MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
						try {
							// Get total physical memory
							final Number totalMemory = (Number) platformMBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"), "TotalPhysicalMemorySize");
							if (totalMemory != null) {
								// Convert to gigabytes
								final long gigabytes = totalMemory.longValue() / 1024L / 1024L / 1024L;
								String heapSize = null;
								if (gigabytes < 1) {
									heapSize = "-Xmx512m";
								} else if (gigabytes < 3) {
									heapSize = "-Xmx1G";
								} else {
									heapSize = String.format("-Xmx%dG", gigabytes - 2);
								}
								if (heapSize != null) {
									// Construct new command line with new VM arg and restart workbench
									System.setProperty(IApplicationContext.EXIT_DATA_PROPERTY, buildCommandLine(heapSize));
									// This might be surplus to the return statement.
									System.setProperty("eclipse.exitcode", Integer.toString(24));
									return org.eclipse.equinox.app.IApplication.EXIT_RELAUNCH;
								}
							}
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		}

		final Display display = PlatformUI.createDisplay();

		WorkbenchStateManager.cleanupWorkbenchState();

		final LicenseState validity = LicenseChecker.checkLicense();
		if (validity != LicenseState.Valid) {

			MessageDialog.openError(display.getActiveShell(), "License Error", "Unable to validate license: " + validity.getMessage());

			display.dispose();
			return IApplication.EXIT_OK;
		}

		initAccessControl();

		cleanupP2();

		final DelayedOpenFileProcessor processor = new DelayedOpenFileProcessor(display);

		// TODO: Handle error conditions better!

		final Location instanceLoc = Platform.getInstanceLocation();
		try {

			if (instanceLoc == null) {
				// Need a workspace
				return IApplication.EXIT_OK;
			}

			if (instanceLoc.isSet()) {

				// Attempt to lock workspace
				if (instanceLoc.lock()) {

					final int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor(processor));
					if (returnCode == PlatformUI.RETURN_RESTART) {
						return IApplication.EXIT_RESTART;
					}
				} else {
					// Tell user about locked
				}
			}

		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (display != null) {
				display.dispose();
			}
			if (instanceLoc != null) {
				instanceLoc.release();
			}
		}
		return IApplication.EXIT_OK;
	}

	/**
	 */
	private void cleanupP2() {
		final BundleContext context = FrameworkUtil.getBundle(Application.class).getBundleContext();
		final ServiceReference<IProvisioningAgentProvider> providerRef = context.getServiceReference(IProvisioningAgentProvider.class);
		if (providerRef == null) {
			throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
		}
		try {
			final IProvisioningAgentProvider provider = context.getService(providerRef);
			if (provider == null) {
				throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
			}

			// See http://wiki.eclipse.org/Equinox/p2/FAQ#But_why_aren.27t_uninstalled_bundles.2Ffeatures_immediately_removed.3F
			// IProvisioningAgentProvider provider = // obtain the IProvisioningAgentProvider using OSGi services
			final IProvisioningAgent agent = provider.createAgent(null); // null = location for running system
			if (agent == null)
				throw new RuntimeException("Location was not provisioned by p2");
			final IProfileRegistry profileRegistry = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
			if (profileRegistry == null)
				throw new RuntimeException("Unable to acquire the profile registry service.");
			// can also use IProfileRegistry.SELF for the current profile
			final IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
			// Profile can be null if we have not enabled p2 support in the runtime config in eclipse. Generated product should be fine.
			if (profile != null) {
				final GarbageCollector gc = (GarbageCollector) agent.getService(GarbageCollector.SERVICE_NAME);
				gc.runGC(profile);
			}
		} catch (final ProvisionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// When you're done, make sure you 'unget' the service.
			context.ungetService(providerRef);
		}
		// return result;
	}

	private void initAccessControl() {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements();

		// Login our default user
		final Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken("user", "password"));

		PluginRegistryHook.initialisePluginXMLEnablements();

		ReplaceableViewManager.initialiseReplaceableViews();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(() -> {
			if (!display.isDisposed()) {
				workbench.close();
			}
		});
	}
	//////////////////////

	private static final String PROP_VM = "eclipse.vm";

	private static final String PROP_VMARGS = "eclipse.vmargs";

	private static final String PROP_COMMANDS = "eclipse.commands";

	private static final String CMD_VMARGS = "-vmargs";

	private static final String CMD_AUTO_MEM = "-auto-mem";
	private static final String CMD_NO_AUTO_MEM = "-no-auto-mem";

	private static final String NEW_LINE = "\n";

	/**
	 * Reconstruct command line arguments and modify to suit
	 * 
	 * Taken from org.eclipse.ui.internal.ide.actions.OpenWorkspaceAction. This required EXIT_RELAUNCH - not EXIT_RESTART to work. Note only works in builds, not from within eclipse.
	 * 
	 * 
	 */
	private String buildCommandLine(final String memory) {
		final StringBuffer result = new StringBuffer(512);

		String property = System.getProperty(PROP_VM);
		if (property != null) {
			result.append(property);
			result.append(NEW_LINE);
		}

		// append the vmargs and commands. Assume that these already end in \n
		// Note: We need to do this twice for some reason. This is the first time
		final String vmargs = System.getProperty(PROP_VMARGS);
		if (vmargs != null) {
			// Strip any existing mem params
			result.append(vmargs.replaceAll("-Xmx[0-9*][a-zA-Z]", ""));
		}

		// append the rest of the args, replacing or adding -no-auto-mem / -auto-mem as required
		property = System.getProperty(PROP_COMMANDS);
		if (property == null) {

		} else {
			// Strip any existing mem params
			property = property.replaceAll("-Xmx[0-9*][a-zA-Z]", "");
			// Remove the auto-mem command
			property = property.replaceAll(CMD_AUTO_MEM, "");

			result.append(property);
		}
		result.append(NEW_LINE);
		result.append(CMD_NO_AUTO_MEM);
		result.append(NEW_LINE);

		// put the vmargs back at the very end (the eclipse.commands property
		// already contains the -vm arg)
		if (result.charAt(result.length() - 1) != '\n') {
			result.append('\n');
		}
		result.append(CMD_VMARGS);
		result.append(NEW_LINE);
		if (vmargs != null) {
			// Note: We need to do this twice for some reason. This is the second time
			// Strip any existing mem params
			result.append(vmargs.replaceAll("-Xmx[0-9*][a-zA-Z]", ""));
		}

		result.append(memory);
		result.append(NEW_LINE);

		@NonNull
		String cmdLine = result.toString();

		// Strip duplicate newlines
		cmdLine = cmdLine.replaceAll("\n\n", "\n");

		return cmdLine;
	}
}
