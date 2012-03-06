/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.optimisation.navigator.scenario;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.navigator.resources.workbench.ResourceExtensionContentProvider;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.eclipse.manager.impl.EclipseJobManagerAdapter;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;
import com.mmxlabs.shiplingo.platform.models.manifest.optimisation.Activator;

/**
 * {@link IContentProvider} implementation providing {@link ScenarioTreeNodeClass} instances where a {@link IContainer} has a child {@link IResource} named ".scenario".
 * 
 * @author Simon Goodall
 * 
 */
@SuppressWarnings("restriction")
public class ScenarioContentProvider extends ResourceExtensionContentProvider implements ITreeContentProvider {

	private final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

	private Viewer viewer;

	/**
	 * {@link IJobManagerListener} implementation to cause the viewer to refresh on job addition/removal
	 */
	private final IEclipseJobManagerListener jobManagerListener = new EclipseJobManagerAdapter() {

		@Override
		public void jobRemoved(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {

			control.removeListener(listener);

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});
		}

		@Override
		public void jobAdded(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {

			control.addListener(listener);

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});
		}
	};

	/**
	 * {@link IJobControlListener} implementation to cause the viewer to refresh on job updates
	 */
	private final IJobControlListener listener = new IJobControlListener() {

		@Override
		public boolean jobStateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {

			// Find the resource for the job
			final Object resource = jobManager.findResourceForJob(control.getJobDescriptor());

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});

			return true;
		}

		@Override
		public boolean jobProgressUpdated(final IJobControl control, final int progressDelta) {

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					// Find the resource for the job
					final Object resource = jobManager.findResourceForJob(control.getJobDescriptor());

					// Trigger a refresh of the tree
					((TreeViewer) viewer).refresh(resource, true);

					// Ensure object is selected before triggering an update
					if (((IStructuredSelection) viewer.getSelection()).toList().contains(resource)) {

						// Trigger a re-selection event to force updates out to
						// reports
						viewer.setSelection(viewer.getSelection());
					}
				}
			});
			return true;
		}
	};

	public ScenarioContentProvider() {
		jobManager.addEclipseJobManagerListener(jobManagerListener);

		for (final IJobDescriptor job : jobManager.getJobs()) {
			final IJobControl control = jobManager.getControlForJob(job);
			control.addListener(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.WorkbenchContentProvider#inputChanged(org.eclipse .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		// Store ref to current viewer
		this.viewer = viewer;
	}

	@Override
	public void dispose() {

		// Clean up listeners
		jobManager.removeEclipseJobManagerListener(jobManagerListener);

		for (final IJobDescriptor job : jobManager.getJobs()) {
			final IJobControl control = jobManager.getControlForJob(job);
			control.removeListener(listener);
		}

		super.dispose();
	}
}
