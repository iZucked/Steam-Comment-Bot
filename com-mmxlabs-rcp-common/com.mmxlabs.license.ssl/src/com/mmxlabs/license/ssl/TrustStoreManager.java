/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Objects;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;

/**
 * A simple class to load a license - a SSL certificate from a disk location and
 * verify it has been signed by the "root" key in the embedded keystore and that
 * it is still in date.
 *
 * @author Simon Goodall
 */
public final class TrustStoreManager {

	private static final String JAVAX_NET_SSL_KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";
	private static final String JAVAX_NET_SSL_KEY_STORE_TYPE = "javax.net.ssl.keyStoreType";
	private static final String JAVAX_NET_SSL_KEY_STORE = "javax.net.ssl.keyStore";
	private static final String JAVAX_NET_SSL_TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType";
	private static final String JAVAX_NET_SSL_TRUST_STORE = "javax.net.ssl.trustStore";
	private static final String JAVAX_NET_SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
	private static final String USER_HOME_PROPERTY = "user.home";

	private static final Logger LOG = LoggerFactory.getLogger(TrustStoreManager.class);

//	
//	// Hardcoded keystore password - only storing public key so not really an issue
//	// - although tampering may be an issue
	private static final String password = "Lok3pDTS";

	private static final String CACERTS_PATH = System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "security" + File.separatorChar + "cacerts"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
	private static final String CACERTS_TYPE = "JKS"; //$NON-NLS-1$

	public static @Nullable Pair<KeyStore, char[]> loadLocalTruststore() throws Exception {

		final File trustStoreFile = getTrustStoreFile();
		final KeyStore keyStore = KeyStore.getInstance("JKS");
		try (final InputStream astream = new FileInputStream(trustStoreFile)) {
			keyStore.load(astream, password.toCharArray());
		}
		return new Pair<>(keyStore, password.toCharArray());
	}

	public static @Nullable Pair<KeyStore, char[]> loadLocalKeystore() throws Exception {

		final File keyStoreFile = getKeyStoreFile();
		final KeyStore keyStore = KeyStore.getInstance("JKS");
		try (final InputStream astream = new FileInputStream(keyStoreFile)) {
			keyStore.load(astream, password.toCharArray());
		}
		return new Pair<>(keyStore, password.toCharArray());
	}

	private static File getTrustStoreFile() {
		return FrameworkUtil.getBundle(TrustStoreManager.class).getDataFile("local-truststore.jks");
	}

	private static File getKeyStoreFile() {
		return FrameworkUtil.getBundle(TrustStoreManager.class).getDataFile("local-keystore.jks");
	}

	/**
	 * Create copies of the default & license keystores and place them in a known
	 * place on the filesystem so we can reference them later
	 *
	 * @param keyStore
	 * @param licenseKeystore
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static void createKeystoreCopiesAndSetSysProps(final KeyStore keyStore, final File keyStoreFile, final KeyStore trustStore, final File trustStoreFile)
			throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {

		try (FileOutputStream stream = new FileOutputStream(keyStoreFile)) {
			keyStore.store(stream, password.toCharArray());
			System.setProperty(JAVAX_NET_SSL_KEY_STORE, keyStoreFile.toString());
			System.setProperty(JAVAX_NET_SSL_KEY_STORE_TYPE, "pkcs12");
			System.setProperty(JAVAX_NET_SSL_KEY_STORE_PASSWORD, password);
		}
		try (FileOutputStream stream = new FileOutputStream(trustStoreFile)) {
			trustStore.store(stream, password.toCharArray());

			System.setProperty(JAVAX_NET_SSL_TRUST_STORE, trustStoreFile.toString());
			System.setProperty(JAVAX_NET_SSL_TRUST_STORE_PASSWORD, password);
			System.setProperty(JAVAX_NET_SSL_TRUST_STORE_TYPE, "pkcs12");
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
	private static void populateDefaultTrustStore(final KeyStore keyStore) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
		String defaultStorePath = System.getProperty(JAVAX_NET_SSL_TRUST_STORE);
		String defaultStoreType = System.getProperty(JAVAX_NET_SSL_TRUST_STORE_TYPE);

		// Windows - use system truststore by default if not specified otherwise
		if (defaultStorePath == null && defaultStoreType == null) {
			if (Objects.equals(Platform.getOS(), Platform.OS_WIN32)) {
				defaultStorePath = "NUL";
				defaultStoreType = "Windows-ROOT";
			}
		}

		if (defaultStorePath == null) {
			defaultStorePath = CACERTS_PATH;
		}
		if (defaultStoreType == null) {
			defaultStoreType = CACERTS_TYPE;
		}

		final String defaultStorePassword = System.getProperty(JAVAX_NET_SSL_TRUST_STORE_PASSWORD);
		final char[] pass = defaultStorePassword == null ? null : defaultStorePassword.toCharArray();

		final KeyStore defaultStore = KeyStore.getInstance(defaultStoreType);
		if (Objects.equals("NUL", defaultStorePath)) {
			defaultStore.load(null);
		} else {
			try (FileInputStream fis = new FileInputStream(defaultStorePath)) {
				defaultStore.load(fis, pass);
			}
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

	public static void refresh() throws Exception {

		// Grab a copy of our internal update server CA root file
		// Load keystore
		final KeyStore trustStore = KeyStore.getInstance("JKS");
		trustStore.load(null, null);

		// Import Java or System trust store into our local copy
		populateDefaultTrustStore(trustStore);

		// Import any extra certificates
		importExtraCertsFromHome(trustStore);
		importExtraCertsInstall(trustStore);

		final File keystoreFile = getKeyStoreFile();
		final File truststoreFile = getTrustStoreFile();

		createKeystoreCopiesAndSetSysProps(LicenseManager.getLicenseKeystore(), keystoreFile, trustStore, truststoreFile);
	}
}
