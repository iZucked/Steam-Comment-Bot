/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

public class SupportedReportFormats {
	private List<SupportFormat> formats = new LinkedList<>();

	public List<SupportFormat> getFormats() {
		return formats;
	}

	public void setFormats(List<SupportFormat> formats) {
		this.formats = formats;
	}

	public List<String> getVersionsFor(@NonNull String type) {

		return formats.stream() //
				.filter(sf -> type.equals(sf.getName())) //
				.map(SupportFormat::getVersion) // .
				.collect(Collectors.toList());

	}
}
