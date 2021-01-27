/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.repo.general.TypeRecord;

public class PortsTypeRecord implements TypeRecord {

	public static final @NonNull TypeRecord INSTANCE = new PortsTypeRecord();

	private PortsTypeRecord() {
	}

	@Override
	public String getType() {
		return "ports";
	}

	@Override
	public String getListURL() {
		return "/ports/versions";
	}

	@Override
	public String getUploadURL() {
		return "/ports/sync/versions/";

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
		return PortsMixin.class;
	}

	@Override
	public void writeHeader(OutputStream os) throws IOException {
		PortsIO.writeHeader(os);
	}
}
