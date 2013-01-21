/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class PauseOptimisationEditorActionDelegate extends AbstractOptimisationEditorActionDelegate {

	private IEditorPart editor;

	private IAction action;

	/**
	 * The constructor.
	 */
	public PauseOptimisationEditorActionDelegate() {

	}

	public void run(final IAction action) {

		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				final ScenarioInstance instance = scenarioServiceEditorInput.getScenarioInstance();

				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IJobDescriptor job = jobManager.findJobForResource(instance.getUuid());
				final IJobControl control = jobManager.getControlForJob(job);

				if (control != null) {
					if (control.getJobState() == EJobState.RUNNING) {
						control.pause();
					}
				}
			}
		}

	}

	@Override
	protected void stateChanged(final IJobControl job, final EJobState oldState, final EJobState newState) {

		boolean enabled = false;
		switch (newState) {
		case CANCELLED:
			break;
		case CANCELLING:
			break;
		case COMPLETED:
			break;
		case CREATED:
			break;
		case INITIALISED:
			break;
		case INITIALISING:
			break;
		case PAUSED:
			break;
		case PAUSING:
			break;
		case RESUMING:
			break;
		case RUNNING:
			enabled = true;
			break;
		case UNKNOWN:
			break;

		}
		if (action != null) {
			action.setEnabled(enabled);
		}
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		this.action = action;

		if (action != null && targetEditor != null && targetEditor.getEditorInput() instanceof IScenarioServiceEditorInput) {
			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

			final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) targetEditor.getEditorInput();

			final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();
			final Object object = instance.getInstance();
			if (object instanceof MMXRootObject) {
				final String uuid = instance.getUuid();

				final IJobDescriptor job = jobManager.findJobForResource(uuid);
				if (job != null) {
					final IJobControl control = jobManager.getControlForJob(job);

					if (control.getJobState() == EJobState.RUNNING) {
						action.setEnabled(true);
						return;
					}
				}

			}
		}
		if (action != null) {
			action.setEnabled(false);
		}
	}

}
