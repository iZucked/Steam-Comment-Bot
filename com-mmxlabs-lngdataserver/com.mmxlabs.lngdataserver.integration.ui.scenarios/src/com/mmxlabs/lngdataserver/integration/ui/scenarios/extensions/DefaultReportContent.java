/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class DefaultReportContent implements IReportContent {

	private final String content;
	private final String version;
	private final String type;

	public DefaultReportContent(final String type, final String version, final String content) {
		this.type = type;
		this.version = version;
		this.content = content;
	}

	@Override
	public String getReportType() {
		return type;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String getVersion() {
		return version;
	}
}
