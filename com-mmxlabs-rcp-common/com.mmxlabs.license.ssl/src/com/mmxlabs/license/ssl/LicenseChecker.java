/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.license.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.license.ssl.internal.Activator;

/**
 * A simple class to load a license - a SSL certificate from a disk location and verify it has been signed by the "root" key in the embedded keystore and that it is still in date.
 * 
 * @author Simon Goodall
 */
public final class LicenseChecker {

	private static final Logger log = LoggerFactory.getLogger(LicenseChecker.class);
	
	/**
	 * @since 4.0
	 */
	public static enum LicenseState {
		Valid, Expired, Unknown, NotYetValid
	}

	// Hardcoded keystore password - only storing public key so not really an issue - although tampering may be an issue
	private static final String password = "Lok3pDTS";

	// private static final char[] password = new char[] { '1', '2', '3', '4', '5', '6' };

	/**
	 * @since 4.0
	 */
	public static LicenseState checkLicense() {

		try {
			// Load keystore
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			// TODO: Load from bundle resource
			final URL keyStoreUrl = Activator.getDefault().getBundle().getResource("keystore.jks");
			final InputStream astream = keyStoreUrl.openStream();
			try {
				keyStore.load(astream, password.toCharArray());
			} finally {
				astream.close();
			}

			final Certificate rootCertificate = keyStore.getCertificate("rootca");
			if (rootCertificate == null) {
				return LicenseState.Unknown;
			}

			// Load the license file
			KeyStore licenseKeystore = null;
			{
				licenseKeystore = getEclipseHomeLicense();
				if (licenseKeystore == null) {
					licenseKeystore = getUserDataLicense();
				}
			}

			if (licenseKeystore == null) {
				return LicenseState.Unknown;
			}

			// Hardcoded alias name in the keystore as part of generation process
			final Certificate licenseCertificate = licenseKeystore.getCertificate("1");

			// Verify self-signed certificate
			rootCertificate.verify(rootCertificate.getPublicKey());

			// Verify license is signed by the server
			licenseCertificate.verify(rootCertificate.getPublicKey());

			// Check dates are valid. We expect a X509 certificate
			if (licenseCertificate instanceof X509Certificate) {
				final X509Certificate x509Certificate = (X509Certificate) licenseCertificate;
				x509Certificate.checkValidity();

				// Create copies of the keystores in a known place on filesystem so we can reference them
				final File keyStoreFile = Activator.getDefault().getBundle().getDataFile("local-keystore.jks");
				final File trustStoreFile = Activator.getDefault().getBundle().getDataFile("local-truststore.jks");
				FileOutputStream stream = null;
				try {
					stream = new FileOutputStream(keyStoreFile);
					licenseKeystore.store(stream, password.toCharArray());
					stream.close();

					stream = new FileOutputStream(trustStoreFile);
					keyStore.store(stream, password.toCharArray());
					System.setProperty("javax.net.ssl.keyStore", keyStoreFile.toString());
					System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
					System.setProperty("javax.net.ssl.keyStorePassword", password);

					System.setProperty("javax.net.ssl.trustStore", trustStoreFile.toString());
					System.setProperty("javax.net.ssl.trustStorePassword", password);

					return LicenseState.Valid;
				} finally {
					if (stream != null) {
						stream.close();
					}
				}
			}

			return LicenseState.Unknown;
		} catch (final CertificateExpiredException e) {
			return LicenseState.Expired;
		} catch (final CertificateNotYetValidException e) {
			return LicenseState.NotYetValid;
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
			return LicenseState.Unknown;
		}
	}

	private static KeyStore getUserDataLicense() {
		FileInputStream inStream = null;
		try {
			// final Certificate licenseCertificate;

			final String userHome = System.getProperty("user.home");
			if (userHome != null) {
				final File f = new File(userHome + "/mmxlabs/license.p12");
				// final CertificateFactory cf = CertificateFactory.getInstance("X.509");
				inStream = new FileInputStream(f);
				final KeyStore instance = KeyStore.getInstance("PKCS12");
				instance.load(inStream, password.toCharArray());
				return instance;
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

	@SuppressWarnings("resource")
	private static KeyStore getEclipseHomeLicense() throws CertificateException, FileNotFoundException {

		InputStream inStream = null;
		try {
			// final Certificate licenseCertificate;
			final String userHome = System.getProperty("eclipse.home.location");
			if (userHome != null) {
				final URL url = new URL(userHome + "/license.p12");
				// final CertificateFactory cf = CertificateFactory.getInstance("X.509");
				inStream = url.openStream();
				final KeyStore instance = KeyStore.getInstance("PKCS12");
				instance.load(inStream, password.toCharArray());
				return instance;
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
