/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances;

import java.net.URL;

import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class DataLoader {

	private DataLoader() {
	}

	public static @Nullable String importData(String filename) throws Exception {
		final URL scenarioURL = DataLoader.class.getResource("/" + filename);
		String data = Resources.toString(scenarioURL, Charsets.UTF_8);
		return data;
	}
}
