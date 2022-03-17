/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.vesselgroups;

import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupDefinition;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsVersion;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselTypeConstants;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.types.AVesselSet;

public class VesselGroupsFromScenarioCopier {
	private VesselGroupsFromScenarioCopier() {

	}

	public static VesselGroupsVersion generateVersion(final FleetModel fleetModel) {

		final VesselGroupsVersion version = new VesselGroupsVersion();
		version.setIdentifier(fleetModel.getVesselGroupVersionRecord().getVersion());
		version.setCreatedBy(fleetModel.getVesselGroupVersionRecord().getCreatedBy());
		version.setCreatedAt(fleetModel.getVesselGroupVersionRecord().getCreatedAt());

		for (final VesselGroup vesselGroup : fleetModel.getVesselGroups()) {
			final VesselGroupDefinition def = new VesselGroupDefinition();
			def.setName(vesselGroup.getName());

			for (AVesselSet<Vessel> entry : vesselGroup.getVessels()) {
				if (entry instanceof Vessel) {
					def.getEntries().add(VesselTypeConstants.VESSEL_PREFIX + entry.getName());
				} else if (entry instanceof VesselGroup) {
					def.getEntries().add(VesselTypeConstants.VESSEL_GROUP_PREFIX + entry.getName());
				} else {
					throw new IllegalStateException("Unknown vessel set type");
				}
			}

			version.getGroups().add(def);
		}

		return version;
	}
}
