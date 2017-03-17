/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.utils;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class AnalyticsSolution {
	private String title;
	private final String id;
	private ScenarioInstance scenarioInstance;
	private EObject solution;
	private boolean createDiffToBaseAction;

	public boolean isCreateDiffToBaseAction() {
		return createDiffToBaseAction;
	}

	public void setCreateDiffToBaseAction(boolean createDiffToBaseAction) {
		this.createDiffToBaseAction = createDiffToBaseAction;
	}

	public AnalyticsSolution(final ScenarioInstance instance, final EObject solution, final String title) {
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

	public EObject getSolution() {
		return solution;
	}

	public void setSolution(final EObject solution) {
		this.solution = solution;
	}

	public String getID() {
		return id;
	}
}
