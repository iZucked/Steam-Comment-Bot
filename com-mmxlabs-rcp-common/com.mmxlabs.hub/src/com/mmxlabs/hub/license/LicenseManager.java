/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.hub.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.license.ssl.LicenseChecker;

import okhttp3.OkHttpClient;
import okio.Okio;

public final class LicenseManager {

	private static final Logger log = LoggerFactory.getLogger(LicenseManager.class);

	private static final OkHttpClient httpClient = HttpClientUtil.basicBuilder().build();

	private static final String LICENSE_FOLDER = "license";

	/**
	 * Gets the currently selected license from the DataHub and overwrites the license in the user's home
	 *
	 * @return the license keystore
	 * @throws IOException
	 */
	public static KeyStore getLicenseFromDatahub() throws IOException {
		KeyStore licenseKeystore = null;

		final var requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(LicenseChecker.LICENSE_ENDPOINT);
		if (requestBuilder == null) {
			return null;
		}

		var request = requestBuilder.build();

		try (var response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 401) {
					log.error("insufficient permissions to retrieve license from DataHub");
				} else if (response.code() == 404) {
					log.error("there is currently no selected license on the DataHub");
				} else {
					log.error("unable to retrieve license from DataHub, unexpected code: " + response);
				}

				return null;
			}

			try (var bufferedSource = response.body().source()) {

				String licenseFolderPath;

				if (isDevEnvironment()) {
					licenseFolderPath = System.getProperty(LicenseChecker.USER_HOME_PROPERTY);
				} else {
					final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
					licenseFolderPath = workspaceLocation.toOSString() + IPath.SEPARATOR + LICENSE_FOLDER;
				}

				// create license folder if necessary
				var licenseFolder = new File(licenseFolderPath);
				licenseFolder.mkdir();

				// try save response to file
				final var licenseFile = new File(licenseFolderPath + File.separator + LicenseChecker.DATAHUB_LICENSE_KEYSTORE);
				try (var bufferedSink = Okio.buffer(Okio.sink(licenseFile))) {
					bufferedSink.writeAll(bufferedSource);
				}

				// try read from file
				try (var inStream = new FileInputStream(licenseFile)) {
					final var instance = KeyStore.getInstance(LicenseChecker.PKCS12);
					final String password = LicenseChecker.getPassword();
					instance.load(inStream, password.toCharArray());
					licenseKeystore = instance;
				} catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
					log.error("failed to read from newly written license file in user's home: " + e.getMessage());
				}
			}
		}
		return licenseKeystore;
	}

	/**
	 * Checks if the license.folder program argument is set. Creates the directory if it does not already exist. LiNGO will save the license in this folder to avoid using a different license per LiNGO
	 * instance. The argument must be the full path to the license folder i.e. -license.folder C:\Users\Username\mmxlabs\license
	 *
	 * @return true if license.folder is set, false otherwise
	 */
	public static boolean isDevEnvironment() {
		final String userHome = System.getProperty(LICENSE_FOLDER);
		if (userHome != null) {
			final var licenseFolder = new File(userHome);
			licenseFolder.mkdir();
			return true;
		} else {
			return false;
		}
	}
}