/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import java.time.LocalDateTime;

public class PortsVersion {
	private String identifier;
	private LocalDateTime createdAt;
	private boolean published;
	
	
	
	public PortsVersion(String identifier, LocalDateTime createdAt, boolean published) {
		super();
		this.identifier = identifier;
		this.createdAt = createdAt;
		this.published = published;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
}
