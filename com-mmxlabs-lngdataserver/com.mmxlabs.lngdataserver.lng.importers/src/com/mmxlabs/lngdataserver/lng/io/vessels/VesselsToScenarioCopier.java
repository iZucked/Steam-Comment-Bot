/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.vessels;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortTypeConstants;
import com.mmxlabs.lngdataserver.integration.vessels.model.FuelConsumption;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselRouteParameters;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.VersionRecord;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class VesselsToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsToScenarioCopier.class);


	public static List<Vessel> doDirectReferenceVesselImport(final VesselsVersion vesselsVersion, IMMXImportContext context) {
		final List<Vessel> referenceVessels = new LinkedList<>();
		for (final com.mmxlabs.lngdataserver.integration.vessels.model.@NonNull Vessel upstreamVessel : vesselsVersion.getVessels()) {
			final Vessel newVessel = FleetFactory.eINSTANCE.createVessel();
			newVessel.setName(upstreamVessel.getName());
			newVessel.setMmxId(upstreamVessel.getMmxId());
			newVessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
			newVessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
			updateVessel(newVessel, upstreamVessel, context);
			referenceVessels.add(newVessel);
			context.registerNamedObject(newVessel);
		}
		return referenceVessels;
	}

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final FleetModel fleetModel, final PortModel portModel, final VesselsVersion vesselsVersion) {

		final CompoundCommand cc = new CompoundCommand("Update Vessel");
		final Map<String, Vessel> vesselsByMMXIdMap = new HashMap<>();
		final Map<String, Vessel> vesselsByNameMap = new HashMap<>();

		final List<Vessel> updatedVessels = new LinkedList<>();
		for (final Vessel vessel : fleetModel.getVessels()) {
			final String mmxId = vessel.getMmxId();
			if (mmxId != null && !mmxId.isEmpty()) {
				// Ignore vessel in current schedule without mmxId
				vesselsByMMXIdMap.put(mmxId, vessel);
			}
			vesselsByNameMap.put(vessel.getName(), vessel);
		}

		final Map<String, APortSet<Port>> portTypeMap = new HashMap<>();

		portModel.getPorts().forEach(c -> portTypeMap.put(PortTypeConstants.PORT_PREFIX + c.getLocation().getMmxId(), c));
		portModel.getPortCountryGroups().forEach(c -> portTypeMap.put(PortTypeConstants.COUNTRY_GROUP_PREFIX + c.getName(), c));
		portModel.getPortGroups().forEach(c -> portTypeMap.put(PortTypeConstants.PORT_GROUP_PREFIX + c.getName(), c));

		final Map<String, BaseFuel> baseFuels = new HashMap<>(fleetModel.getBaseFuels().stream() //
				.collect(Collectors.toMap(BaseFuel::getName, Function.identity())));

		// Create new vessels if needed.
		vesselsVersion.getVessels().stream() //
				.filter(v -> v.getMmxId() != null && !v.getMmxId().isEmpty()) //
				.filter(v -> !vesselsByMMXIdMap.containsKey(v.getMmxId()))//
				.forEach(v -> {
					final Vessel newVessel = FleetFactory.eINSTANCE.createVessel();
					newVessel.setName(v.getName());
					newVessel.setMmxId(v.getMmxId());
					newVessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					newVessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					cc.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, newVessel));
					vesselsByMMXIdMap.put(newVessel.getMmxId(), newVessel);
				});
		vesselsVersion.getVessels().stream() //
				.filter(v -> v.getMmxId() == null || v.getMmxId().isEmpty()) //
				.filter(v -> !vesselsByNameMap.containsKey(v.getName()))//
				.forEach(v -> {
					final Vessel newVessel = FleetFactory.eINSTANCE.createVessel();
					newVessel.setName(v.getName());
					newVessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					newVessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
					cc.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, newVessel));
					vesselsByNameMap.put(newVessel.getName(), newVessel);
				});

		// Create all vessel update
		for (final com.mmxlabs.lngdataserver.integration.vessels.model.@NonNull Vessel upstreamVessel : vesselsVersion.getVessels()) {
			Vessel vesselToUpdate;
			if (upstreamVessel.getMmxId() == null || upstreamVessel.getMmxId().isEmpty()) {
				// throw new IllegalStateException("Upstream vessel has no mmx id");
				vesselToUpdate = vesselsByNameMap.get(upstreamVessel.getName());
			} else {
				vesselToUpdate = vesselsByMMXIdMap.get(upstreamVessel.getMmxId());
			}

			// final Vessel vesselToUpdate =
			// vesselsByMMXIdMap.get(upstreamVessel.getMmxId());
			if (upstreamVessel.getReferenceVessel().isPresent()) {
				final String id = upstreamVessel.getReferenceVessel().get();
				if (vesselsByMMXIdMap.containsKey(id)) {
					cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__REFERENCE, vesselsByMMXIdMap.get(id)));
				} else {
					cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__REFERENCE, vesselsByNameMap.get(id)));
				}
			} else {
				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__REFERENCE, SetCommand.UNSET_VALUE));
			}

			updateVessel(cc, editingDomain, vesselToUpdate, upstreamVessel, baseFuels, portTypeMap, fleetModel);

			updatedVessels.add(vesselToUpdate);
		}

		final Set<Vessel> vesselsToDelete = new HashSet<>(fleetModel.getVessels());
		vesselsToDelete.removeAll(updatedVessels);

		if (!vesselsToDelete.isEmpty()) {
			cc.append(DeleteCommand.create(editingDomain, vesselsToDelete));
		}

		VersionRecord record = fleetModel.getFleetVersionRecord();
		cc.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, vesselsVersion.getCreatedBy()));
		cc.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, vesselsVersion.getCreatedAt()));
		cc.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, vesselsVersion.getIdentifier()));

		return cc;
	}

	public static void updateVessel(Vessel vesselToUpdate, com.mmxlabs.lngdataserver.integration.vessels.model.@NonNull Vessel upstreamVessel, IMMXImportContext context) {
		vesselToUpdate.setShortName(upstreamVessel.getShortName());
		vesselToUpdate.setIMO(upstreamVessel.getImo());
		vesselToUpdate.setNotes(upstreamVessel.getNotes());

		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__TYPE, upstreamVessel.getType());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__SCNT, upstreamVessel.getScnt());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__CAPACITY, upstreamVessel.getCapacity());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__FILL_CAPACITY, upstreamVessel.getFillCapacity());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__SAFETY_HEEL, upstreamVessel.getSafetyHeel());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__COOLING_VOLUME, upstreamVessel.getCoolingVolume());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__WARMING_TIME, upstreamVessel.getWarmingTime());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__PURGE_TIME, upstreamVessel.getPurgeTime());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__PILOT_LIGHT_RATE, upstreamVessel.getPilotLightRate());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__MAX_SPEED, upstreamVessel.getMaxSpeed());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__MIN_SPEED, upstreamVessel.getMinSpeed());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY, upstreamVessel.getHasReliqCapacity());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__REFERENCE_VESSEL, upstreamVessel.getIsReference());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__MMX_REFERENCE, upstreamVessel.getMmxReference());
		directSet(vesselToUpdate, FleetPackage.Literals.VESSEL__MARKER, upstreamVessel.getIsMarker());

		
		deferSetFuel(vesselToUpdate, FleetPackage.Literals.VESSEL__BASE_FUEL, upstreamVessel.getTravelBaseFuel(), context);
		deferSetFuel(vesselToUpdate, FleetPackage.Literals.VESSEL__IDLE_BASE_FUEL, upstreamVessel.getIdleBaseFuel(), context);
		deferSetFuel(vesselToUpdate, FleetPackage.Literals.VESSEL__PILOT_LIGHT_BASE_FUEL, upstreamVessel.getPilotLightBaseFuel(), context);
		deferSetFuel(vesselToUpdate, FleetPackage.Literals.VESSEL__IN_PORT_BASE_FUEL, upstreamVessel.getInPortBaseFuel(), context);

		directSet(vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, upstreamVessel.getBallastAttributes().getIdleBaseRate());
		directSet(vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, upstreamVessel.getBallastAttributes().getIdleNBORate());
		directSet(vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, upstreamVessel.getBallastAttributes().getNboRate());
		directSet(vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, upstreamVessel.getBallastAttributes().getServiceSpeed());
		directSet(vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, upstreamVessel.getDischargeAttributes().getInPortBaseRate());
		directSet(vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE, upstreamVessel.getDischargeAttributes().getInPortNBORate());
		directSetFuelConsumptions(vesselToUpdate.getBallastAttributes(), upstreamVessel.getBallastAttributes().getFuelConsumption());

		directSet(vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, upstreamVessel.getLadenAttributes().getIdleBaseRate());
		directSet(vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, upstreamVessel.getLadenAttributes().getIdleNBORate());
		directSet(vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, upstreamVessel.getLadenAttributes().getNboRate());
		directSet(vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, upstreamVessel.getLadenAttributes().getServiceSpeed());
		directSet(vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, upstreamVessel.getLoadAttributes().getInPortBaseRate());
		directSet(vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE, upstreamVessel.getLoadAttributes().getInPortNBORate());
		directSetFuelConsumptions(vesselToUpdate.getLadenAttributes(), upstreamVessel.getLadenAttributes().getFuelConsumption());

		directSetRouteParameters(vesselToUpdate, upstreamVessel.getRouteParameters());

		final Optional<List<String>> inaccessibleRoutes = upstreamVessel.getInaccessibleRoutes();
		if (inaccessibleRoutes.isPresent()) {
			final List<String> routeIds = inaccessibleRoutes.get();
			final List<RouteOption> mappedRoutes = new LinkedList<>();
			for (final String routeName : routeIds) {
				final RouteOption ro = RouteOption.valueOf(routeName);
				mappedRoutes.add(ro);
			}
			vesselToUpdate.setInaccessibleRoutesOverride(true);
			if (!vesselToUpdate.getInaccessibleRoutes().isEmpty()) {
				vesselToUpdate.getInaccessibleRoutes().clear();
			}
			if (!mappedRoutes.isEmpty()) {
				vesselToUpdate.getInaccessibleRoutes().addAll(mappedRoutes);
			}
		} else {
			vesselToUpdate.setInaccessibleRoutesOverride(false);
		}

		final Optional<List<String>> inaccessiblePorts = upstreamVessel.getInaccessiblePorts();
		if (inaccessiblePorts.isPresent()) {
			context.doLater(new IDeferment() {

				@Override
				public void run(@NonNull IImportContext importContext) {
					final IMMXImportContext context = (IMMXImportContext) importContext;
					final MMXRootObject rootObject = context.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

						final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
						final List<APortSet<Port>> mappedPorts = new LinkedList<>();
						final List<String> portIds = inaccessiblePorts.get();
						portIds.forEach(portMMXId -> {
							portModel.getPorts().stream() //
									.filter(c-> (PortTypeConstants.PORT_PREFIX+c.getLocation().getMmxId()).equals(portMMXId)) //
									.findAny() //
									.ifPresentOrElse(mappedPorts::add, () -> {
										portModel.getPortCountryGroups().stream() //
												.filter(c -> (PortTypeConstants.COUNTRY_GROUP_PREFIX+c.getName()).equals(portMMXId)) //
												.findAny() //
												.ifPresentOrElse(mappedPorts::add, () -> {
													portModel.getPortGroups().stream() //
													.filter(c -> (PortTypeConstants.PORT_GROUP_PREFIX+c.getName()).equals(portMMXId)) //
													.findAny() //
													.ifPresent(mappedPorts::add);
												});
									});
						});

						vesselToUpdate.setInaccessiblePortsOverride(true);
						if (!vesselToUpdate.getInaccessiblePorts().isEmpty()) {
							vesselToUpdate.getInaccessiblePorts().clear();
						}
						if (!mappedPorts.isEmpty()) {
							vesselToUpdate.getInaccessiblePorts().addAll(mappedPorts);
						}
					}
				}

				@Override
				public int getStage() {
					return IMMXImportContext.STAGE_RESOLVE_REFERENCES;
				}
			});
		} else {
			vesselToUpdate.setInaccessiblePortsOverride(false);
		}
	}

	public static void updateVessel(CompoundCommand cc, EditingDomain editingDomain, Vessel vesselToUpdate, com.mmxlabs.lngdataserver.integration.vessels.model.@NonNull Vessel upstreamVessel,
			final Map<String, BaseFuel> baseFuels, final Map<String, APortSet<Port>> portTypeMap, FleetModel fleetModel) {
		// Vessel Model
		cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MMX_ID, upstreamVessel.getMmxId()));
		cc.append(SetCommand.create(editingDomain, vesselToUpdate, MMXCorePackage.Literals.NAMED_OBJECT__NAME, upstreamVessel.getName()));
		cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__SHORT_NAME, upstreamVessel.getShortName()));
		cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__IMO, upstreamVessel.getImo()));
		cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__NOTES, upstreamVessel.getNotes()));

		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__TYPE, upstreamVessel.getType());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__SCNT, upstreamVessel.getScnt());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__CAPACITY, upstreamVessel.getCapacity());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__FILL_CAPACITY, upstreamVessel.getFillCapacity());

		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__SAFETY_HEEL, upstreamVessel.getSafetyHeel());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__COOLING_VOLUME, upstreamVessel.getCoolingVolume());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__WARMING_TIME, upstreamVessel.getWarmingTime());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__PURGE_TIME, upstreamVessel.getPurgeTime());

		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__PILOT_LIGHT_RATE, upstreamVessel.getPilotLightRate());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MAX_SPEED, upstreamVessel.getMaxSpeed());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MIN_SPEED, upstreamVessel.getMinSpeed());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY, upstreamVessel.getHasReliqCapacity());

		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__REFERENCE_VESSEL, upstreamVessel.getIsReference());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MMX_REFERENCE, upstreamVessel.getMmxReference());
		createSet(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MARKER, upstreamVessel.getIsMarker());

		createSetFuelCommand(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__BASE_FUEL, upstreamVessel.getTravelBaseFuel(), baseFuels, fleetModel);
		createSetFuelCommand(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__IDLE_BASE_FUEL, upstreamVessel.getIdleBaseFuel(), baseFuels, fleetModel);
		createSetFuelCommand(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__PILOT_LIGHT_BASE_FUEL, upstreamVessel.getPilotLightBaseFuel(), baseFuels, fleetModel);
		createSetFuelCommand(cc, editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__IN_PORT_BASE_FUEL, upstreamVessel.getInPortBaseFuel(), baseFuels, fleetModel);

		// Ballast Model
		createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, upstreamVessel.getBallastAttributes().getIdleBaseRate());
		createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, upstreamVessel.getBallastAttributes().getIdleNBORate());
		createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, upstreamVessel.getBallastAttributes().getNboRate());
		createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, upstreamVessel.getBallastAttributes().getServiceSpeed());
		createSetFuelConsumptions(cc, editingDomain, vesselToUpdate.getBallastAttributes(), upstreamVessel.getBallastAttributes().getFuelConsumption());

		createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE,
				upstreamVessel.getDischargeAttributes().getInPortBaseRate());
		createSet(cc, editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE,
				upstreamVessel.getDischargeAttributes().getInPortNBORate());

		// Laden Model
		createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, upstreamVessel.getLadenAttributes().getIdleBaseRate());
		createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, upstreamVessel.getLadenAttributes().getIdleNBORate());
		createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, upstreamVessel.getLadenAttributes().getNboRate());
		createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, upstreamVessel.getLadenAttributes().getServiceSpeed());
		createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE, upstreamVessel.getLoadAttributes().getInPortBaseRate());
		createSet(cc, editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE, upstreamVessel.getLoadAttributes().getInPortNBORate());

		createSetFuelConsumptions(cc, editingDomain, vesselToUpdate.getLadenAttributes(), upstreamVessel.getLadenAttributes().getFuelConsumption());

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
				cc.append(RemoveCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES, vesselToUpdate.getInaccessibleRoutes()));
			}
			if (!mappedRoutes.isEmpty()) {
				cc.append(AddCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES, mappedRoutes));
			}
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
				mappedPorts.add(portTypeMap.get(portMMXId));
			}

			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE, Boolean.TRUE));
			if (!vesselToUpdate.getInaccessiblePorts().isEmpty()) {
				cc.append(RemoveCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, vesselToUpdate.getInaccessiblePorts()));
			}
			if (!mappedPorts.isEmpty()) {
				cc.append(AddCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, mappedPorts));
			}
		} else {
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE, Boolean.FALSE));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS, SetCommand.UNSET_VALUE));
		}

	}

	public static void createSetFuelConsumptions(final CompoundCommand cc, @NonNull final EditingDomain editingDomain, final VesselStateAttributes attributes,
			final Optional<List<FuelConsumption>> fuelConsumptions) {

		if (!attributes.getFuelConsumption().isEmpty()) {
			cc.append(SetCommand.create(editingDomain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, Boolean.FALSE));
			cc.append(RemoveCommand.create(editingDomain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION, attributes.getFuelConsumption()));
		}
		if (fuelConsumptions.isPresent()) {
			final List<com.mmxlabs.models.lng.fleet.FuelConsumption> newConsumptions = new LinkedList<>();
			for (final FuelConsumption upstreamFC : fuelConsumptions.get()) {
				final com.mmxlabs.models.lng.fleet.FuelConsumption newFC = FleetFactory.eINSTANCE.createFuelConsumption();
				newFC.setSpeed(upstreamFC.getSpeed());
				newFC.setConsumption(upstreamFC.getConsumption());
				newConsumptions.add(newFC);
			}
			if (!newConsumptions.isEmpty()) {
				cc.append(SetCommand.create(editingDomain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, Boolean.TRUE));
				cc.append(AddCommand.create(editingDomain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION, newConsumptions));
			}
		} else {
			cc.append(SetCommand.create(editingDomain, attributes, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE, Boolean.FALSE));
		}
	}

	public static void directSetFuelConsumptions(final VesselStateAttributes attributes, final Optional<List<FuelConsumption>> fuelConsumptions) {
		if (!attributes.getFuelConsumption().isEmpty()) {
			attributes.setFuelConsumptionOverride(false);
			attributes.getFuelConsumption().clear();
		}
		if (fuelConsumptions.isPresent()) {
			final List<com.mmxlabs.models.lng.fleet.FuelConsumption> newConsumptions = new LinkedList<>();
			for (final FuelConsumption upstreamFC : fuelConsumptions.get()) {
				final com.mmxlabs.models.lng.fleet.FuelConsumption newFC = FleetFactory.eINSTANCE.createFuelConsumption();
				newFC.setSpeed(upstreamFC.getSpeed());
				newFC.setConsumption(upstreamFC.getConsumption());
				newConsumptions.add(newFC);
			}
			if (!newConsumptions.isEmpty()) {
				attributes.setFuelConsumptionOverride(true);
				attributes.getFuelConsumption().addAll(newConsumptions);
			}
		} else {
			attributes.setFuelConsumptionOverride(false);
		}
	}

	public static void createSetRouteParameters(final CompoundCommand cc, @NonNull final EditingDomain editingDomain, final Vessel vesselToUpdate,
			final Optional<List<VesselRouteParameters>> routeParameters) {

		if (!vesselToUpdate.getRouteParameters().isEmpty()) {
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE, Boolean.FALSE));
			cc.append(RemoveCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS, vesselToUpdate.getRouteParameters()));
		}
		if (routeParameters.isPresent()) {
			final List<com.mmxlabs.models.lng.fleet.VesselRouteParameters> newParameters = new LinkedList<>();
			for (final VesselRouteParameters upstreamFC : routeParameters.get()) {
				final com.mmxlabs.models.lng.fleet.VesselRouteParameters newFC = FleetFactory.eINSTANCE.createVesselRouteParameters();
				newFC.setRouteOption(RouteOption.valueOf(upstreamFC.getRoute()));
				newFC.setExtraTransitTime(upstreamFC.getExtraTransitTimeInHours());
				newFC.setLadenNBORate(upstreamFC.getLadenNBORate());
				newFC.setLadenConsumptionRate(upstreamFC.getLadenBunkerRate());
				newFC.setBallastNBORate(upstreamFC.getBallastNBORate());
				newFC.setBallastConsumptionRate(upstreamFC.getBallastBunkerRate());
				newParameters.add(newFC);
			}
			if (!newParameters.isEmpty()) {
				cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE, Boolean.TRUE));
				cc.append(AddCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS, newParameters));
			}
		} else {
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE, Boolean.FALSE));
		}
	}

	public static void directSetRouteParameters(final Vessel vesselToUpdate, final Optional<List<VesselRouteParameters>> routeParameters) {
		if (!vesselToUpdate.getRouteParameters().isEmpty()) {
			vesselToUpdate.setRouteParametersOverride(false);
			vesselToUpdate.getRouteParameters().clear();
		}
		if (routeParameters.isPresent()) {
			final List<com.mmxlabs.models.lng.fleet.VesselRouteParameters> newParameters = new LinkedList<>();
			for (final VesselRouteParameters upstreamFC : routeParameters.get()) {
				final com.mmxlabs.models.lng.fleet.VesselRouteParameters newFC = FleetFactory.eINSTANCE.createVesselRouteParameters();
				newFC.setRouteOption(RouteOption.valueOf(upstreamFC.getRoute()));
				newFC.setExtraTransitTime(upstreamFC.getExtraTransitTimeInHours());
				newFC.setLadenNBORate(upstreamFC.getLadenNBORate());
				newFC.setLadenConsumptionRate(upstreamFC.getLadenBunkerRate());
				newFC.setBallastNBORate(upstreamFC.getBallastNBORate());
				newFC.setBallastConsumptionRate(upstreamFC.getBallastBunkerRate());
				newParameters.add(newFC);
			}
			if (!newParameters.isEmpty()) {
				vesselToUpdate.setRouteParametersOverride(true);
				vesselToUpdate.getRouteParameters().addAll(newParameters);
			}
		} else {
			vesselToUpdate.setRouteParametersOverride(false);
		}
	}

	public static void directSet(final EObject owner, final EStructuralFeature feature, final Optional<?> value) {
		if (value.isPresent()) {
			if (feature.isMany()) {
				((List) owner.eGet(feature)).addAll((Collection) value.get());
			} else {
				owner.eSet(feature, value.get());
			}
		}
	}

	public static void directSet(final EObject owner, final EStructuralFeature feature, final OptionalDouble value) {
		if (value.isPresent()) {
			owner.eSet(feature, value.getAsDouble());
		}
	}

	public static void directSet(final EObject owner, final EStructuralFeature feature, final OptionalInt value) {
		if (value.isPresent()) {
			owner.eSet(feature, value.getAsInt());
		}
	}

	public static void createSet(final CompoundCommand cc, final EditingDomain editingDomain, final EObject owner, final EStructuralFeature feature, final Optional<?> value) {
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

	public static void createSet(final CompoundCommand cc, final EditingDomain editingDomain, final EObject owner, final EStructuralFeature feature, final OptionalDouble value) {
		if (value.isPresent()) {
			cc.append(SetCommand.create(editingDomain, owner, feature, value.getAsDouble()));
		} else {
			cc.append(SetCommand.create(editingDomain, owner, feature, SetCommand.UNSET_VALUE));
		}
	}

	public static void createSet(final CompoundCommand cc, final EditingDomain editingDomain, final EObject owner, final EStructuralFeature feature, final OptionalInt value) {
		if (value.isPresent()) {
			cc.append(SetCommand.create(editingDomain, owner, feature, value.getAsInt()));
		} else {
			cc.append(SetCommand.create(editingDomain, owner, feature, SetCommand.UNSET_VALUE));
		}
	}

	public static void createSetFuelCommand(final CompoundCommand cc, final EditingDomain editingDomain, final EObject owner, final EStructuralFeature feature, final Optional<String> value,
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

	public static void deferSetFuel(final EObject owner, final EStructuralFeature feature, final Optional<String> value, IMMXImportContext context) {
		if (value.isPresent()) {
			context.doLater(new IDeferment() {

				@Override
				public void run(@NonNull IImportContext importContext) {
					final IMMXImportContext context = (IMMXImportContext) importContext;
					final MMXRootObject rootObject = context.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

						final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
						final String expectedBaseFuelName = value.get();
						final Optional<BaseFuel> optBaseFuel = fleetModel.getBaseFuels().stream().filter(bf -> bf.getName().equals(expectedBaseFuelName)).findAny();
						final BaseFuel baseFuel;
						if (optBaseFuel.isPresent()) {
							baseFuel = optBaseFuel.get();
						} else {
							baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
							baseFuel.setName(expectedBaseFuelName);
							fleetModel.getBaseFuels().add(baseFuel);
						}
						owner.eSet(feature, baseFuel);
					}
				}

				@Override
				public int getStage() {
					return IMMXImportContext.STAGE_RESOLVE_REFERENCES;
				}
			});
		}
	}
}
