/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VesselsVersion {

	private String identifier;

	@Reference
	private List<Vessel> vessels = new ArrayList<>();

	private Instant createdAt;
	private String createdBy;

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public VesselsVersion() {
		// jackson
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<Vessel> getVessels() {
		return vessels;
	}

	public void setVessels(List<Vessel> vessels) {
		this.vessels = vessels;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
