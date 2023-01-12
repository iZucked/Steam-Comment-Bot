/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

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

		final HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest("/scenarios/v1/reports/versions", request);
		if (httpClient == null) {
			return;
		}

		try (var response = httpClient.execute(request)) {
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
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public SupportedReportFormats getSupportedVersions() {
		return supportedVersions;
	}

}
