/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.repo.general.TypeRecord;

public class PricingTypeRecord implements TypeRecord {

	public static final @NonNull TypeRecord INSTANCE = new PricingTypeRecord();

	private PricingTypeRecord() {
	}

	@Override
	public String getType() {
		return "pricing";
	}

	@Override
	public String getListURL() {
		return "/pricing/versions";
	}

	@Override
	public String getUploadURL() {
		return "/pricing/sync/versions/";

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
		return PricingMixin.class;
	}

	@Override
	public void writeHeader(OutputStream os) throws IOException {
		PricingIO.writeHeader(os);
	}
}
