package com.mmxlabs.lngdataserver.lng.importers.vesselgroups;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.lngdataserver.integration.general.model.vesselgroups.VesselGroupDefinition;
import com.mmxlabs.lngdataserver.integration.general.model.vesselgroups.VesselGroupsVersion;
import com.mmxlabs.lngdataserver.integration.general.model.vesselgroups.VesselTypeConstants;
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
