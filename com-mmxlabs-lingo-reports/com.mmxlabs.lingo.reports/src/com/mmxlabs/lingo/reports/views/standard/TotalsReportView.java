/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.views.standard.TotalsTransformer.RowData;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class TotalsReportView extends ViewPart {

	private SelectedScenariosService selectedScenariosService;

	private final ArrayList<Integer> sortColumns = new ArrayList<Integer>(4);

	private boolean inverseSort = false;
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.TotalsReportView";

	private GridTableViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private GridViewerColumn delta;

	private GridViewerColumn scheduleColumnViewer;
	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		private final TotalsTransformer transformer = new TotalsTransformer();

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioInstance pinned, final Collection<ScenarioInstance> others, final boolean block) {

			final Runnable r = new Runnable() {
				@Override
				public void run() {
					final List<Object> rowElements = new LinkedList<>();
					int numberOfSchedules = 0;
					List<RowData> pinnedData = null;
					if (pinned != null) {
						LNGScenarioModel instance = selectedDataProvider.getScenarioModel(pinned);
						if (instance != null) {
							final Schedule schedule = ScenarioModelUtil.findSchedule(instance);
							if (schedule != null) {
								pinnedData = transformer.transform(schedule, pinned.getName(), null);
								rowElements.addAll(pinnedData);
								numberOfSchedules++;
							}
						}
					}
					for (final ScenarioInstance other : others) {
						LNGScenarioModel instance = selectedDataProvider.getScenarioModel(other);
						if (instance != null) {
							final Schedule schedule = ScenarioModelUtil.findSchedule(instance);
							if (schedule != null) {
								rowElements.addAll(transformer.transform(schedule, other.getName(), pinnedData));
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
				}
			};
			RunnerHelper.exec(r, block);
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
			if (obj instanceof RowData) {
				final RowData d = (RowData) obj;
				switch (index) {
				case 0:
					return d.scheduleName;
				case 1:
					return d.component;
				case 2:
					return d.type;
				case 3:
					if (TotalsTransformer.TYPE_TIME.equals(d.type)) {
						final long days = d.fitness / 24;
						final long hours = d.fitness % 24;
						return "" + days + "d, " + hours + "h";
					} else {
						return String.format("%,d", d.fitness);
					}
				case 4:
					final Long l = d.deltaFitness;
					if (l != null) {
						if (TotalsTransformer.TYPE_TIME.equals(d.type)) {
							return String.format("%dd, %dh", l / 24, l % 24);
						} else {
							return String.format("%,d", l);
						}
					}
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

		@Override
		public Font getFont(final Object element) {
			if (element instanceof RowData) {
				final RowData d = (RowData) element;
				if ("Total Cost".equals(d.component)) {
					return boldFont;
				}
			}

			return null;
		}

		@Override
		public Color getForeground(final Object element, final int columnIndex) {
			if (columnIndex == 4 && element instanceof RowData) {
				final RowData rowData = (RowData) element;
				final Long l = rowData.deltaFitness;
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
	 * The constructor.
	 */
	public TotalsReportView() {
	}

	private void addSortSelectionListener(final GridColumn column, final int value) {
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

	protected void setSortColumn(final GridColumn column, final int value) {
		if (sortColumns.get(0) == value) {
			inverseSort = !inverseSort;
		} else {
			inverseSort = false;
			sortColumns.remove((Object) value);
			sortColumns.add(0, value);
		}

		// viewer.getTable().setSortColumn(column);
		// viewer.getTable().setSortDirection(inverseSort ? SWT.DOWN : SWT.UP);

		ViewerHelper.refresh(viewer, true);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		selectedScenariosService = (SelectedScenariosService) getSite().getService(SelectedScenariosService.class);

		viewer = new GridTableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(getViewSite());

		scheduleColumnViewer = new GridViewerColumn(viewer, SWT.NONE);
		scheduleColumnViewer.getColumn().setText("Schedule");
		scheduleColumnViewer.getColumn().pack();
		addSortSelectionListener(scheduleColumnViewer.getColumn(), 0);

		final GridViewerColumn tvc1 = new GridViewerColumn(viewer, SWT.NONE);
		tvc1.getColumn().setText("Component");
		tvc1.getColumn().pack();
		addSortSelectionListener(tvc1.getColumn(), 1);

		final GridViewerColumn tvc3 = new GridViewerColumn(viewer, SWT.NONE);
		tvc3.getColumn().setText("Type");
		tvc3.getColumn().pack();
		addSortSelectionListener(tvc3.getColumn(), 2);

		final GridViewerColumn tvc2 = new GridViewerColumn(viewer, SWT.NONE);
		tvc2.getColumn().setText("Total");
		tvc2.getColumn().pack();
		addSortSelectionListener(tvc2.getColumn(), 3);

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);

		sortColumns.add(0);
		sortColumns.add(2);
		sortColumns.add(3);
		sortColumns.add(1);

		// viewer.getGrid().setSortColumn(tvc0.getColumn());
		// viewer.getGrid().setSortDirection(SWT.UP);

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
						sort = r1.type.compareTo(r2.type);
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_Totals");
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);

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

	private void setShowColumns(final boolean showDeltaColumn, int numberOfSchedules) {
		if (showDeltaColumn) {
			if (delta == null) {
				delta = new GridViewerColumn(viewer, SWT.NONE);
				delta.getColumn().setText("Change");
				delta.getColumn().pack();
				addSortSelectionListener(delta.getColumn(), 4);
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
}