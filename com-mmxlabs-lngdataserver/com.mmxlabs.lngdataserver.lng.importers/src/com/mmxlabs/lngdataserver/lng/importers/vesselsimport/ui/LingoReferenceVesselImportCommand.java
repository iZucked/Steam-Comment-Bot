/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.vesselsimport.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsIOConstants;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.ILingoReferenceVesselImportCommandProvider;
import com.mmxlabs.models.lng.fleet.util.VesselConstants;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.EncoderUtil;

public class LingoReferenceVesselImportCommand implements ILingoReferenceVesselImportCommandProvider {

	@Override
	public void run(FleetModel fleetModel, IMMXImportContext context) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());
		
		try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream(String.format("/%s", VesselsIOConstants.JSON_VESSELS_REFERENCE))) {
			final VesselsVersion v = mapper.readValue(inputStream, VesselsVersion.class);
			v.getVessels().stream().forEach(vessel -> {
				vessel.setName(VesselConstants.convertMMXReferenceNameToInternalName(EncoderUtil.decode(vessel.getName())));
				vessel.setIsReference(Optional.of(Boolean.TRUE));
				vessel.setMmxReference(Optional.of(Boolean.TRUE));
			});
			final String versionId = v.getIdentifier();
			if (versionId == null || versionId.isBlank()) {
				throw new IllegalStateException("Vessels version identifier must be present and non-blank.");
			}
			fleetModel.setMMXVesselDBVersion(versionId);
			final List<Vessel> referenceVessels = VesselsToScenarioCopier.doDirectReferenceVesselImport(v, context);
			if (!referenceVessels.isEmpty()) {
				fleetModel.getVessels().addAll(referenceVessels);
			}
			
		}
	}

}
