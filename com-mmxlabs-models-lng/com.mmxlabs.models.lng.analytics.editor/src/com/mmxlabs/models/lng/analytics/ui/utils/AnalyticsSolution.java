/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.utils;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class AnalyticsSolution {
	private String title;
	private final String id;
	private ScenarioInstance scenarioInstance;
	private UUIDObject solution;
	private boolean createDiffToBaseAction;
	private boolean createInsertionOptions;

	public boolean isCreateInsertionOptions() {
		return createInsertionOptions;
	}

	public void setCreateInsertionOptions(boolean createInsertionOptions) {
		this.createInsertionOptions = createInsertionOptions;
	}

	public boolean isCreateDiffToBaseAction() {
		return createDiffToBaseAction;
	}

	public void setCreateDiffToBaseAction(boolean createDiffToBaseAction) {
		this.createDiffToBaseAction = createDiffToBaseAction;
	}

	public AnalyticsSolution(final ScenarioInstance instance, final UUIDObject solution, final String title) {
		scenarioInstance = instance;
		this.solution = solution;
		id = instance.getUuid() + title;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public void setScenarioInstance(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public UUIDObject getSolution() {
		return solution;
	}

	public void setSolution(final UUIDObject solution) {
		this.solution = solution;
	}

	public String getID() {
		return id;
	}
}
