/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;

import com.mmxlabs.jobcontroller.core.impl.LNGSchedulerJob;
import com.mmxlabs.jobcontroller.views.JobManagerView;

/**
 * Run an optimisation for the scenarios selected in the navigator
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ScenarioOptimisationHandler extends AbstractScenarioHandler {
	/**
	 * The constructor.
	 */
	public ScenarioOptimisationHandler() {
	}

	@Override
	public void handleScenario(final ExecutionEvent event,
			final String filename, final Scenario scenario) {
		
		throw new UnsupportedOperationException("No longer supported");
//		
//		IWorkbenchWindow window;
//		try {
//			window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//			final JobManagerView jmv = (JobManagerView) window.getWorkbench()
//					.getActiveWorkbenchWindow().getActivePage()
//					.showView(JobManagerView.ID);
//			LNGSchedulerJob job = new LNGSchedulerJob(scenario);
////			ScenarioOptimisationJob job = new ScenarioOptimisationJob(filename,
////					scenario);
//			jmv.addJob(job ,null);
////			job.start();
//		} catch (ExecutionException e) {
//		} catch (PartInitException e) {
//		}
	}
}
