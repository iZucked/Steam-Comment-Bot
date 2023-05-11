/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.lngdataserver.commons.model.CreatedAtInstantDeserializer;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

public class AtoBviaCVersion {

	private String identifier;

	@JsonDeserialize(using = CreatedAtInstantDeserializer.class)
	private Instant createdAt;

//	@Reference
	private List<AtoBviaCLookupRecord> records = new ArrayList<>();

	public AtoBviaCVersion() {
		// morphia/jackson
	}

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
	
	public List<AtoBviaCLookupRecord> getRecords() {
		return records;
	}

	public void setRecords(List<AtoBviaCLookupRecord> records) {
		this.records = records;
	}
 
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PortDistanceVersion)) {
			return false;
		}

		AtoBviaCVersion version = (AtoBviaCVersion) o;

		return identifier == version.identifier;
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
}
