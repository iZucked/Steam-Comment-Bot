/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ForkAndStartEditorActionDelegate extends StartOptimisationEditorActionDelegate {

	private static final Logger log = LoggerFactory.getLogger(ForkAndStartEditorActionDelegate.class);

	public ForkAndStartEditorActionDelegate() {
		super(true);
	}

	@Override
	public void run(final IAction action) {

		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				final ScenarioInstance instance = scenarioServiceEditorInput.getScenarioInstance();

				final IScenarioService scenarioService = instance.getScenarioService();

				try {
					final Set<String> existingNames = new HashSet<String>();
					for (final Container c : instance.getElements()) {
						if (c instanceof Folder) {
							existingNames.add(((Folder) c).getName());
						} else if (c instanceof ScenarioInstance) {
							existingNames.add(((ScenarioInstance) c).getName());
						}
					}

					final String namePrefix = "O~" + instance.getName();
					String newName = namePrefix;
					int counter = 1;
					while (existingNames.contains(newName)) {
						newName = namePrefix + " (" + counter++ + ")";
					}

					final String finalNewName = getNewName(instance.getName(), newName);
					if (finalNewName != null) {
						final ScenarioInstance fork = scenarioService.duplicate(instance, instance);

						fork.setName(finalNewName);

						try {
							OpenScenarioUtils.openScenarioInstance(editor.getSite().getPage(), fork);
						} catch (final PartInitException e) {
							log.error(e.getMessage(), e);
						}

						OptimisationHelper.evaluateScenarioInstance(jobManager, fork, null, false, optimising, ScenarioLock.OPTIMISER);
					}
				} catch (final IOException e) {
					throw new RuntimeException("Unable to fork scenario", e);
				}
			}
		}
	}

	private String getNewName(final String oldName, final String suggestedName) {
		// TODO: Hook in an element specific validator
		final IInputValidator validator = null;
		final InputDialog dialog = new InputDialog(Display.getDefault().getActiveShell(), "Fork " + oldName, "Choose new name for fork", suggestedName, validator);

		if (dialog.open() == Window.OK) {
			return dialog.getValue();
		}
		return null;
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
					ScenarioService scenarioService = (ScenarioService) c;
					if (!scenarioService.isSupportsForking()) {
						action.setEnabled(false);
						return;
					}
				} else {
					action.setEnabled(false);
					return;
				}
			}

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
					if (!OptimisationHelper.validateScenario(root, optimising)) {
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
