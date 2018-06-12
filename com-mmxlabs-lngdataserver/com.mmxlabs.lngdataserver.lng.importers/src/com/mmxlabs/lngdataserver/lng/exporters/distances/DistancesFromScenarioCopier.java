/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.exporters.distances;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataservice.client.distances.model.GeographicPoint;
import com.mmxlabs.lngdataservice.client.distances.model.Route;
import com.mmxlabs.lngdataservice.client.distances.model.Routes;
import com.mmxlabs.lngdataservice.client.distances.model.RoutingPoint;
import com.mmxlabs.lngdataservice.client.distances.model.Version;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;

public class DistancesFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistancesFromScenarioCopier.class);

	// Virtual canal location ids
	private static final String SuezID = "L_V_SuezC";
	private static final String PanamID = "L_V_Panam";

	public static Version generateVersion(final PortModel portModel) {

		final Version version = new Version();

		int suezDistance = 0;
		int panamaDistance = 0;

		String suezNorth = null;
		String suezSouth = null;
		String panamaNorth = null;
		String panamaSouth = null;

		final Routes routes = new Routes();
		for (final com.mmxlabs.models.lng.port.Route route : portModel.getRoutes()) {
			if (route.getRouteOption() == RouteOption.SUEZ) {
				suezNorth = getMMXId(route.getNorthEntrance());
				suezSouth = getMMXId(route.getSouthEntrance());
				continue;
			}
			if (route.getRouteOption() == RouteOption.PANAMA) {
				panamaNorth = getMMXId(route.getNorthEntrance());
				panamaSouth = getMMXId(route.getSouthEntrance());
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
				versionRoute.setDistance(Float.valueOf(l.getDistance()));
				routes.putRoutesItem(String.format("%s>%s", from, to), versionRoute);

				if (from.equals(suezNorth) && to.equals(suezSouth)) {
					suezDistance = l.getDistance();
				}
				if (from.equals(panamaNorth) && to.equals(panamaSouth)) {
					panamaDistance = l.getDistance();
				}
			}
		}

		if (suezDistance == 0) {
			throw new IllegalStateException("No Suez canal distance found - unable to export data");
		}
		if (panamaDistance == 0) {
			throw new IllegalStateException("No Panama canal distance found - unable to export data");
		}

		final List<com.mmxlabs.lngdataservice.client.distances.model.Location> locations = new LinkedList<>();

		com.mmxlabs.lngdataservice.client.distances.model.Location suezLocation = null;
		com.mmxlabs.lngdataservice.client.distances.model.Location suezNorthernLocation = null;
		com.mmxlabs.lngdataservice.client.distances.model.Location suezSouthernLocation = null;
		com.mmxlabs.lngdataservice.client.distances.model.Location panamaLocation = null;
		com.mmxlabs.lngdataservice.client.distances.model.Location panamaNorthernLocation = null;
		final com.mmxlabs.lngdataservice.client.distances.model.Location panamaSouthernLocation = null;
		for (final Port p : portModel.getPorts()) {
			final String mmxId = getMMXId(p);
			final com.mmxlabs.lngdataservice.client.distances.model.Location versionLocation = new com.mmxlabs.lngdataservice.client.distances.model.Location();
			versionLocation.setMmxId(mmxId);
			versionLocation.setName(p.getName());
			versionLocation.setAliases(new ArrayList<>(p.getLocation().getOtherNames()));

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
			if (suezNorth.equals(mmxId)) {
				suezNorthernLocation = versionLocation;
			}
			if (suezSouth.equals(mmxId)) {
				suezSouthernLocation = versionLocation;
			}
			if (panamaNorth.equals(mmxId)) {
				panamaNorthernLocation = versionLocation;
			}
			if (panamaSouth.equals(mmxId)) {
				panamaNorthernLocation = versionLocation;
			}
			locations.add(versionLocation);
		}

		String distanceDataVersion = portModel.getDistanceDataVersion();
		if (distanceDataVersion == null) {
			distanceDataVersion = "private-" + EcoreUtil.generateUUID();
			portModel.setDistanceDataVersion(distanceDataVersion);
		}
		version.setIdentifier(distanceDataVersion);
		version.setLocations(locations);

		version.setRoutes(routes);

		final List<RoutingPoint> routingPoints = new LinkedList<>();
		{
			final RoutingPoint rp = new RoutingPoint();

			rp.setVirtualLocation(suezLocation);
			rp.setNorthernEntry(suezNorthernLocation);
			rp.setSouthernEntry(suezSouthernLocation);
			rp.setIdentifier("SUZ");
			rp.setDistance(Float.valueOf(suezDistance));

			routingPoints.add(rp);
		}
		{
			final RoutingPoint rp = new RoutingPoint();

			rp.setVirtualLocation(panamaLocation);
			rp.setNorthernEntry(panamaNorthernLocation);
			rp.setSouthernEntry(panamaSouthernLocation);
			rp.setIdentifier("PAN");
			rp.setDistance(Float.valueOf(panamaDistance));

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
