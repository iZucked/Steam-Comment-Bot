/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.eclipse.manager.impl.EclipseJobManagerAdapter;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.jobs.impl.JobControlAdapter;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;
import com.mmxlabs.models.lng.transformer.ui.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * Base {@link IEditorActionDelegate} implementation to cause enabled state to be refreshed on job progress updates.
 * 
 * @see org.eclipse.ui.IEditorActionDelegate
 * @see org.eclipse.ui.IActionDelegate2
 */
public abstract class AbstractOptimisationEditorActionDelegate implements IEditorActionDelegate, IActionDelegate2 {

	/**
	 * {@link IJobControlListener} to trigger event handler enabled state refresh
	 */
	private final IJobControlListener jobListener = new JobControlAdapter() {

		@Override
		public boolean jobStateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {

			if (action != null && editor != null && editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

				final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();

				final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();
				final Object object = instance.getInstance();
				if (object instanceof MMXRootObject) {
					final String uuid = instance.getUuid();

					final IJobDescriptor job = jobManager.findJobForResource(uuid);
					if (job == null) {
						action.setEnabled(true);
						return true;
					}

					final IJobControl actionControl = jobManager.getControlForJob(job);

					if (actionControl == control) {

						// Fire the event
						stateChanged(control, oldState, newState);
					}
				}
			}
			return true;
		}
	};

	protected abstract void stateChanged(final IJobControl job, final EJobState oldState, final EJobState newState);

	/**
	 * {@link IJobManagerListener} to hook up our {@link #jobListener} to any jobs added to the manager, and remove the listener as the job is removed.
	 */
	final IEclipseJobManagerListener jobManagerListener = new EclipseJobManagerAdapter() {

		@Override
		public void jobAdded(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {
			control.addListener(jobListener);
		}

		@Override
		public void jobRemoved(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {

			control.removeListener(jobListener);
		}
	};
	protected IEditorPart editor;
	protected IAction action;

	/**
	 * The constructor.
	 */
	public AbstractOptimisationEditorActionDelegate() {
		final IEclipseJobManager jmv = Activator.getDefault().getJobManager();
		// Hook in a listener to automatically dispose the job once it is no
		// longer needed
		jmv.addEclipseJobManagerListener(jobManagerListener);

		// Hook listener in to current jobs
		for (final IJobDescriptor j : jmv.getJobs()) {
			final IJobControl control = jmv.getControlForJob(j);
			control.addListener(jobListener);
		}
	}

	@Override
	public void dispose() {
		final IEclipseJobManager jmv = Activator.getDefault().getJobManager();

		jmv.removeEclipseJobManagerListener(jobManagerListener);

		for (final IJobDescriptor j : jmv.getJobs()) {
			final IJobControl control = jmv.getControlForJob(j);
			control.removeListener(jobListener);
		}
	}

	@Override
	public void selectionChanged(final IAction action, final ISelection selection) {

	}

	@Override
	public void init(final IAction action) {

	}

	@Override
	public void runWithEvent(final IAction action, final Event event) {
		run(action);
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		this.action = action;
	}

}
