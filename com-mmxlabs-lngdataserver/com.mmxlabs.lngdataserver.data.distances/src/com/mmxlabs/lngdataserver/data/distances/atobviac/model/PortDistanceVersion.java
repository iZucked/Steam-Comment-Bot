package com.mmxlabs.lngdataserver.data.distances.atobviac.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.mmxlabs.lngdataserver.commons.model.CreatedAtInstantDeserializer;

/**
 * @author robert.erdin@gmail.com created on 03/02/17.
 */
public class PortDistanceVersion {

	private String identifier;

	@JsonDeserialize(using = CreatedAtInstantDeserializer.class)
	@JsonSerialize(using = InstantSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]", timezone = "UTC")
	private Instant createdAt;

	// @Reference
	private List<Location> locations = new ArrayList<>();

	// @Reference
	private List<RoutingPoint> routingPoints = new ArrayList<>();

	public List<RoutingPoint> getRoutingPoints() {
		return routingPoints;
	}

	public void setRoutingPoints(List<RoutingPoint> routingPoints) {
		this.routingPoints = routingPoints;
	}

	public PortDistanceVersion() {
		// morphia/jackson
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

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PortDistanceVersion)) {
			return false;
		}

		PortDistanceVersion version = (PortDistanceVersion) o;

		return identifier == version.identifier;
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

}
