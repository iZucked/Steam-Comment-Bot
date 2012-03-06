/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.optimisation.jobmanager.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public abstract class AbstractScenarioHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		final ISelection selection = window.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection items = (IStructuredSelection) selection;

			for (final Object x : items.toList()) {
				if (x instanceof MMXRootObject) {
					final MMXRootObject scenario = (MMXRootObject) x;
					handleScenario(event, "scenario", scenario);
				} else if (x instanceof Resource) {
					for (final Object y : ((Resource) x).getContents()) {
						if (y instanceof MMXRootObject) {
							handleScenario(event, "scenario", (MMXRootObject) y);
						}
					}
				}
			}

		}

		return null;
	}

	public abstract void handleScenario(final ExecutionEvent event, final String filename, final MMXRootObject scenario);
}
