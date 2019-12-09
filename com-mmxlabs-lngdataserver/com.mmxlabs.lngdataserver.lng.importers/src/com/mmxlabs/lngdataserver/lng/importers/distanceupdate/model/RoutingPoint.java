/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model;

/**
 * @author robert.erdin@gmail.com created on 22/01/17.
 */
public class RoutingPoint {

	private String identifier;

	private String virtualLocation;

	private String northernEntry;
	private String southernEntry;

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

	public String getVirtualLocation() {
		return virtualLocation;
	}

	public void setVirtualLocation(String virtualLocation) {
		this.virtualLocation = virtualLocation;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return this.identifier;
	}

	public String getNorthernEntry() {
		return northernEntry;
	}

	public void setNorthernEntry(String northernEntry) {
		this.northernEntry = northernEntry;
	}

	public String getSouthernEntry() {
		return southernEntry;
	}

	public void setSouthernEntry(String southernEntry) {
		this.southernEntry = southernEntry;
	}

}