/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.BasicLocation;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.impl.PortFactoryImpl;

public class DistancesLinesToScenarioCopier {

	private DistancesLinesToScenarioCopier() {

	}

	public static CompoundCommand getUpdateCommand(final @NonNull EditingDomain editingDomain, final @NonNull PortModel portModel, final LocationsVersion locationsVersion,
			final @NonNull List<AtoBviaCLookupRecord> records, final List<AtoBviaCLookupRecord> manualRecords) {

		final CompoundCommand cmd = new CompoundCommand("Update distances");

		final Map<String, String> fallbackUpstreaIDMapping = new HashMap<>();
		final Map<String, String> mmxIdToUpstreaIDMapping = new HashMap<>();
		for (final BasicLocation bl : locationsVersion.getLocations()) {
			final String fallbackId = bl.getFallbackUpstreamId();
			if (fallbackId != null) {
				fallbackUpstreaIDMapping.put(bl.getUpstreamID(), bl.getFallbackUpstreamId());
			}
			mmxIdToUpstreaIDMapping.put(bl.getMmxId(), bl.getUpstreamID());
		}
		//

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
					portToId.put(p, mmxIdToUpstreaIDMapping.get(l.getMmxId()));
				}
			}

			final Map<Pair<Port, Port>, Double> directMatrix = new HashMap<>();

			{
				final Map<Pair<String, String>, AtoBviaCLookupRecord> recordsMap = records.stream() // .
						.filter(AtoBviaCLookupRecord::isAntiPiracy) //
						.collect(Collectors.toMap(r -> Pair.of(r.getFrom(), r.getTo()), r -> r));

				final Map<Pair<String, String>, AtoBviaCLookupRecord> manualRecordsMap = manualRecords == null ? new HashMap<>()
						: manualRecords.stream() // .
								.collect(Collectors.toMap(r -> Pair.of(r.getFrom(), r.getTo()), r -> r));

				final BiFunction<String, String, @Nullable AtoBviaCLookupRecord> lookupFunction = makeLookupFunction(recordsMap, manualRecordsMap, fallbackUpstreaIDMapping);

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
								final @Nullable String fallbackFromID = fallbackUpstreaIDMapping.get(fromID);
								final @Nullable String fallbackToID = fallbackUpstreaIDMapping.get(toID);

								// Check primary id match
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

								final AtoBviaCLookupRecord record = lookupFunction.apply(fromID, toID);
								if (record == null) {

									// No distance found, but maybe with the fallback id we have the same port, if
									// so set the distance to zero rather than just missing
									// We don't do this in the earlier check in case we do have a distance between
									// original id an fallback id that we can use.
									if ((fallbackFromID != null && Objects.equals(fallbackFromID, toID)) //
											|| (fallbackToID != null && Objects.equals(fromID, fallbackToID)) //
											|| (fallbackFromID != null && fallbackToID != null && Objects.equals(fallbackFromID, fallbackToID)) //
									) {
										// Different ports, same upstream code.
										final RouteLine rl = PortFactoryImpl.eINSTANCE.createRouteLine();
										rl.setFrom(from);
										rl.setTo(to);

										rl.setDistance(0.0);
										rl.setErrorCode(null);
										toAdd.add(rl);

										directMatrix.put(portObjectPair, rl.getDistance());
										directMatrixLookup.put(portObjectPair, rl);
									}

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

		// Map of generated canal distance lines
		final Map<Pair<Port, Port>, RouteLine> m = new HashMap<>();

		final List<RouteLine> toAdd = new LinkedList<>();
		for (final Port from : ports) {
			for (final Port to : ports) {
				if (from != to) {

					double a = Double.MAX_VALUE;
					double b = Double.MAX_VALUE;
					{
						Double leg1 = directMatrix.get(new Pair<>(from, canalA));
						// These checks are from distances through the canal from the canal entrance.
						// This 0 distance would not have been included in the directMatrix
						if (leg1 == null && from == canalA) {
							leg1 = 0.0;
						}
						Double leg2 = directMatrix.get(new Pair<>(canalB, to));
						// These checks are from distances through the canal from the canal entrance.
						// This 0 distance would not have been included in the directMatrix
						if (leg2 == null && canalB == to) {
							leg2 = 0.0;
						}

						if (leg1 != null && leg2 != null) {
							a = leg1 + leg2 + canalDistance;
						}
					}
					{
						Double leg1 = directMatrix.get(new Pair<>(from, canalB));
						// These checks are from distances through the canal from the canal entrance.
						// This 0 distance would not have been included in the directMatrix
						if (leg1 == null && from == canalB) {
							leg1 = 0.0;
						}

						Double leg2 = directMatrix.get(new Pair<>(canalA, to));
						// These checks are from distances through the canal from the canal entrance.
						// This 0 distance would not have been included in the directMatrix
						if (leg2 == null && canalA == to) {
							leg2 = 0.0;
						}

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

					// Record the valid distance line
					final Pair<Port, Port> key = Pair.of(from, to);
					m.put(key, rl);
				}

			}
		}

		// We can generate valid distances but only in one direction. The direct matrix
		// should have been fully populated both ways. The likely issue is that one way
		// direct is smaller, the otherway the canal distance is smaller. There is no
		// point paying for the canal if direct is closer, so we ignore the canal route.
		// Here we remove the dangling distance on the assumption the canal is only
		// marginally closer.
		for (final Port from : ports) {
			for (final Port to : ports) {
				if (from != to) {

					final RouteLine forward = m.get(Pair.of(from, to));
					final RouteLine backward = m.get(Pair.of(to, from));

					if (forward == null && backward != null) {
						toAdd.remove(backward);
					}
					if (forward != null && backward == null) {
						toAdd.remove(forward);
					}
				}
			}
		}

		return toAdd;
	}

	private static BiFunction<String, String, @Nullable AtoBviaCLookupRecord> makeLookupFunction(final Map<Pair<String, String>, AtoBviaCLookupRecord> recordsMap, final //
	Map<Pair<String, String>, AtoBviaCLookupRecord> manualRecordsMap, final Map<String, String> fallbackIDMapping) {

		// Test for a valid and usable distance record
		final Predicate<AtoBviaCLookupRecord> isValidRecord = record -> record != null && record.getErrorCode() == null && record.getDistance() >= 0.0;

		// Returns the record if usable, otherwise null
		final BiFunction<String, String, AtoBviaCLookupRecord> getIfValid = (fromID, toID) -> {
			// Try primary ID pair first of all.
			AtoBviaCLookupRecord record = recordsMap.get(Pair.of(fromID, toID));
			if (isValidRecord.test(record)) {
				return record;
			}
			// Try reverse distance
			record = recordsMap.get(Pair.of(toID, fromID));
			if (isValidRecord.test(record)) {
				return record;
			}

			//// Try again with manual overrides

			// Try primary ID pair first of all.

			record = manualRecordsMap.get(Pair.of(fromID, toID));
			if (isValidRecord.test(record)) {
				return record;
			}
			// Try reverse distance
			record = manualRecordsMap.get(Pair.of(toID, fromID));
			if (isValidRecord.test(record)) {
				return record;
			}
			return null;
		};

		return (fromID, toID) -> {

			// Try primary ID pair first of all.
			AtoBviaCLookupRecord record = getIfValid.apply(fromID, toID);
			if (record != null) {
				return record;
			}
			if (fallbackIDMapping.containsKey(fromID)) {
				final String fallbackFromID = fallbackIDMapping.get(fromID);
				record = getIfValid.apply(fallbackFromID, toID);
				if (record != null) {
					return record;
				}
			}
			if (fallbackIDMapping.containsKey(toID)) {
				final String fallbackToID = fallbackIDMapping.get(toID);
				record = getIfValid.apply(fromID, fallbackToID);
				if (record != null) {
					return record;
				}
			}
			if (fallbackIDMapping.containsKey(fromID) && fallbackIDMapping.containsKey(toID)) {
				final String fallbackFromID = fallbackIDMapping.get(fromID);
				final String fallbackToID = fallbackIDMapping.get(toID);

				record = getIfValid.apply(fallbackFromID, fallbackToID);
				if (record != null) {
					return record;
				}
			}
			return null;
		};
	}

}
