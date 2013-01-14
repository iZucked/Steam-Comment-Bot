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
import com.mmxlabs.models.lng.transformer.ui.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class StopOptimisationEditorActionDelegate extends AbstractOptimisationEditorActionDelegate {

	private IEditorPart editor;

	private IAction action;

	/**
	 * The constructor.
	 */
	public StopOptimisationEditorActionDelegate() {
	}

	public void run(IAction action) {

		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				final ScenarioInstance instance = scenarioServiceEditorInput.getScenarioInstance();

				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IJobDescriptor job = jobManager.findJobForResource(instance.getUuid());
				final IJobControl control = jobManager.getControlForJob(job);

				if (control != null) {
					final EJobState jobState = control.getJobState();

					// Can job still be cancelled?
					if (!((jobState == EJobState.CANCELLED) || (jobState == EJobState.CANCELLING) || (jobState == EJobState.COMPLETED))) {
						control.cancel();
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
			enabled = true;
			break;
		case INITIALISED:
			enabled = true;
			break;
		case INITIALISING:
			enabled = true;
			break;
		case PAUSED:
			enabled = true;
			break;
		case PAUSING:
			enabled = true;
			break;
		case RESUMING:
			enabled = true;
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
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
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
					final EJobState jobState = control.getJobState();
					if (!((jobState == EJobState.CANCELLED) || (jobState == EJobState.CANCELLING) || (jobState == EJobState.COMPLETED))) {
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
