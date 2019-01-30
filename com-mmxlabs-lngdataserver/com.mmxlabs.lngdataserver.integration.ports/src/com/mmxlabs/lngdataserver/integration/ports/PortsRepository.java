/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.repo.AbstractGeneralDataRepository;

public class PortsRepository extends AbstractGeneralDataRepository<PortsVersion> {

	public static final @NonNull PortsRepository INSTANCE = new PortsRepository();

	private PortsRepository() {
		super(PortsTypeRecord.INSTANCE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final PortsVersion version) throws Exception {
		final String json = PortsIO.write(version);
		return uploadData(json);
	}

	public PortsVersion getLocalVersion(final String uuid) throws Exception {
		return doGetLocalVersion(uuid, PortsIO::read);
	}
}
