/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.erdin@gmail.com created on 03/02/17.
 */
public class LocationsVersion {

	private List<BasicLocation> locations = new ArrayList<>();

	private List<RoutingPoint> routingPoints = new ArrayList<>();

	public List<RoutingPoint> getRoutingPoints() {
		return routingPoints;
	}

	public void setRoutingPoints(List<RoutingPoint> routingPoints) {
		this.routingPoints = routingPoints;
	}

	public List<BasicLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<BasicLocation> locations) {
		this.locations = locations;
	}
}