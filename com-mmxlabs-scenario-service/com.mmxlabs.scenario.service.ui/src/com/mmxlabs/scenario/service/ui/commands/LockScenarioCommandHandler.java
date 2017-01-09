/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Command Handler to toggle {@link ScenarioInstance} locked state.
 * 
 * @author Simon Goodall
 * 
 */
public class LockScenarioCommandHandler extends AbstractHandler {

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
							final ScenarioInstance model = (ScenarioInstance) element;

							model.setLocked(!model.isLocked());
						}
					}
				}
			}
		});

		return null;
	}
}
