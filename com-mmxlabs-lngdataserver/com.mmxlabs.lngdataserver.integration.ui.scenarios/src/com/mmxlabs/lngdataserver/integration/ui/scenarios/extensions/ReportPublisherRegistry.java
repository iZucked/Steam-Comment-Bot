package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IDataHubStateChangeListener;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SupportFormat;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SupportedReportFormats;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

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

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder() //
			.build();
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

		final Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder("/scenarios/v1/reports/versions");
		if (requestBuilder == null) {
			return;
		}

		final Request request = requestBuilder //
				.get() //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.code() == 404) {
				// Either an old Hub or unknown format.
				// Create a blank object. Publishers will typically interpret no values a "1"
				this.supportedVersions = new SupportedReportFormats();
			} else if (response.isSuccessful()) {
				final String s = response.body().string();
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
