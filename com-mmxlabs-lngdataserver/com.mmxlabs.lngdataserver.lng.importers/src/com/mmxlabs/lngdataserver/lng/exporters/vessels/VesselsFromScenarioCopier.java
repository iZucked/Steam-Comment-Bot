/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.exporters.vessels;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.vessels.model.FuelConsumption;
import com.mmxlabs.lngdataserver.integration.vessels.model.Vessel;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselPortAttributes;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselRouteParameters;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselTravelAttributes;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;

public class VesselsFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsFromScenarioCopier.class);

	public static VesselsVersion generateVersion(final FleetModel fleetModel) {

		final VesselsVersion version = new VesselsVersion();

		final List<Vessel> vessels = new LinkedList<>();
		for (final com.mmxlabs.models.lng.fleet.Vessel lingo_vessel : fleetModel.getVessels()) {
			final Vessel vessel = new Vessel();
			vessels.add(vessel);

			vessel.setMmxId(lingo_vessel.getMmxId());
			vessel.setName(lingo_vessel.getName());
			vessel.setShortName(lingo_vessel.getShortName());
			vessel.setImo(lingo_vessel.getIMO());
			vessel.setNotes(lingo_vessel.getNotes());
			if (lingo_vessel.getType() != null) {
				vessel.setType(Optional.of(lingo_vessel.getType()));
			}

			if (lingo_vessel.isSetScnt()) {
				vessel.setScnt(OptionalInt.of(lingo_vessel.getScnt()));
			}
			if (lingo_vessel.isSetCapacity()) {
				vessel.setCapacity(OptionalInt.of(lingo_vessel.getCapacity()));
			}
			if (lingo_vessel.isSetFillCapacity()) {
				vessel.setFillCapacity(OptionalDouble.of(lingo_vessel.getFillCapacity()));
			}
			if (lingo_vessel.isSetBaseFuel()) {
				vessel.setTravelBaseFuel(Optional.of(lingo_vessel.getBaseFuel().getName()));
			}
			if (lingo_vessel.isSetIdleBaseFuel()) {
				vessel.setIdleBaseFuel(Optional.of(lingo_vessel.getIdleBaseFuel().getName()));
			}
			if (lingo_vessel.isSetInPortBaseFuel()) {
				vessel.setInPortBaseFuel(Optional.of(lingo_vessel.getInPortBaseFuel().getName()));
			}
			if (lingo_vessel.isSetPilotLightBaseFuel()) {
				vessel.setPilotLightBaseFuel(Optional.of(lingo_vessel.getPilotLightBaseFuel().getName()));
			}

			if (lingo_vessel.isSetSafetyHeel()) {
				vessel.setSafetyHeel(OptionalInt.of(lingo_vessel.getSafetyHeel()));
			}
			if (lingo_vessel.isSetCoolingVolume()) {
				vessel.setCoolingVolume(OptionalInt.of(lingo_vessel.getCoolingVolume()));
			}
			if (lingo_vessel.isSetWarmingTime()) {
				vessel.setWarmingTime(OptionalInt.of(lingo_vessel.getWarmingTime()));
			}

			if (lingo_vessel.isSetPilotLightRate()) {
				vessel.setPilotLightRate(OptionalDouble.of(lingo_vessel.getPilotLightRate()));
			}
			if (lingo_vessel.isSetMinSpeed()) {
				vessel.setMinSpeed(OptionalDouble.of(lingo_vessel.getMinSpeed()));
			}
			if (lingo_vessel.isSetMaxSpeed()) {
				vessel.setMaxSpeed(OptionalDouble.of(lingo_vessel.getMaxSpeed()));
			}
			// if (lingo_vessel.isSetPilotLightRate()) {
			vessel.setHasReliqCapacity(Optional.of(lingo_vessel.isHasReliqCapability()));
			// }

			// Laden Attribs
			setAttributes(lingo_vessel, lingo_vessel.getLadenAttributes(), vessel.getLoadAttributes(), vessel.getLadenAttributes());
			setAttributes(lingo_vessel, lingo_vessel.getBallastAttributes(), vessel.getDischargeAttributes(), vessel.getBallastAttributes());

			com.mmxlabs.models.lng.fleet.Vessel lingo_ReferenceVessel = lingo_vessel.getReference();
			if (lingo_ReferenceVessel != null) {
				if (lingo_ReferenceVessel.getMmxId() == null || lingo_ReferenceVessel.getMmxId().isEmpty()) {
					// Temporary fallback...
					vessel.setReferenceVessel(Optional.of(lingo_ReferenceVessel.getName()));
				} else {
					vessel.setReferenceVessel(Optional.of(lingo_ReferenceVessel.getMmxId()));
				}
			}

			// CANAL PARAMS
			if (lingo_vessel.isRouteParametersOverride() || lingo_ReferenceVessel == null) {
				final List<VesselRouteParameters> newParameters = new LinkedList<>();
				for (final com.mmxlabs.models.lng.fleet.VesselClassRouteParameters lingoFC : lingo_vessel.getRouteParameters()) {
					final VesselRouteParameters fc = new VesselRouteParameters();
					fc.setRoute(lingoFC.getRouteOption().toString());
					fc.setExtraTransitTimeInHours(lingoFC.getExtraTransitTime());
					fc.setLadenNBORate(lingoFC.getLadenNBORate());
					fc.setLadenBunkerRate(lingoFC.getLadenConsumptionRate());
					fc.setBallastNBORate(lingoFC.getBallastNBORate());
					fc.setBallastBunkerRate(lingoFC.getBallastConsumptionRate());
					newParameters.add(fc);
				}

				vessel.setRouteParameters(Optional.of(newParameters));
			}

			// ROUTE RESTRICTIONS
			if (lingo_vessel.isInaccessibleRoutesOverride() || lingo_ReferenceVessel == null) {
				final List<String> newRoutes = new LinkedList<>();
				for (final RouteOption rp : lingo_vessel.getInaccessibleRoutes()) {
					newRoutes.add(rp.getLiteral());
				}
				vessel.setInaccessibleRoutes(Optional.of(newRoutes));
			}

			// PORT RESTRICTIONS
			if (lingo_vessel.isInaccessiblePortsOverride() || lingo_ReferenceVessel == null) {
				final List<String> newPorts = new LinkedList<>();
				for (final APortSet<Port> port : lingo_vessel.getInaccessiblePorts()) {
					if (port instanceof Port) {
						newPorts.add(((Port) port).getLocation().getMmxId());
					} else {
						// log error
					}
				}
				vessel.setInaccessiblePorts(Optional.of(newPorts));
			}
		}

		String fleetDataVersion = fleetModel.getFleetDataVersion();
		if (fleetDataVersion == null) {
			fleetDataVersion = "private-" + EcoreUtil.generateUUID();
			fleetModel.setFleetDataVersion(fleetDataVersion);
		}
		version.setIdentifier(fleetDataVersion);
		version.setCreatedAt(LocalDateTime.now());
		version.setVessels(vessels);

		return version;
	}

	private static void setAttributes(final com.mmxlabs.models.lng.fleet.Vessel lingoVessel, final VesselStateAttributes lingoAttributes, final VesselPortAttributes portAttributes,
			final VesselTravelAttributes travelAttributes) {
		if (lingoAttributes.isSetInPortNBORate()) {
			portAttributes.setInPortNBORate(OptionalDouble.of(lingoAttributes.getInPortNBORate()));
		}
		if (lingoAttributes.isSetInPortBaseRate()) {
			portAttributes.setInPortBaseRate(OptionalDouble.of(lingoAttributes.getInPortBaseRate()));
		}

		if (lingoAttributes.isSetServiceSpeed()) {
			travelAttributes.setServiceSped(OptionalDouble.of(lingoAttributes.getServiceSpeed()));
		}
		if (lingoAttributes.isSetNboRate()) {
			travelAttributes.setNboRate(OptionalDouble.of(lingoAttributes.getNboRate()));
		}
		if (lingoAttributes.isSetIdleBaseRate()) {
			travelAttributes.setIdleBaseRate(OptionalDouble.of(lingoAttributes.getIdleBaseRate()));
		}
		if (lingoAttributes.isSetIdleNBORate()) {
			travelAttributes.setIdleNBORate(OptionalDouble.of(lingoAttributes.getIdleNBORate()));
		}

		if (lingoAttributes.isFuelConsumptionOverride() || lingoVessel.getReference() == null) {
			final List<FuelConsumption> newConsumptions = new LinkedList<>();
			for (final com.mmxlabs.models.lng.fleet.FuelConsumption lingoFC : lingoAttributes.getFuelConsumption()) {
				final FuelConsumption fc = new FuelConsumption();
				fc.setSpeed(lingoFC.getSpeed());
				fc.setConsumption(lingoFC.getConsumption());
				newConsumptions.add(fc);
			}

			travelAttributes.setFuelConsumption(Optional.of(newConsumptions));
		}
	}
}
