/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.app.intro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.shiplingo.platform.app.DelayedOpenFileProcessor;

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
//		if (!checkLicense()) {
//
//			MessageDialog.openError(display.getActiveShell(), "License Error", "Unable to validate license");
//
//			display.dispose();
//			return IApplication.EXIT_OK;
//		}

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

	private boolean checkLicense() {

		try {
			// Load keystore
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			// Hardcoded keystore password - only storing public key so not really an issue
			final char[] password = new char[] { '1', '2', '3', '4', '5', '6' };
			// TODO: Load from bundle resource
			keyStore.load(new FileInputStream("C:/temp/certs/dev.jks"), password);

			final Certificate rootCertificate = keyStore.getCertificate("root");
			if (rootCertificate == null) {
				return false;
			}

			// Load the license file
			final Certificate licenseCertificate;
			{
				final File file = Platform.getLocation().append("license.pem").toFile();
				final CertificateFactory cf = CertificateFactory.getInstance("X.509");
				licenseCertificate = cf.generateCertificate(new FileInputStream(file));
			}

			// Verify self-signed certificate
			rootCertificate.verify(rootCertificate.getPublicKey());
			// Verify license is signed by the root
			licenseCertificate.verify(rootCertificate.getPublicKey());

			// Check dates are valid. We expect a X509 certificate
			if (licenseCertificate instanceof X509Certificate) {
				final X509Certificate x509Certificate = (X509Certificate) licenseCertificate;
				x509Certificate.checkValidity();
				return true;
			}

			return false;
		} catch (final Exception e) {
			return false;
		}
	}
}
