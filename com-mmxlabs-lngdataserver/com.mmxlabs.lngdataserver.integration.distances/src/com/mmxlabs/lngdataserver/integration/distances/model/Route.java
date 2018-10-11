package com.mmxlabs.lngdataserver.integration.distances.model;

import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author robert.erdin@gmail.com created on 22/01/17.
 */

public class Route {

	private String provider;

	private float distance;
	private String errorCode;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
