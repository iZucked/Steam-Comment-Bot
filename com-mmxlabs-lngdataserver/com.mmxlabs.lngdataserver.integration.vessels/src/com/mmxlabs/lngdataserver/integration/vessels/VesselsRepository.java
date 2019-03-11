/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.repo.general.AbstractGeneralDataRepository;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;

public class VesselsRepository extends AbstractGeneralDataRepository<VesselsVersion> {

	public static final @NonNull VesselsRepository INSTANCE = new VesselsRepository();

	private VesselsRepository() {
		super(VesselsTypeRecord.INSTANCE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final VesselsVersion version) throws Exception {
		final String json = VesselsIO.write(version);
		return uploadData(json);
	}

	public VesselsVersion getLocalVersion(final String uuid) throws Exception {
		return doGetLocalVersion(uuid, VesselsIO::read);
	}
}
