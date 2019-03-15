/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distances;

import java.util.Collection;
import java.util.EnumMap;
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.distances.model.GeographicPoint;
import com.mmxlabs.lngdataserver.integration.distances.model.Identifier;
import com.mmxlabs.lngdataserver.integration.distances.model.Routes;
import com.mmxlabs.lngdataserver.integration.distances.model.RoutingPoint;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.OtherIdentifiers;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
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
import com.mmxlabs.models.mmxcore.VersionRecord;

public class DistancesToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistancesToScenarioCopier.class);

	public static Command getUpdateCommand(final @NonNull EditingDomain editingDomain, final @NonNull PortModel portModel, final @NonNull DistancesVersion version, final boolean updatePortNames) {

		final CompoundCommand cmd = new CompoundCommand("Update ports");

		// Clear existing route lines before the port delete command
		for (final Route l_route : portModel.getRoutes()) {
			if (!l_route.getLines().isEmpty()) {
				cmd.append(RemoveCommand.create(editingDomain, l_route, PortPackage.Literals.ROUTE__LINES, new LinkedList<>(l_route.getLines())));
			}
		}

		final Set<String> countries = new HashSet<>();
		{
			final Map<String, Port> portMap = new HashMap<>();
			final List<Port> unlinkedPorts = new LinkedList<>();
			for (final Port port : portModel.getPorts()) {
				final Location l = port.getLocation();
				if (l != null) {
					final String mmxId = l.getMmxId();
					if (mmxId != null && !mmxId.isEmpty()) {
						portMap.put(l.getMmxId(), port);
						continue;
					}
				}
				System.out.println("Unlinked port " + port.getName());
				unlinkedPorts.add(port);
			}

			final List<Port> portsAdded = new LinkedList<>();
			final List<Port> portsModified = new LinkedList<>();

			final List<com.mmxlabs.lngdataserver.integration.distances.model.Location> versionLocations = version.getLocations();
			final List<Port> allPorts = new LinkedList<>();
			final Map<String, Port> idToPort = new HashMap<>();
			for (final com.mmxlabs.lngdataserver.integration.distances.model.Location versionLocation : versionLocations) {
				final String mmxId = versionLocation.getMmxId();
				final Port oldPort;
				if (portMap.containsKey(mmxId)) {
					oldPort = portMap.get(mmxId);
					portsModified.add(oldPort);
					allPorts.add(oldPort);
				} else {
					oldPort = PortFactory.eINSTANCE.createPort();
					oldPort.setLocation(PortFactory.eINSTANCE.createLocation());
					oldPort.getLocation().setMmxId(mmxId);
					oldPort.setName(versionLocation.getName());
					oldPort.getLocation().setMmxId(mmxId);

					oldPort.setDefaultWindowSize(1);
					oldPort.setDefaultWindowSizeUnits(TimePeriod.DAYS);

					portsAdded.add(oldPort);
					cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, oldPort));
					allPorts.add(oldPort);
				}
				idToPort.put(mmxId, oldPort);

				// Only update name if not set
				if (updatePortNames || (oldPort.getName() == null || oldPort.getName().isEmpty())) {
					cmd.append(SetCommand.create(editingDomain, oldPort, MMXCorePackage.Literals.NAMED_OBJECT__NAME, versionLocation.getName()));
				}
				cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, versionLocation.getName()));

				cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__MMX_ID, versionLocation.getMmxId()));
				if (versionLocation.getAliases() == null || versionLocation.getAliases().isEmpty()) {
					cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.OTHER_NAMES_OBJECT__OTHER_NAMES, SetCommand.UNSET_VALUE));
				} else {
					cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.OTHER_NAMES_OBJECT__OTHER_NAMES, versionLocation.getAliases()));

				}

				final GeographicPoint geographicPoint = versionLocation.getGeographicPoint();
				if (geographicPoint != null) {
					cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__COUNTRY, geographicPoint.getCountry()));
					cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__TIME_ZONE, geographicPoint.getTimeZone()));
					cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__LAT, geographicPoint.getLat()));
					cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__LON, geographicPoint.getLon()));

					countries.add(geographicPoint.getCountry());
				}

				final List<OtherIdentifiers> otherIdentifiers = new LinkedList<>();
				if (versionLocation.getOtherIdentifiers() != null) {
					for (final Map.Entry<String, Identifier> e : versionLocation.getOtherIdentifiers().entrySet()) {
						final OtherIdentifiers id = PortFactory.eINSTANCE.createOtherIdentifiers();
						id.setIdentifier(e.getValue().getIdentifier());
						id.setProvider(e.getValue().getProvider());
						otherIdentifiers.add(id);
					}
				}
				cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__OTHER_IDENTIFIERS, otherIdentifiers));

			}

			final List<RoutingPoint> routingPoints = version.getRoutingPoints();
			final Set<Port> portsToRemove = new HashSet<>(portModel.getPorts());
			portsToRemove.removeAll(portsModified);
			if (!portsToRemove.isEmpty()) {
				cmd.append(DeleteCommand.create(editingDomain, portsToRemove));
			}

			// Cached for lookup later
			double suezDistance = 0.0;
			double panamaDistance = 0.0;
			Port suezA = null;
			Port suezB = null;
			Port panamaA = null;
			Port panamaB = null;

			final Map<RouteOption, Route> routeMap = new EnumMap<>(RouteOption.class);
			for (final RouteOption option : RouteOption.values()) {

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
				}

				routeMap.put(option, route);

				if (option != RouteOption.DIRECT) {

					for (final RoutingPoint t : routingPoints) {
						final String northSide = t.getNorthernEntry().getMmxId();
						final String southSide = t.getSouthernEntry().getMmxId();
						if ((option == RouteOption.SUEZ && t.getIdentifier().equals("SUZ")) || (option == RouteOption.PANAMA && t.getIdentifier().equals("PAN"))) {
							cmd.append(SetCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__VIRTUAL_PORT, idToPort.get(t.getVirtualLocation().getMmxId())));

							cmd.append(SetCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__DISTANCE, (double) t.getDistance()));
							if (option == RouteOption.SUEZ) {
								suezDistance = (double) t.getDistance();
							} else {
								panamaDistance = (double) t.getDistance();
							}
							// Northside entrance.
							{
								EntryPoint north = route.getNorthEntrance();
								if (north == null) {
									north = PortFactory.eINSTANCE.createEntryPoint();
									north.setName("Northside");
									cmd.append(SetCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__NORTH_ENTRANCE, north));
								}
								cmd.append(SetCommand.create(editingDomain, north, PortPackage.Literals.ENTRY_POINT__PORT, idToPort.get(northSide)));
								if (option == RouteOption.SUEZ) {
									suezA = idToPort.get(northSide);
								} else {
									panamaA = idToPort.get(northSide);
								}
							}
							// Southside entrance
							{
								EntryPoint south = route.getSouthEntrance();
								if (south == null) {
									south = PortFactory.eINSTANCE.createEntryPoint();
									south.setName("Southside");
									cmd.append(SetCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__SOUTH_ENTRANCE, south));
								}
								cmd.append(SetCommand.create(editingDomain, south, PortPackage.Literals.ENTRY_POINT__PORT, idToPort.get(southSide)));
								if (option == RouteOption.SUEZ) {
									suezB = idToPort.get(southSide);
								} else {
									panamaB = idToPort.get(southSide);
								}
							}
						}
					}
				}
			}
			// Fill in direct distances
			{

				final Map<Pair<Port, Port>, Double> directMatrix = new HashMap<>();

				{
					final Route route = routeMap.get(RouteOption.DIRECT);
					if (route != null) {

						final List<RouteLine> toAdd = new LinkedList<>();
						final Routes routes = version.getRoutes();

						final Map<Pair<Port, Port>, RouteLine> directMatrixLookup = new HashMap<>();
						final List<Pair<Port, Port>> toCheck = new LinkedList<>();

						for (final Map.Entry<String, com.mmxlabs.lngdataserver.integration.distances.model.Route> e : routes.getRoutes().entrySet()) {
							final String key = e.getKey();
							final String ports[] = key.split(">");
							final String from = ports[0];
							final String to = ports[1];
							final com.mmxlabs.lngdataserver.integration.distances.model.Route r = e.getValue();
							{
								final double distance = r.getDistance();
								// if (distance < 0.0) {
								// continue;
								// }
								final RouteLine rl = PortFactoryImpl.eINSTANCE.createRouteLine();
								rl.setFrom(idToPort.get(from));
								rl.setTo(idToPort.get(to));

								if (rl.getFrom() == null || rl.getTo() == null) {
									continue;
								}
								rl.setDistance(distance);
								rl.setProvider(r.getProvider());
								rl.setErrorCode(r.getErrorCode());
								toAdd.add(rl);

								directMatrix.put(new Pair<>(rl.getFrom(), rl.getTo()), rl.getDistance());
								directMatrixLookup.put(new Pair<>(rl.getFrom(), rl.getTo()), rl);

								if (distance < 1.0) {
									toCheck.add(new Pair<>(rl.getFrom(), rl.getTo()));
								}
							}
						}

						for (final Pair<Port, Port> p : toCheck) {
							final Pair<Port, Port> rev = new Pair<>(p.getSecond(), p.getFirst());
							final RouteLine altRouteLine = directMatrixLookup.get(rev);
							if (altRouteLine != null) {
								if (altRouteLine.getDistance() >= 0.0) {
									// Found alternative

								}
							}
						}

						if (!toAdd.isEmpty()) {
							cmd.append(AddCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__LINES, toAdd));
						}
					}
				}
				// validateDirectRoutes(directMatrix);

				{
					final Route canalRoute = routeMap.get(RouteOption.SUEZ);
					if (canalRoute != null) {
						final List<RouteLine> toAdd = createCanalRoutes(allPorts, suezDistance, suezA, suezB, directMatrix);
						// validateCanalRoutes(toAdd, directMatrix);
						if (!toAdd.isEmpty()) {
							cmd.append(AddCommand.create(editingDomain, canalRoute, PortPackage.Literals.ROUTE__LINES, toAdd));
						}
					}
				}
				{
					final Route canalRoute = routeMap.get(RouteOption.PANAMA);
					if (canalRoute != null) {
						final List<RouteLine> toAdd = createCanalRoutes(allPorts, panamaDistance, panamaA, panamaB, directMatrix);
						// validateCanalRoutes(toAdd, directMatrix);
						if (!toAdd.isEmpty()) {
							cmd.append(AddCommand.create(editingDomain, canalRoute, PortPackage.Literals.ROUTE__LINES, toAdd));
						}
					}
				}
			}
		}

		// Update country groups

		{
			// Gather existing curves
			final Map<String, PortCountryGroup> map = new HashMap<>();
			final Set<PortCountryGroup> existing = new HashSet<>();

			portModel.getPortCountryGroups().forEach(c -> map.put(c.getName(), c));
			portModel.getPortCountryGroups().forEach(existing::add);

			final Set<PortCountryGroup> updated = new HashSet<>();

			for (final String countryName : countries) {
				PortCountryGroup existingGroup = map.get(countryName);
				if (existingGroup == null) {
					existingGroup = PortFactory.eINSTANCE.createPortCountryGroup();
					existingGroup.setName(countryName);
					cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS, existingGroup));
				}
				updated.add(existingGroup);
			}

			existing.removeAll(updated);
			if (!existing.isEmpty()) {
				cmd.append(DeleteCommand.create(editingDomain, existing));
			}
		}

		VersionRecord record = portModel.getDistanceVersionRecord();
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));

		return cmd;

	}

	private static void validateDirectRoutes(final Map<Pair<Port, Port>, Double> directMatrix) {
		final Collection<Pair<Port, Port>> existing = new HashSet<>();
		final Collection<Pair<Port, Port>> lookingFor = new HashSet<>();
		for (final Pair<Port, Port> rl : directMatrix.keySet()) {
			if (directMatrix.get(rl) < 0.0) {
				continue;
			}
			existing.add(Pair.of(rl.getFirst(), rl.getSecond()));
			lookingFor.add(Pair.of(rl.getSecond(), rl.getFirst()));
		}
		lookingFor.removeAll(existing);
		for (final Pair<Port, Port> p : lookingFor) {
			System.out.printf("%s -> %s. %f %f\n", p.getFirst().getName(), p.getSecond().getName(), directMatrix.get(p), directMatrix.get(new Pair<>(p.getSecond(), p.getFirst())));
		}
		assert lookingFor.isEmpty();

	}

	private static void validateCanalRoutes(final List<RouteLine> toAdd, final Map<Pair<Port, Port>, Double> directMatrix) {
		final Collection<Pair<Port, Port>> existing = new HashSet<>();
		final Collection<Pair<Port, Port>> lookingFor = new HashSet<>();
		final Map<Pair<Port, Port>, RouteLine> m = new HashMap<>();
		for (final RouteLine rl : toAdd) {
			existing.add(Pair.of(rl.getFrom(), rl.getTo()));
			lookingFor.add(Pair.of(rl.getTo(), rl.getFrom()));
			m.put(Pair.of(rl.getTo(), rl.getFrom()), rl);
		}
		lookingFor.removeAll(existing);
		for (final Pair<Port, Port> p : lookingFor) {
			System.out.printf("%s -> %s. %f %f %f\n", p.getFirst().getName(), p.getSecond().getName(), directMatrix.get(p), directMatrix.get(new Pair<>(p.getSecond(), p.getFirst())),
					m.get(p).getDistance());
		}
		assert lookingFor.isEmpty();

	}

	private static List<RouteLine> createCanalRoutes(final List<Port> ports, final double canalDistance, final Port canalA, final Port canalB, final Map<Pair<Port, Port>, Double> directMatrix) {

		final List<RouteLine> toAdd = new LinkedList<>();
		for (final Port from : ports) {
			for (final Port to : ports) {
				if (from != to) {
					double a = Double.MAX_VALUE;
					double b = Double.MAX_VALUE;
					{
						final Double leg1 = directMatrix.get(new Pair<>(from, canalA));
						final Double leg2 = directMatrix.get(new Pair<>(canalB, to));
						if (leg1 != null && leg2 != null) {
							a = leg1 + leg2 + canalDistance;
						}
					}
					{
						final Double leg1 = directMatrix.get(new Pair<>(from, canalB));
						final Double leg2 = directMatrix.get(new Pair<>(canalA, to));
						if (leg1 != null && leg2 != null) {
							b = leg1 + leg2 + canalDistance;
						}
					}

					double distance;
					if (a != Double.MAX_VALUE && b != Double.MAX_VALUE) {
						distance = Math.min(a, b);
					} else if (a != Double.MAX_VALUE) {
						distance = a;
					} else if (b != Double.MAX_VALUE) {
						distance = b;
					} else {
						distance = Double.MAX_VALUE;
					}

					if (distance < 0.0 || distance == Double.MAX_VALUE) {
						// Is canal distance valid?
						continue;
					}

					final Double direct_distance = directMatrix.get(new Pair<>(from, to));
					if (direct_distance == null || direct_distance == 0.0) {
						// No direct? skip canal distance
						continue;
					}
					if (direct_distance != null && direct_distance > 0.0 && direct_distance < distance) {
						// Canal distance is greater than canal distance
						// Skip - but note some code assumes if a laden canal distance is present,
						// there is a ballast distance also.
						// distance = direct_distance;
						continue;
					}

					if (distance >= 20000) {
						// Distance is silly
						continue;
					}
					final RouteLine rl = PortFactoryImpl.eINSTANCE.createRouteLine();
					rl.setFrom(from);
					rl.setTo(to);

					if (rl.getFrom() == null || rl.getTo() == null) {
						continue;
					}
					rl.setDistance(distance);
					toAdd.add(rl);
				}

			}
		}
		return toAdd;
	}

}
