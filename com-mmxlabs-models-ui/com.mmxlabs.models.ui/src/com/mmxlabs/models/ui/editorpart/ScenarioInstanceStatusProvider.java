/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioValidationListener;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioInstanceStatusProvider extends DefaultStatusProvider {
	private ScenarioModelRecord modelRecord;

	private final @NonNull IScenarioValidationListener validationListener = new IScenarioValidationListener() {
		@Override
		public void validationChanged(@NonNull ScenarioModelRecord modelRecord, @NonNull IStatus status) {
			fireStatusChanged(status);
		}
	};

	public ScenarioInstanceStatusProvider(final @NonNull ScenarioInstance scenarioInstance) {
		this.modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		this.modelRecord.addValidationListener(validationListener);
	}

	public void dispose() {
		if (modelRecord != null) {
			modelRecord.removeValidationListener(validationListener);
			modelRecord = null;
		}

	}

	@Override
	public IStatus getStatus() {
		// Oops, must be called after dispose....
		if (modelRecord == null) {
			return Status.OK_STATUS;
		}
		return modelRecord.getValidationStatus();
	}

}
