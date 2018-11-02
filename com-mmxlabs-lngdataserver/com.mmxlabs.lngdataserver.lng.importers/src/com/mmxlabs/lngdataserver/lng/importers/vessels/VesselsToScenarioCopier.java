/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.vessels;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.vessels.model.FuelConsumption;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselRouteParameters;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class VesselsToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsToScenarioCopier.class);

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final FleetModel fleetModel, final PortModel portModel, final VesselsVersion vesselsVersion) {

		final CompoundCommand cc = new CompoundCommand("Update Vessel");
		final Map<String, Vessel> vesselsMap = new HashMap<>();

		// Ignore vessel in current schedule without mmxId
		for (final Vessel vessel : fleetModel.getVessels()) {
			final String mmxId = vessel.getMmxId();
			if (mmxId != null && !mmxId.isEmpty()) {
				vesselsMap.put(mmxId, vessel);
			}
		}

		final Map<String, Port> portProvider = portModel.getPorts().stream() //
				.filter(p -> p.getLocation() != null) //
				.filter(p -> p.getLocation().getMmxId() != null) //
				.collect(Collectors.toMap(p -> p.getLocation().getMmxId(), Function.identity()));

		final Map<String, BaseFuel> baseFuels = new HashMap<>(fleetModel.getBaseFuels().stream() //
				.collect(Collectors.toMap(BaseFuel::getName, Function.identity())));

		// Create new vessels if needed.
		vesselsVersion.getVessels().stream() //
				.filter(v -> v.getMmxId() != null && !v.getMmxId().isEmpty()) //
				.filter(v -> !vesselsMap.containsKey(v.getMmxId()))//
				.forEach(v -> {
					final Vessel newVessel = FleetFactory.eINSTANCE.createVessel();
					newVessel.setMmxId(v.getMmxId());
					newVessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					newVessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					cc.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, newVessel));
					vesselsMap.put(newVessel.getMmxId(), newVessel);
				});

		// Create all vessel update
		for (final com.mmxlabs.lngdataserver.integration.vessels.model.@NonNull Vessel upstreamVessel : vesselsVersion.getVessels()) {
			if (upstreamVessel.getMmxId() == null || upstreamVessel.getMmxId().isEmpty()) {
				throw new IllegalStateException("Upstream vessel has no mmx id");
			}

			final Vessel vesselToUpdate = vesselsMap.get(upstreamVessel.getMmxId());
			if (upstreamVessel.getReferenceVessel().isPresent()) {
				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__REFERENCE, vesselsMap.get(upstreamVessel.getReferenceVessel().get())));
			} else {
				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__REFERENCE, SetCommand.UNSET_VALUE));
			}

			// Vessel Model
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MMX_ID, upstreamVessel.getMmxId()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, MMXCorePackage.Literals.NAMED_OBJECT__NAME, upstreamVessel.getName()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__SHORT_NAME, upstreamVessel.getShortName()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__IMO, upstreamVessel.getImo()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__NOTES, upstreamVessel.getNotes()));

			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__SCNT, upstreamVessel.getScnt());
			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__CAPACITY, upstreamVessel.getCapacity());
			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__FILL_CAPACITY, upstreamVessel.getFillCapacity());

			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__SAFETY_HEEL, upstreamVessel.getSafetyHeel());
			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__COOLING_VOLUME, upstreamVessel.getCoolingVolume());
			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__WARMING_TIME, upstreamVessel.getWarmingTime());

			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__PILOT_LIGHT_RATE, upstreamVessel.getPilotLightRate());
			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MAX_SPEED, upstreamVessel.getMaxSpeed());
			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MIN_SPEED, upstreamVessel.getMinSpeed());
			createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY, upstreamVessel.getHasReliqCapacity());

			createSetFuelCommand(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__BASE_FUEL, upstreamVessel.getTravelBaseFuel(), baseFuels, fleetModel);
			createSetFuelCommand(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__IDLE_BASE_FUEL, upstreamVessel.getIdleBaseFuel(), baseFuels, fleetModel);
			createSetFuelCommand(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__PILOT_LIGHT_BASE_FUEL, upstreamVessel.getPilotLightBaseFuel(), baseFuels, fleetModel);
			createSetFuelCommand(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__IN_PORT_BASE_FUEL, upstreamVessel.getInPortBaseFuel(), baseFuels, fleetModel);

			// Ballast Model
			createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, upstreamVessel.getBallastAttributes().getIdleBaseRate());
			createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, upstreamVessel.getBallastAttributes().getIdleNBORate());
			createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, upstreamVessel.getBallastAttributes().getIdleNBORate());
			createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, upstreamVessel.getBallastAttributes().getServiceSped());
			createSetFuelConsumptions(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.FUEL_CONSUMPTION__CONSUMPTION,
					upstreamVessel.getBallastAttributes().getFuelConsumption());

			createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE,
					upstreamVessel.getDischargeAttributes().getInPortBaseRate());
			createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE,
					upstreamVessel.getDischargeAttributes().getInPortNBORate());

			// Laden Model
			createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, upstreamVessel.getLadenAttributes().getIdleBaseRate());
			createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, upstreamVessel.getLadenAttributes().getIdleNBORate());
			createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, upstreamVessel.getLadenAttributes().getIdleNBORate());
			createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, upstreamVessel.getLadenAttributes().getServiceSped());
			createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, upstreamVessel.getLoadAttributes().getInPortBaseRate());
			createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE, upstreamVessel.getLoadAttributes().getInPortNBORate());

			createSetFuelConsumptions(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.FUEL_CONSUMPTION__CONSUMPTION,
					upstreamVessel.getLadenAttributes().getFuelConsumption());

			// CANAL PARAMS
			createSetRouteParameters(cc, editingDomain, vesselToUpdate, upstreamVessel.getRouteParameters());

			// ROUTE RESTRICTIONS
			final Optional<List<String>> inaccessibleRoutes = upstreamVessel.getInaccessibleRoutes();
			if (inaccessibleRoutes.isPresent()) {
				final List<String> routeIds = inaccessibleRoutes.get();
				final List<RouteOption> mappedRoutes = new LinkedList<>();
				for (final String routeName : routeIds) {
					final RouteOption ro = RouteOption.valueOf(routeName);
					mappedRoutes.add(ro);
				}

				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE, Boolean.TRUE));
				if (!vesselToUpdate.getInaccessibleRoutes().isEmpty()) {
					cc.append(AddCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES, vesselToUpdate.getInaccessibleRoutes()));
				}
				cc.append(AddCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES, mappedRoutes));
			} else {
				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE, Boolean.FALSE));
				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES, SetCommand.UNSET_VALUE));
			}

			// PORT RESTRICTIONS
			final Optional<List<String>> inaccessiblePorts = upstreamVessel.getInaccessiblePorts();
			if (inaccessiblePorts.isPresent()) {
				final List<String> portIds = inaccessiblePorts.get();
				final List<APortSet<Port>> mappedPorts = new LinkedList<>();
				for (final String portMMXId : portIds) {
					final Port p = portProvider.get(portMMXId);
					if (p != null) {
						mappedPorts.add(p);
					} else {
						// Log error
					}
				}

				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE, Boolean.TRUE));
				if (!vesselToUpdate.getInaccessiblePorts().isEmpty()) {
					cc.append(AddCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, vesselToUpdate.getInaccessiblePorts()));
				}
				cc.append(AddCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, mappedPorts));
			} else {
				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE, Boolean.FALSE));
				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, SetCommand.UNSET_VALUE));
			}
		}
		return cc;
	}

	private static void createSetFuelConsumptions(final CompoundCommand cc, @NonNull final EditingDomain editingDomain, final VesselStateAttributes attributes, final EStructuralFeature feature,
			final Optional<List<FuelConsumption>> fuelConsumptions) {

		if (!attributes.getFuelConsumption().isEmpty()) {
			cc.append(SetCommand.create(editingDomain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, Boolean.FALSE));
			cc.append(RemoveCommand.create(editingDomain, attributes, feature, attributes.getFuelConsumption()));
		}
		final List<com.mmxlabs.models.lng.fleet.FuelConsumption> newConsumptions = new LinkedList<>();
		if (fuelConsumptions.isPresent()) {
			for (final FuelConsumption upstreamFC : fuelConsumptions.get()) {
				final com.mmxlabs.models.lng.fleet.FuelConsumption newFC = FleetFactory.eINSTANCE.createFuelConsumption();
				newFC.setSpeed(upstreamFC.getSpeed());
				newFC.setConsumption(upstreamFC.getConsumption());
				newConsumptions.add(newFC);
			}
		}
		if (!newConsumptions.isEmpty()) {
			cc.append(SetCommand.create(editingDomain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, Boolean.TRUE));
			cc.append(AddCommand.create(editingDomain, attributes, feature, newConsumptions));
		}

	}

	private static void createSetRouteParameters(final CompoundCommand cc, @NonNull final EditingDomain editingDomain, final Vessel vesselToUpdate,
			final Optional<List<VesselRouteParameters>> routeParameters) {

		if (!vesselToUpdate.getRouteParameters().isEmpty()) {
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE, Boolean.FALSE));
			cc.append(RemoveCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS, vesselToUpdate.getRouteParameters()));
		}
		final List<VesselClassRouteParameters> newParameters = new LinkedList<>();
		if (routeParameters.isPresent()) {
			for (final VesselRouteParameters upstreamFC : routeParameters.get()) {
				final com.mmxlabs.models.lng.fleet.VesselClassRouteParameters newFC = FleetFactory.eINSTANCE.createVesselClassRouteParameters();
				newFC.setRouteOption(RouteOption.valueOf(upstreamFC.getRoute()));
				newFC.setExtraTransitTime(upstreamFC.getExtraTransitTimeInHours());
				newFC.setLadenNBORate(upstreamFC.getLadenNBORate());
				newFC.setLadenConsumptionRate(upstreamFC.getLadenBunkerRate());
				newFC.setBallastNBORate(upstreamFC.getBallastNBORate());
				newFC.setBallastConsumptionRate(upstreamFC.getBallastBunkerRate());
				newParameters.add(newFC);
			}
		}
		if (!newParameters.isEmpty()) {
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE, Boolean.TRUE));
			cc.append(AddCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS, newParameters));
		}

	}

	private static void createSet(final CompoundCommand cc, final EditingDomain editingDomain, final EObject owner, final EStructuralFeature feature, final Optional<?> value) {
		if (value.isPresent()) {
			if (feature.isMany()) {
				cc.append(AddCommand.create(editingDomain, owner, feature, value.get()));
			} else {
				cc.append(SetCommand.create(editingDomain, owner, feature, value.get()));
			}
		} else {
			cc.append(SetCommand.create(editingDomain, owner, feature, SetCommand.UNSET_VALUE));
		}
	}

	private static void createSet(final CompoundCommand cc, final EditingDomain editingDomain, final EObject owner, final EStructuralFeature feature, final OptionalDouble value) {
		if (value.isPresent()) {
			cc.append(SetCommand.create(editingDomain, owner, feature, value.getAsDouble()));
		} else {
			cc.append(SetCommand.create(editingDomain, owner, feature, SetCommand.UNSET_VALUE));
		}
	}

	private static void createSet(final CompoundCommand cc, final EditingDomain editingDomain, final EObject owner, final EStructuralFeature feature, final OptionalInt value) {
		if (value.isPresent()) {
			cc.append(SetCommand.create(editingDomain, owner, feature, value.getAsInt()));
		} else {
			cc.append(SetCommand.create(editingDomain, owner, feature, SetCommand.UNSET_VALUE));
		}
	}

	private static void createSetFuelCommand(final CompoundCommand cc, final EditingDomain editingDomain, final EObject owner, final EStructuralFeature feature, final Optional<String> value,
			final Map<String, BaseFuel> baseFuelMap, final FleetModel fleetModel) {
		if (value.isPresent()) {
			final BaseFuel bf = baseFuelMap.computeIfAbsent(value.get(), name -> {
				final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
				baseFuel.setName(name);
				cc.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__BASE_FUELS, baseFuel));
				return baseFuel;
			});
			cc.append(SetCommand.create(editingDomain, owner, feature, bf));
		} else {
			cc.append(SetCommand.create(editingDomain, owner, feature, SetCommand.UNSET_VALUE));
		}
	}
}
