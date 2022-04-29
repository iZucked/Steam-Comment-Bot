/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.mmxlabs.models.lng.transformer.ui.jobmanagers.IJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.LocalJobManager;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IProgressProvider;
import com.mmxlabs.scenario.service.ui.IProgressProvider.IProgressChanged;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class StopOptimisationEditorActionDelegate extends ActionDelegate implements IEditorActionDelegate {

	protected IEditorPart editor;
	protected IAction action;

	@Override
	public void dispose() {

		ServiceHelper.withAllServices(IJobManager.class, null, mgr -> {
			mgr.removeChangedListener(listener);
			return true;
		});

		action = null;
		editor = null;

	}

	private final IProgressChanged listener = new IProgressChanged() {
		@Override
		public void changed(final Object element) {
			setActiveEditor(action, editor);
		}

		@Override
		public void listChanged() {
			setActiveEditor(action, editor);
		}
	};

	public StopOptimisationEditorActionDelegate() {

		ServiceHelper.withAllServices(IJobManager.class, null, mgr -> {
			mgr.addChangedListener(listener);
			return true;
		});
	}

	@Override
	public void run(final IAction action) {

		if (editor.getEditorInput() instanceof final IScenarioServiceEditorInput editorInput) {
			final ScenarioInstance instance = editorInput.getScenarioInstance();

			if (instance.isReadonly()) {
				action.setEnabled(false);
				return;
			}

			ServiceHelper.withAllServices(IJobManager.class, null, mgr -> {
				mgr.cancelAll(instance.getUuid());
				return true;
			});
		}
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		this.action = action;

		if (action != null && targetEditor != null && targetEditor.getEditorInput() instanceof final IScenarioServiceEditorInput editorInput) {

			action.setEnabled(false);

			final ScenarioInstance instance = editorInput.getScenarioInstance();

			if (instance != null) {
				ServiceHelper.withAllServices(IJobManager.class, null, mgr -> {
					final boolean runningJob = mgr.hasActiveJob(instance.getUuid());
					if (runningJob) {
						action.setEnabled(true);
						return false;
					}
					return true;
				});
			}
		}
	}
}
