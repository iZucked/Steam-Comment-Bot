/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.time.Instant;
import java.util.Objects;

public class CloudOptimisationDataResultRecord {

	private String uuid;
	
	private String jobid;

	private String creator;
	
	private Instant creationDate;
	
	private String anonyMapFileName;

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
	
	public String getAnonyMapFileName() {
		return this.anonyMapFileName;
	}
	
	public void setAnonyMapFileName(String anonyMapFileName) {
		this.anonyMapFileName = anonyMapFileName;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof CloudOptimisationDataResultRecord) {
			CloudOptimisationDataResultRecord other = (CloudOptimisationDataResultRecord) obj;
			return Objects.equals(uuid, other.getUuid());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}
