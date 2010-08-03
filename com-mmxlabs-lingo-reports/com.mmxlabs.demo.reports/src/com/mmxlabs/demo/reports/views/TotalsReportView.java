package com.mmxlabs.demo.reports.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.demo.reports.Activator;
import com.mmxlabs.demo.reports.views.TotalsContentProvider.RowData;
import com.mmxlabs.demo.reports.views.actions.PackTableColumnsAction;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.core.ManagedJobListenerNotifier;

public class TotalsReportView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.demo.reports.views.TotalsReportView";

	private TableViewer viewer;

	private List<IManagedJob> selectedJobs = new LinkedList<IManagedJob>();
	private IManagedJobListener jobListener;
	private IJobManagerListener jobManagerListener;

	private PackTableColumnsAction packColumnsAction;

	class ViewLabelProvider extends CellLabelProvider implements
			ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {

			if (obj instanceof RowData) {
				RowData d = (RowData) obj;
				if (index == 0) {
					return d.component;
				} else {
					return String.format("%,d", d.fitness);
				}
			}
			return null;
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return null;
		}

		@Override
		public void update(ViewerCell cell) {

		}
	}

	/**
	 * The constructor.
	 */
	public TotalsReportView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new TotalsContentProvider());
		viewer.setInput(getViewSite());

		TableViewerColumn tvc1 = new TableViewerColumn(viewer, SWT.NONE);
		tvc1.getColumn().setText("Component");
		tvc1.getColumn().pack();

		TableViewerColumn tvc2 = new TableViewerColumn(viewer, SWT.NONE);
		tvc2.getColumn().setText("Total");
		tvc2.getColumn().pack();

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);

		// Create the help context id for the viewer's control
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(viewer.getControl(), "com.mmxlabs.demo.reports.viewer");
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		jobListener = new ManagedJobListenerNotifier() {

			@Override
			public void jobNotified(IManagedJob job) {

				if (selectedJobs.contains(job)) {
					setInput(job.getSchedule());
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

		selectedJobs.addAll(Activator.getDefault().getJobManager().getSelectedJobs());
		if (selectedJobs.isEmpty()) {
			setInput(null);
		} else {
			setInput(selectedJobs.get(0).getSchedule());
			selectedJobs.get(0).addManagedJobListener(jobListener);
		}
		
		Activator.getDefault().getJobManager()
				.addJobManagerListener(jobManagerListener);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				TotalsReportView.this.fillContextMenu(manager);
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
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(packColumnsAction);
	}

	private void makeActions() {
		packColumnsAction = new PackTableColumnsAction(viewer);
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
}