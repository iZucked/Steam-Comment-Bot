/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * An optimiser job meta-data record.
 *
 */
public class JobDataRecord {

	/**
	 * UUID of original scenario
	 */
	private String scenarioUUID;
	/**
	 * Display name of original scenario
	 */
	private String scenarioName;

	@JsonIgnore
	private String taskParameters;

	/**
	 * The UUID of the resulting optimisation (not needed for a sandbox) once saved..
	 */
	private String resultUUID;

	private Instant creationDate;

	// Type of optimisation
	private String type;
	// Sub type if applicable, e.g. Define mode of sandbox
	private String subType;

	// UUID of component e.g. sandbox
	private String componentUUID;
	
	private int batchSize = 1;

	@JsonIgnore
	private ScenarioInstance scenarioInstance;

	public String getScenarioUUID() {
		return scenarioUUID;
	}

	public void setScenarioUUID(final String uuid) {
		this.scenarioUUID = uuid;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(final Instant creationDate) {
		this.creationDate = creationDate;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getComponentUUID() {
		return componentUUID;
	}

	public void setComponentUUID(final String componentUUID) {
		this.componentUUID = componentUUID;
	}

	public String getResultUUID() {
		return resultUUID;
	}

	public void setResultUUID(final String resultUUID) {
		this.resultUUID = resultUUID;
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public void setScenarioInstance(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(final String subType) {
		this.subType = subType;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(final String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getTaskParameters() {
		return taskParameters;
	}

	public void setTaskParameters(final String taskParameters) {
		this.taskParameters = taskParameters;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
}
