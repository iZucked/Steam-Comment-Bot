package com.mmxlabs.rcp.navigator.scenario;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.navigator.resources.workbench.ResourceExtensionContentProvider;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.core.impl.JobManagerListener;

/**
 * {@link IContentProvider} implementation providing
 * {@link ScenarioTreeNodeClass} instances where a {@link IContainer} has a
 * child {@link IResource} named ".scenario".
 * 
 * @author Simon Goodall
 * 
 */
@SuppressWarnings("restriction")
public class ScenarioContentProvider extends ResourceExtensionContentProvider
		implements ITreeContentProvider {

	private final IJobManager jobManager = Activator.getDefault()
			.getJobManager();

	private Viewer viewer;

	/**
	 * {@link IJobManagerListener} implementation to cause the viewer to refresh
	 * on job addition/removal
	 */
	private final IJobManagerListener jobManagerListener = new JobManagerListener() {

		@Override
		public void jobRemoved(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

			job.removeListener(listener);

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});
		}

		@Override
		public void jobAdded(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

			job.addListener(listener);

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});
		}
	};

	/**
	 * {@link IManagedJobListener} implementation to cause the viewer to refresh
	 * on job updates
	 */
	private final IManagedJobListener listener = new IManagedJobListener() {

		@Override
		public boolean jobStateChanged(final IManagedJob job,
				final JobState oldState, final JobState newState) {

			// Find the resource for the job
			final IResource resource = jobManager.findResourceForJob(job);

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});

			return true;
		}

		@Override
		public boolean jobProgressUpdated(final IManagedJob job,
				final int progressDelta) {

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					// Find the resource for the job
					final IResource resource = jobManager
							.findResourceForJob(job);

					// Trigger a refresh of the tree
					((TreeViewer) viewer).refresh(resource, true);
					
					// Ensure object is selected before triggering an update
					if (((IStructuredSelection) viewer.getSelection()).toList()
							.contains(resource)) {


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
		jobManager.addJobManagerListener(jobManagerListener);

		for (final IManagedJob job : jobManager.getJobs()) {
			job.addListener(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.model.WorkbenchContentProvider#inputChanged(org.eclipse
	 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		// Store ref to current viewer
		this.viewer = viewer;
	}

	@Override
	public void dispose() {

		// Clean up listeners
		jobManager.removeJobManagerListener(jobManagerListener);

		for (final IManagedJob job : jobManager.getJobs()) {
			job.removeListener(listener);
		}

		super.dispose();
	}
}
