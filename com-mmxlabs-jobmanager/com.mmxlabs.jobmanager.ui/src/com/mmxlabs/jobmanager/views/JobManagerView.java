/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.views;

import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.ui.Activator;
import com.mmxlabs.rcp.common.actions.PackActionFactory;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view shows data obtained from the model. The sample creates a dummy model on the fly, but a real implementation would connect
 * to the model available either in this or another plug-in (e.g. the workspace). The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be presented in the view. Each view can present the same model objects using different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views in order to ensure that objects of the same type are presented in the same way everywhere.
 * <p>
 */

public class JobManagerView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.jobcontroller.views.JobManager";

	private TreeViewer viewer;
	private Action packAction;
	private Action startAction;
	private Action pauseAction;
	private Action stopAction;
	private Action removeAction;

	private final IEclipseJobManagerListener jobManagerListener;

	private final IEclipseJobManager jobManager = Activator.getDefault().getEclipseJobManager();

	private final IJobControlListener jobListener = new IJobControlListener() {
		@Override
		public boolean jobStateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {
			JobManagerView.this.refresh();
			switch (newState) {
			case PAUSED:
			case RUNNING:
				refresh();
				break;
			default:
				break;
			}
			return true;
		}

		private void refresh() {
			JobManagerView.this.refresh();
			getSite().getShell().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					updateActionEnablement(viewer.getSelection());
				}
			});
		}

		@Override
		public boolean jobProgressUpdated(final IJobControl control, final int progressDelta) {
			JobManagerView.this.refresh();
			return true;
		}
	};

	/**
	 * The constructor.
	 */
	public JobManagerView() {
		assert jobListener != null;
		this.jobManagerListener = new JobManagerViewRefreshListener(this, jobListener);
	}

	public void submitJob(final IJobDescriptor theJob, final IResource resource) {
		final IJobControl control = jobManager.submitJob(theJob, resource);
		control.prepare();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		final TreeViewerColumn tvc0 = new TreeViewerColumn(viewer, SWT.None);
		tvc0.getColumn().setText("");

		final TreeViewerColumn tvc1 = new TreeViewerColumn(viewer, SWT.None);
		tvc1.getColumn().setText("Name");

		final TreeViewerColumn tvc2 = new TreeViewerColumn(viewer, SWT.None);
		tvc2.getColumn().setText("Progress");

		final TreeViewerColumn tvc3 = new TreeViewerColumn(viewer, SWT.None);
		tvc3.getColumn().setText("Status");

		viewer.getTree().setLinesVisible(true);
		viewer.getTree().setHeaderVisible(true);

		viewer.setContentProvider(new JobManagerContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider(getSite().getShell(), jobManager));
		viewer.setInput(jobManager);

		// Set initial column sizes
		for (final TreeColumn c : viewer.getTree().getColumns()) {
			c.pack();
		}

		// Create the help context id for the viewer's control

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.jobmanager.viewer");

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {

				final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

				updateActionEnablement(selection);
			}
		});

		jobManager.addEclipseJobManagerListener(jobManagerListener);

		// Register listener on existing jobs
		for (final IJobDescriptor job : jobManager.getJobs()) {
			final IJobControl control = jobManager.getControlForJob(job);
			assert jobListener != null;
			control.addListener(jobListener);
		}

		// update button state
		updateActionEnablement(viewer.getSelection());

		// Make the table a selection provider
		getSite().setSelectionProvider(viewer);
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				JobManagerView.this.fillContextMenu(manager);
			}
		});
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(final IMenuManager manager) {
		manager.add(startAction);
		manager.add(pauseAction);
		manager.add(stopAction);
		manager.add(removeAction);
	}

	private void fillContextMenu(final IMenuManager manager) {
		manager.add(startAction);
		manager.add(pauseAction);
		manager.add(stopAction);
		manager.add(removeAction);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(packAction);
		manager.add(startAction);
		manager.add(pauseAction);
		manager.add(stopAction);
		manager.add(removeAction);
	}

	private void makeActions() {

		packAction = PackActionFactory.createPackColumnsAction(viewer);

		startAction = new Action() {
			@Override
			public void run() {

				final Iterator<IJobDescriptor> itr = getTreeSelectionIterator();
				while (itr.hasNext()) {
					// TODO: Check states
					final IJobDescriptor job = itr.next();
					final IJobControl control = jobManager.getControlForJob(job);
					if (control.getJobState() == EJobState.CREATED) {
						control.prepare();
						control.start();
					} else if (control.getJobState() == EJobState.PAUSED) {
						control.resume();
					} else {
						control.start();
					}
				}
			}
		};
		startAction.setText("Start");
		startAction.setToolTipText("Start/Resume Job");
		startAction.setImageDescriptor(Activator.getImageDescriptor("/icons/elcl16/resume_co.gif"));
		startAction.setDisabledImageDescriptor(Activator.getImageDescriptor("/icons/dlcl16/resume_co.gif"));

		pauseAction = new Action() {
			@Override
			public void run() {

				final Iterator<IJobDescriptor> itr = getTreeSelectionIterator();
				while (itr.hasNext()) {
					// TODO: Check states
					final IJobDescriptor job = itr.next();
					final IJobControl control = jobManager.getControlForJob(job);
					control.pause();
				}
			}
		};
		pauseAction.setText("Suspend");
		pauseAction.setToolTipText("Suspend Job");
		pauseAction.setImageDescriptor(Activator.getImageDescriptor("/icons/elcl16/suspend_co.gif"));
		pauseAction.setDisabledImageDescriptor(Activator.getImageDescriptor("/icons/dlcl16/suspend_co.gif"));

		stopAction = new Action() {
			@Override
			public void run() {

				final Iterator<IJobDescriptor> itr = getTreeSelectionIterator();
				while (itr.hasNext()) {
					final IJobDescriptor job = itr.next();
					final IJobControl control = jobManager.getControlForJob(job);
					control.cancel();
				}
			}
		};
		stopAction.setText("Abort");
		stopAction.setToolTipText("Abort Job");
		stopAction.setImageDescriptor(Activator.getImageDescriptor("/icons/elcl16/terminate_co.gif"));
		stopAction.setDisabledImageDescriptor(Activator.getImageDescriptor("/icons/dlcl16/terminate_co.gif"));

		removeAction = new Action() {
			@Override
			public void run() {

				final Iterator<IJobDescriptor> itr = getTreeSelectionIterator();
				while (itr.hasNext()) {
					final IJobDescriptor job = itr.next();

					final IJobControl control = jobManager.getControlForJob(job);

					control.addListener(new IJobControlListener() {

						@Override
						public boolean jobStateChanged(final IJobControl jc, final EJobState oldState, final EJobState newState) {
							if (newState == EJobState.CANCELLED) {
								jobManager.removeJob(job);
								return false;
							}
							return true;
						}

						@Override
						public boolean jobProgressUpdated(final IJobControl job, final int progressDelta) {
							return true;
						}
					});

					control.cancel();
				}
			}
		};
		removeAction.setText("Remove");
		removeAction.setToolTipText("Abort and remove Job from view");
		removeAction.setImageDescriptor(Activator.getImageDescriptor("/icons/elcl16/rem_co.gif"));
		removeAction.setDisabledImageDescriptor(Activator.getImageDescriptor("/icons/dlcl16/rem_co.gif"));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void refresh() {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				viewer.refresh();
			}
		});
	}

	private void updateActionEnablement(final ISelection selection) {

		if (selection.isEmpty()) {
			startAction.setEnabled(false);
			pauseAction.setEnabled(false);
			stopAction.setEnabled(false);
			removeAction.setEnabled(false);
		} else {
			boolean startEnabled = false;
			boolean pauseEnabled = false;
			boolean stopEnabled = false;

			if (selection instanceof IStructuredSelection) {
				// Loop through jobs enabling buttons as valid. Operations
				// will do nothing if not valid.
				final Iterator<IJobDescriptor> itr = getTreeSelectionIterator();
				while (itr.hasNext()) {

					final IJobDescriptor job = itr.next();
					final IJobControl control = jobManager.getControlForJob(job);

					switch (control.getJobState()) {
					case CREATED:
						startEnabled = true;
						break;
					case CANCELLED:
						startEnabled = true;
						break;
					case COMPLETED:
						startEnabled = true;
						break;
					case INITIALISED:
						startEnabled = true;
						break;
					case PAUSED:
						startEnabled = true;
						stopEnabled = true;
						break;
					case PAUSING:
						stopEnabled = true;
						break;
					case RESUMING:
						stopEnabled = true;
						break;
					case RUNNING:
						pauseEnabled = true;
						stopEnabled = true;
						break;
					case UNKNOWN:
						break;
					default:
						pauseEnabled = false;
						stopEnabled = false;
						break;
					}
				}
			}

			startAction.setEnabled(startEnabled);
			pauseAction.setEnabled(pauseEnabled);
			stopAction.setEnabled(stopEnabled);

			removeAction.setEnabled(true);
		}
	}

	@Override
	public void dispose() {
		jobManager.removeEclipseJobManagerListener(jobManagerListener);

		// Register listener on existing jobs
		for (final IJobDescriptor job : jobManager.getJobs()) {
			final IJobControl control = jobManager.getControlForJob(job);
			assert jobListener != null;
			control.removeListener(jobListener);
		}

		super.dispose();
	}

	/**
	 * Helper method to return a typed {@link Iterator} from the {@link #viewer} selection.
	 * 
	 * @return
	 */
	private Iterator<IJobDescriptor> getTreeSelectionIterator() {
		return new SelectedJobIterator((IStructuredSelection) viewer.getSelection());
	}
}