/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import scenario.schedule.Schedule;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackTableColumnsAction;
import com.mmxlabs.shiplingo.platform.reports.ScheduleAdapter;
import com.mmxlabs.shiplingo.platform.reports.views.TotalsContentProvider.RowData;

public class TotalsReportView extends ViewPart implements ISelectionListener {
	private final ArrayList<Integer> sortColumns = new ArrayList<Integer>(4);

	private boolean inverseSort = false;
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.demo.reports.views.TotalsReportView";

	private TableViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private IEclipseJobManagerListener jobManagerListener;

	static class ViewLabelProvider extends CellLabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(final Object obj, final int index) {

			if (obj instanceof RowData) {
				final RowData d = (RowData) obj;
				switch (index) {
				case 0:
					return d.scheduleName;
				case 1:
					return d.component;
				case 2:
					return d.isCost ? "Cost" : "Profit";
				case 3:
					return String.format("%,d", d.fitness);
				}
			}
			return "";
		}

		@Override
		public Image getColumnImage(final Object obj, final int index) {
			return null;
		}

		@Override
		public void update(final ViewerCell cell) {

		}
	}

	/**
	 * The constructor.
	 */
	public TotalsReportView() {
	}

	private void addSortSelectionListener(final TableColumn column, final int value) {
		column.addSelectionListener(new SelectionAdapter() {
			{
				final SelectionAdapter self = this;
				column.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						column.removeSelectionListener(self);
					}
				});
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				setSortColumn(column, value);
			}
		});
	}

	protected void setSortColumn(final TableColumn column, final int value) {
		if (sortColumns.get(0) == value) {
			inverseSort = !inverseSort;
		} else {
			inverseSort = false;
			sortColumns.remove((Object) value);
			sortColumns.add(0, value);
		}

		viewer.getTable().setSortColumn(column);
		viewer.getTable().setSortDirection(inverseSort ? SWT.DOWN : SWT.UP);

		viewer.refresh();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION) {
			@Override
			protected void inputChanged(final Object input, final Object oldInput) {
				super.inputChanged(input, oldInput);

				final boolean inputEmpty = (input == null) || ((input instanceof Collection) && ((Collection<?>) input).isEmpty());
				final boolean oldInputEmpty = (oldInput == null) || ((oldInput instanceof Collection) && ((Collection<?>) oldInput).isEmpty());

				if (inputEmpty != oldInputEmpty) {

					if (packColumnsAction != null) {
						packColumnsAction.run();
					}
				}
			};
		};
		viewer.setContentProvider(new TotalsContentProvider());
		viewer.setInput(getViewSite());

		final TableViewerColumn tvc0 = new TableViewerColumn(viewer, SWT.NONE);
		tvc0.getColumn().setText("Schedule");
		tvc0.getColumn().pack();
		addSortSelectionListener(tvc0.getColumn(), 0);

		final TableViewerColumn tvc1 = new TableViewerColumn(viewer, SWT.NONE);
		tvc1.getColumn().setText("Component");
		tvc1.getColumn().pack();
		addSortSelectionListener(tvc1.getColumn(), 1);

		final TableViewerColumn tvc3 = new TableViewerColumn(viewer, SWT.NONE);
		tvc3.getColumn().setText("Type");
		tvc3.getColumn().pack();
		addSortSelectionListener(tvc3.getColumn(), 2);

		final TableViewerColumn tvc2 = new TableViewerColumn(viewer, SWT.NONE);
		tvc2.getColumn().setText("Total");
		tvc2.getColumn().pack();
		addSortSelectionListener(tvc2.getColumn(), 3);

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);

		sortColumns.add(0);
		sortColumns.add(2);
		sortColumns.add(3);
		sortColumns.add(1);

		viewer.getTable().setSortColumn(tvc0.getColumn());
		viewer.getTable().setSortDirection(SWT.UP);

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				final RowData r1 = (RowData) e1;
				final RowData r2 = (RowData) e2;

				final int d = inverseSort ? -1 : 1;
				int sort = 0;
				final Iterator<Integer> iterator = sortColumns.iterator();
				while (iterator.hasNext() && (sort == 0)) {
					switch (iterator.next()) {
					case 0:
						sort = r1.scheduleName.compareTo(r2.scheduleName);
						break;
					case 1:
						sort = r1.component.compareTo(r2.component);
						break;
					case 2:
						if (r1.isCost == r2.isCost) {
							sort = 0;
						} else if (r1.isCost) {
							sort = -1;
						} else {
							sort = 1;
						}
						break;
					case 3:
						sort = ((Long) r1.fitness).compareTo(r2.fitness);
						break;
					}
				}

				return d * sort;
				// return super.compare(viewer, e1, e2);
			}
		});

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.demo.reports.TotalsReportView");
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		//
		// getSite().getPage().addSelectionListener("com.mmxlabs.rcp.navigator",
		// this);
		//
		// final ISelection selection = getSite().getWorkbenchWindow()
		// .getSelectionService()
		// .getSelection("com.mmxlabs.rcp.navigator");
		//
		// selectionChanged(null, selection);

		jobManagerListener = ScheduleAdapter.registerView(viewer);
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				TotalsReportView.this.fillContextMenu(manager);
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
		manager.add(new Separator());
	}

	private void fillContextMenu(final IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(new GroupMarker("pack"));
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));

		manager.appendToGroup("pack", packColumnsAction);
		manager.appendToGroup("copy", copyTableAction);
	}

	private void makeActions() {
		packColumnsAction = new PackTableColumnsAction(viewer);
		copyTableAction = new CopyTableToClipboardAction(viewer.getTable());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
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
	public void selectionChanged(final IWorkbenchPart arg0, final ISelection selection) {

		final List<Schedule> schedules = ScheduleAdapter.getSchedules(selection);
		if (!schedules.isEmpty()) {
			setInput(schedules);
		} else {
			setInput(null);
		}
	}

	@Override
	public void dispose() {
		// getSite().getPage().removeSelectionListener(
		// "com.mmxlabs.rcp.navigator", this);

		ScheduleAdapter.deregisterView(jobManagerListener);
		super.dispose();
	}
}