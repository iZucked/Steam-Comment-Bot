package com.mmxlabs.lngdataserver.integration.ports.model;

public class PortCountryGroup implements IPortGroup {
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String getGroupID() {
		return "PCG_" + country.toLowerCase();
	}
}
