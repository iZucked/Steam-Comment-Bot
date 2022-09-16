/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.Task;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;

public class CloudOptimisationDataResultRecord {

	// /**
	// * UUID of original scenario
	// */
	private String uuid;

	private String jobid;

	private String anonyMapFileName;

	// // Type of optimisation
	private String type;

	private boolean complete;

	@JsonIgnore
	public Task task;

	private int batchSize;

	public JobDataRecord job;

	private @NonNull ResultStatus status = ResultStatus.notfound();

	public String getUuid() {
		return uuid;
	}

	//
	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	/**
	 * Returns true if the tasks has been submitted or is currently running
	 *
	 * @return
	 */
	@JsonIgnore
	public boolean isActive() {
		return !complete && (status != null && (status.isSubmitted() || status.isRunning()));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

}
