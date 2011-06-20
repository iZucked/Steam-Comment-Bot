/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.rcp.navigator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.impl.DisposeOnRemoveListener;
import com.mmxlabs.jobcontroller.core.impl.JobManagerListener;
import com.mmxlabs.jobcontroller.core.impl.LNGSchedulerJob;
import com.mmxlabs.rcp.common.actions.PackTreeColumnsAction;

public class TheNavigator extends CommonNavigator {

	private final ResourceListener resourceListener = new ResourceListener();

	final IJobManagerListener jobManagerListener = new JobManagerListener() {

		/**
		 * Update the checked status of items in the tree
		 * 
		 * @param item
		 * @param res
		 * @param check
		 */
		private void checkItems(final TreeItem item, final IResource res,
				final boolean check) {

			// See if we have a match
			if (item.getData() instanceof IResource) {
				final IResource resource = (IResource) item.getData();
				if (resource == res) {
					item.setChecked(check);
					// TODO: Break out of recursive loop?
				}
			}
			// Recurse down the tree...
			for (final TreeItem i : item.getItems()) {
				checkItems(i, res, check);
			}
		}

		@Override
		public void jobSelected(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

			final TreeItem[] items = TheNavigator.this.getCommonViewer()
					.getTree().getItems();
			for (final TreeItem i : items) {
				checkItems(i, resource, true);
			}
		}

		@Override
		public void jobDeselected(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {
			final TreeItem[] items = TheNavigator.this.getCommonViewer()
					.getTree().getItems();
			for (final TreeItem i : items) {
				checkItems(i, resource, false);
			}
		}
	};

	@Override
	protected CommonViewer createCommonViewerObject(final Composite aParent) {
		return new CommonViewer(getViewSite().getId(), aParent, SWT.MULTI
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
	}

	@Override
	protected CommonViewer createCommonViewer(final Composite aParent) {

		final CommonViewer aViewer = createCommonViewerObject(aParent);

		final Tree treeTable = aViewer.getTree();

		treeTable.setHeaderVisible(true);
		treeTable.setLinesVisible(true);
		// treeTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		// treeTable.setFont(aParent.getFont());
		// PlatformUI.getWorkbench().getHelpSystem()
		// .setHelp(treeTable, ContextIds.VIEW_SERVERS);

		// add columns
		final TreeColumn column = new TreeColumn(treeTable, SWT.SINGLE);
		column.setText("Resource");
		column.setWidth(150);
		// column.setWidth(cols[0]);
		// column.addSelectionListener(getHeaderListener(0));
		treeTable.setSortColumn(column);
		treeTable.setSortDirection(SWT.UP);

		final TreeColumn column2 = new TreeColumn(treeTable, SWT.SINGLE);

		column2.setText("Status");
		column2.setWidth(150);
		// column2.setWidth(cols[1]);
		// column2.addSelectionListener(getHeaderListener(1));

		initListeners(aViewer);
		aViewer.getNavigatorContentService().restoreState(memento);

		/**
		 * Create a listener to keep the selected jobs up-to-date
		 */
		treeTable.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(final Event event) {

				// Without SWT.CHECK style?
				if (event.item instanceof Button
						&& event.item.getData() instanceof IResource) {
					final IResource resource = (IResource) event.item.getData();
					if (resource == null) {
						return;
					}
					final Button button = (Button) event.item;

					// Set selection status
					final IJobManager jobManager = Activator.getDefault()
							.getJobManager();

					if (jobManager.findJobForResource(resource) == null) {
						final Scenario scenario = (Scenario) resource
								.getAdapter(Scenario.class);

						if (scenario == null) {
							return;
						}

						String name = resource.getName();

						final String fileExtension = resource
								.getFileExtension();
						if (fileExtension != null) {
							name = name.replaceAll("." + fileExtension, "");
						}
						scenario.setName(name);

						final LNGSchedulerJob j = new LNGSchedulerJob(scenario);
						jobManager.addJob(j, resource);
					}
					jobManager.setResourceSelection(resource,
							button.getSelection());
				}

				else if (event.item instanceof TreeItem) {
					final TreeItem ti = (TreeItem) event.item;
					if (ti.getData() instanceof IResource) {

						final IResource resource = (IResource) event.item
								.getData();

						if (resource == null) {
							return;
						}

						final Scenario scenario = (Scenario) resource
								.getAdapter(Scenario.class);

						if (scenario == null) {
							// Only allow resources with a scenario to be
							// checked
							ti.setChecked(false);
							return;
						}

						String name = resource.getName();

						final String fileExtension = resource
								.getFileExtension();
						if (fileExtension != null) {
							name = name.replaceAll("." + fileExtension, "");
						}
						scenario.setName(name);

						// Set selection status
						final IJobManager jobManager = Activator.getDefault()
								.getJobManager();

						// If checked, then ensure we have a job
						if (ti.getChecked()
								&& jobManager.findJobForResource(resource) == null) {

							// Create a job, but do not prepare it for fast
							// viewing.
							final LNGSchedulerJob j = new LNGSchedulerJob(
									scenario);

							jobManager
									.addJobManagerListener(new DisposeOnRemoveListener(
											j));

							jobManager.addJob(j, resource);
						}

						jobManager.setResourceSelection(resource,
								ti.getChecked());
					}
				}
			}
		});

		Activator.getDefault().getJobManager()
				.addJobManagerListener(jobManagerListener);

		final IActionBars bars = getViewSite().getActionBars();
		// Add pack columns action
		{
			final Action a = new PackTreeColumnsAction(aViewer);
			bars.getToolBarManager().add(a);
			bars.getToolBarManager().update(true);
		}
		return aViewer;
	}

	@Override
	public void dispose() {

		Activator.getDefault().getJobManager()
				.removeJobManagerListener(jobManagerListener);

		resourceListener.dispose();

		super.dispose();
	}

	/**
	 * {@link IResourceChangeListener} implementation to remove jobs in the
	 * CREATED state on filesystem changes.
	 * 
	 * @author Simon Goodall
	 * 
	 */
	private static class ResourceListener implements IResourceChangeListener,
			IResourceDeltaVisitor {

		public ResourceListener() {
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
					IResourceChangeEvent.POST_CHANGE);
		}

		public void dispose() {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		}

		@Override
		public void resourceChanged(final IResourceChangeEvent event) {
			try {
				final IResourceDelta delta = event.getDelta();
				delta.accept(this);
			} catch (final CoreException e) {
				// System.out.println("Resource Changed Fail - " +
				// e.toString());
			}
		}

		@Override
		public boolean visit(final IResourceDelta delta) throws CoreException {
			final IResource changedResource = delta.getResource();

			if (changedResource.getType() == IResource.FILE
					&& changedResource.getFileExtension().equals("scenario")) {
				// try {

				// final String path = ((IFile) changedResource).getFullPath()
				// .toString();
				// final URI uri = URI.createPlatformResourceURI(path, true);
				// final Resource res = resourceSet.getResource(uri, false);
				// if (res != null) {
				// res.unload();
				// // Only load resource if not removed
				// if ((delta.getKind() & IResourceDelta.REMOVED) == 0) {
				// res.load(resourceSet.getLoadOptions());
				// }
				// }

//				boolean reselect = false;
				final IJobManager jobManager = Activator.getDefault()
						.getJobManager();
				{
					// If checked, then ensure we have a job
					final IManagedJob oldJ = jobManager
							.findJobForResource(changedResource);
					if (oldJ == null) {
						return false;
					}
					if (oldJ.getJobState() != JobState.CREATED) {
						return false;
					}
//					reselect = jobManager.getSelectedJobs().contains(oldJ);
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							jobManager.removeJob(oldJ);

						}
					});

				}
				return false;
			}
			return true;
		}

	}

}
