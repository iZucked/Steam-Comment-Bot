/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.financial.settled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;

public class SettledPricesRepository extends AbstractGenericDataRepository<SettledPricesVersion> {

	private static final Logger LOG = LoggerFactory.getLogger(SettledPricesRepository.class);

	public static final SettledPricesRepository INSTANCE = new SettledPricesRepository();

	private SettledPricesRepository() {
		super(SettledPriceIO.MODEL_TYPE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final SettledPricesVersion version) throws Exception {
		return doPublishOutputStream(version, version.getIdentifier(), SettledPriceIO::write);
	}

	public SettledPricesVersion getLocalVersion(final String versionTag) throws Exception {
		return doGetLocalVersion(versionTag, SettledPriceIO::read);
	}
}
