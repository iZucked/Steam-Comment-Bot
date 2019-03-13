/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;

public class VesselsRepository extends AbstractGenericDataRepository<VesselsVersion> {

	public static final @NonNull VesselsRepository INSTANCE = new VesselsRepository();

	private VesselsRepository() {
		super(VesselsIO.MODEL_TYPE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final VesselsVersion version) throws Exception {
		return doPublishOutputStream(version, version.getIdentifier(), VesselsIO::write);
	}

	public VesselsVersion getLocalVersion(final String uuid) throws Exception {
		return doGetLocalVersion(uuid, VesselsIO::read);
	}
}
