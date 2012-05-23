/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;
import com.mmxlabs.shiplingo.platform.models.optimisation.propertytesters.JobStatePropertyTester;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class PauseOptimisationHandler extends AbstractOptimisationHandler {

	/**
	 * The constructor.
	 */
	public PauseOptimisationHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof ScenarioInstance) {
					final ScenarioInstance instance = (ScenarioInstance) obj;

					final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
					final IJobDescriptor job = jobManager.findJobForResource(instance.getUuid());
					final IJobControl control = jobManager.getControlForJob(job);

					if (control != null) {
						if (control.getJobState() == EJobState.RUNNING) {
							control.pause();
						}
					}
				}
			}
		}

		// Update button state?

		return null;
	}
	
	@Override
	public void setEnabled(final Object evaluationContext) {
		final JobStatePropertyTester tester = new JobStatePropertyTester();
		boolean enabled = false;
		if (evaluationContext instanceof IEvaluationContext) {
			final IEvaluationContext context = (IEvaluationContext) evaluationContext;
			final Object defaultVariable = context.getDefaultVariable();

			if (defaultVariable instanceof List<?>) {
				final List<?> variables = (List<?>) defaultVariable;

				for (final Object var : variables) {
					if (var instanceof ScenarioInstance) {
						final ScenarioInstance scenarioInstance = (ScenarioInstance) var;
						if (tester.test(scenarioInstance, "canPause", null, null)) {
							enabled = true;
						} else {
							super.setBaseEnabled(false);
							return;
						}
					} else {
						super.setBaseEnabled(false);
						return;
					}
				}
			}
		}

		super.setBaseEnabled(enabled);
	}
}
