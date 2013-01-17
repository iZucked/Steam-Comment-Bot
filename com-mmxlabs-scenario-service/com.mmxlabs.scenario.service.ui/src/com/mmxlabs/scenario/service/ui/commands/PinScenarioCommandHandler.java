/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.internal.Activator;

/**
 * Command handler which sets/clears the pin on a scenario.
 * @author hinton
 *
 */
public class PinScenarioCommandHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final ISelection selection = activePage.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (final Iterator<?> iterator = strucSelection.iterator(); iterator.hasNext();) {
				final Object element = iterator.next();
				if (element instanceof ScenarioInstance) {
					final ScenarioInstance model = (ScenarioInstance) element;
					final ScenarioInstance pinned = Activator.getDefault().getScenarioServiceSelectionProvider().getPinnedInstance();
					if (model == pinned) {
						Activator.getDefault().getScenarioServiceSelectionProvider().setPinnedInstance(null);
					} else {
						Activator.getDefault().getScenarioServiceSelectionProvider().setPinnedInstance(model);
					}
				}
			}
		}

		return null;
	}
}
