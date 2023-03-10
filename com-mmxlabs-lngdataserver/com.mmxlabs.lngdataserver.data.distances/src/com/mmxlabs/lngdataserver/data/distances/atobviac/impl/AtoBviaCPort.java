/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AtoBviaCPort {
    @JsonProperty("Code")
    private String code;

    @JsonProperty("CountryCode")
    private String countryCode;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("LatGeodetic")
    private double latGeodetic;

    @JsonProperty("Lon")
    private double lon;

    @JsonProperty("Locode")
    private String locode;

    @JsonProperty("Aliases")
    private List<String> aliases;

    @JsonProperty("TimeZone")
    private AtoBviaCTimezone timezone;
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatGeodetic() {
        return latGeodetic;
    }

    public void setLatGeodetic(double latGeodetic) {
        this.latGeodetic = latGeodetic;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getLocode() {
        return locode;
    }

    public void setLocode(String locode) {
        this.locode = locode;
    }

	public AtoBviaCTimezone getTimezone() {
		return timezone;
	}

	public void setTimezone(AtoBviaCTimezone timezone) {
		this.timezone = timezone;
	}
}
