/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.portgroups;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class PortGroupsVersion {

	private String identifier;

	private Instant createdAt;

	private String createdBy;

	private List<PortGroupDefinition> groups = new LinkedList<>();

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

	public List<PortGroupDefinition> getGroups() {
		return groups;
	}

	public void setGroups(List<PortGroupDefinition> groups) {
		this.groups = groups;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
