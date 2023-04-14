/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.mmxlabs.rcp.common.json.CreatedAtInstantDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "published", "current", "metaInformation" })
public class PricingVersion {

	@JsonDeserialize(using = CreatedAtInstantDeserializer.class)
	@JsonSerialize(using = InstantSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]", timezone = "UTC")
	private Instant createdAt;

	private String identifier;

	private Map<String, Curve> curves = new HashMap<>();

	private List<Curve> curvesList = new LinkedList<>();

	private String createdBy;

	public PricingVersion() {
		// jackson
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Map<String, Curve> getCurves() {
		return curves;
	}

	public void setCurves(Map<String, Curve> curves) {
		this.curves = curves;
	}

	public List<Curve> getCurvesList() {
		return curvesList;
	}

	public void setCurvesList(List<Curve> curvesList) {
		this.curvesList = curvesList;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
