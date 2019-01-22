/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mmxlabs.lngdataserver.integration.pricing.PricingVersionDateDeserializer;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingVersion {

	@JsonDeserialize(using = PricingVersionDateDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]")
	private LocalDateTime createdAt;

	private String identifier;

	@Reference
	private Map<String, Curve> curves = new HashMap<>();

	public PricingVersion() {
		// jackson
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
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
}
