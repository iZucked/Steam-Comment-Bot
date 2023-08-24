/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.paper;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.paper.PricingIO;
import com.mmxlabs.lngdataserver.integration.paper.PricingRepository;
import com.mmxlabs.lngdataserver.integration.paper.PricingTypeRecord;
import com.mmxlabs.lngdataserver.integration.paper.model.PaperVersion;
import com.mmxlabs.lngdataserver.integration.repo.general.AbstractGeneralDataRepository;

public class PricingRepository extends AbstractGeneralDataRepository<PaperVersion> {

	public static final @NonNull PricingRepository INSTANCE = new PricingRepository();

	private PricingRepository() {
		super(PricingTypeRecord.INSTANCE);
		startListenForNewLocalVersions();
	}

	@Override
	public boolean publishVersion(final PaperVersion version) throws Exception {
		final String json = PricingIO.write(version);
		return uploadData(json);
	}

	@Override
	public PaperVersion getLocalVersion(final String uuid) throws Exception   {
		return doGetLocalVersion(uuid, PricingIO::read);
	}
}
