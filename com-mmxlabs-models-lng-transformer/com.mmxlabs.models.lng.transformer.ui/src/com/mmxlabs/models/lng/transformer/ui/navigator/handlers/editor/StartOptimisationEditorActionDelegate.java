/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * A {@link IEditorActionDelegate} implementation to start or resume an optimisation.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StartOptimisationEditorActionDelegate extends AbstractOptimisationEditorActionDelegate {

	private static final Logger log = LoggerFactory.getLogger(StartOptimisationEditorActionDelegate.class);

	protected final boolean optimising;

	/**
	 * The constructor.
	 */
	public StartOptimisationEditorActionDelegate(final boolean optimising) {
		this.optimising = optimising;
	}

	public StartOptimisationEditorActionDelegate() {
		this(true);
	}

	@Override
	public void run(final IAction methodAction) {

		runLastMode();
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {

		super.setActiveEditor(action, targetEditor);

		final boolean enabled = false;
		if (action != null && targetEditor != null && targetEditor.getEditorInput() instanceof IScenarioServiceEditorInput) {
			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

			final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) targetEditor.getEditorInput();

			final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();
			final Object object = instance.getInstance();
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
					if (!OptimisationHelper.validateScenario(root)) {
						action.setEnabled(false);
						return;
					}

				}

			}
		}
		if (action != null) {
			action.setEnabled(enabled);
		}
	}

	@Override
	protected void stateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {

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

	@Override
	protected void runLastMode() {
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				OptimisationHelper.evaluateScenarioInstance(jobManager, scenarioServiceEditorInput.getScenarioInstance(), null, true, optimising, ScenarioLock.OPTIMISER);
			}
		}
	}

	@Override
	protected void runWithMode(final String mode) {
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				OptimisationHelper.evaluateScenarioInstance(jobManager, scenarioServiceEditorInput.getScenarioInstance(), mode, false, optimising, ScenarioLock.OPTIMISER);
			}
		}
	}

	@Override
	protected void runCustomMode() {
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				OptimisationHelper.evaluateScenarioInstance(jobManager, scenarioServiceEditorInput.getScenarioInstance(), OptimisationHelper.PARAMETER_MODE_CUSTOM, false, optimising,
						ScenarioLock.OPTIMISER);
			}
		}
	}

	@Override
	protected void editAndRunCustomMode() {
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				OptimisationHelper.evaluateScenarioInstance(jobManager, scenarioServiceEditorInput.getScenarioInstance(), OptimisationHelper.PARAMETER_MODE_CUSTOM, true, optimising,
						ScenarioLock.OPTIMISER);
			}
		}

	}
}
