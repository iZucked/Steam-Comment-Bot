package com.mmxlabs.rcp.navigator.scenario;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.navigator.resources.workbench.ResourceExtensionContentProvider;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.rcp.navigator.ecore.EcoreContentProvider;




@SuppressWarnings("restriction")
public class ScenarioContentProvider extends ResourceExtensionContentProvider
		implements ITreeContentProvider {

	EcoreContentProvider scp = new EcoreContentProvider();
	
	private Scenario getScenario(Object obj) {
		for (Object o : scp.getChildren(obj)) {
			if (o instanceof Scenario) {
				return (Scenario)o;
			}
		}
		return null;
	}
	
	Viewer viewer;

	IJobManager jobManager = Activator.getDefault().getJobManager();

	final Map<IResource, ScenarioTreeNodeClass> nodeMap = new WeakHashMap<IResource, ScenarioTreeNodeClass>();

	IJobManagerListener jobManagerListener = new IJobManagerListener() {

		@Override
		public void jobSelected(final IJobManager jobManager, final IManagedJob job,
				final IResource resource) {

		}

		@Override
		public void jobRemoved(final IJobManager jobManager, final IManagedJob job,
				final IResource resource) {
			
			job.removeListener(listener);

			final ScenarioTreeNodeClass node = nodeMap.get(resource);

			
			if (node != null) {
				
				node.setJob(null);

				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						((TreeViewer) viewer).refresh(node, true);
					}
				});
			}

		}

		@Override
		public void jobDeselected(final IJobManager jobManager, final IManagedJob job,
				final IResource resource) {
			// TODO Auto-generated method stub

		}

		@Override
		public void jobAdded(final IJobManager jobManager, final IManagedJob job,
				final IResource resource) {
			
			final ScenarioTreeNodeClass node = nodeMap.get(resource);

			if (node != null) {
				job.addListener(listener);
				node.setJob(job);
				
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						((TreeViewer) viewer).refresh(node, true);
					}
				});
			}
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
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		this.viewer = viewer;
	}

	// TODO: Weak -> Weak?

	IManagedJobListener listener = new IManagedJobListener() {

		@Override
		public void jobStateChanged(final IManagedJob job, final JobState oldState,
				final JobState newState) {

			final IResource resource = jobManager.findResourceForJob(job);

			// TODO Auto-generated method stub
			final ScenarioTreeNodeClass node = nodeMap.get(resource);
			if (node != null) {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						((TreeViewer) viewer).refresh(node, true);
						
					}
				});
			}
		}

		@Override
		public void jobProgressUpdated(final IManagedJob job, final int progressDelta) {

		}
	};

	@Override
	public Object[] getChildren(final Object element) {

		// >> Better idea - forget derived, just hide .scenario and replace
		// folders with the RandomTreeNode object

		// GEnerate proper hieratchy/.

		// TODO: Wrong scenario/schedule selected?

		// Remember to add ion/out to plugin.xml

		final IContainer container;
		if (element instanceof ScenarioTreeNodeClass) {
			final ScenarioTreeNodeClass node = (ScenarioTreeNodeClass) element;
			container = node.getContainer();
		}

		else if (element instanceof IContainer) {

			container = (IContainer) element;
		} else {
			container = null;
		}

		final List<Object> children = new ArrayList<Object>();
		if (container != null) {
			try {
				for (final IResource member : container.members()) {
					if (member.isAccessible()) {

						if (member instanceof IContainer) {

							// Find hidden .scenario resource
							final IResource scenarioMember = ((IContainer) member)
									.findMember(".scenario");

							if (scenarioMember != null) {
								
								// Create and link
								final ScenarioTreeNodeClass node = new ScenarioTreeNodeClass();
								
								node.setContainer((IContainer) member);
								node.setResource(scenarioMember);
								
								nodeMap.put(scenarioMember, node);

								final IManagedJob job = jobManager.findJobForResource(scenarioMember);
								if (job != null) {
									node.setJob(job);
									job.addListener(listener);
								}
								
								final Object[] scenarioObjects = scp.getChildren(scenarioMember);
								
								if (scenarioObjects != null) {
									for (final Object o : scenarioObjects) {
										if (o instanceof Scenario) {
											node.setScenario((Scenario) o);
											// Assuming one scenario
											continue;
										}
									}
								}
								
								children.add(node);
								continue;
							}
						}

						children.add(member);

					}
				}
			} catch (final CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return children.toArray();
		}

		// TODO Auto-generated method stub
		return super.getChildren(element);
	}

	@Override
	public boolean hasChildren(final Object element) {

		if (element instanceof IResource) {
			// IJobManager m;
			// ScenarioOptimisationJob j;
			// Scenario s = j.getScenario();
			// s.eResource().getResourceSet();

		}

		// TODO Auto-generated method stub
		return super.hasChildren(element);
	}

	@Override
	protected void processDelta(final IResourceDelta delta) {

		// System.out.println(delta);

		// TODO: Process Deltas to refresh nodes
//		TreeViewer v = (TreeViewer) viewer;
//		v.refresh(true);

		// delta.getAffectedChildren()TestContentProvider[
		//
		// TODO Auto-generated method stub
		super.processDelta(delta);
	}

	// HANDLE UPDTES TO CONTAINTE?RESOURE AND TRIGGER VBIWER REFEREv

	@Override
	public void dispose() {

		jobManager.removeJobManagerListener(jobManagerListener);

		 for (final ScenarioTreeNodeClass node : nodeMap.values()) {
			 IManagedJob job = node.getJob();
			 if (job != null) {
				 job.removeListener(listener);
			 }
		 }
		 nodeMap.clear();

		// TODO Auto-generated method stub
		super.dispose();
	}
}
