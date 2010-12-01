package com.mmxlabs.jobcontroller.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.core.impl.TestUtils;
import com.mmxlabs.jobmanager.ui.Activator;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class JobManagerView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.jobcontroller.views.JobManager";

	private TableViewer viewer;
	private Action startAction;
	private Action pauseAction;
	private Action stopAction;
	private Action removeAction;
	private Action toggleDisplayAction;
	private Action createDummyJobAction;

	private Action doubleClickAction;

	private final IJobManager jobManager = Activator.getDefault()
			.getJobManager();

	public void addJob(IManagedJob theJob) {
		theJob.init();
		jobManager.addJob(theJob);
	}
	
	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class ViewContentProvider implements IStructuredContentProvider {
		@Override
		public void inputChanged(final Viewer v, final Object oldInput,
				final Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(final Object parent) {

			if (parent instanceof List<?>) {
				final List<IManagedJob> jobs = (List) parent;

				return jobs.toArray();
			}

			return null;
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		private final Map<Object, Image> imageCache = new HashMap<Object, Image>();

		@Override
		public String getColumnText(final Object obj, final int index) {

			if (obj instanceof IManagedJob) {
				final IManagedJob job = (IManagedJob) obj;
				switch (index) {
				case 1:
					return job.getName();
				case 2:
					return Integer.toString(job.getProgress());
				case 3:
					return Integer.toString(job.getTotalProgress());
				case 4:
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
				JobState state = (JobState) key;

				switch (state) {
				case ABORTED:
					return getSite().getShell().getDisplay()
							.getSystemImage(SWT.ICON_ERROR);
				case COMPLETED:
					desc = Activator
							.getImageDescriptor("/icons/elcl16/terminate_co.gif");
					break;
				case INITIALISED:
					desc = Activator
							.getImageDescriptor("/icons/elcl16/terminate_co.gif");
					break;
				case PAUSED:
					desc = Activator
							.getImageDescriptor("/icons/elcl16/suspend_co.gif");
					break;
				case PAUSING:
					desc = Activator
							.getImageDescriptor("/icons/dlcl16/suspend_co.gif");
					break;
				case RESUMING:
					desc = Activator
							.getImageDescriptor("/icons/dlcl16/resume_co.gif");
					break;
				case RUNNING:
					desc = Activator
							.getImageDescriptor("/icons/elcl16/resume_co.gif");
					break;
				case UNKNOWN:
					return getSite().getShell().getDisplay()
							.getSystemImage(SWT.ICON_WARNING);
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

	private final IManagedJobListener listener = new IManagedJobListener() {

		@Override
		public void jobStopped(final IManagedJob job) {
			refresh();
		}

		@Override
		public void jobStarted(final IManagedJob job) {
			refresh();
		}

		@Override
		public void jobResumed(final IManagedJob job) {
			refresh();
			getSite().getShell().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateActionEnablement(viewer.getSelection());

				}
			});
		}

		@Override
		public void jobResuming(final IManagedJob job) {
			refresh();
		}

		@Override
		public void jobProgressUpdate(final IManagedJob job,
				final int progressDelta) {
			refresh();
		}

		@Override
		public void jobPaused(final IManagedJob job) {
			refresh();
			getSite().getShell().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateActionEnablement(viewer.getSelection());

				}
			});
		}

		@Override
		public void jobPausing(final IManagedJob job) {
			refresh();
		}

		@Override
		public void jobCompleted(final IManagedJob job) {
			refresh();
		}

		@Override
		public void jobCancelled(final IManagedJob job) {
			refresh();
		}
	};

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		final TableViewerColumn tvc0 = new TableViewerColumn(viewer, SWT.None);

		final TableViewerColumn tvc1 = new TableViewerColumn(viewer, SWT.None);
		tvc1.getColumn().setText("Name");

		final TableViewerColumn tvc2 = new TableViewerColumn(viewer, SWT.None);
		tvc2.getColumn().setText("Progress");

		final TableViewerColumn tvc3 = new TableViewerColumn(viewer, SWT.None);
		tvc3.getColumn().setText("Total Progress");

		final TableViewerColumn tvc4 = new TableViewerColumn(viewer, SWT.None);
		tvc4.getColumn().setText("Status");

		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);

		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		// viewer.setSorter(new NameSorter());
		viewer.setInput(jobManager.getJobs());

		// Set initial column sizes
		for (final TableColumn c : viewer.getTable().getColumns()) {
			c.pack();
		}

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), "com.mmxlabs.jobmanager.viewer");

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {

				final IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				updateActionEnablement(selection);
			}
		});

		jobManager.addJobManagerListener(new IJobManagerListener() {

			@Override
			public void jobRemoved(final IJobManager jobManager,
					final IManagedJob job) {
				job.removeManagedJobListener(listener);
				refresh();
			}

			@Override
			public void jobAdded(final IJobManager jobManager,
					final IManagedJob job) {
				job.addManagedJobListener(listener);
				refresh();
			}

			@Override
			public void jobSelected(IJobManager jobManager, IManagedJob job) {
				// TODO Auto-generated method stub
				refresh();
			}

			@Override
			public void jobDeselected(IJobManager jobManager, IManagedJob job) {
				// TODO Auto-generated method stub
				refresh();
			}
		});

		// Register listener on existing jobs
		for (final IManagedJob job : jobManager.getJobs()) {
			job.addManagedJobListener(listener);
			refresh();
		}

		// update button state
		updateActionEnablement((IStructuredSelection) viewer.getSelection());

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
		manager.add(createDummyJobAction);
		manager.add(toggleDisplayAction);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(toggleDisplayAction);
		manager.add(startAction);
		manager.add(pauseAction);
		manager.add(stopAction);
		manager.add(removeAction);
	}

	private void makeActions() {
		startAction = new Action() {
			@Override
			public void run() {

				final IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				final Iterator<IManagedJob> itr = selection.iterator();
				while (itr.hasNext()) {
					// TODO: Check states
					final IManagedJob job = itr.next();
					if (job.getJobState() == JobState.PAUSED) {
						job.resume();
					} else {
						job.start();
					}
				}
			}
		};
		startAction.setText("Start");
		startAction.setToolTipText("Start/Resume Job");
		startAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/elcl16/resume_co.gif"));
		startAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/dlcl16/resume_co.gif"));

		pauseAction = new Action() {
			@Override
			public void run() {

				final IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				final Iterator<IManagedJob> itr = selection.iterator();
				while (itr.hasNext()) {
					// TODO: Check states
					final IManagedJob job = itr.next();
					job.pause();
				}
			}
		};
		pauseAction.setText("Suspend");
		pauseAction.setToolTipText("Suspend Job");
		pauseAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/elcl16/suspend_co.gif"));
		pauseAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/dlcl16/suspend_co.gif"));

		stopAction = new Action() {
			@Override
			public void run() {

				final IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				final Iterator<IManagedJob> itr = selection.iterator();
				while (itr.hasNext()) {
					// TODO: Check states
					final IManagedJob job = itr.next();
					job.stop();
				}
			}
		};
		stopAction.setText("Abort");
		stopAction.setToolTipText("Abort Job");
		stopAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/elcl16/terminate_co.gif"));
		stopAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/dlcl16/terminate_co.gif"));

		removeAction = new Action() {
			@Override
			public void run() {

				final IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				final Iterator<IManagedJob> itr = selection.iterator();
				while (itr.hasNext()) {
					final IManagedJob job = itr.next();
					job.stop();
					jobManager.removeJob(job);
				}
			}
		};
		removeAction.setText("Remove");
		removeAction.setToolTipText("Abort and remove Job from view");
		removeAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/elcl16/rem_co.gif"));
		removeAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/dlcl16/rem_co.gif"));

		toggleDisplayAction = new Action() {
			@Override
			public void run() {

				final IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				final Iterator<IManagedJob> itr = selection.iterator();
				while (itr.hasNext()) {
					final IManagedJob job = itr.next();
					jobManager.toggleJobSelection(job);
				}
			}
		};
		toggleDisplayAction.setText("Toggle Display Flag");
		toggleDisplayAction
				.setToolTipText("Toggle whether or not a job is linked to views");
		toggleDisplayAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/console_view.gif"));

		createDummyJobAction = new Action() {

			int counter = 0;

			@Override
			public void run() {
				// ISelection selection = viewer.getSelection();
				// Object obj = ((IStructuredSelection) selection)
				// .getFirstElement();
				// showMessage("Double-click detected on " + obj.toString());
				final String name = "Job " + counter++;
				final long seed = 1;

				// Build opt data
				final IOptimisationData<ISequenceElement> data = TestUtils
						.createProblem();
				// Generate initial state
				final ISequences<ISequenceElement> initialSequences = TestUtils
						.createInitialSequences(data, seed);

				final IFitnessFunctionRegistry fitnessRegistry = TestUtils
						.createFitnessRegistry();
				final IConstraintCheckerRegistry constraintRegistry = TestUtils
						.createConstraintRegistry();

				final OptimisationContext<ISequenceElement> context = new OptimisationContext<ISequenceElement>(
						data, initialSequences, new ArrayList<String>(
								fitnessRegistry.getFitnessComponentNames()),
						fitnessRegistry,
						new ArrayList<String>(constraintRegistry
								.getConstraintCheckerNames()),
						constraintRegistry);

//				final OptManagedJob job = new OptManagedJob(name, context);
//
//				job.init();
//
//				jobManager.addJob(job);

			}
		};
		createDummyJobAction.setText("Create dummy job");
		createDummyJobAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/ctool16/launchtrace_wiz.gif"));

		doubleClickAction = new Action() {

			int counter = 0;

			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Iterator<Object> iter = ((IStructuredSelection) selection)
						.iterator();
				while (iter.hasNext()) {
					Object obj = iter.next();
//					if (obj instanceof OptManagedJob) {
//						jobManager.toggleJobSelection((OptManagedJob) obj);
//					}
				}

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
				final Iterator<IManagedJob> itr = ((IStructuredSelection) selection)
						.iterator();
				while (itr.hasNext()) {
					final IManagedJob job = itr.next();

					switch (job.getJobState()) {
					case ABORTED:
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

}