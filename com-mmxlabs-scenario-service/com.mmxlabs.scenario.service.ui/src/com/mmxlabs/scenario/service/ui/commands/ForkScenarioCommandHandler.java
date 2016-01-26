/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

/**
 * @author Simon Goodall
 * 
 */
public class ForkScenarioCommandHandler extends AbstractHandler {

	private static final Logger log = LoggerFactory.getLogger(ForkScenarioCommandHandler.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		final Exception exceptions[] = new Exception[1];

		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

			@Override
			public void run() {
				final ISelection selection = activePage.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection strucSelection = (IStructuredSelection) selection;
					final Iterator<?> itr = strucSelection.iterator();
					while (itr.hasNext()) {
						final Object element = itr.next();

						if (element instanceof ScenarioInstance) {
							final ScenarioInstance instance = (ScenarioInstance) element;
							try {
								ScenarioServiceModelUtils.createAndOpenFork(instance, false);
							} catch (final IOException e) {
								exceptions[0] = e;
							}
						}
					}
				}
			}
		});

		if (exceptions[0] != null) {
			throw new ExecutionException("Unable to fork scenario: " + exceptions[0], exceptions[0]);
		}

		return null;

	}
}
