/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ForkAndStartOptimisationHandler extends StartOptimisationHandler {

	private static final Logger log = LoggerFactory.getLogger(ForkAndStartOptimisationHandler.class);

	public ForkAndStartOptimisationHandler() {
		super(true);
	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof ScenarioInstance) {

					final ScenarioInstance instance = (ScenarioInstance) obj;

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

						final String namePrefix = "[O] " + instance.getName();
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
								OpenScenarioUtils.openScenarioInstance(HandlerUtil.getActiveSite(event).getPage(), fork);
							} catch (final PartInitException e) {
								log.error(e.getMessage(), e);
							}

							return evaluateScenarioInstance(jobManager, fork, optimising, ScenarioLock.OPTIMISER);
						}
					} catch (final IOException e) {
						throw new ExecutionException("Unable to fork scenario", e);
					}

				}

			}
		}

		return null;
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
}
