/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class DeleteScenarioCommandHandler extends AbstractHandler {
	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				final Object element = iterator.next();
				if (element instanceof Container) {
					final Container container = (Container) element;
					int subCount = container.getContainedInstanceCount();
					if (container instanceof ScenarioInstance) subCount--;
					if (subCount > 0) {
						final MessageDialog dialog = new MessageDialog(HandlerUtil.getActiveShell(event), 
								"Delete " + container.getName() + " and contents?", 
								null, "Do you really want to delete " + container.getName() + " and its contents (" + subCount + " scenarios)", MessageDialog.CONFIRM, 
								new String[] {"Don't Delete", "Delete"}, 0);
						if (dialog.open() != 1) {
							return null;
						}
					}
					final IScenarioService service = container.getScenarioService();
					service.delete(container);
				}
			}
		}

		return null;
	}
}
