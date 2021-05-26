/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.license.ssl.internal.Activator;

/**
 * A simple class to load a license - a SSL certificate from a disk location and
 * verify it has been signed by the "root" key in the embedded keystore and that
 * it is still in date.
 *
 * @author Simon Goodall
 */
public final class LicenseChecker {

	private static final String LICENSE_FILE_PROPERTY = "lingo.license.file";
	public static final String USER_HOME_PROPERTY = "user.home";
	public static final String LICENSE_FOLDER = "license.folder";
	public static final String MMXLABS_FOLDER = "mmxlabs";
	public static final String LICENSE_KEYSTORE = "license.p12";
	public static final String DATAHUB_LICENSE_KEYSTORE = "datahub.p12";
	public static final String LICENSE_ENDPOINT = "/license";
	public static final String PKCS12 = "PKCS12";

	@SuppressWarnings("serial")
	public static class InvalidLicenseException extends Exception {
	}

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

	// Hardcoded keystore password - only storing public key so not really an issue
	// - although tampering may be an issue
	private static final String password = "Lok3pDTS";

	/**
	 * Used by Application.java to avoid duplicating the password
	 *
	 * @return the keystore password
	 */
	public static String getPassword() {
		return password;
	}

	private static final String CACERTS_PATH = System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "security" + File.separatorChar + "cacerts"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
	private static final String CACERTS_TYPE = "JKS"; //$NON-NLS-1$

	public static @Nullable Pair<KeyStore, char[]> loadLocalKeystore() throws Exception {

		// Trigger license check to ensure the data store is created.
		if (checkLicense() == LicenseState.Valid) {
			final File trustStoreFile = Activator.getDefault().getBundle().getDataFile("local-truststore.jks");
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			try (final InputStream astream = new FileInputStream(trustStoreFile)) {
				keyStore.load(astream, password.toCharArray());
			}
			return new Pair<>(keyStore, password.toCharArray());
		}
		return null;
	}

	private static LicenseState state = null;

	public static synchronized LicenseState checkLicense() {
		if (state == null) {
			KeyStore licenseKeystore = getLicenseKeystore();
			state = doCheckLicense(licenseKeystore);
		}
		return state;
	}

	public static synchronized LicenseState doCheckLicense(KeyStore licenseKeystore) {

		if (licenseKeystore == null) {
			return LicenseState.KeystoreNotFound;
		}

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
			} else {
				return LicenseState.Unknown;
			}

			// Load in existing certificates from default store. We replace the default
			// store with our own, so make sure other bits of the app using a truststore
			// still work.
			populateDefaultKeystore(keyStore);
			createKeystoreCopies(keyStore, licenseKeystore);

			return LicenseState.Valid;
		} catch (final CertificateExpiredException e) {
			return LicenseState.Expired;
		} catch (final CertificateNotYetValidException e) {
			return LicenseState.NotYetValid;
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
			return LicenseState.Unknown;
		}
	}

	/**
	 * Create copies of the default & license keystores and place them in a known place on the filesystem so we can reference them later
	 *
	 * @param keyStore
	 * @param licenseKeystore
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	private static void createKeystoreCopies(KeyStore keyStore, KeyStore licenseKeystore) throws FileNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
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
			System.setProperty("javax.net.ssl.trustStoreType", "pkcs12");
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
	private static void populateDefaultKeystore(KeyStore keyStore) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
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

		importExtraCertsFromHome(keyStore);
		importExtraCertsInstall(keyStore);
	}

	/**
	 * Get license keystore from one of the following locations, respectively: system properties, eclipse home, user's mmxlabs folder
	 *
	 * @return the license keystore if it exists, otherwise null
	 */
	private static KeyStore getLicenseKeystore() {
		// Load the license file
		KeyStore licenseKeystore = null;

		try {

			licenseKeystore = getLicenseFromSystemProperty();

			if (licenseKeystore == null) {
				licenseKeystore = getEclipseHomeLicense();
			}

			if (licenseKeystore == null) {
				licenseKeystore = getUserDataLicense();
			}
		} catch (CertificateException e) {
			log.error("failed to get certificate from license keystore: {0}", e.getMessage());
		}
		return licenseKeystore;
	}

	/**
	 * Get license keystore from user's mmxlabs directory
	 *
	 * @return license keystore
	 */
	private static KeyStore getUserDataLicense() {
		final String userHome = System.getProperty(USER_HOME_PROPERTY);
		if (userHome != null) {
			final File f = new File(userHome + File.separator + MMXLABS_FOLDER + File.separator + LICENSE_KEYSTORE);
			if (f.exists()) {
				try (FileInputStream inStream = new FileInputStream(f)) {
					final KeyStore instance = KeyStore.getInstance(PKCS12);
					instance.load(inStream, password.toCharArray());
					return instance;
				} catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
					// Maybe better to catch some of these exception types and feedback to user?
					log.error("failed to get license from user's mmxlabs folder: " + e.getMessage());
				}
			}
		}
		return null;
	}


	/**
	 * Get license from system property (as defined in Eclipse runtime program arguments)
	 *
	 * @return license keystore
	 * @throws CertificateException
	 */
	private static KeyStore getLicenseFromSystemProperty() throws CertificateException {
		final String licenseFile = System.getProperty(LICENSE_FILE_PROPERTY);
		if (licenseFile != null) {
			try {
				final File f = new File(licenseFile);
				if (f.exists()) {
					try (InputStream inStream = new FileInputStream(f)) {
						final KeyStore instance = KeyStore.getInstance(PKCS12);
						instance.load(inStream, password.toCharArray());
						return instance;
					}
				}
			} catch (final IOException | NoSuchAlgorithmException | KeyStoreException e) {
				// Maybe better to catch some of these exception types and feedback to user?
				log.error("failed to get license from system property home: " + e.getMessage());
			}
		}
		return null;
	}


	/**
	 * Get license keystore from Eclipse home directory
	 * @return
	 * @throws CertificateException
	 */
	private static KeyStore getEclipseHomeLicense() throws CertificateException {

		final String userHome = System.getProperty("eclipse.home.location");
		if (userHome != null) {
			try {
				final URL url = new URL(userHome + File.separator + LICENSE_KEYSTORE);
				try (InputStream inStream = url.openStream()) {
					final KeyStore instance = KeyStore.getInstance(PKCS12);
					instance.load(inStream, password.toCharArray());
					return instance;
				}
			} catch (final IOException | NoSuchAlgorithmException | KeyStoreException e) {
				// Maybe better to catch some of these exception types and feedback to user?
				log.error("failed to get license from eclipse home: " + e.getMessage());
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

	public static File getCACertsFileFromEclipseHomeURL(String eclipseHomeLocation) throws URISyntaxException {
		if (eclipseHomeLocation != null) {
			final String uriString = (eclipseHomeLocation + "/cacerts/").replaceAll(" ", "%20");
			return new File(new URI(uriString));
		}
		return null;
	}

	private static void importExtraCertsFromHome(final KeyStore keystore) {
		final String userHome = System.getProperty("eclipse.home.location");
		try {
			File f = getCACertsFileFromEclipseHomeURL(userHome);
			if (f != null && f.exists() && f.isDirectory()) {
				for (final File certFile : f.listFiles()) {
					if (certFile.isFile()) {
						try (FileInputStream inStream = new FileInputStream(certFile)) {
							final CertificateFactory factory = CertificateFactory.getInstance("X.509");
							final X509Certificate cert = (X509Certificate) factory.generateCertificate(inStream);
							keystore.setCertificateEntry(certFile.getName(), cert);
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
							log.error("Unable to import certificate " + f.getAbsolutePath(), e);
						}
					}
				}
			}
		}
	}
}
