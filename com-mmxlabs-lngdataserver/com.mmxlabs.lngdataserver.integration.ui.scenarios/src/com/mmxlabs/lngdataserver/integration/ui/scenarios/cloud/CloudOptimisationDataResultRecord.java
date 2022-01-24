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
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class CloudOptimisationDataResultRecord {

	/**
	 * UUID of original scenario
	 */
	private String uuid;
	/**
	 * Display name of original scenario
	 */
	private String originalName;

	/**
	 * The optimisation result name as this is not passed to the cloud and back
	 */
	private String resultName;

	/**
	 * The UUID of the resulting optimisation (not needed for a sandbox) once
	 * saved..
	 */
	private String resultUUID;

	private String jobid;

	private String creator;

	private Instant creationDate;

	private String anonyMapFileName;

	// Type of optimisation
	private String type;
	private String subType;

	// UUID of component e.g. sandbox
	private String componentUUID;

	private boolean remote;

	private boolean deleted;

	private long localRuntime;
	private long cloudRuntime;

	private boolean localJob;

	private @NonNull ResultStatus status = ResultStatus.notfound();

	@JsonIgnore
	private File result;
	
	@JsonIgnore
	private ScenarioInstance scenarioInstance;

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

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getResultUUID() {
		return resultUUID;
	}

	public void setResultUUID(String resultUUID) {
		this.resultUUID = resultUUID;
	}

	public boolean isLocalJob() {
		return localJob;
	}

	public void setLocalJob(boolean localJob) {
		this.localJob = localJob;
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

}
