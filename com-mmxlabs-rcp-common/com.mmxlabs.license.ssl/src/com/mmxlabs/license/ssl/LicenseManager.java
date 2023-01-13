/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.license.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.http.ssl.SSLContextBuilder;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LicenseManager {

	private static final Logger LOG = LoggerFactory.getLogger(LicenseManager.class);

	private static final String PKCS12 = "PKCS12";

	// Hardcoded keystore password - only storing public key so not really an issue
	// - although tampering may be an issue
	private static final String KEYSTORE_CODE = "Lok3pDTS";

	private static final String LICENSE_FILE_PROPERTY = "lingo.license.file";
	private static final String USER_HOME_PROPERTY = "user.home";
	private static final String MMXLABS_FOLDER = "mmxlabs";
	private static final String LICENSE_KEYSTORE = "license.p12";

	private static File hubLicense = null;

	public static KeyStore loadLicenseKeyFile(final InputStream inStream) throws Exception {
		final KeyStore instance = KeyStore.getInstance(PKCS12);
		instance.load(inStream, KEYSTORE_CODE.toCharArray());
		return instance;
	}

	public static @Nullable KeyStore loadLicenseKeyFile(final File f) throws Exception {
		if (f.exists()) {
			try (FileInputStream inStream = new FileInputStream(f)) {
				return loadLicenseKeyFile(inStream);
			}
		}
		return null;
	}

	/**
	 * Get license keystore from one of the following locations, respectively:
	 * system properties, eclipse home, user's mmxlabs folder
	 *
	 * @return the license keystore if it exists, otherwise null
	 */
	public static @Nullable KeyStore getLicenseKeystore() {
		// Load the license file
		KeyStore licenseKeystore = null;

		if (hubLicense != null) {
			try {
				return loadLicenseKeyFile(hubLicense);
			} catch (final Exception e) {
				LOG.error(String.format("Exception reading licence from Data Hub: %s", e.getMessage()), e);
			}
		}

		try {
			licenseKeystore = getLicenseFromSystemProperty();
		} catch (final Exception e) {
			LOG.error(String.format("Exception reading license from system property: %s", e.getMessage()), e);
		}
		if (licenseKeystore == null) {
			try {
				licenseKeystore = getEclipseHomeLicense();
			} catch (final Exception e) {
				LOG.error(String.format("Exception reading licence from LiNGO folder: %s", e.getMessage()), e);
			}
		}
		if (licenseKeystore == null) {
			try {
				licenseKeystore = getUserDataLicense();
			} catch (final Exception e) {
				LOG.error(String.format("Exception reading license from user home folder: %s", e.getMessage()), e);
			}
		}

		return licenseKeystore;
	}

	public static void loadLicenseKeystore(SSLContextBuilder builder) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
		builder.loadKeyMaterial(getLicenseKeystore(), KEYSTORE_CODE.toCharArray());

	}

	/**
	 * Get license keystore from user's mmxlabs directory
	 *
	 * @return license keystore
	 */

	public static @Nullable KeyStore getUserDataLicense() throws Exception {
		final String userHome = System.getProperty(USER_HOME_PROPERTY);
		if (userHome != null) {
			final File f = new File(userHome + File.separator + MMXLABS_FOLDER + File.separator + LICENSE_KEYSTORE);
			if (f.exists()) {
				return loadLicenseKeyFile(f);
			}
		}
		return null;
	}

	/**
	 * Get license from system property (as defined in Eclipse runtime program
	 * arguments)
	 *
	 * @return license keystore
	 */

	public static @Nullable KeyStore getLicenseFromSystemProperty() throws Exception {
		final String licenseFile = System.getProperty(LICENSE_FILE_PROPERTY);
		if (licenseFile != null) {
			return loadLicenseKeyFile(new File(licenseFile));
		}
		return null;
	}

	/**
	 * Get license keystore from Eclipse home directory
	 * 
	 * @return
	 * @throws Exception
	 */
	public static @Nullable KeyStore getEclipseHomeLicense() throws Exception {

		final String userHome = System.getProperty("eclipse.home.location");
		if (userHome != null) {
			final URL url = new URL(userHome + LICENSE_KEYSTORE);
			try (InputStream inStream = url.openStream()) {
				return loadLicenseKeyFile(inStream);
			} catch (FileNotFoundException e) {
				// Ignore
			}
		}
		return null;
	}

	public static Certificate loadLicenseRoot() throws Exception {

		final URL trustStoreUrl = FrameworkUtil.getBundle(LicenseManager.class).getResource("clientauthca.pem");
		if (trustStoreUrl == null) {
			throw new RuntimeException("License root not found");
		}
		Certificate rootCertificate = null;
		try (InputStream inStream = trustStoreUrl.openStream()) {
			final CertificateFactory factory = CertificateFactory.getInstance("X.509");
			rootCertificate = factory.generateCertificate(inStream);
		} catch (final Exception e) {
			LOG.error("Unable to import certificate", e);
		}
		if (rootCertificate == null) {
			throw new RuntimeException("License root store not found in store");
		}

		return rootCertificate;

	}

	public static void updateHubLicense(File hubLicense) {
		LicenseManager.hubLicense = hubLicense;
	}

	public static X509Certificate getClientLicense(final @Nullable KeyStore licenseKeystore) throws Exception {

		// Hardcoded alias name in the keystore as part of generation process
		final Certificate licenseCertificate = licenseKeystore.getCertificate("1");

		// Check dates are valid. We expect a X509 certificate
		if (licenseCertificate instanceof final X509Certificate x509Certificate) {
			return x509Certificate;
		} else {
			throw new ClassCastException("Certificate is not an X509Certificate");
		}
	}
}
