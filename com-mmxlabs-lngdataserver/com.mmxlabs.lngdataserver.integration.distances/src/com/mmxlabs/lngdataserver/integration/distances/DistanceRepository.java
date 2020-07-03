/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;

public class DistanceRepository extends AbstractGenericDataRepository<DistancesVersion> {

	public static final @NonNull DistanceRepository INSTANCE = new DistanceRepository();

	private DistanceRepository() {
		super(DistancesIO.MODEL_TYPE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final DistancesVersion version) throws Exception {
		return doPublishOutputStream(version, version.getIdentifier(), DistancesIO::write);
	}

	public DistancesVersion getLocalVersion(final String uuid) throws Exception {
		return doGetLocalVersion(uuid, DistancesIO::read);
	}
}