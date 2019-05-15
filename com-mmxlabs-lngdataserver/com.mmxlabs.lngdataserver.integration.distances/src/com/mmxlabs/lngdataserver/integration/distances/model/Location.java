/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author robert.erdin@minimaxlabs.com created on 22/01/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mmxId")
public class Location {

	private String mmxId;

	private boolean virtual = false;

	private Map<String, Identifier> otherIdentifiers;

	private String name;

	private List<String> aliases;

	private GeographicPoint geographicPoint;

	public static Location of(String name, String mmxId, String abcId) {
		Location result = new Location();
		result.setName(name);
		result.setMmxId(mmxId);
		result.setOtherIdentifiers(new HashMap<>());
		result.setGeographicPoint(new GeographicPoint());
		result.getOtherIdentifiers().put("abc", new Identifier(abcId, "abc"));
		return result;
	}

	public String getMmxId() {
		return mmxId;
	}

	public void setMmxId(String mmxId) {
		this.mmxId = mmxId;
	}

	public Map<String, Identifier> getOtherIdentifiers() {
		return otherIdentifiers;
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

	public void setOtherIdentifiers(Map<String, Identifier> otherIdentifiers) {
		this.otherIdentifiers = otherIdentifiers;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
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

		Location port = (Location) o;

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
