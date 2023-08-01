/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AtoBviaCLookupRecord {

	private String version;
	private String from;
	private String to;
	private double distance;
	private String errorCode;
	private boolean antiPiracy = true;

	@JsonIgnore
	private String id;

	public void updateId() {
		id = String.format("%s__%s__%s", from, to, antiPiracy); // Changed
	}

	public AtoBviaCLookupRecord() {

	}

	private AtoBviaCLookupRecord(final String version, final String from, final String to) {
		this.version = version;
		this.from = from;
		this.to = to;
	}

	public static AtoBviaCLookupRecord fail(final String version, final String from, final String to) {
		return upstreamError(version, from, to, "timeout");
	}

	public static AtoBviaCLookupRecord distance(final String version, final String from, final String to, final double distance) {
		AtoBviaCLookupRecord r = new AtoBviaCLookupRecord(version, from, to);
		r.errorCode = null;
		r.distance = distance;
		return r;
	}

	public static AtoBviaCLookupRecord upstreamError(final String version, final String from, final String to, final String errorCode) {
		AtoBviaCLookupRecord r = new AtoBviaCLookupRecord(version, from, to);
		r.errorCode = errorCode;
		r.distance = -1.0f;
		return r;
	}

	public double getDistance() {
		return distance;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean getAntiPiracy() {
		return this.antiPiracy;
	}

	public void setAntiPiracy(boolean antiPiracy) {
		this.antiPiracy = antiPiracy;
	}

	@JsonIgnore
	public String getKey() {
		updateId();
		return id;
	}

	public static String updateIdFor(String f, String t, boolean p) {
		return String.format("%s__%s__%s", f, t, p);
	}

}