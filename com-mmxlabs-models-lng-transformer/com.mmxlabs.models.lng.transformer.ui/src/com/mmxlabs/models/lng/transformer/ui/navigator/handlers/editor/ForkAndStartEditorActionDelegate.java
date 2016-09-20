/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.io.IOException;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ForkAndStartEditorActionDelegate extends StartOptimisationEditorActionDelegate {

	public ForkAndStartEditorActionDelegate() {
		super(true);
	}

	@Override
	protected void doRun(final IEclipseJobManager jobManager, final ScenarioInstance instance, final String parameterMode, final boolean promptForOptimiserSettings, final boolean optimising,
			final String k) {

		if (instance != null) {
			try {
				ScenarioInstance fork = ScenarioServiceModelUtils.createAndOpenFork(instance, true);
				if (fork != null) {
					OptimisationHelper.evaluateScenarioInstance(jobManager, fork, parameterMode, promptForOptimiserSettings, optimising, k, !optimising);
				}
			} catch (final IOException e) {
				throw new RuntimeException("Unable to fork scenario", e);
			}
		}
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {

		this.editor = targetEditor;
		this.action = action;

		final boolean enabled = false;
		if (action != null && targetEditor != null && targetEditor.getEditorInput() instanceof IScenarioServiceEditorInput) {
			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

			final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) targetEditor.getEditorInput();

			final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();
			if (instance.isLoadFailure()) {
				action.setEnabled(false);
				return;
			}
			if (instance.isReadonly()) {
				action.setEnabled(false);
				return;
			}
			{
				Container c = instance;
				while (c != null && !(c instanceof ScenarioService)) {
					c = c.getParent();
				}
				if (c instanceof ScenarioService) {
					final ScenarioService scenarioService = (ScenarioService) c;
					if (!scenarioService.isSupportsForking()) {
						action.setEnabled(false);
						return;
					}
				} else {
					action.setEnabled(false);
					return;
				}
			}

			try (final ModelReference modelReference = instance.getReference("ForkAndStartEditorActionDelegate")) {
				final Object object = modelReference.getInstance();
				if (object instanceof MMXRootObject) {
					final MMXRootObject root = (MMXRootObject) object;
					final String uuid = instance.getUuid();

					final IJobDescriptor job = jobManager.findJobForResource(uuid);
					if (job == null) {
						action.setEnabled(true);
						return;
					}

					final IJobControl control = jobManager.getControlForJob(job);
					if (control != null) {
						stateChanged(control, EJobState.UNKNOWN, control.getJobState());
						return;
					} else {

						// New optimisation, so check there are no validation errors.
						if (!OptimisationHelper.validateScenario(root, optimising)) {
							action.setEnabled(false);
							return;
						}

					}
				}
			}
		}
		if (action != null) {
			action.setEnabled(enabled);
		}
	}

	@Override
	protected void stateChanged(final IJobControl job, final EJobState oldState, final EJobState newState) {

		boolean enabled = false;
		switch (newState) {
		case CANCELLED:
			enabled = true;
			break;
		case CANCELLING:
			break;
		case COMPLETED:
			enabled = true;
			break;
		case CREATED:
			enabled = true;
			break;
		case INITIALISED:
			enabled = true;
			break;
		case INITIALISING:
			break;
		case PAUSED:
			enabled = true;
			break;
		case PAUSING:
			break;
		case RESUMING:
			break;
		case RUNNING:
			break;
		case UNKNOWN:
			break;

		}
		if (action != null) {
			action.setEnabled(enabled);
		}
	}
}
