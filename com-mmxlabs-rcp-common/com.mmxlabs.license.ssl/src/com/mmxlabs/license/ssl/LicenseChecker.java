/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple class to load a license - a SSL certificate from a disk location and
 * verify it has been signed by the "root" key in the embedded keystore and that
 * it is still in date.
 *
 * @author Simon Goodall
 */
public final class LicenseChecker {

	@SuppressWarnings("serial")
	public static class InvalidLicenseException extends Exception {
	}

	private static final Logger LOG = LoggerFactory.getLogger(LicenseChecker.class);

	private static LicenseState state = null;

	public static synchronized LicenseState checkLicense() {
		if (state == null) {
			final KeyStore licenseKeystore = LicenseManager.getLicenseKeystore();
			state = doCheckLicense(licenseKeystore);
		}
		return state;
	}

	public static synchronized LicenseState doCheckLicense(final @Nullable KeyStore licenseKeystore) {

		if (licenseKeystore == null) {
			return LicenseState.KeystoreNotFound;
		}

		try {
			final Certificate rootCertificate = LicenseManager.loadLicenseRoot();

			final X509Certificate licenseCertificate = LicenseManager.getClientLicense(licenseKeystore);

			// Verify self-signed certificate
			rootCertificate.verify(rootCertificate.getPublicKey());

			// Verify license is signed by the server
			licenseCertificate.verify(rootCertificate.getPublicKey());

			// Check dates are valid
			licenseCertificate.checkValidity();

			return LicenseState.Valid;
		} catch (final CertificateExpiredException e) {
			return LicenseState.Expired;
		} catch (final CertificateNotYetValidException e) {
			return LicenseState.NotYetValid;
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
			return LicenseState.Unknown;
		}
	}
}
