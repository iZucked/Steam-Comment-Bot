/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.BasicLocation;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.GeographicPoint;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.LocationsVersion;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.PortReplacement;
import com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model.RoutingPoint;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateError;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateItem;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateStep;
import com.mmxlabs.lngdataserver.lng.importers.update.UpdateWarning;
import com.mmxlabs.lngdataserver.lng.importers.update.UserUpdateStep;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.importer.PortModelImporter;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class LocationsToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationsToScenarioCopier.class);

	public static List<UpdateItem> generateUpdates(final EditingDomain editingDomain, final PortModel portModel, final LocationsVersion version,
			@Nullable final List<PortReplacement> portReplacements) {

		final List<UpdateItem> steps = new LinkedList<>();
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
				unlinkedPorts.add(port);
			}

			final List<Port> portsAdded = new LinkedList<>();
			final List<Port> portsModified = new LinkedList<>();

			final List<BasicLocation> versionLocations = version.getLocations();
			final List<Port> allPorts = new LinkedList<>();
			final Map<String, Port> idToPort = new HashMap<>();

			for (final BasicLocation versionLocation : versionLocations) {
				final GeographicPoint geographicPoint = versionLocation.getGeographicPoint();
				if (geographicPoint != null) {
					countries.add(geographicPoint.getCountry());
				}
				final String mmxId = versionLocation.getMmxId();
				final Port oldPort;

				if (portMap.containsKey(mmxId)) {
					oldPort = portMap.get(mmxId);
					portsModified.add(oldPort);
					allPorts.add(oldPort);

					// TODO: This can be superceeded (or has already been...) by the later rename
					// and replace checks.
					if (!Objects.equals(oldPort.getName().toLowerCase(), versionLocation.getName().toLowerCase())) {
						if (versionLocation.getAliases() == null || !versionLocation.getAliases().contains(oldPort.getName())) {
							final UpdateStep step2 = new UpdateWarning(String.format("Existing port called %s should be called %s", oldPort.getName(), versionLocation.getName()), "Rename?", cmd -> {
								cmd.append(SetCommand.create(editingDomain, oldPort, MMXCorePackage.Literals.NAMED_OBJECT__NAME, versionLocation.getName()));
							});
							steps.add(step2);
						}

					}

					// Check aliases
					if (versionLocation.getAliases() != null) {
						List<String> toAdd = new LinkedList<>();
						for (String a : versionLocation.getAliases()) {
							if (!oldPort.getLocation().getOtherNames().contains(a)) {
								toAdd.add(a);
							}
							if (!toAdd.isEmpty()) {
								final UpdateStep step2 = new UpdateWarning(String.format("Adding aliases (%s) to port %s", toAdd.stream().collect(Collectors.joining(" ,")),  versionLocation.getName()),
										cmd -> {
											cmd.append(AddCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.OTHER_NAMES_OBJECT__OTHER_NAMES, toAdd));
										});
								steps.add(step2);
							}
						}

					}

					final Location portLocation = oldPort.getLocation();
					if (geographicPoint != null && portLocation != null) {
						final String versionObjectTimezone = versionLocation.getGeographicPoint().getTimeZone();
						if (!Objects.equals(portLocation.getTimeZone().toLowerCase(), versionObjectTimezone.toLowerCase())) {
							final UpdateStep step2 = new UpdateWarning(String.format("Existing port %s has new timezone of %s", oldPort.getName(), versionObjectTimezone), "Update?", cmd -> {
								cmd.append(SetCommand.create(editingDomain, portLocation, PortPackage.Literals.LOCATION__TIME_ZONE, versionObjectTimezone));
							});
							steps.add(step2);
						}

						if (!Objects.equals(oldPort.getLocation().getCountry(), geographicPoint.getCountry())) {
							final UpdateStep step = new UpdateStep("Update country", cmd -> {
								cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__COUNTRY, geographicPoint.getCountry()));
							});
							steps.add(step);
						}
						if (!Objects.equals(oldPort.getLocation().getName(), versionLocation.getName())) {
							final UpdateStep step = new UpdateStep("Update location name", cmd -> {
								cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, versionLocation.getName()));
							});
							steps.add(step);
						}

						if (versionLocation.getLocode() != null && !versionLocation.getLocode().isBlank()) {
							if (!Objects.equals(oldPort.getLocation().getLocode(), versionLocation.getLocode())) {
								final UpdateStep step = new UpdateStep("Update locode", cmd -> {
									cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__LOCODE, versionLocation.getLocode()));
								});
								steps.add(step);
							}
						}

						if ((Math.abs(portLocation.getLat() - geographicPoint.getLat()) > 0.001) || (Math.abs(portLocation.getLon() - geographicPoint.getLon()) > 0.001)) {

							final UpdateStep step2 = new UpdateWarning(
									String.format("Existing port %s has new lat/lon of (%,.3f, %,.3f)", oldPort.getName(), geographicPoint.getLat(), geographicPoint.getLon()), "Update?", cmd -> {
										cmd.append(SetCommand.create(editingDomain, portLocation, PortPackage.Literals.LOCATION__LAT, geographicPoint.getLat()));
										cmd.append(SetCommand.create(editingDomain, portLocation, PortPackage.Literals.LOCATION__LON, geographicPoint.getLon()));
									});
							steps.add(step2);
						}
					}

				} else {
					oldPort = PortFactory.eINSTANCE.createPort();
					oldPort.setLocation(PortFactory.eINSTANCE.createLocation());
					oldPort.getLocation().setMmxId(mmxId);

					oldPort.setName(versionLocation.getName());
					oldPort.getLocation().setName(versionLocation.getName());

					if (versionLocation.getAliases() != null && !versionLocation.getAliases().isEmpty()) {
						oldPort.getLocation().getOtherNames().addAll(versionLocation.getAliases());
					}

					if (versionLocation.getLocode() != null && !versionLocation.getLocode().isBlank()) {
						oldPort.getLocation().setLocode(versionLocation.getLocode());
					}

					oldPort.setDefaultWindowSize(1);
					oldPort.setDefaultWindowSizeUnits(TimePeriod.DAYS);

					portsAdded.add(oldPort);
					allPorts.add(oldPort);
					String country = "";
					if (geographicPoint != null) {
						oldPort.getLocation().setCountry(geographicPoint.getCountry());
						country = " in " + geographicPoint.getCountry();
						oldPort.getLocation().setTimeZone(geographicPoint.getTimeZone());
						oldPort.getLocation().setLat(geographicPoint.getLat());
						oldPort.getLocation().setLon(geographicPoint.getLon());
					}
					final UserUpdateStep step = new UserUpdateStep(String.format("Creating new port %s%s. Please review port data.", oldPort.getName(), country), cmd -> {
						cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, oldPort));
					});
					steps.add(step);

					for (final Port port : portModel.getPorts()) {
						if (Objects.equals(port.getName(), oldPort.getName())) {
							if (!Objects.equals(port.getLocation().getMmxId(), oldPort.getLocation().getMmxId())) {

								for (final BasicLocation bl : versionLocations) {
									if (Objects.equals(bl.getMmxId(), port.getLocation().getMmxId())) {
										final UpdateStep step2 = new UpdateError(
												String.format("Existing port called %s conflicts with new port of the same name. The old port should be called %s", port.getName(), bl.getName()),
												"Rename existing and replace existing references with new port?", cmd -> {

													final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(Collections.singleton(port),
															editingDomain.getResourceSet());
													final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(port);
													if (usages != null) {
														for (final EStructuralFeature.Setting setting : usages) {
															if (setting.getEStructuralFeature() != PortPackage.Literals.PORT_MODEL__PORTS) {
																final EObject eObject = setting.getEObject();
																if (setting.getEStructuralFeature().isMany()) {
																	final Collection<?> col = (Collection<?>) eObject.eGet(setting.getEStructuralFeature());
																	cmd.append(ReplaceCommand.create(editingDomain, eObject, setting.getEStructuralFeature(), port, Collections.singleton(oldPort)));
																} else {
																	cmd.append(SetCommand.create(editingDomain, eObject, setting.getEStructuralFeature(), oldPort));
																}
															}
														}
													}
												});
										steps.add(step2);
										break;
									}
								}
							}
						}
					}

				}
				idToPort.put(mmxId, oldPort);
			}

			// Ports to remove from the scenario.
			final Set<Port> portsToRemove = new HashSet<>(portModel.getPorts());
			portsToRemove.removeAll(portsModified);

			// Check the replacements list first. We may merge one port into another instead
			if (portReplacements != null) {
				for (final var pr : portReplacements) {
					final String oldMMXID = pr.getOldPort();
					// Find the old port, if it still exists
					if (idToPort.containsKey(oldMMXID) || portsToRemove.stream().anyMatch(p -> oldMMXID.equals(p.getLocation().getMmxId()))) {

						Port oldPort = idToPort.get(oldMMXID);
						if (oldPort == null) {
							oldPort = portsToRemove.stream().filter(p -> oldMMXID.equals(p.getLocation().getMmxId())).findFirst().get();
						}
						final Port pOldPort = oldPort;

						// Remove from the general remove list
						portsToRemove.remove(oldPort);
						final UpdateError step = new UpdateError(pr.getMessage(), cmd -> {

							cmd.append(new CompoundCommand() {

								@Override
								public boolean canExecute() {
									return true;
								}

								@Override
								public void execute() {

									final Port newPort = idToPort.get(pr.getNewPort());

									// Find all cross-references and replace the old reference with the new one.
									// If the cross-reference is in a list which already contains the new port, then we just remove instead.
									final var references = EcoreUtil.UsageCrossReferencer.findAll(Collections.singletonList(pOldPort), editingDomain.getResourceSet());
									for (final Map.Entry<EObject, Collection<EStructuralFeature.Setting>> e : references.entrySet()) {
										final Collection<EStructuralFeature.Setting> usages = e.getValue();
										for (final EStructuralFeature.Setting setting : usages) {
											// PortModel container? remove later
											if (setting.getEStructuralFeature() instanceof final EReference ref && ref.isContainment()) {
												continue;
											}

											final EObject owner = setting.getEObject();
											if (setting.getEStructuralFeature().isMany()) {
												final List l = ((List<?>) owner.eGet(setting.getEStructuralFeature()));
												if (l.contains(newPort)) {
													// New port already exists, just remove the old one
													appendAndExecute(RemoveCommand.create(editingDomain, owner, setting.getEStructuralFeature(), pOldPort));
												} else {
													// Replace the old with the new
													appendAndExecute(ReplaceCommand.create(editingDomain, owner, setting.getEStructuralFeature(), pOldPort, Collections.singleton(newPort)));
												}
											} else {
												// Replace the old with the new
												appendAndExecute(SetCommand.create(editingDomain, owner, setting.getEStructuralFeature(), newPort));
											}
										}
									}

									// Remove the port. No need for delete as this will just re-run the cross referencer
									appendAndExecute(RemoveCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, pOldPort));

									// Clean up missing routes.
									for (final Route route : portModel.getRoutes()) {
										final List<RouteLine> toDelete = new LinkedList<>();
										for (final RouteLine rl : route.getLines()) {
											if (portsToRemove.contains(rl.getFrom()) || portsToRemove.contains(rl.getTo())) {
												toDelete.add(rl);
											}
										}
										if (!toDelete.isEmpty()) {
											appendAndExecute(RemoveCommand.create(editingDomain, route, PortPackage.Literals.ROUTE__LINES, toDelete));
										}
									}
								}
							});
						});
						steps.add(step);
					}
				}
			}

			// Any ports to remove that have not been merged?
			if (!portsToRemove.isEmpty()) {
				final String lbl = portsToRemove.stream().map(p -> p.getName()).collect(Collectors.joining(", "));
				final UpdateStep step = new UpdateError(String.format("Remove unsupported ports %s", lbl), cmd -> {
					cmd.append(DeleteCommand.create(editingDomain, portsToRemove));

					// Clean up missing routes.
					final List<RouteLine> toDelete = new LinkedList<>();
					for (final Route route : portModel.getRoutes()) {
						for (final RouteLine rl : route.getLines()) {
							if (portsToRemove.contains(rl.getFrom()) || portsToRemove.contains(rl.getTo())) {
								toDelete.add(rl);
							}
						}
					}
					if (!toDelete.isEmpty()) {
						cmd.append(DeleteCommand.create(editingDomain, toDelete));
					}
				});
				steps.add(step);
			}

			final List<RoutingPoint> routingPoints = version.getRoutingPoints();
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

					final Route pRoute = route;
					final UpdateStep step = new UserUpdateStep(String.format("Adding route  %s", route.getName()), cmd -> {
						cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__ROUTES, pRoute));
					});
					steps.add(step);
				}

				routeMap.put(option, route);

				if (option != RouteOption.DIRECT) {
					final Route pRoute = route;

					for (final RoutingPoint t : routingPoints) {
						final String northSide = t.getNorthernEntry();
						final String southSide = t.getSouthernEntry();
						if ((option == RouteOption.SUEZ && t.getIdentifier().equals("SUZ")) || (option == RouteOption.PANAMA && t.getIdentifier().equals("PAN"))) {

							if (Math.abs(t.getDistance() - route.getDistance()) > 0.1) {
								final UpdateStep step = new UserUpdateStep(String.format("Update canal distance (%s)", option.getName()), cmd -> {
									cmd.append(SetCommand.create(editingDomain, pRoute, PortPackage.Literals.ROUTE__DISTANCE, t.getDistance()));
								});
								steps.add(step);
							}

							// Northside entrance.
							{
								EntryPoint north = route.getNorthEntrance();
								if (north == null) {
									north = PortFactory.eINSTANCE.createEntryPoint();
									north.setName("Northside");
									final EntryPoint pNorth = north;
									final UpdateStep step = new UserUpdateStep(String.format("Add canal northside entrance (%s - %s)", option.getName(), pNorth.getName()), cmd -> {
										cmd.append(SetCommand.create(editingDomain, pRoute, PortPackage.Literals.ROUTE__NORTH_ENTRANCE, pNorth));
									});
									steps.add(step);

								}
								final EntryPoint pNorth = north;
								if (pNorth.getPort() != idToPort.get(northSide)) {
									final UpdateStep step = new UserUpdateStep(String.format("Set canal northside port (%s - %s)", option.getName(), idToPort.get(northSide).getName()), cmd -> {
										cmd.append(SetCommand.create(editingDomain, pNorth, PortPackage.Literals.ENTRY_POINT__PORT, idToPort.get(northSide)));
									});
									steps.add(step);
								}
							}
							// Southside entrance
							{
								EntryPoint south = route.getSouthEntrance();
								if (south == null) {
									south = PortFactory.eINSTANCE.createEntryPoint();
									south.setName("Southside");

									final EntryPoint pSouth = south;
									final UpdateStep step = new UserUpdateStep(String.format("Add canal southside entrance (%s - %s)", option.getName(), pSouth.getName()), cmd -> {
										cmd.append(SetCommand.create(editingDomain, pRoute, PortPackage.Literals.ROUTE__SOUTH_ENTRANCE, pSouth));
									});
									steps.add(step);

								}
								final EntryPoint pSouth = south;
								if (pSouth.getPort() != idToPort.get(southSide)) {
									final UpdateStep step = new UserUpdateStep(String.format("Set canal sorthside port (%s - %s)", option.getName(), idToPort.get(southSide).getName()), cmd -> {
										cmd.append(SetCommand.create(editingDomain, pSouth, PortPackage.Literals.ENTRY_POINT__PORT, idToPort.get(southSide)));
									});
									steps.add(step);
								}
							}
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
					final PortCountryGroup pExistingGroup = existingGroup;
					final UpdateStep step = new UserUpdateStep(String.format("Adding country group  %s", existingGroup.getName()), cmd -> {
						cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS, pExistingGroup));
					});
					steps.add(step);
				}
				updated.add(existingGroup);
			}

			existing.removeAll(updated);
			if (!existing.isEmpty()) {
				final String lbl = existing.stream().map(p -> p.getName()).collect(Collectors.joining(", "));
				final UpdateStep step = new UpdateError(String.format("Remove unsupported country groups %s", lbl), cmd -> {
					cmd.append(DeleteCommand.create(editingDomain, existing));
				});
				steps.add(step);
			}
		}

		return steps;
	}

}
