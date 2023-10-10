/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.paper;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.repo.general.TypeRecord;

public class PaperTypeRecord implements TypeRecord {

	public static final @NonNull TypeRecord INSTANCE = new PaperTypeRecord();

	private PaperTypeRecord() {
	}

	@Override
	public String getType() {
		return "paper";
	}

	@Override
	public String getListURL() {
		return "/api/paper/versions";
	}

	@Override
	public String getUploadURL() {
		return "/api/paper/versions";
	}
	
	@Override
	public String getCurrentURL() {
		return "/api/paper/version/current/id";
	}

	@Override
	public String getDownloadURL(String uuid) {
		return String.format("%s/%s", getUploadURL(), uuid);
	}

	@Override
	public String getDeleteURL(String uuid) {
		return null;
	}

	public java.lang.Class<?> getMixin() {
		return PaperMixin.class;
	}

	@Override
	public void writeHeader(OutputStream os) throws IOException {
		PaperIO.writeHeader(os);
	}
}