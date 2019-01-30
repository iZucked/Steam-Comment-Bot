package com.mmxlabs.lngdataserver.integration.general.model;

import com.mmxlabs.lngdataserver.integration.general.repo.AbstractGenericDataRepository;

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
