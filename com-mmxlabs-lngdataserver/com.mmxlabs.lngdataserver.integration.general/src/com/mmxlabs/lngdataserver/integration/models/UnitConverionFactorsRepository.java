/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models;

import com.mmxlabs.lngdataserver.integration.repo.generic.AbstractGenericDataRepository;

public class UnitConverionFactorsRepository extends AbstractGenericDataRepository<UnitConversionFactorsVersion> {

	public static final UnitConverionFactorsRepository INSTANCE = new UnitConverionFactorsRepository();

	private UnitConverionFactorsRepository() {
		super(UnitConversionFactorsIO.MODEL_TYPE);
		startListenForNewLocalVersions();
	}

	public boolean publishVersion(final UnitConversionFactorsVersion version) throws Exception {
		return doPublishOutputStream(version, version.getIdentifier(), UnitConversionFactorsIO::write);
	}

	public UnitConversionFactorsVersion getLocalVersion(final String versionTag) throws Exception {
		return doGetLocalVersion(versionTag, UnitConversionFactorsIO::read);
	}
}
