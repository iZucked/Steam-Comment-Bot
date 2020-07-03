/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.model;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author robert.erdin@gmail.com created on 22/01/17.
 */
public class RoutingPoint {

	private String identifier;

	private Location virtualLocation;

	private Set<Location> entryPoints;

	private double distance;

	public RoutingPoint() {
		// used for morphia/jackson
	}

	public RoutingPoint(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Location getVirtualLocation() {
		return virtualLocation;
	}

	public void setVirtualLocation(Location virtualLocation) {
		this.virtualLocation = virtualLocation;
	}

	public Set<Location> getEntryPoints() {
		return entryPoints;
	}

	public void setEntryPoints(Set<Location> entryPoints) {
		this.entryPoints = entryPoints;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@JsonIdentityReference
	@JsonIgnore
	public Location getNorthernEntry() {
		if (entryPoints.size() != 2) {
			throw new IllegalStateException("Number of canal entry points != 2");
		}
		return entryPoints.stream().sorted((ep1, ep2) -> Double.compare(ep1.getGeographicPoint().getLat(), ep2.getGeographicPoint().getLat())).collect(Collectors.toList()).get(1);
	}

	@JsonIdentityReference
	@JsonIgnore
	public Location getSouthernEntry() {
		return entryPoints.stream().sorted((ep1, ep2) -> Double.compare(ep1.getGeographicPoint().getLat(), ep2.getGeographicPoint().getLat())).collect(Collectors.toList()).get(0);
	}

	@Override
	public String toString() {
		return this.identifier;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RoutingPoint)) {
			return false;
		}

		RoutingPoint that = (RoutingPoint) o;

		return identifier.equals(that.identifier);
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
}