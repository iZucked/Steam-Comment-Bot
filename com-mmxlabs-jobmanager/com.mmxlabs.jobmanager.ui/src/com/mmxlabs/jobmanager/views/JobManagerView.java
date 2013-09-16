/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.views;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

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
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
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
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ResourceTransfer;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.ui.Activator;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackTreeColumnsAction;

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
	private Action toggleDisplayAction;

	private Action doubleClickAction;

	private final IEclipseJobManagerListener jobManagerListener = new IEclipseJobManagerListener() {

		@Override
		public void jobRemoved(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {

			control.removeListener(jobListener);

			JobManagerView.this.refresh();
		}

		@Override
		public void jobAdded(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {

			control.addListener(jobListener);

			JobManagerView.this.refresh();
		}

		@Override
		public void jobManagerAdded(final IEclipseJobManager eclipseJobManager, final IJobManager jobManager) {
			JobManagerView.this.refresh();

		}

		@Override
		public void jobManagerRemoved(final IEclipseJobManager eclipseJobManager, final IJobManager jobManager) {
			JobManagerView.this.refresh();

		}
	};

	private final IEclipseJobManager jobManager = Activator.getDefault().getEclipseJobManager();

	public void submitJob(final IJobDescriptor theJob, final IResource resource) {
		final IJobControl control = jobManager.submitJob(theJob, resource);
		control.prepare();
	}

	private class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

		@Override
		public String getColumnText(final Object obj, final int index) {

			if (obj instanceof IJobManager) {
				switch (index) {
				case 0:
					return ((IJobManager) obj).getJobManagerDescriptor().getName();
				}
			} else if (obj instanceof IJobDescriptor) {
				final IJobDescriptor job = (IJobDescriptor) obj;
				final IJobControl control = jobManager.getControlForJob(job);
				switch (index) {
				case 1:
					return job.getJobName();
				case 2:
					return Integer.toString(control.getProgress()) + "%";
				case 3:
					return control.getJobState().toString();
				}
			}

			return null;
		}

		@Override
		public Image getColumnImage(final Object obj, final int index) {

			// TODO: Cache images -- they need to be disposed

			if (index == 0) {
				if (obj instanceof IJobDescriptor) {
					final IJobDescriptor job = (IJobDescriptor) obj;
//					if (jobManager.getSelectedJobs().contains(job)) {
//						return getCachedImage("/icons/console_view.gif");
//					} else {
						return null;
//					}
				}
			}

			if (index == 1) {
				if (obj instanceof IJobDescriptor) {
					final IJobDescriptor job = (IJobDescriptor) obj;
					final IJobControl control = jobManager.getControlForJob(job);

					return getCachedImage(control.getJobState());
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
			if (key instanceof EJobState) {
				final EJobState state = (EJobState) key;

				switch (state) {
				case CANCELLED:
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

	private final IJobControlListener jobListener = new IJobControlListener() {
		@Override
		public boolean jobStateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {
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
		public boolean jobProgressUpdated(final IJobControl control, final int progressDelta) {
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
								final IJobDescriptor job = (IJobDescriptor) adapterManger.getAdapter(res, IJobDescriptor.class);

								if (job != null) {

									// Adapter may return an existing job for the resource .. so skip it
									if (jobManager.getJobs().contains(job)) {
										monitor.worked(1);
										continue;
									}

									jobManager.submitJob(job, res);

									// Hook in a listener to automatically dispose the job once it is no
									// longer needed
									jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));

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
		viewer.setLabelProvider(new ViewLabelProvider());
		// viewer.setSorter(new NameSorter());
		viewer.setInput(jobManager);

		// Set initial column sizes
		for (final TreeColumn c : viewer.getTree().getColumns()) {
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

		jobManager.addEclipseJobManagerListener(jobManagerListener);

		// Register listener on existing jobs
		for (final IJobDescriptor job : jobManager.getJobs()) {
			final IJobControl control = jobManager.getControlForJob(job);
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

		toggleDisplayAction = new Action() {
			@Override
			public void run() {

				final Iterator<IJobDescriptor> itr = getTreeSelectionIterator();
				while (itr.hasNext()) {
					final IJobDescriptor job = itr.next();
//					jobManager.toggleJobSelection(job);
				}
			}
		};
		toggleDisplayAction.setText("Toggle Display Flag");
		toggleDisplayAction.setToolTipText("Toggle whether or not a job is linked to views");
		toggleDisplayAction.setImageDescriptor(Activator.getImageDescriptor("/icons/console_view.gif"));

		doubleClickAction = new Action() {

			@Override
			public void run() {
				// final Iterator<IJobDescriptor> itr = getTableSelectionIterator();
				// while (itr.hasNext()) {
				// final IJobDescriptor job = itr.next();
				// jobManager.toggleJobSelection(job);
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
				final Iterator<IJobDescriptor> itr = getTreeSelectionIterator();
				while (itr.hasNext()) {

					final IJobDescriptor job = itr.next();
					final IJobControl control = jobManager.getControlForJob(job);

					switch (control.getJobState()) {
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

		jobManager.removeEclipseJobManagerListener(jobManagerListener);

		// Register listener on existing jobs
		for (final IJobDescriptor job : jobManager.getJobs()) {
			final IJobControl control = jobManager.getControlForJob(job);
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
		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		/**
		 * Wrapper around the tree iterator which only contains IJobDescriptors.
		 */
		final Iterator<IJobDescriptor> itr = new Iterator<IJobDescriptor>() {
			final Iterator<?> selectionItr = selection.iterator();

			private IJobDescriptor next = null;

			@Override
			public boolean hasNext() {
				next = null;
				while (selectionItr.hasNext() && (next == null)) {
					final Object obj = selectionItr.next();
					if (obj instanceof IJobDescriptor) {
						next = (IJobDescriptor) obj;
						return true;
					}
				}
				return false;
			}

			@Override
			public IJobDescriptor next() {
				// Assume null means end of list.
				if (next == null) {
					throw new NoSuchElementException();
				}

				final IJobDescriptor job = next;
				next = null;
				return job;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not implemeneted");
			}
		};
		// We assume there is only a single type of object in the table
		return itr;
	}
}