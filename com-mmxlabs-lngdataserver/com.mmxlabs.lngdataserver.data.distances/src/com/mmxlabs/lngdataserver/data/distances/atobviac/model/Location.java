package com.mmxlabs.lngdataserver.data.distances.atobviac.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author robert.erdin@minimaxlabs.com created on 22/01/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties("virtual")
public class Location {
	/**
	 * Constant string for upstreamID field.
	 */
	public static final String FIELD_UPSTEAM_ID = "upstreamID";

	private String mmxId;

	private String name;

	private List<String> aliases;

	private GeographicPoint geographicPoint;

	private String locode;

	private String upstreamID;

	@JsonProperty("fallbackUpstreamId")
	@JsonAlias("fallbackUpstreamID")
	private String fallbackUpstreamID;

	private String notes;

	public Location() {
		setGeographicPoint(new GeographicPoint());
	}

	public static Location of(final String name, final String mmxId, final String abcId) {
		final Location result = new Location();
		result.setName(name);
		result.setMmxId(mmxId);
		result.setUpstreamID(abcId);
		result.setGeographicPoint(new GeographicPoint());
		return result;
	}

	public String getMmxId() {
		return mmxId;
	}

	public void setMmxId(final String mmxId) {
		this.mmxId = mmxId;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setAliases(final List<String> aliases) {
		this.aliases = aliases;
	}

	public GeographicPoint getGeographicPoint() {
		return geographicPoint;
	}

	public void setGeographicPoint(final GeographicPoint geographicPoint) {
		this.geographicPoint = geographicPoint;
	}

	@Override
	public String toString() {
		return name + " (" + mmxId + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Location port = (Location) o;

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

	public String getUpstreamID() {
		return upstreamID;
	}

	public void setUpstreamID(final String upstreamID) {
		this.upstreamID = upstreamID;
	}

	public String getFallbackUpstreamID() {
		return fallbackUpstreamID;
	}

	public void setFallbackUpstreamID(final String fallbackUpstreamID) {
		this.fallbackUpstreamID = fallbackUpstreamID;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(final String notes) {
		this.notes = notes;
	}

	public String getLocode() {
		return locode;
	}

	public void setLocode(String locode) {
		this.locode = locode;
	}

}
