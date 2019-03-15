/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.models.bunkerfuels.BunkerFuelsVersion;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupsVersion;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.io.bunkerfuels.BunkerFuelsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.PortFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.portgroups.PortGroupsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.vesselgroups.VesselGroupsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public final class LiNGODataExportHelper {

	private LiNGODataExportHelper() {

	}

	public static void exportLingoDataFiles(final @NonNull ScenarioInstance currentScenario, @NonNull final File target) throws IOException {

		@NonNull
		final ScenarioModelRecord sourceModelRecord = SSDataManager.Instance.getModelRecord(currentScenario);

		try (final IScenarioDataProvider sdp = sourceModelRecord.aquireScenarioDataProvider("LiNGODataExportHelper:1")) {
			final PortModel portModel = ScenarioModelUtil.getPortModel(sdp);
			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(sdp);

			final ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			{
				final DistancesVersion distancesVersion = DistancesFromScenarioCopier.generateVersion(portModel);
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File(target, "distances.json"), distancesVersion);
			}
			{
				final PortsVersion portsVersion = PortFromScenarioCopier.generateVersion(portModel);
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File(target, "ports.json"), portsVersion);
			}
			{
				final PortGroupsVersion portsVersion = PortGroupsFromScenarioCopier.generateVersion(portModel);
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File(target, "port-groups.json"), portsVersion);
			}
			{
				final VesselsVersion vesselsVersion = VesselsFromScenarioCopier.generateVersion(fleetModel);
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File(target, "vessels.json"), vesselsVersion);
			}
			{
				final BunkerFuelsVersion vesselsVersion = BunkerFuelsFromScenarioCopier.generateVersion(fleetModel);
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File(target, "bunker-fuels.json"), vesselsVersion);
			}
			{
				final VesselGroupsVersion vesselsVersion = VesselGroupsFromScenarioCopier.generateVersion(fleetModel);
				mapper.writerWithDefaultPrettyPrinter().writeValue(new File(target, "vessel-groups.json"), vesselsVersion);
			}
			
			// Other stuff
			{
				
			}
		}
	}
}
