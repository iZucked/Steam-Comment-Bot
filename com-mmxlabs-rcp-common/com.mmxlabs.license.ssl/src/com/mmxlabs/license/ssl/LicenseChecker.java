/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.license.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.eclipse.jdt.annotation.Nullable;
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
	 */
	public enum LicenseState {
		Valid, //
		Expired("License has expired. Please contact Minimax Labs."), //
		Unknown("Unkown problem validating license file."), //
		NotYetValid("License is not valid yet. Please contact Minimax Labs."), //
		KeystoreNotFound("Unable to find license file");

		private final String message;

		private LicenseState() {
			message = toString();
		}

		private LicenseState(final String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	// Hardcoded keystore password - only storing public key so not really an issue - although tampering may be an issue
	private static final String password = "Lok3pDTS";

	private static final String CACERTS_PATH = System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "security" + File.separatorChar + "cacerts"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
	private static final String CACERTS_TYPE = "JKS"; //$NON-NLS-1$

	public static LicenseState checkLicense() {

		try {
			// Load keystore
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			// TODO: Load from bundle resource
			final URL keyStoreUrl = Activator.getDefault().getBundle().getResource("keystore.jks");
			if (keyStoreUrl == null) {
				return LicenseState.KeystoreNotFound;
			}
			try (final InputStream astream = keyStoreUrl.openStream()) {
				keyStore.load(astream, password.toCharArray());
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
				return LicenseState.KeystoreNotFound;
			}

			// Hardcoded alias name in the keystore as part of generation process
			final Certificate licenseCertificate = licenseKeystore.getCertificate("1");

			// Verify self-signed certificate
			rootCertificate.verify(rootCertificate.getPublicKey());

			// Verify license is signed by the server
			licenseCertificate.verify(rootCertificate.getPublicKey());

			// Load in existing certificates from default store. We replace the default store with our own, so make sure other bits of the app using a truststore still work.
			{
				String defaultStorePath = System.getProperty("javax.net.ssl.trustStore");
				if (defaultStorePath == null) {
					defaultStorePath = CACERTS_PATH;
				}
				String defaultStoreType = System.getProperty("javax.net.ssl.trustStoreType");
				if (defaultStoreType == null) {
					defaultStoreType = CACERTS_TYPE;
				}

				final String defaultStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
				final char[] pass = defaultStorePassword == null ? null : defaultStorePassword.toCharArray();

				final KeyStore defaultStore = KeyStore.getInstance(defaultStoreType);
				try (FileInputStream fis = new FileInputStream(defaultStorePath)) {
					defaultStore.load(fis, pass);
					final Enumeration<String> enumerator = defaultStore.aliases();
					while (enumerator.hasMoreElements()) {
						final String alias = enumerator.nextElement();
						keyStore.setCertificateEntry(alias, defaultStore.getCertificate(alias));
					}
				} catch (final IOException e) {
					log.error(e.getMessage(), e);
				}
			}

			// Check dates are valid. We expect a X509 certificate
			if (licenseCertificate instanceof X509Certificate) {
				final X509Certificate x509Certificate = (X509Certificate) licenseCertificate;
				x509Certificate.checkValidity();

				importExtraCertsFromHome(keyStore);
				importExtraCertsInstall(keyStore);

				// Create copies of the keystores in a known place on filesystem so we can reference them
				final File keyStoreFile = Activator.getDefault().getBundle().getDataFile("local-keystore.jks");
				final File trustStoreFile = Activator.getDefault().getBundle().getDataFile("local-truststore.jks");

				try (FileOutputStream stream = new FileOutputStream(keyStoreFile)) {
					licenseKeystore.store(stream, password.toCharArray());
				}
				try (FileOutputStream stream = new FileOutputStream(trustStoreFile)) {
					keyStore.store(stream, password.toCharArray());
					System.setProperty("javax.net.ssl.keyStore", keyStoreFile.toString());
					System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
					System.setProperty("javax.net.ssl.keyStorePassword", password);

					System.setProperty("javax.net.ssl.trustStore", trustStoreFile.toString());
					System.setProperty("javax.net.ssl.trustStorePassword", password);

					return LicenseState.Valid;

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

		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/mmxlabs/license.p12");
			try (FileInputStream inStream = new FileInputStream(f)) {
				final KeyStore instance = KeyStore.getInstance("PKCS12");
				instance.load(inStream, password.toCharArray());
				return instance;
			} catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
				// Ignore
				// Maybe better to catch some of these exception types and feedback to user?
			}
		}
		return null;
	}

	private static KeyStore getEclipseHomeLicense() throws CertificateException {

		final String userHome = System.getProperty("eclipse.home.location");
		if (userHome != null) {
			try {
				final URL url = new URL(userHome + "/license.p12");
				try (InputStream inStream = url.openStream()) {
					final KeyStore instance = KeyStore.getInstance("PKCS12");
					instance.load(inStream, password.toCharArray());
					return instance;
				}
			} catch (final IOException | NoSuchAlgorithmException | KeyStoreException e) {
				// Ignore
				// Maybe better to catch some of these exception types and feedback to user?
			}
		}
		return null;

	}

	/**
	 * Loads the client license certificate into memory.
	 * 
	 * @return The certificate, or null if there were problems other than exceptions
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws KeyStoreException
	 */
	public static @Nullable X509Certificate getClientLicense() throws CertificateException, KeyStoreException {
		// Load the license file
		KeyStore licenseKeystore = null;
		{
			licenseKeystore = getEclipseHomeLicense();
			if (licenseKeystore == null) {
				licenseKeystore = getUserDataLicense();
			}
		}

		if (licenseKeystore == null) {
			return null;
		}

		// Hardcoded alias name in the keystore as part of generation process
		final Certificate licenseCertificate = licenseKeystore.getCertificate("1");

		if (licenseCertificate instanceof X509Certificate) {
			return (X509Certificate) licenseCertificate;
		}
		return null;

	}

	private static void importExtraCertsFromHome(final KeyStore keystore) {
		final String userHome = System.getProperty("eclipse.home.location");
		if (userHome != null) {
			try {
				File f = new File(new URI(userHome + "/cacerts/"));
				if (f.exists() && f.isDirectory()) {
					for (final File certFile : f.listFiles()) {
						if (certFile.isFile()) {
							try (FileInputStream inStream = new FileInputStream(certFile)) {
								final CertificateFactory factory = CertificateFactory.getInstance("X.509");
								final X509Certificate cert = (X509Certificate) factory.generateCertificate(inStream);
								keystore.setCertificateEntry(f.getName(), cert);
							} catch (final Exception e) {
								log.error("Unable to import certificate " + f.getAbsolutePath(), e);
							}
						}
					}
				}
			} catch (final URISyntaxException e1) {
				// Ignore
			}
		}
	}

	private static void importExtraCertsInstall(final KeyStore keystore) {
		final String userHome = System.getProperty("user.home");
		if (userHome != null) {
			final File f = new File(userHome + "/mmxlabs/cacerts/");
			if (f.exists() && f.isDirectory()) {
				for (final File certFile : f.listFiles()) {
					if (certFile.isFile()) {
						try (FileInputStream inStream = new FileInputStream(certFile)) {
							final CertificateFactory factory = CertificateFactory.getInstance("X.509");
							final X509Certificate cert = (X509Certificate) factory.generateCertificate(inStream);
							keystore.setCertificateEntry(f.getName(), cert);
						} catch (final Exception e) {
							log.error("Unable to import certificate " + f.getAbsolutePath(), e);
						}
					}
				}
			}
		}
	}
}
