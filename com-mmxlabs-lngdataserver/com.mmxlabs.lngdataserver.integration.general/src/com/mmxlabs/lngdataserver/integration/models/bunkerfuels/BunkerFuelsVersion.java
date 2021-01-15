/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.bunkerfuels;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class BunkerFuelsVersion {

	private String identifier;

	private Instant createdAt;

	private String createdBy;

	private List<BunkerFuelDefinition> fuels = new LinkedList<>();

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public List<BunkerFuelDefinition> getFuels() {
		return fuels;
	}

	public void setFuels(List<BunkerFuelDefinition> fuels) {
		this.fuels = fuels;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
