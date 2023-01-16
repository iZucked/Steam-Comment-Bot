/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import java.io.IOException;

import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IDataHubStateChangeListener;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SupportedReportFormats;

public class ReportPublisherRegistry {

	private static ReportPublisherRegistry INSTANCE;

	public static ReportPublisherRegistry getInstance() {
		synchronized (ReportPublisherRegistry.class) {
			if (INSTANCE == null) {
				INSTANCE = new ReportPublisherRegistry();
			}
		}
		return INSTANCE;
	}

	private SupportedReportFormats supportedVersions = new SupportedReportFormats();

	private ReportPublisherRegistry() {

		DataHubServiceProvider.getInstance().addDataHubStateListener(new IDataHubStateChangeListener() {

			@Override
			public void hubStateChanged(final boolean online, final boolean loggedin, final boolean changedToOnlineAndLoggedIn) {
				if (changedToOnlineAndLoggedIn) {
					refreshState();
				}
			}

			@Override
			public void hubPermissionsChanged() {

			}
		});

		// Trigger initial look up
		refreshState();
	}

	public synchronized void refreshState() {
		try {
			DataHubServiceProvider.getInstance().doGetRequest("/scenarios/v1/reports/versions", response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode == 404) {
					// Either an old Hub or unknown format.
					// Create a blank object. Publishers will typically interpret no values a "1"
					this.supportedVersions = new SupportedReportFormats();
				} else if (HttpClientUtil.isSuccessful(responseCode)) {
					final String s = EntityUtils.toString(response.getEntity());
					final SupportedReportFormats formats = new ObjectMapper().readValue(s, SupportedReportFormats.class);
					this.supportedVersions = formats;
				}
				return null;
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SupportedReportFormats getSupportedVersions() {
		return supportedVersions;
	}

}
