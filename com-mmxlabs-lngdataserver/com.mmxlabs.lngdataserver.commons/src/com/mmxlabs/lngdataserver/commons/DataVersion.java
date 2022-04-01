/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.commons;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DataVersion {

	private String identifier;
	private Instant createdAt;
	private boolean published;
	private boolean current;

	public DataVersion(String identifier, Instant createdAt, boolean published) {
		this.identifier = identifier;
		this.createdAt = createdAt;
		this.published = published;
	}

	public DataVersion(String identifier, Instant createdAt, boolean published, boolean current) {
		this.identifier = identifier;
		this.createdAt = createdAt;
		this.published = published;
		this.current = current;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getFullIdentifier() {
		if (identifier.contains("initial_version")) {
			return getIdentifier();
		}
		return String.format("%s", createdAt.atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
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

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

}
