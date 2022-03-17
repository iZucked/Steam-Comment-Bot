/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.custom;

import java.time.Instant;
import java.util.Objects;

public class CustomReportDataRecord {

	private String uuid;

	private String creator;
	
	private Instant creationDate;

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
		if (obj instanceof CustomReportDataRecord) {
			CustomReportDataRecord other = (CustomReportDataRecord) obj;
			return Objects.equals(uuid, other.getUuid());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}
