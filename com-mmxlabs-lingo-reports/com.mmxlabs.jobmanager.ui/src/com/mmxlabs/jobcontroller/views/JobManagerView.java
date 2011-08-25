/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.views;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ResourceTransfer;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.core.impl.DisposeOnRemoveListener;
import com.mmxlabs.jobmanager.ui.Activator;
import com.mmxlabs.rcp.common.actions.PackTableColumnsAction;

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

	private TableViewer viewer;
	private Action packAction;
	private Action startAction;
	private Action pauseAction;
	private Action stopAction;
	private Action removeAction;
	private Action toggleDisplayAction;

	private Action doubleClickAction;

	private final IJobManagerListener jobManagerListener = new IJobManagerListener() {

		@Override
		public void jobRemoved(final IJobManager jobManager, final IManagedJob job, final IResource resource) {

			job.removeListener(jobListener);

			JobManagerView.this.refresh();
		}

		@Override
		public void jobAdded(final IJobManager jobManager, final IManagedJob job, final IResource resource) {

			job.addListener(jobListener);

			JobManagerView.this.refresh();
		}

		@Override
		public void jobSelected(final IJobManager jobManager, final IManagedJob job, final IResource resource) {

			JobManagerView.this.refresh();
		}

		@Override
		public void jobDeselected(final IJobManager jobManager, final IManagedJob job, final IResource resource) {

			JobManagerView.this.refresh();
		}
	};

	private final IJobManager jobManager = Activator.getDefault().getJobManager();

	public void addJob(final IManagedJob theJob, final IResource resource) {
		theJob.prepare();
		jobManager.addJob(theJob, resource);
	}

	private class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

		@Override
		public String getColumnText(final Object obj, final int index) {

			if (obj instanceof IManagedJob) {
				final IManagedJob job = (IManagedJob) obj;
				switch (index) {
				case 1:
					return job.getJobName();
				case 2:
					return Integer.toString(job.getProgress()) + "%";
				case 3:
					return job.getJobState().toString();
				}
			}

			return null;
		}

		@Override
		public Image getColumnImage(final Object obj, final int index) {

			// TODO: Cache images -- they need to be disposed

			if (index == 0) {
				if (obj instanceof IManagedJob) {
					final IManagedJob job = (IManagedJob) obj;
					if (jobManager.getSelectedJobs().contains(job)) {
						return getCachedImage("/icons/console_view.gif");
					} else {
						return null;
					}
				}
			}

			if (index == 1) {
				if (obj instanceof IManagedJob) {
					final IManagedJob job = (IManagedJob) obj;

					return getCachedImage(job.getJobState());
				}
			}

			return null;
		}

		@Override
		public Image getImage(final Object obj) {
			return null;
		}

		Image getCachedImage(final Object key) {

			if (imageCache.containsKey(key)) {
				return imageCache.get(key);
			}

			ImageDescriptor desc = null;
			if (key instanceof JobState) {
				final JobState state = (JobState) key;

				switch (state) {
				case CANCELLED:
				case ERROR:
					return getSite().getShell().getDisplay().getSystemImage(SWT.ICON_ERROR);
				case CANCELLING:
					return getSite().getShell().getDisplay().getSystemImage(SWT.ICON_ERROR);
				case COMPLETED:
					desc = Activator.getImageDescriptor("/icons/elcl16/terminate_co.gif");
					break;
				case INITIALISED:
					desc = Activator.getImageDescriptor("/icons/elcl16/terminate_co.gif");
					break;
				case PAUSED:
					desc = Activator.getImageDescriptor("/icons/elcl16/suspend_co.gif");
					break;
				case PAUSING:
					desc = Activator.getImageDescriptor("/icons/dlcl16/suspend_co.gif");
					break;
				case RESUMING:
					desc = Activator.getImageDescriptor("/icons/dlcl16/resume_co.gif");
					break;
				case RUNNING:
					desc = Activator.getImageDescriptor("/icons/elcl16/resume_co.gif");
					break;
				case UNKNOWN:
					return getSite().getShell().getDisplay().getSystemImage(SWT.ICON_WARNING);
				}
			} else {
				desc = Activator.getImageDescriptor(key.toString());
			}

			// Cache image
			if (desc != null) {
				final Image img = desc.createImage();
				imageCache.put(key, img);
				return img;
			}

			return null;

		}

		@Override
		public void dispose() {

			for (final Image image : imageCache.values()) {
				image.dispose();
			}

			imageCache.clear();

			super.dispose();
		}
	}

	/**
	 * The constructor.
	 */
	public JobManagerView() {

	}

	private final IManagedJobListener jobListener = new IManagedJobListener() {
		@Override
		public boolean jobStateChanged(final IManagedJob job, final JobState oldState, final JobState newState) {
			JobManagerView.this.refresh();
			switch (newState) {
			case PAUSED:
			case RUNNING:
				JobManagerView.this.refresh();
				getSite().getShell().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						updateActionEnablement(viewer.getSelection());
					}
				});
				break;

			}
			return true;
		}

		@Override
		public boolean jobProgressUpdated(final IManagedJob job, final int progressDelta) {
			JobManagerView.this.refresh();
			return true;
		}
	};

	private final DropTargetListener dropTargetListener = new DropTargetAdapter() {

		@Override
		public void dragOver(final DropTargetEvent event) {
			// always indicate a copy
			event.detail = DND.DROP_COPY;
			event.feedback = DND.FEEDBACK_NONE;
		}

		@Override
		public void dragOperationChanged(final DropTargetEvent event) {
			// always indicate a copy
			event.detail = DND.DROP_COPY;
			event.feedback = DND.FEEDBACK_NONE;

		}

		@Override
		public void dragEnter(final DropTargetEvent event) {
			// always indicate a copy
			event.detail = DND.DROP_COPY;
			event.feedback = DND.FEEDBACK_NONE;

		}

		@Override
		public void drop(final DropTargetEvent event) {

			performDrop(event);
		}

		private void performDrop(final DropTargetEvent event) {

			if (ResourceTransfer.getInstance().isSupportedType(event.currentDataType)) {
				Assert.isTrue(event.data instanceof IResource[]);
				final IResource[] files = (IResource[]) event.data;
				final IRunnableWithProgress runnable = new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

						final IAdapterManager adapterManger = Platform.getAdapterManager();
						monitor.beginTask("Create Jobs", files.length);
						try {
							for (int i = 0; i < files.length; i++) {

								// Check to see if user has cancelled operation
								if (monitor.isCanceled()) {
									break;
								}

								final IResource res = files[i];
								// Use adapter factory to create a new job instance.
								final IManagedJob job = (IManagedJob) adapterManger.getAdapter(res, IManagedJob.class);

								if (job != null) {

									// Adapter may return an existing job for the resource .. so skip it
									if (jobManager.getJobs().contains(job)) {
										monitor.worked(1);
										continue;
									}

									jobManager.addJob(job, res);

									// Hook in a listener to automatically dispose the job once it is no
									// longer needed
									jobManager.addJobManagerListener(new DisposeOnRemoveListener(job));

								}
								monitor.worked(1);
							}

						} finally {
							monitor.done();
						}
					}
				};
				// Run job modally in a progress dialog
				final Display d = Display.getCurrent();
				final ProgressMonitorDialog dialog = new ProgressMonitorDialog(d.getActiveShell());
				try {
					dialog.run(true, true, runnable);
				} catch (final InvocationTargetException e) {
					Activator.error(e.getMessage(), e);
				} catch (final InterruptedException e) {
					Activator.error(e.getMessage(), e);
				}
			}
		}
	};

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		final TableViewerColumn tvc0 = new TableViewerColumn(viewer, SWT.None);
		tvc0.getColumn().setText("");

		final TableViewerColumn tvc1 = new TableViewerColumn(viewer, SWT.None);
		tvc1.getColumn().setText("Name");

		final TableViewerColumn tvc2 = new TableViewerColumn(viewer, SWT.None);
		tvc2.getColumn().setText("Progress");

		final TableViewerColumn tvc3 = new TableViewerColumn(viewer, SWT.None);
		tvc3.getColumn().setText("Status");

		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new ViewLabelProvider());
		// viewer.setSorter(new NameSorter());
		viewer.setInput(jobManager.getJobs());

		// Set initial column sizes
		for (final TableColumn c : viewer.getTable().getColumns()) {
			c.pack();
		}

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.jobmanager.viewer");

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		final Transfer[] dropTransferTypes = new Transfer[] { ResourceTransfer.getInstance() };

		viewer.addDropSupport(DND.DROP_COPY, dropTransferTypes, dropTargetListener);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {

				final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

				updateActionEnablement(selection);
			}
		});

		jobManager.addJobManagerListener(jobManagerListener);

		// Register listener on existing jobs
		for (final IManagedJob job : jobManager.getJobs()) {
			job.addListener(jobListener);
		}
		// // TODO: Is this required here?
		// refresh();

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
		manager.add(toggleDisplayAction);
	}

	private void fillContextMenu(final IMenuManager manager) {
		manager.add(startAction);
		manager.add(pauseAction);
		manager.add(stopAction);
		manager.add(removeAction);
		manager.add(toggleDisplayAction);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(packAction);
		manager.add(toggleDisplayAction);
		manager.add(startAction);
		manager.add(pauseAction);
		manager.add(stopAction);
		manager.add(removeAction);
	}

	private void makeActions() {

		packAction = new PackTableColumnsAction(viewer);

		startAction = new Action() {
			@Override
			public void run() {

				final Iterator<IManagedJob> itr = getTableSelectionIterator();
				while (itr.hasNext()) {
					// TODO: Check states
					final IManagedJob job = itr.next();
					if (job.getJobState() == JobState.CREATED) {
						job.prepare();
						job.start();
					} else if (job.getJobState() == JobState.PAUSED) {
						job.resume();
					} else {
						job.start();
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

				final Iterator<IManagedJob> itr = getTableSelectionIterator();
				while (itr.hasNext()) {
					// TODO: Check states
					final IManagedJob job = itr.next();
					job.pause();
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

				final Iterator<IManagedJob> itr = getTableSelectionIterator();
				while (itr.hasNext()) {
					final IManagedJob job = itr.next();
					job.cancel();
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

				final Iterator<IManagedJob> itr = getTableSelectionIterator();
				while (itr.hasNext()) {
					final IManagedJob job = itr.next();
					job.cancel();
					jobManager.removeJob(job);
				}
			}
		};
		removeAction.setText("Remove");
		removeAction.setToolTipText("Abort and remove Job from view");
		removeAction.setImageDescriptor(Activator.getImageDescriptor("/icons/elcl16/rem_co.gif"));
		removeAction.setDisabledImageDescriptor(Activator.getImageDescriptor("/icons/dlcl16/rem_co.gif"));

		toggleDisplayAction = new Action() {
			@Override
			public void run() {

				final Iterator<IManagedJob> itr = getTableSelectionIterator();
				while (itr.hasNext()) {
					final IManagedJob job = itr.next();
					jobManager.toggleJobSelection(job);
				}
			}
		};
		toggleDisplayAction.setText("Toggle Display Flag");
		toggleDisplayAction.setToolTipText("Toggle whether or not a job is linked to views");
		toggleDisplayAction.setImageDescriptor(Activator.getImageDescriptor("/icons/console_view.gif"));

		doubleClickAction = new Action() {

			@Override
			public void run() {
				// final Iterator<IManagedJob> itr = getTableSelectionIterator();
				// while (itr.hasNext()) {
				// final Object obj = itr.next();
				// if (obj instanceof OptManagedJob) {
				// jobManager.toggleJobSelection((OptManagedJob) obj);
				// }
				// }
			}
		};
		doubleClickAction.setText("Set visible job");

	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(final DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
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

		// final IStructuredSelection selection = (IStructuredSelection) viewer
		// .getSelection();
		//
		if (selection.isEmpty()) {
			startAction.setEnabled(false);
			pauseAction.setEnabled(false);
			stopAction.setEnabled(false);
			removeAction.setEnabled(false);
			toggleDisplayAction.setEnabled(false);
		} else {
			boolean startEnabled = false;
			boolean pauseEnabled = false;
			boolean stopEnabled = false;
			boolean toogleDisplayEnabled = false;

			if (selection instanceof IStructuredSelection) {
				// Loop through jobs enabling buttons as valid. Operations
				// will do nothing if not valid.
				final Iterator<IManagedJob> itr = getTableSelectionIterator();
				while (itr.hasNext()) {
					final IManagedJob job = itr.next();

					switch (job.getJobState()) {
					case CREATED:
						startEnabled = true;
						toogleDisplayEnabled = true;
						break;
					case CANCELLED:
						startEnabled = true;
						toogleDisplayEnabled = true;
						break;
					case COMPLETED:
						startEnabled = true;
						toogleDisplayEnabled = true;
						break;
					case INITIALISED:
						startEnabled = true;
						toogleDisplayEnabled = true;
						break;
					case PAUSED:
						startEnabled = true;
						stopEnabled = true;
						toogleDisplayEnabled = true;
						break;
					case PAUSING:
						stopEnabled = true;
						toogleDisplayEnabled = true;
						break;
					case RESUMING:
						stopEnabled = true;
						toogleDisplayEnabled = true;
						break;
					case RUNNING:
						pauseEnabled = true;
						stopEnabled = true;
						toogleDisplayEnabled = true;
						break;
					case UNKNOWN:
						break;

					}
				}
			}

			startAction.setEnabled(startEnabled);
			pauseAction.setEnabled(pauseEnabled);
			stopAction.setEnabled(stopEnabled);
			toggleDisplayAction.setEnabled(toogleDisplayEnabled);

			removeAction.setEnabled(true);
		}
	}

	@Override
	public void dispose() {

		jobManager.removeJobManagerListener(jobManagerListener);

		// Register listener on existing jobs
		for (final IManagedJob job : jobManager.getJobs()) {
			job.removeListener(jobListener);
		}

		super.dispose();
	}

	/**
	 * Helper method to return a typed {@link Iterator} from the {@link #viewer} selection.
	 * 
	 * @return
	 */
	private Iterator<IManagedJob> getTableSelectionIterator() {
		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		// We assume there is only a single type of object in the table
		@SuppressWarnings("unchecked")
		final Iterator<IManagedJob> itr = selection.iterator();
		return itr;
	}
}