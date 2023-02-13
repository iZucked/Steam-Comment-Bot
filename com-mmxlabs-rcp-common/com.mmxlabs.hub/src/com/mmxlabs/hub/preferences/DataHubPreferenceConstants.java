/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.preferences;

public class DataHubPreferenceConstants {

	private DataHubPreferenceConstants() {

	}

	// Changing this forces users to re-enter a Datahub URL into LiNGO
	public static final String P_DATAHUB_URL_KEY = "URL";

	public static final String P_USERNAME_KEY = "Username";
	public static final String P_PASSWORD_KEY = "Password";

	public static final String P_ACCESS_TOKEN = "AcessToken";

	public static final String P_FORCE_BASIC_AUTH = "ForceBasicAuth";
	public static final String P_PREFER_EDGE_BROWSER = "PreferEdgeBrowser";

	public static final String P_ENABLE_BASE_CASE_SERVICE_KEY = "EnableBaseCaseService";
	public static final String P_ENABLE_TEAM_SERVICE_KEY = "EnableTeamService";
	public static final String P_DISABLE_SSL_HOSTNAME_CHECK = "DisableSSLHostnameCheck";

}
