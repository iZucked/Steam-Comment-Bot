/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.exporters.vessels;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataservice.client.vessel.model.Version;
import com.mmxlabs.lngdataservice.client.vessel.model.Vessel;
import com.mmxlabs.models.lng.fleet.FleetModel;

public class VesselsFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsFromScenarioCopier.class);

	public static Version generateVersion(FleetModel fleetModel) {

		Version version = new Version();

		List<Vessel> vessels = new LinkedList<>();
		for (com.mmxlabs.models.lng.fleet.Vessel lingo_vessel : fleetModel.getVessels()) {
			Vessel vessel = new Vessel();
			vessels.add(vessel);
		}

		String fleetDataVersion = fleetModel.getFleetDataVersion();
		if (fleetDataVersion == null) {
			fleetDataVersion = "private-" + EcoreUtil.generateUUID();
			fleetModel.setFleetDataVersion(fleetDataVersion);
		}
		version.setIdentifier(fleetDataVersion);
//		version.setCreatedAt(LocalDateTime.now());
		version.setVessels(vessels);

		return version;
	}
}
