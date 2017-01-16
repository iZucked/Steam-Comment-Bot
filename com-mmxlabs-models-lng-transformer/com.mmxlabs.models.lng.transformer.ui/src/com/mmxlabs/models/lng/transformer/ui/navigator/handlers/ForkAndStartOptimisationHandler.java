/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ForkAndStartOptimisationHandler extends StartOptimisationHandler {

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

			final Exception[] exceptions = new Exception[1];

			BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

				@Override
				public void run() {
					final Iterator<?> itr = strucSelection.iterator();
					while (itr.hasNext()) {
						final Object obj = itr.next();
						if (obj instanceof ScenarioInstance) {

							final ScenarioInstance instance = (ScenarioInstance) obj;
							try {
								ScenarioInstance fork = ScenarioServiceModelUtils.createAndOpenFork(instance, true);
								if (fork != null) {
									OptimisationHelper.evaluateScenarioInstance(jobManager, fork, null, /* prompt if optimising */optimising, optimising, ScenarioLock.OPTIMISER, !optimising);
								}
							} catch (final IOException e) {
								exceptions[0] = e;
							}
						}
					}
				}
			});
			if (exceptions[0] != null) {
				throw new ExecutionException("Unable to fork scenario: " + exceptions[0].getMessage(), exceptions[0]);
			}
		}
		return null;
	}
}
