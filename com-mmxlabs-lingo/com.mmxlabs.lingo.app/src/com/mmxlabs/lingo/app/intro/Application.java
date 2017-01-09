/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.intro;

import java.io.IOException;

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
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}
}
