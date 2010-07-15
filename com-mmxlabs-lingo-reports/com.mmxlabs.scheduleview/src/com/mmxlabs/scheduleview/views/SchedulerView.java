package com.mmxlabs.scheduleview.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.nebula.widgets.ganttchart.AbstractSettings;
import org.eclipse.nebula.widgets.ganttchart.ISettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.ZoomInAction;
import com.mmxlabs.ganttviewer.ZoomOutAction;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.core.ManagedJobListenerNotifier;
import com.mmxlabs.scheduleview.Activator;

public class SchedulerView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.scheduleview.views.SchedulerView";

	private GanttChartViewer viewer;
	private Action zoomInAction;
	private Action zoomOutAction;

	private List<IManagedJob> selectedJobs = new LinkedList<IManagedJob>();

	private IManagedJobListener jobListener;

	private IJobManagerListener jobManagerListener;

	/**
	 * The constructor.
	 */
	public SchedulerView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Gantt Chart settings object
		ISettings settings = new AbstractSettings() {
			@Override
			public boolean enableResizing() {
				return false;
			}

			@Override
			public boolean showPlannedDates() {
				return false;
			}

			@Override
			public String getTextDisplayFormat() {
				return "#name#";
			}

			@Override
			public int getSectionTextSpacer() {
				return 0;
			}

			@Override
			public int getMinimumSectionHeight() {
				return 10;
			}
		};

		viewer = new GanttChartViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER, settings);
		viewer.setContentProvider(new AnnotatedScheduleContentProvider());
		viewer.setLabelProvider(new AnnotatedSequenceLabelProvider());

		// viewer.setSorter(new NameSorter());

		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(viewer.getControl(), "com.mmxlabs.scheduleview.viewer");

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		// getSite().getPage().addSelectionListener(JobManagerView.ID,
		// new ISelectionListener() {
		//
		// @Override
		// public void selectionChanged(IWorkbenchPart part,
		// ISelection selection) {
		//
		// selectedJob = null;
		// IStructuredSelection ss = (IStructuredSelection) selection;
		// if (ss.isEmpty() == false) {
		// selectedJob = (IManagedJob) ss.iterator().next();
		// setInput(selectedJob.getSchedule());
		// } else {
		// setInput(null);
		// }
		// refresh();
		// }
		// });

		jobListener = new ManagedJobListenerNotifier() {

			@Override
			public void jobNotified(IManagedJob job) {

				if (selectedJobs.contains(job)) {
					// setInput(selectedJob.getSchedule());
					refresh();
				}
			}
		};

		jobManagerListener = new IJobManagerListener() {

			@Override
			public void jobRemoved(IJobManager jobManager, IManagedJob job) {
				job.removeManagedJobListener(jobListener);
			}

			@Override
			public void jobAdded(IJobManager jobManager, IManagedJob job) {
				job.addManagedJobListener(jobListener);
			}

			@Override
			public void jobSelected(IJobManager jobManager, IManagedJob job) {
				selectedJobs.add(job);
				if (selectedJobs.isEmpty()) {
					setInput(null);
				} else {
					setInput(selectedJobs.get(0).getSchedule());
				}
			}

			@Override
			public void jobDeselected(IJobManager jobManager, IManagedJob job) {
				selectedJobs.remove(job);
				if (selectedJobs.isEmpty()) {
					setInput(null);
				} else {
					setInput(selectedJobs.get(0).getSchedule());
				}

			}
		};

		Activator.getDefault().getJobManager()
				.addJobManagerListener(jobManagerListener);

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				SchedulerView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
	}

	private void makeActions() {

		zoomInAction = new ZoomInAction(viewer.getGanttChart());
		zoomInAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		zoomOutAction = new ZoomOutAction(viewer.getGanttChart());
		zoomOutAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
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
				if (!viewer.getControl().isDisposed()) {
					viewer.refresh();
				}
			}
		});
	}

	public void setInput(final Object input) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {
					viewer.setInput(input);
				}
			}
		});
	}

	@Override
	public void dispose() {

		selectedJobs.clear();

		super.dispose();
	}
}