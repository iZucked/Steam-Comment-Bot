/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataImporter {
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public void importData() {

		final OkHttpClient CLIENT = new OkHttpClient();
		final Bundle bundle = FrameworkUtil.getBundle(DataImporter.class);
		final String location = "/data";
		final String endpoint = "/distances/sync/versions";
		{
			try {
				final File f = new File(FileLocator.toFileURL(bundle.getResource(location)).toURI());
				if (f.exists() && f.isDirectory()) {
					final Set<String> knownVersions = new HashSet<>();
					{
						final String versionURL = BackEndUrlProvider.INSTANCE.getUrl() + "/distances/versions";
						final Request request = new Request.Builder().url(versionURL).get().build();
						try (final Response response = CLIENT.newCall(request).execute()) {
							final String jsonData = response.body().string();
							final JSONArray Jobject = new JSONArray(jsonData);
							for (int i = 0; i < Jobject.length(); ++i) {
								final JSONObject versionObject = Jobject.getJSONObject(i);
								final String versionString = versionObject.getString("identifier");
								knownVersions.add(versionString);
							}
						}
					}
					final String url = BackEndUrlProvider.INSTANCE.getUrl() + endpoint;

					for (final File child : f.listFiles()) {
						if (child.isFile() && child.getName().endsWith(".json")) {
							final String version = child.getName().replaceAll(".json", "");
							if (knownVersions.contains(version)) {
								// Already imported
								continue;
							}
							try {
								final String json = Files.toString(child, Charsets.UTF_8);
								final RequestBody body = RequestBody.create(JSON, json);
								final Request request = new Request.Builder().url(url).post(body).build();
								try (final Response response = CLIENT.newCall(request).execute()) {
									// System.out.println(response.message());
								}
							} catch (final Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
