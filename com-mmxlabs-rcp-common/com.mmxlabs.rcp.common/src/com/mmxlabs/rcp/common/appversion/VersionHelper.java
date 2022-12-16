/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.appversion;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;

import com.google.common.io.ByteStreams;
import com.mmxlabs.rcp.common.ServiceHelper;

public class VersionHelper {
	
	private String version = null;
	private String code = null;
	private boolean workspaceVersion = true;
	private boolean isFileRead = false;
	
	private static VersionHelper instance = new VersionHelper();
	
	public @Nullable String getClientVersion() {
		if (version == null && !isFileRead) {
			readVersionFile();
		}
		return version;
	}
	
	public @Nullable String getClientCode() {
		if (code == null && !isFileRead) {
			readVersionFile();
		}
		if (code == null) {
			code = ServiceHelper.withOptionalService(IClientVersionsProvider.class, p -> {
				if (p != null) {
					return p.getClientCode();
				}
				return null;
			});
		}
		return code;
	}
	
	private void readVersionFile() {
		final String userHome = System.getProperty("eclipse.home.location");
		if (userHome != null) {
			try {
				final URL url = new URL(userHome + "/version.json");
				byte[] versionBytes;
				try (InputStream is = url.openStream()) {
					versionBytes = ByteStreams.toByteArray(is);
				}
				String jsonText = new String(versionBytes, StandardCharsets.UTF_8);
				JSONObject json = new JSONObject(jsonText);

				version = json.getString("version");
				code = json.getString("code");
				isFileRead = true;
				
				workspaceVersion = false;
			} catch (final IOException e) {
				// Ignore
				// Maybe better to catch some of these exception types and feedback to user?
			}
		}
	}
	
	public static VersionHelper getInstance() {
		return instance;
	}

	/**
	 * Returns true if build is running from workspace. Implied by lack of a version.json in runtime root folder.
	 * @return
	 */
	public boolean isWorkspaceVersion() {
		return workspaceVersion;
	}

	public void setClientCode(String code) {
		this.code = code;
		
	}
}
