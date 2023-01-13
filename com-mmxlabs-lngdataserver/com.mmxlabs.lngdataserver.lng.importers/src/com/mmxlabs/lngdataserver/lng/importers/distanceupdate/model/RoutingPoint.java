/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author robert.erdin@gmail.com created on 22/01/17.
 */
@JsonIgnoreProperties("virtualLocation")
public class RoutingPoint {

	private String identifier;

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