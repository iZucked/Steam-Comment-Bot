/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Command Handler to present an {@link InputDialog} to the user to rename a {@link Folder} or {@link ScenarioInstance}.
 * 
 * @author Simon Goodall
 * 
 */
public class RenameElementHandler extends AbstractHandler {

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
						final Object element = iterator.next();

						if (element instanceof ScenarioInstance) {
							final ScenarioInstance instance = (ScenarioInstance) element;

							final String newName = getNewName(instance.getName());
							if (newName != null) {
								instance.setName(newName);
							}
						} else if (element instanceof Folder) {
							final Folder folder = (Folder) element;
							final String newName = getNewName(folder.getName());
							if (newName != null) {
								folder.setName(newName);
							}
						} else if (element instanceof ScenarioFragment) {
							ScenarioFragment scenarioFragment = (ScenarioFragment) element;
							final String newName = getNewName(scenarioFragment.getName());
							if (newName != null) {
								scenarioFragment.setName(newName);
							}
						}
					}
				}
			}
		});

		return null;
	}

	private String getNewName(final String oldName) {
		// TODO: Hook in an element specific validator
		final IInputValidator validator = null;
		final InputDialog dialog = new InputDialog(Display.getDefault().getActiveShell(), "Rename element", "Choose new element name", oldName, validator);

		if (dialog.open() == Window.OK) {
			return dialog.getValue();
		}
		return null;
	}
}
