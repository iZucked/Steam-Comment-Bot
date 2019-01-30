package com.mmxlabs.lngdataserver.integration.general.model.vesselgroups;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class VesselGroupsVersion {

	private String identifier;

	private Instant createdAt;

	private String createdBy;

	private List<VesselGroupDefinition> groups = new LinkedList<>();

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

	public List<VesselGroupDefinition> getGroups() {
		return groups;
	}

	public void setGroups(List<VesselGroupDefinition> groups) {
		this.groups = groups;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
