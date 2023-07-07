/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.auth;

import java.net.URI;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;

public interface ICloudAuthenticationProvider {

	@Nullable
	Pair<String, String> provideAuthenticationHeader(URI uri) throws Exception ;
}
