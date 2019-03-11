package com.mmxlabs.lngdataserver.integration.models.bunkerfuels;

import java.io.IOException;

import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;

public class BunkerFuelsRepository extends AbstractGenericDataRepository<BunkerFuelsVersion> {

	public static final BunkerFuelsRepository INSTANCE = new BunkerFuelsRepository();

	private BunkerFuelsRepository() {
		super(BunkerFuelsIO.MODEL_TYPE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final BunkerFuelsVersion version) throws Exception {
		return doPublishOutputStream(version, version.getIdentifier(), BunkerFuelsIO::write);
	}

	public BunkerFuelsVersion getLocalVersion(final String versionTag) throws Exception {
		return doGetLocalVersion(versionTag, BunkerFuelsIO::read);
	}
}
