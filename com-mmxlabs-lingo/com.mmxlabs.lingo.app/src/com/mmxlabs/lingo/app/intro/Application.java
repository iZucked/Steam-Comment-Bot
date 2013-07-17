/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseChecker.LicenseState;
import com.mmxlabs.rcp.common.application.DelayedOpenFileProcessor;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(final IApplicationContext context) {

		final Display display = PlatformUI.createDisplay();
		LicenseState validity = LicenseChecker.checkLicense();
		if (validity != LicenseState.Valid) {

			MessageDialog.openError(display.getActiveShell(), "License Error", "Unable to validate license " + validity.name());

			display.dispose();
			return IApplication.EXIT_OK;
		}

		initAccessControl();

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

	private void initAccessControl() {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements();

		// Login our default user
		final Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken("user", "password"));
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
