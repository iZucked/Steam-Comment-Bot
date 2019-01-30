/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.exporters.distances;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.mmxlabs.lngdataserver.integration.distances.model.GeographicPoint;
import com.mmxlabs.lngdataserver.integration.distances.model.Identifier;
import com.mmxlabs.lngdataserver.integration.distances.model.Route;
import com.mmxlabs.lngdataserver.integration.distances.model.Routes;
import com.mmxlabs.lngdataserver.integration.distances.model.RoutingPoint;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.OtherIdentifiers;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;

public class DistancesFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistancesFromScenarioCopier.class);

	// Virtual canal location ids
	private static final String SuezID = "L_V_SuezC";
	private static final String PanamID = "L_V_Panam";

	private DistancesFromScenarioCopier() {

	}

	public static DistancesVersion generateVersion(final PortModel portModel) {

		final DistancesVersion version = new DistancesVersion();

		double suezDistance = 0;
		double panamaDistance = 0;

		String suezNorth = null;
		String suezSouth = null;
		String panamaNorth = null;
		String panamaSouth = null;

		final Routes routes = new Routes();
		for (final com.mmxlabs.models.lng.port.Route route : portModel.getRoutes()) {
			if (route.getRouteOption() == RouteOption.SUEZ) {
				suezNorth = getMMXId(route.getNorthEntrance());
				suezSouth = getMMXId(route.getSouthEntrance());
				suezDistance = route.getDistance();
				continue;
			}
			if (route.getRouteOption() == RouteOption.PANAMA) {
				panamaNorth = getMMXId(route.getNorthEntrance());
				panamaSouth = getMMXId(route.getSouthEntrance());
				panamaDistance = route.getDistance();
				continue;
			}

			if (route.getRouteOption() != RouteOption.DIRECT) {
				continue;
			}

			for (final RouteLine l : route.getLines()) {
				final String from = getMMXId(l.getFrom());
				final String to = getMMXId(l.getTo());

				if (from == null || to == null) {
					throw new IllegalStateException("Port with no MMX id found - unable to export data");
				}

				final Route versionRoute = new Route();
				versionRoute.setDistance(Double.valueOf(l.getDistance()).floatValue());
				versionRoute.setProvider(l.getProvider());
				versionRoute.setErrorCode(l.getErrorCode());
				routes.getRoutes().put(String.format("%s>%s", from, to), versionRoute);
			}
		}

		if (suezDistance == 0) {
			throw new IllegalStateException("No Suez canal distance found - unable to export data");
		}
		if (panamaDistance == 0) {
			throw new IllegalStateException("No Panama canal distance found - unable to export data");
		}

		final List<com.mmxlabs.lngdataserver.integration.distances.model.Location> locations = new LinkedList<>();

		com.mmxlabs.lngdataserver.integration.distances.model.Location suezLocation = null;
		com.mmxlabs.lngdataserver.integration.distances.model.Location suezNorthernLocation = null;
		com.mmxlabs.lngdataserver.integration.distances.model.Location suezSouthernLocation = null;
		com.mmxlabs.lngdataserver.integration.distances.model.Location panamaLocation = null;
		com.mmxlabs.lngdataserver.integration.distances.model.Location panamaNorthernLocation = null;
		com.mmxlabs.lngdataserver.integration.distances.model.Location panamaSouthernLocation = null;
		for (final Port p : portModel.getPorts()) {
			final String mmxId = getMMXId(p);
			final com.mmxlabs.lngdataserver.integration.distances.model.Location versionLocation = new com.mmxlabs.lngdataserver.integration.distances.model.Location();
			versionLocation.setMmxId(mmxId);
			versionLocation.setName(p.getName());
			if (!p.getLocation().getOtherNames().isEmpty()) {
				versionLocation.setAliases(new ArrayList<>(p.getLocation().getOtherNames()));
			}

			if (!p.getLocation().getOtherIdentifiers().isEmpty()) {
				versionLocation.setOtherIdentifiers(new LinkedHashMap<>());
				for (OtherIdentifiers i : p.getLocation().getOtherIdentifiers()) {
					versionLocation.getOtherIdentifiers().put(i.getProvider(), new Identifier(i.getIdentifier(), i.getProvider()));
				}
			}

			final GeographicPoint gp = new GeographicPoint();
			gp.setCountry(p.getLocation().getCountry());
			gp.setLat(p.getLocation().getLat());
			gp.setLon(p.getLocation().getLon());
			gp.setTimeZone(p.getLocation().getTimeZone());
			versionLocation.setGeographicPoint(gp);

			if (SuezID.contentEquals(mmxId)) {
				versionLocation.setVirtual(true);
				suezLocation = versionLocation;
			}
			if (PanamID.contentEquals(mmxId)) {
				versionLocation.setVirtual(true);
				panamaLocation = versionLocation;
			}
			if (Objects.equal(suezNorth, mmxId)) {
				suezNorthernLocation = versionLocation;
			}
			if (Objects.equal(suezSouth, mmxId)) {
				suezSouthernLocation = versionLocation;
			}
			if (Objects.equal(panamaNorth, mmxId)) {
				panamaNorthernLocation = versionLocation;
			}
			if (Objects.equal(panamaSouth, mmxId)) {
				panamaSouthernLocation = versionLocation;
			}
			locations.add(versionLocation);
		}

		version.setIdentifier(portModel.getDistanceVersionRecord().getVersion());
		version.setCreatedBy(portModel.getDistanceVersionRecord().getCreatedBy());
		version.setCreatedAt(portModel.getDistanceVersionRecord().getCreatedAt());

		version.setLocations(locations);

		version.setRoutes(routes);

		final List<RoutingPoint> routingPoints = new LinkedList<>();
		{
			final RoutingPoint rp = new RoutingPoint();
			rp.setEntryPoints(new LinkedHashSet<>());
			rp.setVirtualLocation(suezLocation);
			rp.getEntryPoints().add(suezNorthernLocation);
			rp.getEntryPoints().add(suezSouthernLocation);
			rp.setIdentifier("SUZ");
			rp.setDistance(Double.valueOf(suezDistance).floatValue());

			routingPoints.add(rp);
		}
		{
			final RoutingPoint rp = new RoutingPoint();
			rp.setEntryPoints(new LinkedHashSet<>());

			rp.setVirtualLocation(panamaLocation);
			rp.getEntryPoints().add(panamaNorthernLocation);
			rp.getEntryPoints().add(panamaSouthernLocation);
			rp.setIdentifier("PAN");
			rp.setDistance(Double.valueOf(panamaDistance).floatValue());

			routingPoints.add(rp);
		}

		version.setRoutingPoints(routingPoints);
		return version;
	}

	private static String getMMXId(final EntryPoint entryPoint) {
		if (entryPoint != null) {
			final Port port = entryPoint.getPort();
			return getMMXId(port);
		}
		return null;
	}

	private static String getMMXId(final Port port) {
		if (port != null) {
			final Location location = port.getLocation();
			if (location != null) {
				final String mmxId = location.getMmxId();
				if (mmxId != null && !mmxId.isEmpty()) {
					return mmxId;
				}
			}
		}
		return null;
	}
}
