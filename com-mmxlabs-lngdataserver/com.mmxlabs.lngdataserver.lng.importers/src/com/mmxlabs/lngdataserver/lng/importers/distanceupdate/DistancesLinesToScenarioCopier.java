/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.OtherIdentifiers;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.impl.PortFactoryImpl;

public class DistancesLinesToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistancesLinesToScenarioCopier.class);

	public static CompoundCommand getUpdateCommand(final @NonNull EditingDomain editingDomain, final @NonNull PortModel portModel, final @NonNull List<AtoBviaCLookupRecord> records) {

		final CompoundCommand cmd = new CompoundCommand("Update distances");

		// Clear existing route lines before the port delete command
		for (final Route l_route : portModel.getRoutes()) {
			if (!l_route.getLines().isEmpty()) {
				cmd.append(RemoveCommand.create(editingDomain, l_route, PortPackage.Literals.ROUTE__LINES, new LinkedList<>(l_route.getLines())));
			}
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

			routeMap.put(option, route);

			if (option == RouteOption.PANAMA) {
				panamaA = route.getNorthEntrance().getPort();
				panamaB = route.getSouthEntrance().getPort();
				panamaDistance = route.getDistance();
			} else if (option == RouteOption.SUEZ) {
				suezA = route.getNorthEntrance().getPort();
				suezB = route.getSouthEntrance().getPort();
				suezDistance = route.getDistance();
			}
		}
		// Fill in direct distances
		{
			final Map<Port, String> portToId = new HashMap<>();
			for (final Port p : portModel.getPorts()) {
				final Location l = p.getLocation();
				if (l != null) {
					for (final OtherIdentifiers oi : l.getOtherIdentifiers()) {
						if ("abc".equalsIgnoreCase(oi.getProvider()) && !oi.getIdentifier().isEmpty()) {
							portToId.put(p, oi.getIdentifier());
						}
						break;
					}
				}

			}

			final Map<Pair<Port, Port>, Double> directMatrix = new HashMap<>();

			{
				final Map<Pair<String, String>, AtoBviaCLookupRecord> recordsMap = records.stream() // .
						.collect(Collectors.toMap(r -> Pair.of(r.getFrom(), r.getTo()), r -> r));

				// for (AtoBviaCLookupRecord record : records) {
				//// record.get
				// }

				{
					final Route route = routeMap.get(RouteOption.DIRECT);
					if (route != null) {

						final List<RouteLine> toAdd = new LinkedList<>();

						final Map<Pair<Port, Port>, RouteLine> directMatrixLookup = new HashMap<>();
						final List<Pair<Port, Port>> toCheck = new LinkedList<>();

						for (final Port from : portModel.getPorts()) {
							final String fromID = portToId.get(from);
							if (fromID == null) {
								continue;
							}
							for (final Port to : portModel.getPorts()) {
								final String toID = portToId.get(to);
								if (toID == null) {
									continue;
								}
								if (from == to) {
									continue;
								}

								final Pair<Port, Port> portObjectPair = Pair.of(from, to);
								final Pair<String, String> portIdPair = Pair.of(fromID, toID);

								if (Objects.equals(fromID, toID)) {
									// Different ports, same upstream code.
									final RouteLine rl = PortFactoryImpl.eINSTANCE.createRouteLine();
									rl.setFrom(from);
									rl.setTo(to);

									rl.setDistance(0.0);
									rl.setErrorCode(null);
									toAdd.add(rl);

									directMatrix.put(portObjectPair, rl.getDistance());
									directMatrixLookup.put(portObjectPair, rl);

									continue;
								}

								AtoBviaCLookupRecord record = recordsMap.get(portIdPair);
								if (record == null || record.getErrorCode() != null || record.getDistance() < 1.0) {
									// Use reverse distance if this is valid
									final AtoBviaCLookupRecord altRecord = recordsMap.get(Pair.of(toID, fromID));
									if (altRecord != null && altRecord.getErrorCode() == null && altRecord.getDistance() >= 0.0) {
										record = altRecord;
									}
								}

								if (record == null) {
									continue;
								}
								if (record.getErrorCode() != null) {
									continue;
								}
								if (record.getDistance() < 1.0) {
									continue;
								}
								final RouteLine rl = PortFactoryImpl.eINSTANCE.createRouteLine();
								rl.setFrom(from);
								rl.setTo(to);

								rl.setDistance(record.getDistance());
								rl.setProvider("abc");
								rl.setErrorCode(null);
								toAdd.add(rl);

								directMatrix.put(portObjectPair, rl.getDistance());
								directMatrixLookup.put(portObjectPair, rl);

								if (rl.getDistance() < 1.0) {

									toCheck.add(portObjectPair);
									System.out.println(portObjectPair);
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
				validateDirectRoutes(directMatrix);

				{
					final Route canalRoute = routeMap.get(RouteOption.SUEZ);
					if (canalRoute != null) {
						final List<RouteLine> toAdd = createCanalRoutes(portModel.getPorts(), suezDistance, suezA, suezB, directMatrix);
						// validateCanalRoutes(toAdd, directMatrix);
						if (!toAdd.isEmpty()) {
							cmd.append(AddCommand.create(editingDomain, canalRoute, PortPackage.Literals.ROUTE__LINES, toAdd));
						}
					}
				}
				{
					final Route canalRoute = routeMap.get(RouteOption.PANAMA);
					if (canalRoute != null) {
						final List<RouteLine> toAdd = createCanalRoutes(portModel.getPorts(), panamaDistance, panamaA, panamaB, directMatrix);
						// validateCanalRoutes(toAdd, directMatrix);
						if (!toAdd.isEmpty()) {
							cmd.append(AddCommand.create(editingDomain, canalRoute, PortPackage.Literals.ROUTE__LINES, toAdd));
						}
					}
				}
			}
		}

		// VersionRecord record = MMXCoreFactory.eINSTANCE.createVersionRecord();
		// record.setVersion(newID);
		// record.setCreatedBy(UsernameProvider.getUsername());
		// record.setCreatedAt(Instant.now());
		// final Command cmd = SetCommand.create(editingDomain, modelRoot, versionRecordFeature, record);
		// VersionRecord record = portModel.getDistanceVersionRecord();
		// cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		// cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		// cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));

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
