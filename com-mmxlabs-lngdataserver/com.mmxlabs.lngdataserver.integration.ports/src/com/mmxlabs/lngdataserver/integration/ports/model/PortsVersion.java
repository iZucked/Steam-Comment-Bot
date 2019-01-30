/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PortsVersion {

	private String identifier;

	private List<Port> ports = new ArrayList<>();

	private Instant createdAt;

	private String createdBy;

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final Instant createdAt) {
		this.createdAt = createdAt;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<Port> getPorts() {
		return ports;
	}

	public void setPorts(final List<Port> ports) {
		this.ports = ports;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
