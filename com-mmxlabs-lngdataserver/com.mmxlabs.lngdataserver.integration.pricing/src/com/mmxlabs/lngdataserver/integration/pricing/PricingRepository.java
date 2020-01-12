/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.lngdataserver.integration.repo.general.AbstractGeneralDataRepository;

public class PricingRepository extends AbstractGeneralDataRepository<PricingVersion> {

	public static final @NonNull PricingRepository INSTANCE = new PricingRepository();

	private PricingRepository() {
		super(PricingTypeRecord.INSTANCE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final PricingVersion version) throws Exception {
		final String json = PricingIO.write(version);
		return uploadData(json);
	}

	public PricingVersion getLocalVersion(final String uuid) throws Exception   {
		return doGetLocalVersion(uuid, PricingIO::read);
	}

}
