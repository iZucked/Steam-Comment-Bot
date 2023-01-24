/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.license.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple class to load a license - a SSL certificate from a disk location and verify it has been signed by the "root" key in the embedded keystore and that it is still in date.
 *
 * @author Simon Goodall
 */
public final class TrustStoreManager {

	private static final String USER_HOME_PROPERTY = "user.home";

	private static final Logger LOG = LoggerFactory.getLogger(TrustStoreManager.class);

	private static final String CACERTS_PATH = System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "security" + File.separatorChar + "cacerts"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
	private static final String CACERTS_TYPE = "JKS"; //$NON-NLS-1$

	/**
	 * Populate the default keystore with the necessary certificates
	 *
	 * @param keyStore
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 */
	private static void populateWithWindowsTrustStore(final KeyStore keyStore) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {

		final KeyStore defaultStore = KeyStore.getInstance("Windows-ROOT");
		defaultStore.load(null);

		final Enumeration<String> enumerator = defaultStore.aliases();
		while (enumerator.hasMoreElements()) {
			final String alias = enumerator.nextElement();
			keyStore.setCertificateEntry(alias, defaultStore.getCertificate(alias));
		}
	}

	/**
	 * Populate the default keystore with the necessary certificates
	 *
	 * @param keyStore
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 */
	private static void populateWithJavaTrustStore(final KeyStore keyStore) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
		String defaultStorePath = CACERTS_PATH;
		String defaultStoreType = CACERTS_TYPE;

		final KeyStore defaultStore = KeyStore.getInstance(defaultStoreType);
		try (FileInputStream fis = new FileInputStream(defaultStorePath)) {
			defaultStore.load(fis, null);
		}

		final Enumeration<String> enumerator = defaultStore.aliases();
		while (enumerator.hasMoreElements()) {
			final String alias = enumerator.nextElement();
			keyStore.setCertificateEntry(alias, defaultStore.getCertificate(alias));
		}
	}

	public static File getCACertsFileFromEclipseHomeURL(final String eclipseHomeLocation) throws URISyntaxException {
		if (eclipseHomeLocation != null) {
			final String location = (eclipseHomeLocation + "/cacerts/").replaceAll(" ", "%20");
			return new File(new URI(location));
		}
		return null;
	}

	public static void importExtraCertsFromHome(final KeyStore keystore) {
		final String userHome = System.getProperty("eclipse.home.location");
		try {
			final File f = getCACertsFileFromEclipseHomeURL(userHome);
			if (f != null && f.exists() && f.isDirectory()) {
				for (final File certFile : f.listFiles()) {
					if (certFile.isFile()) {
						try (FileInputStream inStream = new FileInputStream(certFile)) {
							final CertificateFactory factory = CertificateFactory.getInstance("X.509");
							final X509Certificate cert = (X509Certificate) factory.generateCertificate(inStream);
							keystore.setCertificateEntry(certFile.getName(), cert);
						} catch (final Exception e) {
							LOG.error("Unable to import certificate " + f.getAbsolutePath(), e);
						}
					}
				}
			}
		} catch (final URISyntaxException e1) {
			// Ignore
		}
	}

	private static void importExtraCertsInstall(final KeyStore keystore) {
		final String userHome = System.getProperty(USER_HOME_PROPERTY);
		if (userHome != null) {
			final File f = new File(userHome + "/mmxlabs/cacerts/");
			if (f.exists() && f.isDirectory()) {
				for (final File certFile : f.listFiles()) {
					if (certFile.isFile()) {
						try (FileInputStream inStream = new FileInputStream(certFile)) {
							final CertificateFactory factory = CertificateFactory.getInstance("X.509");
							final X509Certificate cert = (X509Certificate) factory.generateCertificate(inStream);
							keystore.setCertificateEntry(certFile.getName(), cert);
						} catch (final Exception e) {
							LOG.error("Unable to import certificate " + f.getAbsolutePath(), e);
						}
					}
				}
			}
		}
	} 

	public static KeyStore loadTruststore(boolean includeLocalCerts, boolean useJavaTruststore, boolean useWindowsTruststore) throws Exception {

		final KeyStore trustStore = KeyStore.getInstance("JKS");
		trustStore.load(null, null);

		// Import Java or System trust store into our local copy
		if (useJavaTruststore) {
			populateWithJavaTrustStore(trustStore);
		}
		if (useWindowsTruststore) {
			populateWithWindowsTrustStore(trustStore);
		}
		// Import any extra certificates
		if (includeLocalCerts) {
			importExtraCertsFromHome(trustStore);
			importExtraCertsInstall(trustStore);
		}
		return trustStore;
	}
}
