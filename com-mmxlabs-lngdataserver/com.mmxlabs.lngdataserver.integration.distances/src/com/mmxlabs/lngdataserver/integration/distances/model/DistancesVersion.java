/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * @author robert.erdin@gmail.com created on 03/02/17.
 */
@Entity("versions")
public class DistancesVersion {

	private String identifier;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss+00:00")
	private LocalDateTime createdAt;

	@Reference
	private Routes routes = new Routes();

	@Reference
	private List<Location> locations = new ArrayList<>();

	@Reference
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
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
}
