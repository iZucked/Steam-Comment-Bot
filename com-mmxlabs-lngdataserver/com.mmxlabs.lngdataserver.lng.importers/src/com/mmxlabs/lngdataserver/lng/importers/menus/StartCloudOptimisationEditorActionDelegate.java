/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.ScenarioServicePushToCloudAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * Action to start the cloud optimisation
 * The following sequence of actions is assumed:
 *  0. Get the optimisation/optioniser/sandbox settings.
 * 	1. Evaluate the scenario.
 * 	2. Anonymise the scenario. Create a map of scenario-to-anonymisation-map.
 * 	3. Create an encrypted zip archive for scenario, parameters, manifest and jvm options
 * 		3a. (Optional) Compute the shasum
 * 	4. Upload the archive
 * 		4a. Receive the confirmation that archive is uploaded.
 * 	5. (Optional; depends on 3a) Upload the shasum.
 * 		5a. (Optional; Future dev) Receive the confirmation that shasum is correct.
 * 	6. (Optional; Future dev) Query the time remaining.
 * 	7. Report to user that opti job is submitted.
 * @author FM
 *
 */
public class StartCloudOptimisationEditorActionDelegate extends ActionDelegate implements IEditorActionDelegate {

	protected IEditorPart targetEditor;
	protected IAction action;
	protected ScenarioInstance currentScenarioInstance;

	@Override
	public void run(IAction action) {
		BusyIndicator.showWhile(Display.getDefault(), () -> {
			final ScenarioInstance si = currentScenarioInstance;
			if (si != null) {
				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(si);
				try (ModelReference mr = modelRecord.aquireReference("StartCloudOptimisationEditorActionDelegate::run")) {
					if (!mr.isLocked()) {
						ScenarioServicePushToCloudAction.uploadScenario(si, true, null);
					}
				}
			}
		});
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
		this.action = action;

		boolean enabled = false;
		if (this.targetEditor != null) {
			final IEditorInput editorInput = targetEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput iScenarioServiceEditorInput) {
				final @NonNull ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();
				this.currentScenarioInstance = instance;

				if (instance == null) {
					action.setEnabled(false);
					return;
				}

				ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
				if (modelRecord == null) {
					action.setEnabled(false);
					return;
				}

				if (instance.isReadonly() || instance.isCloudLocked()) {
					action.setEnabled(false);
					return;
				}

				if (modelRecord.isLoadFailure()) {
					action.setEnabled(false);
					return;
				}

				enabled = true;
			}
		}

		if (action != null) {
			// TODO: Exceptions can be thrown here on application shutdown. It appears the
			// eclipse code does not check for disposed state.
			action.setEnabled(enabled);
		}

	}

}
