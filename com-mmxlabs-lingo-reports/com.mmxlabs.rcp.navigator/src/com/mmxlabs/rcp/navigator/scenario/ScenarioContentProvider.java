package com.mmxlabs.rcp.navigator.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.navigator.resources.workbench.ResourceExtensionContentProvider;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.rcp.navigator.ecore.EcoreContentProvider;

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

	EcoreContentProvider scp = new EcoreContentProvider();

	Viewer viewer;

	IJobManager jobManager = Activator.getDefault().getJobManager();
	//
	// /**
	// * Mapping between {@link IResource} and current
	// * {@link ScenarioTreeNodeClass} representation
	// */
	// final Map<IResource, ScenarioTreeNodeClass> nodeMap = new
	// WeakHashMap<IResource, ScenarioTreeNodeClass>();

	// IScenarioTreeNodeListener scenarioTreeNodeListener = new
	// IScenarioTreeNodeListener() {
	//
	// @Override
	// public void scenarioChanged(final ScenarioTreeNodeClass node, final
	// Scenario oldScenario, final Scenario newScenario) {
	//
	// }
	//
	// @Override
	// public void resourceChanged(final ScenarioTreeNodeClass node, final
	// IResource oldResource, final IResource newResource) {
	//
	// }
	//
	// @Override
	// public void jobChanged(final ScenarioTreeNodeClass node, final
	// IManagedJob oldJob, final IManagedJob newJob) {
	//
	// if (node != null) {
	// Display.getDefault().asyncExec(new Runnable() {
	//
	// @Override
	// public void run() {
	// ((TreeViewer) viewer).refresh(node, true);
	// }
	// });
	// }
	// }
	// };

	IJobManagerListener jobManagerListener = new IJobManagerListener() {

		@Override
		public void jobSelected(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

		}

		@Override
		public void jobRemoved(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

			job.removeListener(listener);

			// final ScenarioTreeNodeClass node = nodeMap.get(resource);

			// if (node != null) {

			// node.setJob(null);

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});
			// }
		}

		@Override
		public void jobDeselected(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

		}

		@Override
		public void jobAdded(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

			// final ScenarioTreeNodeClass node = nodeMap.get(resource);
			//
			// if (node != null) {
			job.addListener(listener);
			// node.setJob(job);

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});
			// }
		}
	};

	public ScenarioContentProvider() {
		jobManager.addJobManagerListener(jobManagerListener);
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

	// TODO: Weak -> Weak?

	IManagedJobListener listener = new IManagedJobListener() {

		@Override
		public void jobStateChanged(final IManagedJob job,
				final JobState oldState, final JobState newState) {

			// RE_SELECT SELECTIOB

			final IResource resource = jobManager.findResourceForJob(job);
			//
			// final ScenarioTreeNodeClass node = nodeMap.get(resource);
			// if (node != null) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					((TreeViewer) viewer).refresh(resource, true);
				}
			});
			// }
		}

		@Override
		public void jobProgressUpdated(final IManagedJob job,
				final int progressDelta) {

			final TreeViewer tv = (TreeViewer) viewer;
			// Skip TreeViewer as it filters out identical selections, but talk
			// direct to Tree and re-select current items.
			// TODO: Should check selected items are the correct resources!
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					final TreeItem[] current = tv.getTree().getSelection();
					tv.getTree().setSelection(current);
				}
			});

		}
	};

	@Override
	public Object[] getChildren(final Object element) {

		// >> Better idea - forget derived, just hide .scenario and replace
		// folders with the RandomTreeNode object

		// Generate proper hierarchy/.

		// TODO: Wrong scenario/schedule selected?

		// Remember to add ion/out to plugin.xml

		if (element instanceof IContainer) {
			IContainer container = (IContainer) element;

			final List<Object> children = new ArrayList<Object>();
			try {
				for (final IResource member : container.members()) {
					if (member.isAccessible()) {

						if (member.getType() == IResource.FILE
								&& member.getFileExtension().equals("scenario")) {

							// // Create and link
							// final ScenarioTreeNodeClass node = new
							// ScenarioTreeNodeClass();
							//
							// node.setResource(member);
							// node.addListener(scenarioTreeNodeListener);
							//
							// nodeMap.put(member, node);

							final IManagedJob job = jobManager
									.findJobForResource(member);
							if (job != null) {
								// node.setJob(job);
								job.addListener(listener);
							}

							// final Object[] scenarioObjects =
							// scp.getChildren(member);
							//
							// if (scenarioObjects != null) {
							// for (final Object o : scenarioObjects) {
							// if (o instanceof Scenario) {
							// node.setScenario((Scenario) o);
							// // Assuming one scenario
							// continue;
							// }
							// }
							// }

							children.add(member);
						} else {
							children.add(member);
						}
					}
				}
			} catch (final CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return children.toArray();
		}

		return super.getChildren(element);
	}

	@Override
	public boolean hasChildren(final Object element) {

//		if (element instanceof ScenarioTreeNodeClass) {
//			// No?
//		}

		return super.hasChildren(element);
	}

	@Override
	protected void processDelta(final IResourceDelta delta) {

		// TODO: Check the Delta and do stuff if needed

		super.processDelta(delta);
	}

	@Override
	public void dispose() {

		jobManager.removeJobManagerListener(jobManagerListener);

		for (final IManagedJob job : jobManager.getJobs()) {
			job.removeListener(listener);
		}

		super.dispose();
	}
}
