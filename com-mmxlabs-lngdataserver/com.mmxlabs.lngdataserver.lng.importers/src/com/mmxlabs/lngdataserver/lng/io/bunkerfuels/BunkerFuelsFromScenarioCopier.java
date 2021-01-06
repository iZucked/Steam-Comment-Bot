/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.bunkerfuels;

import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelDefinition;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsVersion;
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
