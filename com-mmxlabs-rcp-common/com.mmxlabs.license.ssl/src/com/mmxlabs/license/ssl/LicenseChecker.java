/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.license.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;

import com.mmxlabs.license.ssl.internal.Activator;

/**
 * A simple class to load a license - a SSL certificate from a disk location and verify it has been signed by the "root" key in the embedded keystore and that it is still in date.
 * 
 * @author Simon Goodall
 */
public final class LicenseChecker {

	// Hardcoded keystore password - only storing public key so not really an issue - although tampering may be an issue
	private static final char[] password = new char[] { '1', '2', '3', '4', '5', '6' };

	public static boolean checkLicense() {

		try {
			// Load keystore
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			// TODO: Load from bundle resource
			final URL keyStoreUrl = Activator.getDefault().getBundle().getResource("keystore.jks");
			keyStore.load(keyStoreUrl.openStream(), password);

			final Certificate rootCertificate = keyStore.getCertificate("root");
			if (rootCertificate == null) {
				return false;
			}

			// Load the license file
			Certificate licenseCertificate = null;
			{
				licenseCertificate = getEclipseHomeLicense();
				if (licenseCertificate == null) {
					licenseCertificate = getUserDataLicense();
				}
			}

			if (licenseCertificate == null) {
				return false;
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

	private static Certificate getUserDataLicense() {
		FileInputStream inStream = null;
		try {
			final Certificate licenseCertificate;

			final String userHome = System.getProperty("user.home");
			if (userHome != null) {
				final File f = new File(userHome + "/mmxlabs/license.pem");
				final CertificateFactory cf = CertificateFactory.getInstance("X.509");
				inStream = new FileInputStream(f);
				licenseCertificate = cf.generateCertificate(inStream);
				return licenseCertificate;
			}
		} catch (final Exception e) {
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (final IOException e) {
					// Ignore
				}
			}
		}
		return null;
	}

	private static Certificate getEclipseHomeLicense() throws CertificateException, FileNotFoundException {

		InputStream inStream = null;
		try {
			final Certificate licenseCertificate;
			final String userHome = System.getProperty("eclipse.home.location");
			if (userHome != null) {
				final URL url = new URL(userHome + "/license.pem");
				final CertificateFactory cf = CertificateFactory.getInstance("X.509");
				inStream = url.openStream();
				licenseCertificate = cf.generateCertificate(inStream);
				return licenseCertificate;
			}
		} catch (final Exception e) {
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (final IOException e) {
					// Ignore
				}
			}
		}
		return null;

	}
}
