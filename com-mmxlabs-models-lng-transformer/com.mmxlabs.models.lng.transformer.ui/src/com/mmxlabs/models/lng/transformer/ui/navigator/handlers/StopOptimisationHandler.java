/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StopOptimisationHandler extends AbstractOptimisationHandler {

	/**
	 * The constructor.
	 */
	public StopOptimisationHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
			BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), new Runnable() {

				@Override
				public void run() {
					final Iterator<?> itr = strucSelection.iterator();
					while (itr.hasNext()) {
						final Object obj = itr.next();
						if (obj instanceof ScenarioInstance) {
							final ScenarioInstance instance = (ScenarioInstance) obj;
							final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
							final IJobDescriptor job = jobManager.findJobForResource(instance.getUuid());
							final IJobControl control = jobManager.getControlForJob(job);

							if (control != null) {
								final EJobState jobState = control.getJobState();

								// Can job still be cancelled?
								if (!((jobState == EJobState.CANCELLED) || (jobState == EJobState.CANCELLING) || (jobState == EJobState.COMPLETED))) {
									control.cancel();
								}
							}
						}
					}
				}
			});
		}

		return null;
	}
}
