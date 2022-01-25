/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.time.Instant;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CloudOptimisationDataResultRecord {

	/**
	 * UUID of original scenario
	 */
	private String uuid;
	/**
	 * Display name of original scenario
	 */
	private String originalName;

	private String jobid;

	private String creator;

	private Instant creationDate;

	private String anonyMapFileName;

	// Type of optimisation
	private String type;

	// UUID of component e.g. sandbox
	private String componentUUID;

	private boolean remote;

	private boolean deleted;

	private long localRuntime;
	private long cloudRuntime;

	private @NonNull ResultStatus status = ResultStatus.notfound();

	@JsonIgnore
	private File result;

	public CloudOptimisationDataResultRecord copy() {
		CloudOptimisationDataResultRecord r = new CloudOptimisationDataResultRecord();
		r.anonyMapFileName = this.anonyMapFileName;
		r.creationDate = this.creationDate;
		r.creator = this.creator;
		r.deleted = this.deleted;
		r.jobid = this.jobid;
		r.originalName = this.originalName;
		r.remote = this.remote;
		r.result = this.result;
		r.cloudRuntime = this.cloudRuntime;
		r.localRuntime = this.localRuntime;
		r.status = this.status.copy();
		r.uuid = this.uuid;
		r.type = this.type;
		r.componentUUID = this.componentUUID;

		return r;
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

	public String getAnonyMapFileName() {
		return this.anonyMapFileName;
	}

	public void setAnonyMapFileName(String anonyMapFileName) {
		this.anonyMapFileName = anonyMapFileName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
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
		if (obj instanceof CloudOptimisationDataResultRecord other) {
			return Objects.equals(jobid, other.getJobid());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return jobid.hashCode();
	}

	public ResultStatus getStatus() {
		return status;
	}

	public void setStatus(@NonNull ResultStatus status) {
		this.status = status;
	}

	public File getResult() {
		return result;
	}

	public void setResult(File result) {
		this.result = result;
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
	public void setLocalRuntime(long l) {
		this.localRuntime = l;

	}

	public long getLocalRuntime() {
		return localRuntime;
	}

	public long getCloudRuntime() {
		return cloudRuntime;
	}

	public void setCloudRuntime(long cloudRuntime) {
		this.cloudRuntime = cloudRuntime;
	}

	/**
	 * Returns true if the tasks has been submitted or is currently running
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isActive() {
		return status != null && (status.isSubmitted() || status.isRunning());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComponentUUID() {
		return componentUUID;
	}

	public void setComponentUUID(String componentUUID) {
		this.componentUUID = componentUUID;
	}

}
