/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.utils;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class AnalyticsSolution {
	private String title;
	private final String id;
	private ScenarioInstance scenarioInstance;
	private UUIDObject solution;
	private final ModelReference modelReference;

	public AnalyticsSolution(final ScenarioInstance instance, final UUIDObject solution, final String title) {
		scenarioInstance = instance;
		this.solution = solution;
		id = instance.getUuid() + title;
		this.title = title;

		@NonNull
		ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
		this.modelReference = modelRecord.aquireReference("AnalyticsSolution:1");
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.modelReference != null) {
			this.modelReference.close();
		}
		super.finalize();
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

	public void open() {
		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.post("create-change-set-view", this);
	}
}
