/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.bunkerfuels;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.lngdataserver.integration.general.model.bunkerfuels.BunkerFuelDefinition;
import com.mmxlabs.lngdataserver.integration.general.model.bunkerfuels.BunkerFuelsVersion;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;

public class BunkerFuelsFromScenarioCopier {
	private BunkerFuelsFromScenarioCopier() {

	}

	public static BunkerFuelsVersion generateVersion(final FleetModel fleetModel) {

		final BunkerFuelsVersion version = new BunkerFuelsVersion();

		version.setIdentifier(fleetModel.getBunkerFuelsVersionRecord().getVersion());
		version.setCreatedBy(fleetModel.getBunkerFuelsVersionRecord().getCreatedBy());
		version.setCreatedAt(fleetModel.getBunkerFuelsVersionRecord().getCreatedAt());

		for (final BaseFuel baseFuel : fleetModel.getBaseFuels()) {
			final BunkerFuelDefinition def = new BunkerFuelDefinition();
			def.setName(baseFuel.getName());
			def.setEquivalenceFactor(baseFuel.getEquivalenceFactor());

			version.getFuels().add(def);
		}

		return version;
	}
}
