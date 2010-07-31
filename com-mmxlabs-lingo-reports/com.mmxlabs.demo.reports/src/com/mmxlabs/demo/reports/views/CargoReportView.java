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
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.demo.reports.Activator;
import com.mmxlabs.demo.reports.views.CargoContentProvider.RowData;
import com.mmxlabs.demo.reports.views.actions.PackTableColumnsAction;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.core.ManagedJobListenerNotifier;

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

public class CargoReportView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.demo.reports.views.CargoReportView";

	private TableViewer viewer;

	private List<IManagedJob> selectedJobs = new LinkedList<IManagedJob>();
	private IManagedJobListener jobListener;
	private IJobManagerListener jobManagerListener;

	private PackTableColumnsAction packColumnsAction;

	class ViewLabelProvider extends CellLabelProvider implements
			ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			try {
				if (obj instanceof RowData) {
					RowData rd = (RowData) obj;
					switch (index) {
					case 0:
						return rd.id;
					case 1:
						return rd.vessel;//.getName();
					case 2:
						return rd.loadSlot.getPort().getName();
					case 3:
						return rd.dischargeSlot.getPort().getName();
					case 4:
						return rd.loadDate.getTime().toLocaleString();
					case 5:
						return rd.dischargeDate.getTime().toLocaleString();
					case 6:
						return Long.toString(rd.loadVolume);
					case 7:
						return Long.toString(rd.dischargeVolume);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

			return null;
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return null;
		}

		@Override
		public void update(ViewerCell cell) {
			// TODO Auto-generated method stub

		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public CargoReportView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new CargoContentProvider());
		viewer.setInput(getViewSite());

		String[] columns = new String[] { "ID", "Vessel", "Load Slot",
				"Discharge Slot", "Load Date", "Discharge Date", "Load Volume",
				"Discharge Volume" };

		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		for (String cname : columns) {
			TableViewerColumn tvc = new TableViewerColumn(viewer, SWT.None);
			tvc.getColumn().setText(cname);
			tvc.getColumn().pack();
		}

		viewer.setLabelProvider(new ViewLabelProvider());
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
				CargoReportView.this.fillContextMenu(manager);
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