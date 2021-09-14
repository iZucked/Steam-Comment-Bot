/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class SandboxScenario {
	private ScenarioInstance scenarioInstance;
	private OptionAnalysisModel model;
	private final ModelReference modelReference;

	public SandboxScenario(final ScenarioInstance instance, final OptionAnalysisModel model) {
		scenarioInstance = instance;
		this.model = model;

		@NonNull
		ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(instance);
		this.modelReference = modelRecord.aquireReference("SandboxScenario:1");
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.modelReference != null) {
			this.modelReference.close();
		}
		super.finalize();
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public void setScenarioInstance(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	public OptionAnalysisModel getSandboxModel() {
		return model;
	}

	public void setSandboxModel(final OptionAnalysisModel model) {
		this.model = model;
	}

	public void open() {
		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.post("create-sandbox-view", this);
	}

	public static void open(final ScenarioInstance instance, final OptionAnalysisModel model) {
		new SandboxScenario(instance, model).open();
	}

	public @Nullable MMXRootObject getRootObject() {
		return (MMXRootObject) modelReference.getInstance();
	}
}
