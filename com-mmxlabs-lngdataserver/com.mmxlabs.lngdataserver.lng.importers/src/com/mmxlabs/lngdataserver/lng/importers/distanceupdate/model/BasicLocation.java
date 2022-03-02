/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author robert.erdin@minimaxlabs.com created on 22/01/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mmxId")
public class BasicLocation {

	private String mmxId;
	private String upstreamID;
	private String fallbackUpstreamId;

	private boolean virtual = false;

	private String name;

	private List<String> aliases;

	private GeographicPoint geographicPoint;

	public static BasicLocation of(String name, String mmxId, String upstreamID) {
		BasicLocation result = new BasicLocation();
		result.setName(name);
		result.setMmxId(mmxId);
		result.setUpstreamID(upstreamID);
		result.setGeographicPoint(new GeographicPoint());
		return result;
	}

	public String getUpstreamID() {
		return upstreamID;
	}

	public void setUpstreamID(String upstreamID) {
		this.upstreamID = upstreamID;
	}

	public String getMmxId() {
		return mmxId;
	}

	public void setMmxId(String mmxId) {
		this.mmxId = mmxId;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public GeographicPoint getGeographicPoint() {
		return geographicPoint;
	}

	public void setGeographicPoint(GeographicPoint geographicPoint) {
		this.geographicPoint = geographicPoint;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public String getFallbackUpstreamId() {
		return fallbackUpstreamId;
	}

	public void setFallbackUpstreamId(String fallbackUpstreamId) {
		this.fallbackUpstreamId = fallbackUpstreamId;
	}

	@Override
	public String toString() {
		return name + " (" + mmxId + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		BasicLocation port = (BasicLocation) o;

		if (!mmxId.equals(port.mmxId))
			return false;
		return name.equals(port.name);
	}

	@Override
	public int hashCode() {
		int result = mmxId.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}
}
