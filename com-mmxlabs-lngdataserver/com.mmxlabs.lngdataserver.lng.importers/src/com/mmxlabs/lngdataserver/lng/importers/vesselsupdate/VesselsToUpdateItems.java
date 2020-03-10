/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.vesselsupdate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortTypeConstants;
import com.mmxlabs.lngdataserver.integration.vessels.model.FuelConsumption;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselTravelAttributes;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItem;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateStep;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateWarning;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsFromScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.types.APortSet;

public class VesselsToUpdateItems {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsToUpdateItems.class);

	public static List<UpdateItem> generateUpdates(final EditingDomain editingDomain, final PortModel portModel, final FleetModel fleetModel, final VesselsVersion vesselRecords) {

		final List<UpdateItem> steps = new LinkedList<>();
		final Map<String, Vessel> allVessels = new HashMap<>();
		for (final Vessel v : fleetModel.getVessels()) {
			allVessels.put(v.getName(), v);
		}

		final Map<String, APortSet<Port>> portTypeMap = new HashMap<>();

		portModel.getPorts().forEach(c -> portTypeMap.put(PortTypeConstants.PORT_PREFIX + c.getLocation().getMmxId(), c));
		portModel.getPortCountryGroups().forEach(c -> portTypeMap.put(PortTypeConstants.COUNTRY_GROUP_PREFIX + c.getName(), c));
		portModel.getPortGroups().forEach(c -> portTypeMap.put(PortTypeConstants.PORT_GROUP_PREFIX + c.getName(), c));

		final Map<String, BaseFuel> baseFuels = new HashMap<>(fleetModel.getBaseFuels().stream() //
				.collect(Collectors.toMap(BaseFuel::getName, Function.identity())));

		for (final var upstreamVessel : vesselRecords.getVessels()) {

			final Vessel v = allVessels.get(upstreamVessel.getName());

			boolean vesselToAdd = false;
			final Vessel vesselToUpdate;
			if (v == null) {
				// New vessel

				final Vessel newVessel = FleetFactory.eINSTANCE.createVessel();
				newVessel.setName(upstreamVessel.getName());
				newVessel.setShortName(upstreamVessel.getShortName());
				newVessel.setMmxId(upstreamVessel.getMmxId());
				newVessel.setIMO(upstreamVessel.getImo());
				newVessel.setNotes(upstreamVessel.getNotes());

				newVessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
				newVessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());

//					cc.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, newVessel));
				vesselToUpdate = newVessel;

				vesselToAdd = true;
			} else {
				// Existing
				vesselToUpdate = v;
				try {
					final ObjectMapper mapper = new ObjectMapper();
					mapper.registerModule(new Jdk8Module());

					final var tmpVessel = VesselsFromScenarioCopier.createModelVessel(v);

					// Sort fuel curves for comparison stability.
					sort(upstreamVessel.getLadenAttributes());
					sort(upstreamVessel.getBallastAttributes());

					sort(tmpVessel.getLadenAttributes());
					sort(tmpVessel.getBallastAttributes());

					final String newRecord = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(upstreamVessel);
					final String existingRecord = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tmpVessel);

					if (Objects.equals(existingRecord, newRecord)) {
						continue;
					}

				} catch (final Exception e) {
					e.printStackTrace();
				}

			}
			final Consumer<CompoundCommand> updateEverything = cc -> {
				VesselsToScenarioCopier.updateVessel(cc, editingDomain, vesselToUpdate, upstreamVessel, baseFuels, portTypeMap, fleetModel);
			};

			if (vesselToAdd) {
				final UpdateStep step2 = new UpdateStep(String.format("Adding new reference vessel called %s", upstreamVessel.getName()), cmd -> {
					cmd.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, vesselToUpdate));
					updateEverything.accept(cmd);
				});
				steps.add(step2);
			} else {
				final UpdateStep step2 = new UpdateWarning(String.format("Differences in reference vessel called %s", upstreamVessel.getName()), "Update?", updateEverything);
				steps.add(step2);
			}
		}

		return steps;
	}

	private static void sort(final VesselTravelAttributes attributes) {
		if (attributes.getFuelConsumption().isPresent()) {
			final List<FuelConsumption> list = attributes.getFuelConsumption().get();
			list.sort((a, b) -> Double.compare(a.getSpeed(), b.getSpeed()));
		}
	}

}
