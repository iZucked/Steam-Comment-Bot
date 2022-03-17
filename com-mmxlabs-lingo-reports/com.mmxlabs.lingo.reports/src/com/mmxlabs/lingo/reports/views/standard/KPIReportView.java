/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContentsGenerators;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.views.standard.KPIReportTransformer.RowData;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.scenario.service.ScenarioResult;

public class KPIReportView extends ViewPart {

	private static final Logger log = LoggerFactory.getLogger(KPIReportView.class);

	private ScenarioComparisonService selectedScenariosService;

	private final ArrayList<Integer> sortColumns = new ArrayList<>(4);

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.KPIReportView";

	private GridTableViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private GridViewerColumn delta;

	private GridViewerColumn scheduleColumnViewer;

	private final @NonNull ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		private final KPIReportTransformer transformer = new KPIReportTransformer();

		@Override
		public void selectedDataProviderChanged(@NonNull final ISelectedDataProvider selectedDataProvider, final boolean block) {

			final Runnable r = () -> {
				final List<Object> rowElements = new LinkedList<>();

				int numberOfSchedules = 0;
				List<RowData> pinnedData = null;
				final ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();
				if (pinned != null) {
					final ScheduleModel scheduleModel = pinned.getTypedResult(ScheduleModel.class);
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							pinnedData = transformer.transform(schedule, pinned, null);
							rowElements.addAll(pinnedData);
							numberOfSchedules++;
						}
					}
				}
				for (final ScenarioResult other : selectedDataProvider.getOtherScenarioResults()) {
					final ScheduleModel scheduleModel = other.getTypedResult(ScheduleModel.class);
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							rowElements.addAll(transformer.transform(schedule, other, pinnedData));
							numberOfSchedules++;
						}
					}
				}

				setShowColumns(pinned != null, numberOfSchedules);

				ViewerHelper.setInput(viewer, true, rowElements);
				if (!rowElements.isEmpty()) {
					if (packColumnsAction != null) {
						packColumnsAction.run();
					}
				}
			};

			ViewerHelper.runIfViewerValid(viewer, block, r);
		}
	};

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
			if (obj instanceof RowData d) {
				switch (index) {
				case 0:
					return d.scheduleName;
				case 1:
					return d.component;
				case 2:
					return format(d.value, d.type);
				case 3:
					if (d.deltaValue != null) {
						return format(d.deltaValue, d.type);
					}
				}
			}
			return "";
		}

		private String format(final Long value, final String type) {
			if (value == null)
				return "";
			if (KPIReportTransformer.TYPE_TIME.equals(type)) {
				final long days = value / 24;
				final long hours = value % 24;
				return "" + days + "d, " + hours + "h";
			} else if (KPIReportTransformer.TYPE_COST.equals(type)) {
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
			if (element instanceof RowData d) {
				if ("Total Cost".equals(d.component)) {
					return boldFont;
				}
			}

			return null;
		}

		@Override
		public Color getForeground(final Object element, final int columnIndex) {
			if (columnIndex == 3 && element instanceof final RowData rowData) {
				final Long l = rowData.deltaValue;
				if (l == null || l.longValue() == 0l) {
					return null;
				} else if ((l < 0 && !rowData.minimise) || (l > 0 && rowData.minimise)) {
					return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
				} else {
					return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
				}
			}
			return null;
		}

		@Override
		public Color getBackground(final Object element, final int columnIndex) {
			return null;
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		selectedScenariosService = getSite().getService(ScenarioComparisonService.class);

		viewer = new GridTableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridViewerHelper.configureLookAndFeel(viewer);

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(getViewSite());

		scheduleColumnViewer = new GridViewerColumn(viewer, SWT.NONE);
		scheduleColumnViewer.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		scheduleColumnViewer.getColumn().setText("Schedule");
		scheduleColumnViewer.getColumn().pack();

		final GridViewerColumn tvc1 = new GridViewerColumn(viewer, SWT.NONE);
		tvc1.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		tvc1.getColumn().setText("KPI");
		tvc1.getColumn().pack();

		final GridViewerColumn tvc2 = new GridViewerColumn(viewer, SWT.NONE);
		tvc2.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		tvc2.getColumn().setText("Value");
		tvc2.getColumn().pack();

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);

		sortColumns.add(0);
		sortColumns.add(1);
		sortColumns.add(2);
		sortColumns.add(3);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_KPI");
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		viewer.addOpenListener(event -> {
			if (event.getSelection() instanceof final IStructuredSelection structuredSelection) {
				if (structuredSelection.size() == 1) {
					final Object obj = structuredSelection.getFirstElement();
					if (obj instanceof final RowData rowData) {
						final String viewId = rowData.viewID;
						if (viewId != null) {
							try {
								getSite().getPage().showView(viewId);
							} catch (final PartInitException e) {
								log.error(e.getMessage(), e);
							}
						}

					}
				}
			}
		});

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);

	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(KPIReportView.this::fillContextMenu);
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
		packColumnsAction = new PackGridTableColumnsAction(viewer);
		copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	@Override
	public void dispose() {

		selectedScenariosService.removeListener(selectedScenariosServiceListener);

		super.dispose();
	}

	private void setShowColumns(final boolean showDeltaColumn, final int numberOfSchedules) {
		if (showDeltaColumn) {
			if (delta == null) {
				delta = new GridViewerColumn(viewer, SWT.NONE);
				delta.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				delta.getColumn().setText("Change");
				delta.getColumn().pack();
				viewer.setLabelProvider(viewer.getLabelProvider());
			}
		} else {
			if (delta != null) {
				delta.getColumn().dispose();
				delta = null;
			}
		}

		scheduleColumnViewer.getColumn().setVisible(numberOfSchedules > 1);
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			return adapter.cast(ReportContentsGenerators.createJSONFor(selectedScenariosServiceListener, viewer.getGrid()));
		}

		return super.getAdapter(adapter);
	}
}