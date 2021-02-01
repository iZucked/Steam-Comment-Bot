/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.repo.general.TypeRecord;

public class VesselsTypeRecord implements TypeRecord {

	public static final @NonNull TypeRecord INSTANCE = new VesselsTypeRecord();

	private VesselsTypeRecord() {
	}

	@Override
	public String getType() {
		return "vessels";
	}

	@Override
	public String getListURL() {
		return "/vessels/versions";
	}

	@Override
	public String getUploadURL() {
		return "/vessels/sync/versions/";

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
		return VesselsMixin.class;
	}

	@Override
	public void writeHeader(OutputStream os) throws IOException {
		VesselsIO.writeHeader(os);
	}
}
