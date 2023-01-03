/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.repo.general.TypeRecord;

public class DistancesTypeRecord implements TypeRecord {

	public static final @NonNull TypeRecord INSTANCE = new DistancesTypeRecord();

	private DistancesTypeRecord() {
	}

	@Override
	public String getType() {
		return "distances";
	}

	@Override
	public String getListURL() {
		return "/distances/versions";
	}

	@Override
	public String getUploadURL() {
		return "/distances/sync/versions/";

	}

	@Override
	public String getDownloadURL(String uuid) {
		return String.format("%s%s", getUploadURL(), uuid);
	}

	@Override
	public String getDeleteURL(String uuid) {
		return null;
	}

	public java.lang.Class<?> getMixin() {
		return DistancesMixin.class;
	}

	@Override
	public void writeHeader(OutputStream os) throws IOException {
		DistancesIO.writeHeader(os);
	}
}
