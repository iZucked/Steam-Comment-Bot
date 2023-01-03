/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.license;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseManager;

import okhttp3.OkHttpClient;
import okio.Okio;

public final class HubLicenseManager {

	private static final Logger LOG = LoggerFactory.getLogger(HubLicenseManager.class);

	private static final OkHttpClient httpClient = HttpClientUtil.basicBuilder().build();

	static final String LICENSE_FOLDER = "license";
	private static final String DATAHUB_LICENSE_KEYSTORE = "datahub.p12";
	private static final String LICENSE_ENDPOINT = "/license";

	public static void refreshHubLicense() {
		// check if the datahub license management is active and if hub license is valid
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DATAHUB_LICENSE_MANAGEMENT)) {
			final File licenseKeystore = getLicenseFromDatahub();
			LicenseManager.updateHubLicense(licenseKeystore);
		}
	}

	/**
	 * Gets the currently selected license from the DataHub and overwrites the
	 * license in the user's home
	 *
	 * @return the license keystore
	 * @throws IOException
	 */
	public static File getLicenseFromDatahub() {

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final IPath licenseFolderPath = workspaceLocation.append(LICENSE_FOLDER);
		licenseFolderPath.toFile().mkdirs();

		// try save response to file
		final File licenseFile = new File(licenseFolderPath.toFile(), DATAHUB_LICENSE_KEYSTORE);

		final var requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(LICENSE_ENDPOINT);
		if (requestBuilder == null) {
			if (licenseFile.exists()) {
				return licenseFile;
			}
			return null;
		}
		try {
			final var request = requestBuilder.build();
			try (var response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					if (response.code() == 401) {
						LOG.error("insufficient permissions to retrieve license from DataHub");
					} else if (response.code() == 404) {
						LOG.error("there is currently no selected license on the DataHub");
					} else {
						LOG.error("unable to retrieve license from DataHub, unexpected code: " + response);
					}

					// 4xx codes generally mean the server responded properly but the user can't
					// have a license. Maybe the license has been removed or the lingo permission
					// has been revoked. Other error code (e.g. 5xx) may just be temporary failures
					// such as a DataHub restart.
					if (response.code() >= 400 && response.code() < 500) {
						if (licenseFile.exists()) {
							licenseFile.delete();
						}
					}

					return null;
				}

				try (var bufferedSource = response.body().source()) {

					try (var bufferedSink = Okio.buffer(Okio.sink(licenseFile))) {
						bufferedSink.writeAll(bufferedSource);
					}
					return licenseFile;
				}
			}
		} catch (final IOException e) {
			LOG.error("Error downloading license: " + e.getMessage(), e);
			if (licenseFile.exists()) {
				return licenseFile;
			}
			return null;
		}
	}
}
