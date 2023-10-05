/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.paper;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.paper.model.PaperVersion;
import com.mmxlabs.lngdataserver.integration.repo.general.AbstractGeneralDataRepository;

public class PaperRepository extends AbstractGeneralDataRepository<PaperVersion> {

	public static final @NonNull PaperRepository INSTANCE = new PaperRepository();

	private PaperRepository() {
		super(PaperTypeRecord.INSTANCE);
		startListenForNewLocalVersions();
	}

	@Override
	public boolean publishVersion(final PaperVersion version) throws Exception {
		final String json = PaperIO.write(version);
		return uploadData(json);
	}

	@Override
	public PaperVersion getLocalVersion(final String uuid) throws Exception   {
		return doGetLocalVersion(uuid, PaperIO::read);
	}
}
