/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.views.HorizontalKPIContentProvider.RowData;

/**
 * @since 3.0
 */
public class HorizontalKPIReportView extends ViewPart {

	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.HorizontalKPIReportView";

	private GridTableViewer viewer;

	private HorizontalKPIContentProvider contentProvider;

	// private IEditorPart activeEditor = null;
	private ScheduleModel scheduleModel;

	private IPartListener partListener;

	private ScenarioViewerSynchronizer viewerSynchronizer;

	class ViewLabelProvider extends CellLabelProvider implements ITableLabelProvider, IFontProvider, ITableColorProvider {

		private final Font boldFont;

		public ViewLabelProvider() {
			final Font systemFont = Display.getDefault().getSystemFont();
			// Clone the font data
			final FontData fd = new FontData(systemFont.getFontData()[0].toString());
			// Set the bold bit.
			fd.setStyle(fd.getStyle() | SWT.BOLD);
			boldFont = new Font(Display.getDefault(), fd);
		}

		@Override
		public void dispose() {
			boldFont.dispose();
			super.dispose();
		}

		@Override
		public String getColumnText(final Object obj, final int index) {
			if (obj instanceof RowData) {
				final RowData d = (RowData) obj;
				final RowData pinD = contentProvider.getPinnedData();
				Long rtn = null;
				switch (index) {
				case 0:
					return "P&L";
				case 1:
					rtn = (d.pnl != null ? d.pnl - (pinD != null ? pinD.pnl : 0) : null);
					return format(rtn, KPIContentProvider.TYPE_COST);
				case 2:
					return "Shipping Cost";
				case 3:
					rtn = (d.shippingCost != null ? d.shippingCost - (pinD != null ? pinD.shippingCost : 0) : null);
					return format(rtn, KPIContentProvider.TYPE_COST);
				case 4:
					return "Idle Time";
				case 5:
					rtn = (d.idleTime != null ? d.idleTime - (pinD != null ? pinD.idleTime : 0) : null);
					return format(rtn, KPIContentProvider.TYPE_TIME);
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

		@Override
		public Font getFont(final Object element) {
			return boldFont;
		}

		@Override
		public Color getForeground(final Object element, final int columnIndex) {

			if (element instanceof RowData) {
				int color = SWT.COLOR_DARK_GRAY;
				switch (columnIndex) {
				case 0:
					color = SWT.COLOR_BLACK;
					break;
				case 1:
					final RowData pinD = contentProvider.getPinnedData();
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						final RowData d = (RowData) element;
						color = (d.pnl - pinD.pnl) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				}
				return Display.getCurrent().getSystemColor(color);
			}
			return null;
		}

		@Override
		public Color getBackground(final Object element, final int columnIndex) {
			// TODO Auto-generated method stub
			return null;
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

		// For some reason we've ended up with a small row height.
		// This appears to fix it, but no idea why the problem occurs in first place.
		// SG: 2013-04-17
		viewer.setAutoPreferredHeight(true);
		// KPI columns - two per KPI - name, value
		for (int i = 0; i < 2 * 3; ++i) {
			final GridViewerColumn tvc = new GridViewerColumn(viewer, SWT.NONE);
			int width = 100;
			switch (i) {
			case 0:
				width = 33; // "P&L"
				break;
			case 2:
				width = 85; // "Shipping Cost"
				break;
			case 4:
				width = 61; // "Idle time"
				break;
			}
			tvc.getColumn().setWidth(width);
		}

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(false);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), ID);

		partListener = new IPartListener() {

			@Override
			public void partOpened(final IWorkbenchPart part) {
			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {
			}

			@Override
			public void partClosed(final IWorkbenchPart part) {

			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {
				if (part instanceof IEditorPart) {
					// Active editor changed
					activeEditorChange((IEditorPart) part);
				}
				viewer.refresh();
			}

			@Override
			public void partActivated(final IWorkbenchPart part) {
				if (part instanceof IEditorPart) {
					// Active editor changed
					activeEditorChange((IEditorPart) part);
				}
				viewer.refresh();
			}
		};
		getSite().getPage().addPartListener(partListener);
		// Set initial active editor
		activeEditorChange(getSite().getPage().getActiveEditor());

		viewerSynchronizer = ScenarioViewerSynchronizer.registerView(viewer, new ScheduleElementCollector() {
			// private boolean hasPin = false;
			// private int numberOfSchedules;

			@Override
			public void beginCollecting() {
				// numberOfSchedules = 0;
			}

			@Override
			public void endCollecting() {
				// setShowColumns(false, 1);
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean pinned) {

				if (pinned || (scheduleModel != null && schedule == scheduleModel.getSchedule())) {
					// ++numberOfSchedules;
					return Collections.singleton(schedule);
				} else {
					return Collections.emptySet();
				}
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
				if (!viewer.getControl().isDisposed()) {
					viewer.refresh();
				}
			}
		});
	}

	@Override
	public void dispose() {
		ScenarioViewerSynchronizer.deregisterView(viewerSynchronizer);
		viewerSynchronizer = null;

		getSite().getPage().removePartListener(partListener);
		super.dispose();
	}

	private void activeEditorChange(final IEditorPart activeEditor) {
		ScheduleModel scheduleModel = null;
		if (activeEditor != null) {
			final IEditorInput editorInput = activeEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput ssInput = (IScenarioServiceEditorInput) editorInput;
				final ScenarioInstance scenarioInstance = ssInput.getScenarioInstance();
				if (scenarioInstance != null) {
					final EObject instance = scenarioInstance.getInstance();
					if (instance instanceof LNGScenarioModel) {
						final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
						final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
						if (portfolioModel != null) {
							scheduleModel = portfolioModel.getScheduleModel();
						}
					}
				}
			}
		}
		// this.activeEditor = activeEditor;
		this.scheduleModel = scheduleModel;
	}

}