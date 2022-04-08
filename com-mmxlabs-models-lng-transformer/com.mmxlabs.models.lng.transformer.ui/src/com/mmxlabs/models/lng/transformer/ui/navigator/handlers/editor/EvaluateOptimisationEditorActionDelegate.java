/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.mmxlabs.models.lng.transformer.ui.jobmanagers.LocalJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.evaluate.EvaluateTask;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IProgressProvider.IProgressChanged;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class EvaluateOptimisationEditorActionDelegate extends ActionDelegate implements IEditorActionDelegate {

	protected IEditorPart editor;
	protected IAction action;

	public EvaluateOptimisationEditorActionDelegate() {
		LocalJobManager.INSTANCE.addChangedListener(listener);

	}

	@Override
	public void dispose() {

		LocalJobManager.INSTANCE.removeChangedListener(listener);

		action = null;
		editor = null;

	}

	private IProgressChanged listener = new IProgressChanged() {
		@Override
		public void changed(Object element) {
			setActiveEditor(action, editor);
		}

		@Override
		public void listChanged() {
			setActiveEditor(action, editor);
		}
	};

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		this.action = action;
		if (action != null) {
			action.setEnabled(false);

			if (targetEditor != null && targetEditor.getEditorInput() instanceof IScenarioServiceEditorInput iScenarioServiceEditorInput) {
				final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();
				if (instance == null) {
					return;
				}

				ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
				if (modelRecord == null) {
					return;
				}

				if (instance.isReadonly()) {
					return;
				}

				if (modelRecord.isLoadFailure()) {
					return;
				}
				action.setEnabled(true);
			}
		}
	}
	//
	// @Override
	// protected void stateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {
	//
	// boolean enabled = false;
	// switch (newState) {
	// case CANCELLED:
	// enabled = true;
	// break;
	// case CANCELLING:
	// break;
	// case COMPLETED:
	// enabled = true;
	// break;
	// case CREATED:
	// enabled = true;
	// break;
	// case INITIALISED:
	// enabled = true;
	// break;
	// case INITIALISING:
	// break;
	// case RUNNING:
	// break;
	// case UNKNOWN:
	// break;
	//
	// }
	// if (action != null) {
	// action.setEnabled(enabled);
	// }
	// }

	@Override
	public void run(final IAction action) {
		if (editor != null && editor.getEditorInput() instanceof IScenarioServiceEditorInput iScenarioServiceEditorInput) {

			final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();
			if (instance == null) {
				action.setEnabled(false);
				return;
			}

			ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
			if (modelRecord == null) {
				action.setEnabled(false);
				return;
			}

			if (instance.isReadonly()) {
				action.setEnabled(false);
				return;
			}

			if (modelRecord.isLoadFailure()) {
				action.setEnabled(false);
				return;
			}

			EvaluateTask.submit(instance);
		}
	}
	//
	// protected void doRun(final IEclipseJobManager jobManager, final ScenarioInstance instance, final boolean promptForOptimiserSettings, final boolean optimising) {
	// Set<String> existingNames = new HashSet<>();
	// instance.getFragments().forEach(f -> existingNames.add(f.getName()));
	// instance.getElements().forEach(f -> existingNames.add(f.getName()));
	//
	// OptimisationHelper.evaluateScenarioInstance(jobManager, instance, promptForOptimiserSettings, optimising, !optimising, "Optimisation", existingNames);
	// }
}
