/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;

public class PortsRepository extends AbstractGenericDataRepository<PortsVersion> {

	public static final @NonNull PortsRepository INSTANCE = new PortsRepository();

	private PortsRepository() {
		super(PortsIO.MODEL_TYPE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final PortsVersion version) throws Exception {
		return doPublishOutputStream(version, version.getIdentifier(), PortsIO::write);
	}

	public PortsVersion getLocalVersion(final String uuid) throws Exception {
		return doGetLocalVersion(uuid, PortsIO::read);
	}
}
