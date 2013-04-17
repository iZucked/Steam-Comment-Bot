/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.views.HorizontalKPIContentProvider.RowData;

/**
 * @since 3.0
 */
public class HorizontalKPIReportView extends ViewPart {

	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.HorizontalKPIReportView";

	private GridTableViewer viewer;

	private ScenarioViewerSynchronizer viewerSynchronizer;

	private HorizontalKPIContentProvider contentProvider;

	private GridViewerColumn scheduleColumnViewer;

	class ViewLabelProvider extends CellLabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(final Object obj, final int index) {
			if (obj instanceof RowData) {
				final RowData d = (RowData) obj;
				switch (index) {
				case 0:
					return d.scheduleName;
				case 1:
					return "P&L";
				case 2:
					return format(d.pnl, KPIContentProvider.TYPE_COST);
				case 3:
					return "Shipping Cost";
				case 4:
					return format(d.shippingCost, KPIContentProvider.TYPE_COST);
				case 5:
					return "Idle Time";
				case 6:
					return format(d.idleTime, KPIContentProvider.TYPE_TIME);
				}
			}
			return "";
		}

		private String format(final Long value, final String type) {
			if (value == null)
				return "";
			if (KPIContentProvider.TYPE_TIME.equals(type)) {
				final long days = value / 24;
				final long hours = value % 24;
				return "" + days + "d, " + hours + "h";
			} else if (KPIContentProvider.TYPE_COST.equals(type)) {
				return String.format("$%,d", value);
			} else {
				return String.format("%,d", value);
			}
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
	public HorizontalKPIReportView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new GridTableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		this.contentProvider = new HorizontalKPIContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setInput(getViewSite());

		scheduleColumnViewer = new GridViewerColumn(viewer, SWT.NONE);
		scheduleColumnViewer.getColumn().setText("Schedule");
		scheduleColumnViewer.getColumn().pack();
		// For some reason we've ended up with a small row height.
		// This appears to fix it, but no idea why the problem occurs in first place.
		// SG: 2013-04-17
		viewer.setAutoPreferredHeight(true);
		// KPI columns - two per KPI - name, value
		for (int i = 0; i < 2 * 3; ++i) {
			final GridViewerColumn tvc = new GridViewerColumn(viewer, SWT.NONE);
			tvc.getColumn().setWidth(100);
		}

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(false);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), ID);

		viewerSynchronizer = ScenarioViewerSynchronizer.registerView(viewer, new ScheduleElementCollector() {
			private int numberOfSchedules;

			@Override
			public void beginCollecting() {
				numberOfSchedules = 0;
			}

			@Override
			public void endCollecting() {
				setShowColumns(numberOfSchedules);
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean pinned) {
				++numberOfSchedules;
				return Collections.singleton(schedule);
			}
		});

		// viewer.addOpenListener(new IOpenListener() {
		//
		// @Override
		// public void open(final OpenEvent event) {
		// if (event.getSelection() instanceof IStructuredSelection) {
		// final IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
		// if (structuredSelection.size() == 1) {
		// final Object obj = structuredSelection.getFirstElement();
		// if (obj instanceof RowData) {
		// final RowData rowData = (RowData) obj;
		// // final String viewId = rowData.viewID;
		// // if (viewId != null) {
		// // try {
		// // getSite().getPage().showView(viewId);
		// // } catch (final PartInitException e) {
		// // log.error(e.getMessage(), e);
		// // }
		// // }
		//
		// }
		// }
		// }
		// }
		// });
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

		ScenarioViewerSynchronizer.deregisterView(viewerSynchronizer);
		super.dispose();
	}

	private void setShowColumns(int numberOfSchedules) {

		scheduleColumnViewer.getColumn().setVisible(numberOfSchedules > 1);
	}
}