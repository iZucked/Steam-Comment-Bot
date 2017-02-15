package com.mmxlabs.models.lng.analytics.ui.utils;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.ActionPlan;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class AnalyticsSolution {
	private String title;
	private String id;
	private ScenarioInstance scenarioInstance;
	private EObject solution;

	public AnalyticsSolution(ScenarioInstance instance, EObject solution, String title) {
		scenarioInstance = instance;
		this.solution = solution;
		id = title;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public void setScenarioInstance(ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public EObject getSolution() {
		return solution;
	}

	public void setSolution(EObject solution) {
		this.solution = solution;
	}

	public String getID() {
		return null;
	}
}
