/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ModelReference;
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

		editAndRunCustomMode();
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		assert action != null;
		super.setActiveEditor(action, targetEditor);

		final boolean enabled = false;
		if (targetEditor != null && targetEditor.getEditorInput() instanceof IScenarioServiceEditorInput) {
			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

			final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) targetEditor.getEditorInput();

			final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();

			if (instance.isReadonly()) {
				action.setEnabled(false);
				return;
			}

			if (instance.isLoadFailure()) {
				action.setEnabled(false);
				return;
			}

			try (final ModelReference modelReference = instance.getReference()) {
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
			// TODO: Exceptions can be thrown here on application shutdown. It appears the eclipse code does not check for disposed state.
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
				doRun(jobManager, scenarioServiceEditorInput.getScenarioInstance(), null, false, optimising, ScenarioLock.OPTIMISER);
			}
		}
	}

	@Override
	protected void runWithMode(final String mode) {
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				doRun(jobManager, scenarioServiceEditorInput.getScenarioInstance(), mode, false, optimising, ScenarioLock.OPTIMISER);
			}
		}
	}

	@Override
	protected void runCustomMode() {
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				doRun(jobManager, scenarioServiceEditorInput.getScenarioInstance(), OptimisationHelper.PARAMETER_MODE_CUSTOM, false, optimising, ScenarioLock.OPTIMISER);
			}
		}
	}

	@Override
	protected void editAndRunCustomMode() {
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				doRun(jobManager, scenarioServiceEditorInput.getScenarioInstance(), OptimisationHelper.PARAMETER_MODE_CUSTOM, true, optimising, ScenarioLock.OPTIMISER);
			}
		}
	}

	protected void doRun(final IEclipseJobManager jobManager, final ScenarioInstance instance, final String parameterMode, final boolean promptForOptimiserSettings, final boolean optimising,
			final String k) {
		OptimisationHelper.evaluateScenarioInstance(jobManager, instance, parameterMode, promptForOptimiserSettings, optimising, k, !optimising);
	}
}
