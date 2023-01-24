/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.vesselgroups;

import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;

public class VesselGroupsRepository extends AbstractGenericDataRepository<VesselGroupsVersion> {

	public static final VesselGroupsRepository INSTANCE = new VesselGroupsRepository();

	private VesselGroupsRepository() {
		super(VesselGroupsIO.MODEL_TYPE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final VesselGroupsVersion version) throws Exception {
		return doPublishOutputStream(version, version.getIdentifier(), VesselGroupsIO::write);
	}

	public VesselGroupsVersion getLocalVersion(final String versionTag) throws Exception {
		return doGetLocalVersion(versionTag, VesselGroupsIO::read);
	}
}
