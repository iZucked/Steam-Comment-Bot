/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.ui.jobmanager.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;

public abstract class AbstractScenarioHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		ISelection selection = window.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection items = (IStructuredSelection) selection;

			for (final Object x : items.toList()) {
				if (x instanceof Scenario) {
					final Scenario scenario = (Scenario) x;
					handleScenario(event, "scenario", scenario);
				} else if (x instanceof Resource) {
					for (final Object y : ((Resource) x).getContents()) {
						if (y instanceof Scenario) {
							handleScenario(event, "scenario", (Scenario) y);
						}
					}
				}
			}

		}

		return null;
	}

	public abstract void handleScenario(final ExecutionEvent event, final String filename, final Scenario scenario);
}
