/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports.model;

import java.time.LocalTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

public class Port {

	private String locationMmxId;

	private String shortName;
	private int defaultWindowSize;
	private TimePeriod defaultWindowSizeUnits;
	private Set<PortCapability> capabilities;
	private int berths;

	// Load Capabilities
	private int loadDuration;
	private boolean allowCooldown;
	private double cvValue;

	// Discharge capabilities
	private int dischargeDuration;
	private double minCvValue;
	private double maxCvValue;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	@JsonSerialize(using = LocalTimeSerializer.class)
	private LocalTime defaultStartTime;

	public LocalTime getDefaultStartTime() {
		return defaultStartTime;
	}

	public void setDefaultStartTime(LocalTime defaultStartTime) {
		this.defaultStartTime = defaultStartTime;
	}

	public String getLocationMmxId() {
		return locationMmxId;
	}

	public void setLocationMmxId(String locationMmxId) {
		this.locationMmxId = locationMmxId;
	}

	public int getLoadDuration() {
		return loadDuration;
	}

	public void setLoadDuration(int loadDuration) {
		this.loadDuration = loadDuration;
	}

	public int getDischargeDuration() {
		return dischargeDuration;
	}

	public void setDischargeDuration(int dischargeDuration) {
		this.dischargeDuration = dischargeDuration;
	}

	public int getBerths() {
		return berths;
	}

	public void setBerths(int berths) {
		this.berths = berths;
	}

	public double getCvValue() {
		return cvValue;
	}

	public void setCvValue(double cvValue) {
		this.cvValue = cvValue;
	}

	public double getMinCvValue() {
		return minCvValue;
	}

	public void setMinCvValue(double minCvValue) {
		this.minCvValue = minCvValue;
	}

	public double getMaxCvValue() {
		return maxCvValue;
	}

	public void setMaxCvValue(double maxCvValue) {
		this.maxCvValue = maxCvValue;
	}

	public Set<PortCapability> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(Set<PortCapability> capabilities) {
		this.capabilities = capabilities;
	}

	public boolean getAllowCooldown() {
		return allowCooldown;
	}

	public void setAllowCooldown(boolean allowCooldown) {
		this.allowCooldown = allowCooldown;
	}

	public int getDefaultWindowSize() {
		return defaultWindowSize;
	}

	public void setDefaultWindowSize(int defaultWindowSize) {
		this.defaultWindowSize = defaultWindowSize;
	}

	public TimePeriod getDefaultWindowSizeUnits() {
		return defaultWindowSizeUnits;
	}

	public void setDefaultWindowSizeUnits(TimePeriod defaultWindowSizeUnits) {
		this.defaultWindowSizeUnits = defaultWindowSizeUnits;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
