/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers.editor;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

/**
 * @author Simon Goodall
 * 
 */
public class ForkScenarioEditorActionDelegate implements IEditorActionDelegate, IActionDelegate2 {

	private IEditorPart editor;

	private IAction action;

	@Override
	public void run(final IAction action) {

		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				final ScenarioInstance instance = (ScenarioInstance) scenarioServiceEditorInput.getScenarioInstance();

				final IScenarioService scenarioService = instance.getScenarioService();

				try {
					final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

					final Set<String> existingNames = new HashSet<String>();
					for (final Container c : instance.getElements()) {
						if (c instanceof Folder) {
							existingNames.add(((Folder) c).getName());
						} else if (c instanceof ScenarioInstance) {
							existingNames.add(((ScenarioInstance) c).getName());
						}
					}

					final String namePrefix = "Fork " + df.format(new Date()) + " - " + instance.getName();
					String newName = namePrefix;
					int counter = 1;
					while (existingNames.contains(newName)) {
						newName = namePrefix + " (" + counter++ + ")";
					}

					final String finalNewName = getNewName(instance.getName(), newName);
					if (finalNewName != null) {
						final ScenarioInstance fork = scenarioService.duplicate(instance, instance);

						fork.setName(finalNewName);
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

		if (dialog.open() == InputDialog.OK) {
			return dialog.getValue();
		}
		return null;
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		this.action = action;
		if (action != null) {
			action.setEnabled(true);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IAction action) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void runWithEvent(IAction action, Event event) {
		run(action);
	}
}
