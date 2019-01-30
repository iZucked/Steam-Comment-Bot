/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.repo.AbstractGeneralDataRepository;

public class DistanceRepository extends AbstractGeneralDataRepository<DistancesVersion> {

	public static final @NonNull DistanceRepository INSTANCE = new DistanceRepository();

	private DistanceRepository() {
		super(DistancesTypeRecord.INSTANCE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final DistancesVersion version) throws Exception {
		final String json = DistancesIO.write(version);
		return uploadData(json);
	}

	public DistancesVersion getLocalVersion(final String uuid) throws Exception {
		return doGetLocalVersion(uuid, DistancesIO::read);
	}
}