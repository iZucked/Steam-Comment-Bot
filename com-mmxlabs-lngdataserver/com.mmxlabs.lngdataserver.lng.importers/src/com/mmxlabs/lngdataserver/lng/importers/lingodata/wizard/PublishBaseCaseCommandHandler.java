/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.lngdataserver.lng.importers.menus.ScenarioServicePublishAction;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class PublishBaseCaseCommandHandler extends AbstractHandler {

	@Override
	public void setEnabled(final Object evaluationContext) {
		setBaseEnabled(UpstreamUrlProvider.INSTANCE.isAvailable());
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final Exception exceptions[] = new Exception[1];

		final Display display = HandlerUtil.getActiveShellChecked(event).getDisplay();
		BusyIndicator.showWhile(display, () -> {
			final ISelection selection = activePage.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection strucSelection = (IStructuredSelection) selection;
				final Iterator<?> itr = strucSelection.iterator();
				while (itr.hasNext()) {
					final Object element = itr.next();

					if (element instanceof ScenarioInstance) {
						final ScenarioInstance instance = (ScenarioInstance) element;
						try {
							if (MessageDialog.openQuestion(display.getActiveShell(), "Confirm base case publish", String.format("Publish scenario %s as base case?", instance.getName()))) {
								ScenarioServicePublishAction.publishScenario(instance);
							}
						} catch (final Exception e) {
							exceptions[0] = e;
						}
					}
				}
			}
		});

		if (exceptions[0] != null)

		{
			throw new ExecutionException("Unable to publish basecase: " + exceptions[0], exceptions[0]);
		}

		return null;
	}

}
