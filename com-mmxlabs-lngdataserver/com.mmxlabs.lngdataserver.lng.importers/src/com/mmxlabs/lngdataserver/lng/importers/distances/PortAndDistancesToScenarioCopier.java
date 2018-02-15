package com.mmxlabs.lngdataserver.lng.importers.distances;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Triple;
import com.mmxlabs.lngdataserver.integration.distances.IDistanceProvider;
import com.mmxlabs.lngdataserver.integration.distances.ILocationProvider;
import com.mmxlabs.lngdataserver.integration.distances.UpstreamRoutingPointFetcher;
import com.mmxlabs.lngdataserver.integration.distances.exceptions.LocationNotFoundException;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.impl.PortFactoryImpl;
import com.mmxlabs.models.lng.port.importer.PortModelImporter;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class PortAndDistancesToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortAndDistancesToScenarioCopier.class);

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final ILocationProvider portProvider, @NonNull final IDistanceProvider distanceProvider,
			@NonNull final PortModel portModel) {

		final CompoundCommand cmd = new CompoundCommand("Update ports");

		Map<String, Port> portMap = new HashMap<>();
		List<Port> unlinkedPorts = new LinkedList<>();
		for (final Port port : portModel.getPorts()) {
			Location l = port.getLocation();
			if (l != null) {
				String mmxId = l.getMmxId();
				if (mmxId != null && !mmxId.isEmpty()) {
					portMap.put(l.getMmxId(), port);
					continue;
				}
			}
			System.out.println("Unlinked port " + port.getName());
			unlinkedPorts.add(port);
		}

		List<Port> portsAdded = new LinkedList<>();
		List<Port> portsModified = new LinkedList<>();

		List<Port> newPorts = portProvider.getPorts();
		List<Port> allPorts = new LinkedList<>();
		Map<String, Port> idToPort = new HashMap<>();
		for (Port newPort : newPorts) {
			Location newLocation = newPort.getLocation();
			String mmxId = newLocation.getMmxId();

			final Port oldPort;
			if (portMap.containsKey(mmxId)) {
				oldPort = portMap.get(mmxId);
				portsModified.add(oldPort);
				allPorts.add(oldPort);
			} else {
				oldPort = PortFactory.eINSTANCE.createPort();
				oldPort.setLocation(PortFactory.eINSTANCE.createLocation());

				oldPort.setDefaultWindowSize(1);
				oldPort.setDefaultWindowSizeUnits(TimePeriod.DAYS);

				portsAdded.add(oldPort);
				cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, oldPort));
				allPorts.add(oldPort);
			}
			idToPort.put(mmxId, oldPort);
			cmd.append(SetCommand.create(editingDomain, oldPort, MMXCorePackage.Literals.NAMED_OBJECT__NAME, newLocation.getName()));

			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, newLocation.getName()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.OTHER_NAMES_OBJECT__OTHER_NAMES, newLocation.getOtherNames()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__COUNTRY, newLocation.getCountry()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__TIME_ZONE, newLocation.getTimeZone()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__LAT, newLocation.getLat()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__LON, newLocation.getLon()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__MMX_ID, newLocation.getMmxId()));

		}

		Set<Port> portsToRemove = new HashSet<>(portModel.getPorts());
		portsToRemove.removeAll(portsModified);
		// portsToRemove.removeAll(portsadded);
		if (!portsToRemove.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, portsToRemove));
		}

		List<Triple<String, String, String>> routingPoints;
		try {
			routingPoints = UpstreamRoutingPointFetcher.getRoutingPoints(BackEndUrlProvider.INSTANCE.getUrl(), distanceProvider.getVersion(), "", "");
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}

		for (RouteOption option : RouteOption.values()) {

			Route route = null;
			for (final Route l_route : portModel.getRoutes()) {
				if (l_route.getRouteOption() == option) {
					route = l_route;
					break;
				}
			}
			if (route == null) {
				route = PortFactory.eINSTANCE.createRoute();
				route.setRouteOption(option);
				switch (option) {
				case DIRECT:
					route.setName(PortModelImporter.DIRECT_NAME);
					break;
				case PANAMA:
					route.setName(PortModelImporter.PANAMA_CANAL_NAME);
					break;
				case SUEZ:
					route.setName(PortModelImporter.SUEZ_CANAL_NAME);
					break;
				default:
					break;
				}
				cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__ROUTES, route));
			} else {
				cmd.append(RemoveCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__LINES, route.getLines()));
			}
			final List<RouteLine> toAdd = new LinkedList<>();

			for (final Port from : allPorts) {
				for (final Port to : allPorts) {
					// skip identity
					if (from.equals(to)) {
						continue;
					}
					final RouteLine rl = PortFactoryImpl.eINSTANCE.createRouteLine();
					rl.setFrom(from);
					rl.setTo(to);
					try {
						Location fromLoc = from.getLocation();
						Location toLoc = to.getLocation();
						final int distance = distanceProvider.getDistance(fromLoc.getMmxId(), toLoc.getMmxId(), ViaKeyMapper.mapVia(route));
						if (distance != Integer.MAX_VALUE) {
							rl.setDistance(distance);
							toAdd.add(rl);
						}
					} catch (final LocationNotFoundException e) {
						// no action needed
					}
				}
			}
			if (!toAdd.isEmpty()) {
				cmd.append(AddCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__LINES, toAdd));
			}

			if (option != RouteOption.DIRECT) {

				for (Triple<String, String, String> t : routingPoints) {
					String northSide = t.getSecond();
					String southSide = t.getThird();
					if (option == RouteOption.SUEZ && t.getFirst().equals("SUZ") || option == RouteOption.PANAMA && t.getFirst().equals("PAN")) {

						{
							EntryPoint north = route.getNorthEntrance();
							if (north == null) {
								north = PortFactory.eINSTANCE.createEntryPoint();
								north.setName("Northside");
								cmd.append(SetCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__NORTH_ENTRANCE, north));
							}
							cmd.append(SetCommand.create(editingDomain, north, PortPackage.Literals.ENTRY_POINT__PORT, idToPort.get(northSide)));
						}
						{
							EntryPoint south = route.getSouthEntrance();
							if (south == null) {
								south = PortFactory.eINSTANCE.createEntryPoint();
								south.setName("Southside");
								cmd.append(SetCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__SOUTH_ENTRANCE, south));
							}
							cmd.append(SetCommand.create(editingDomain, south, PortPackage.Literals.ENTRY_POINT__PORT, idToPort.get(southSide)));
						}
					}
				}
			}
		}

		cmd.append(SetCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__DISTANCE_DATA_VERSION, distanceProvider.getVersion()));

		return cmd;

	}
}
