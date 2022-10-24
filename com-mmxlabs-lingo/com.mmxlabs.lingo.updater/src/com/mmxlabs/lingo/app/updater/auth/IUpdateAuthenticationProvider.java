/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.updater.auth;

import java.net.URL;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;

public interface IUpdateAuthenticationProvider {

	@Nullable
	Pair<String, String> provideAuthenticationHeader(URL url) throws Exception ;
}
