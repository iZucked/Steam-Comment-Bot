/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.time.LocalDateTime;

public class PricingVersion {
	private String identifier;
	private LocalDateTime createdAt;
	private boolean published;
	private boolean current;
	
	
	public PricingVersion(String identifier, LocalDateTime createdAt, boolean published, boolean current) {
		this.identifier = identifier;
		this.createdAt = createdAt;
		this.published = published;
		this.current = current;
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
	
	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
}
