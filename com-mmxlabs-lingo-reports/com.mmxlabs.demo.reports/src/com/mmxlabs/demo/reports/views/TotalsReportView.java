/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views;

import java.util.List;

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
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import scenario.schedule.Schedule;

import com.mmxlabs.demo.reports.ScheduleAdapter;
import com.mmxlabs.demo.reports.views.TotalsContentProvider.RowData;
import com.mmxlabs.rcp.common.actions.PackTableColumnsAction;

public class TotalsReportView extends ViewPart implements ISelectionListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.demo.reports.views.TotalsReportView";

	private TableViewer viewer;

	private PackTableColumnsAction packColumnsAction;

	class ViewLabelProvider extends CellLabelProvider implements
			ITableLabelProvider {
		@Override
		public String getColumnText(final Object obj, final int index) {

			if (obj instanceof RowData) {
				final RowData d = (RowData) obj;
				if (index == 0) {
					return d.component;
				} else {
					return String.format("%,d", d.fitness);
				}
			}
			return null;
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

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new TotalsContentProvider());
		viewer.setInput(getViewSite());

		final TableViewerColumn tvc1 = new TableViewerColumn(viewer, SWT.NONE);
		tvc1.getColumn().setText("Component");
		tvc1.getColumn().pack();

		final TableViewerColumn tvc2 = new TableViewerColumn(viewer, SWT.NONE);
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

		getSite().getPage().addSelectionListener("com.mmxlabs.rcp.navigator",
				this);

		final ISelection selection = getSite().getWorkbenchWindow()
				.getSelectionService()
				.getSelection("com.mmxlabs.rcp.navigator");

		selectionChanged(null, selection);
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

	@Override
	public void selectionChanged(final IWorkbenchPart arg0,
			final ISelection selection) {

		final List<Schedule> schedules = ScheduleAdapter
				.getSchedules(selection);
		if (!schedules.isEmpty()) {
			setInput(schedules.get(0));
		} else {
			setInput(null);
		}
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(
				"com.mmxlabs.rcp.navigator", this);
		super.dispose();
	}
}