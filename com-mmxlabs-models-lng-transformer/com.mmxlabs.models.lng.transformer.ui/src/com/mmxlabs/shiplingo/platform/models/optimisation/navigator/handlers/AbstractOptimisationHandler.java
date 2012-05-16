/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.eclipse.manager.impl.EclipseJobManagerAdapter;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.jobs.impl.JobControlAdapter;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

/**
 * Base {@link IHandler} implementation to cause enabled state to be refreshed on job progress updates.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public abstract class AbstractOptimisationHandler extends AbstractHandler {

	/**
	 * {@link IJobControlListener} to trigger event handler enabled state refresh
	 */
	private final IJobControlListener jobListener = new JobControlAdapter() {

		@Override
		public boolean jobStateChanged(final IJobControl job, final EJobState oldState, final EJobState newState) {

			// Create Event to trigger enabled state update
			final HandlerEvent event = new HandlerEvent(AbstractOptimisationHandler.this, true, false);

			// Fire the event
			AbstractOptimisationHandler.this.fireHandlerChanged(event);

			return true;
		}
	};

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

	private final ISelectionListener selectionListener = new ISelectionListener() {

		@Override
		public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
			// Create Event to trigger enabled state update
			final HandlerEvent handlerEvent = new HandlerEvent(AbstractOptimisationHandler.this, true, false);

			// Fire the event
			AbstractOptimisationHandler.this.fireHandlerChanged(handlerEvent);
		}
	};

	/**
	 * The constructor.
	 */
	public AbstractOptimisationHandler() {
		final IEclipseJobManager jmv = Activator.getDefault().getJobManager();
		// Hook in a listener to automatically dispose the job once it is no
		// longer needed
		jmv.addEclipseJobManagerListener(jobManagerListener);

		// Hook listener in to current jobs
		for (final IJobDescriptor j : jmv.getJobs()) {
			final IJobControl control = jmv.getControlForJob(j);
			control.addListener(jobListener);
		}

		// Track user selection changes and cause enabled state to change
		try {
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(selectionListener);
				}
			});
		} catch (final Throwable thr) {}
	}

	@Override
	public void dispose() {

		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (activeWorkbenchWindow != null) {
					activeWorkbenchWindow.getSelectionService().removeSelectionListener(selectionListener);
				}
			}
		});

		final IEclipseJobManager jmv = Activator.getDefault().getJobManager();

		jmv.removeEclipseJobManagerListener(jobManagerListener);

		for (final IJobDescriptor j : jmv.getJobs()) {
			final IJobControl control = jmv.getControlForJob(j);
			control.removeListener(jobListener);
		}
		super.dispose();
	}

}
