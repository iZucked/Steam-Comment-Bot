/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author robert.erdin@minimaxlabs.com created on 22/01/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mmxId")
@JsonIgnoreProperties({"virtual", "otherIdentifiers"})
public class Location {

	private String mmxId;

	private String name;

	private String notes;

	private List<String> aliases;

	private GeographicPoint geographicPoint;

	public static Location of(String name, String mmxId) {
		Location result = new Location();
		result.setName(name);
		result.setMmxId(mmxId);
		result.setGeographicPoint(new GeographicPoint());
		return result;
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

		if (!mmxId.equals(port.mmxId)) {
			return false;
		}
		return name.equals(port.name);
	}

	@Override
	public int hashCode() {
		int result = mmxId.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
