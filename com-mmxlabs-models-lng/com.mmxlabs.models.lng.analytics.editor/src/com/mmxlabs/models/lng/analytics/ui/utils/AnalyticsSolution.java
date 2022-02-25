/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.utils;

import java.util.UUID;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class AnalyticsSolution {

	public static final String OPEN_RESULTS_VIEW = "OPEN_RESULTS_VIEW";
	public static final String OPEN_RESULTS_VIEW_WITH_SCREEN = "OPEN_RESULTS_VIEW_WITH_SCREEN";

	private String title;
	private final String id;
	private ScenarioInstance scenarioInstance;
	private UUIDObject solution;
	private final ModelReference modelReference;
	private @Nullable ScenarioModelRecord modelRecord;

	public AnalyticsSolution(final ScenarioInstance instance, final UUIDObject solution, final String title) {
		scenarioInstance = instance;
		this.solution = solution;
		id = instance.getUuid() + title;
		this.title = title;

		@NonNull
		ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(instance);
		this.modelReference = modelRecord.aquireReference("AnalyticsSolution:1");
	}

	public AnalyticsSolution(final ScenarioModelRecord modelRecord, final UUIDObject solution, final String title) {
		this.modelRecord = modelRecord;
		this.solution = solution;
		id = UUID.randomUUID().toString() + title;
		this.title = title;

		this.modelReference = modelRecord.aquireReference("AnalyticsSolution:2");
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
		eventBroker.post(OPEN_RESULTS_VIEW, this);
	}

	public void openAndSwitchScreen() {
		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.post(OPEN_RESULTS_VIEW_WITH_SCREEN, this);
	}

	public @Nullable ScenarioModelRecord getModelRecord() {
		return modelRecord;
	}
}
