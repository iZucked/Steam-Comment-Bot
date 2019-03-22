/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.portgroups;

import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;

public class PortGroupsRepository extends AbstractGenericDataRepository<PortGroupsVersion> {

	public static final PortGroupsRepository INSTANCE = new PortGroupsRepository();

	private PortGroupsRepository() {
		super(PortGroupsIO.MODEL_TYPE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final PortGroupsVersion version) throws Exception {
		return doPublishOutputStream(version, version.getIdentifier(), PortGroupsIO::write);
	}

	public PortGroupsVersion getLocalVersion(final String versionTag) throws Exception {
		return doGetLocalVersion(versionTag, PortGroupsIO::read);
	}
}
