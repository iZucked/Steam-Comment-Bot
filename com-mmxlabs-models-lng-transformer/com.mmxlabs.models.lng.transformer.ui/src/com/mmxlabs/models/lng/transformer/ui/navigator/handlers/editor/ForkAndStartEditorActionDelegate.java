/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
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
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ForkAndStartEditorActionDelegate extends StartOptimisationEditorActionDelegate {

	public ForkAndStartEditorActionDelegate() {
		super(true);
	}

	@Override
	protected void doRun(final IEclipseJobManager jobManager, final ScenarioInstance instance, final String parameterMode, final boolean promptForOptimiserSettings, final boolean optimising) {

		if (instance != null) {
			try {
				ScenarioInstance fork = ScenarioServiceModelUtils.createAndOpenFork(instance, true);
				if (fork != null) {
					Set<String> existingNames = new HashSet<>();
					fork.getFragments().forEach(f -> existingNames.add(f.getName()));
					fork.getElements().forEach(f -> existingNames.add(f.getName()));
					OptimisationHelper.evaluateScenarioInstance(jobManager, fork, parameterMode, promptForOptimiserSettings, optimising, !optimising, "Optimisation", existingNames);
				}
			} catch (final Exception e) {
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
			@NonNull
			ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
			if (modelRecord.isLoadFailure()) {
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

			try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ForkAndStartEditorActionDelegate")) {
				final Object object = scenarioDataProvider.getScenario();
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
						boolean relaxedValidation = "Period Scenario".equals(modelRecord.getName());
						// New optimisation, so check there are no validation errors.
						if (!OptimisationHelper.validateScenario(scenarioDataProvider, optimising, false, relaxedValidation)) {
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
