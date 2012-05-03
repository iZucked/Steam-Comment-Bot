/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class LockScenarioCommandHandler extends AbstractHandler {

	private IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				Object element = iterator.next();
				if (element instanceof ScenarioInstance) {
					ScenarioInstance model = (ScenarioInstance) element;

					model.setLocked(!model.isLocked());
				}
			}
		}

		return null;
	}
}
