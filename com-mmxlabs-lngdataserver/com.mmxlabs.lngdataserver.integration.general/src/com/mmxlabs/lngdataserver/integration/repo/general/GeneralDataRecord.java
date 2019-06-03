/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.general;

import java.time.Instant;

import com.google.common.base.Objects;

public class GeneralDataRecord {

	private String uuid;

	private String creator;

	private Instant creationDate;

	private String type;
	
	private String contentType;
	
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof GeneralDataRecord) {
			final GeneralDataRecord other = (GeneralDataRecord) obj;
			return Objects.equal(uuid, other.getUuid());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
