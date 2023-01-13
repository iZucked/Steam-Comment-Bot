/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ValueMatrixScenario {
	private ScenarioInstance scenarioInstance;
	private SwapValueMatrixModel model;
	private final ModelReference modelReference;

	public ValueMatrixScenario(final ScenarioInstance instance, final SwapValueMatrixModel model) {
		scenarioInstance = instance;
		this.model = model;

		@NonNull
		ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(instance);
		this.modelReference = modelRecord.aquireReference("ValueMatrixScenario:1");
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

	public SwapValueMatrixModel getValueMatrixModel() {
		return model;
	}

	public void setValueMatrixModel(final SwapValueMatrixModel model) {
		this.model = model;
	}

	public void open() {
		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.post("create-value-matrix-view", this);
	}

//	public void openAndShowResult() {
//		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
//		eventBroker.post("create-sandbox-view-with-result", this);
//	}

	public static void open(final ScenarioInstance instance, final SwapValueMatrixModel model) {
		new ValueMatrixScenario(instance, model).open();
	}

//	public static void openAndShowResult(final ScenarioInstance instance, final OptionAnalysisModel model) {
//		new ValueMatrixScenario(instance, model).openAndShowResult();
//	}

	public @Nullable MMXRootObject getRootObject() {
		return (MMXRootObject) modelReference.getInstance();
	}
}
