/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.erdin@gmail.com created on 03/02/17.
 */
public class DistancesVersion {

	private String identifier;

	private String abcIdentifier;

	private Instant createdAt;
	
	private String createdBy;

	private Routes routes = new Routes();

	private List<Location> locations = new ArrayList<>();

	private List<RoutingPoint> routingPoints = new ArrayList<>();

	public DistancesVersion() {
		// morphia/jackson
	}

	public List<RoutingPoint> getRoutingPoints() {
		return routingPoints;
	}

	public void setRoutingPoints(List<RoutingPoint> routingPoints) {
		this.routingPoints = routingPoints;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Routes getRoutes() {
		return routes;
	}

	public void setRoutes(Routes routes) {
		this.routes = routes;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DistancesVersion))
			return false;

		DistancesVersion version = (DistancesVersion) o;

		return identifier == version.identifier;
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

	public String getAbcIdentifier() {
		return abcIdentifier;
	}

	public void setAbcIdentifier(String abcIdentifier) {
		this.abcIdentifier = abcIdentifier;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
