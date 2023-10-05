/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ReentrantSelectionManager;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.EmptyColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.RowHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.TopLeftRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 */
public abstract class SimpleTabularReportView<T> extends ViewPart {

	private ScenarioComparisonService selectedScenariosService;

	private final ArrayList<ColumnManager<T>> sortColumns = new ArrayList<>();
	protected final ArrayList<ColumnManager<T>> columnManagers = new ArrayList<>();

	private boolean inverseSort = false;

	protected GridTreeViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private final String helpContextId;

	protected boolean pinnedMode = false;

	protected Image pinImage;

	@NonNull
	protected final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {

			final Runnable r = () -> {
				final AbstractSimpleTabularReportTransformer<T> transformer = createTransformer();

				columnManagers.clear();

				int numberOfSchedules = 0;
				Pair<Schedule, ScenarioResult> pinnedPair = null;
				List<Pair<Schedule, ScenarioResult>> otherPairs = new LinkedList<>();
				ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();
				if (pinned != null) {
					final ScheduleModel scheduleModel = pinned.getTypedResult(ScheduleModel.class);
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							pinnedPair = new Pair<>(schedule, pinned);
							numberOfSchedules++;
						}
					}
				}
				for (final ScenarioResult other : selectedDataProvider.getOtherScenarioResults()) {
					final ScheduleModel scheduleModel = other.getTypedResult(ScheduleModel.class);
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							otherPairs.add(new Pair<>(schedule, other));
							numberOfSchedules++;
						}
					}
				}
				final Object[] expanded = viewer.getExpandedElements();
				final List<T> rowElements = transformer.createData(pinnedPair, otherPairs);

				columnManagers.addAll(transformer.getColumnManagers(selectedDataProvider));
				clearColumns();
				addColumns();
				setShowColumns(pinned != null, numberOfSchedules);

				viewer.getLabelProvider().dispose();
				viewer.setLabelProvider(new ViewLabelProvider());

				ViewerHelper.setInput(viewer, true, rowElements);

				if (!rowElements.isEmpty()) {
					applyExpansionOnNewElements(expanded, rowElements);
					if (packColumnsAction != null) {
						packColumnsAction.run();
					}
				}
			};
			ViewerHelper.runIfViewerValid(viewer, block, r);
		}

		@Override
		public void selectedObjectChanged(org.eclipse.e4.ui.model.application.ui.basic.MPart source, org.eclipse.jface.viewers.ISelection selection) {
			doSelectionChanged(source, selection);
		}

	};

	/**
	 * Allows to use array of previously expanded elements to expand the newly
	 * generated row elements
	 * 
	 * @param expanded
	 * @param rowElements
	 */
	protected void applyExpansionOnNewElements(final Object[] expanded, final List<?> rowElements) {

	}

	public class ViewLabelProvider extends CellLabelProvider implements ITableLabelProvider, ITableFontProvider, ITableColorProvider {

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(final Object obj, final int index) {
			if (index >= columnManagers.size()) {
				return "";
			}
			return columnManagers.get(index).getColumnText((T) obj);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Image getColumnImage(final Object obj, final int index) {
			return columnManagers.get(index).getColumnImage((T) obj);
		}

		@Override
		public void update(final ViewerCell cell) {

		}

		@Override
		public Font getFont(final Object obj, final int index) {
			return columnManagers.get(index).getFont((T) obj);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Color getBackground(final Object obj, final int index) {
			return columnManagers.get(index).getBackground((T) obj);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Color getForeground(final Object obj, final int index) {
			return columnManagers.get(index).getForeground((T) obj);
		}
	}

	/**
	 * The constructor.
	 * 
	 * @param helpContextId
	 */
	protected SimpleTabularReportView(final String helpContextId) {
		this.helpContextId = helpContextId;
	}

	private void addSortSelectionListener(final GridColumn column, final ColumnManager<T> value) {
		column.addSelectionListener(new SelectionAdapter() {
			{
				final SelectionAdapter self = this;
				column.addDisposeListener(e -> column.removeSelectionListener(self));
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				setSortColumn(column, value);
			}
		});
		if (!sortColumns.contains(value)) {
			sortColumns.add(value);
		}
	}

	protected void setSortColumn(final GridColumn column, final ColumnManager<T> value) {
		if (sortColumns.get(0) == value) {
			inverseSort = !inverseSort;
		} else {
			inverseSort = false;
			sortColumns.remove((Object) value);
			sortColumns.add(0, value);
		}

		ViewerHelper.refresh(viewer, true);
	}

	final List<GridViewerColumn> viewerColumns = new ArrayList<>();

	protected ReentrantSelectionManager selectionManager;

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		pinImage = Activator.getDefault().getImageRegistry().get(Activator.Implementation.IMAGE_PINNED_ROW);

		selectedScenariosService = getSite().getService(ScenarioComparisonService.class);

		viewer = new GridTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.getGrid().setRowHeaderRenderer(new RowHeaderRenderer());
		viewer.getGrid().setTopLeftRenderer(new TopLeftRenderer());
		viewer.getGrid().setEmptyColumnHeaderRenderer(new EmptyColumnHeaderRenderer());

		viewer.setContentProvider(createContentProvider());
		viewer.setInput(getViewSite());

		// get a list of column managers from the content / column provider
		// List<ColumnManager<T>> columnManagers = contentProvider.getColumnManagers();

		/*
		 * // add corresponding columns to the report for (ColumnManager<T> cv:
		 * columnManagers) { String name = cv.getName(); GridViewerColumn gvc = new
		 * GridViewerColumn(viewer, SWT.NONE); GridColumn gc = gvc.getColumn();
		 * gc.setText(name); gc.pack(); addSortSelectionListener(gc, cv); }
		 */

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);

		viewer.setComparator(new ViewerComparator() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				final int d = inverseSort ? -1 : 1;
				for (final ColumnManager<T> cm : sortColumns) {
					final int sort = cm.compare((T) e1, (T) e2);
					if (sort != 0) {
						return d * sort;
					}
				}

				return 0;
			}
		});

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), helpContextId);
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		selectionManager = new ReentrantSelectionManager(viewer, selectedScenariosServiceListener, selectedScenariosService, false);
		try {
			selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
		} catch (Exception e) {
			// Ignore any initial issues.
		}
	}

	/**
	 * Must be implemented by descendant classes to produce a
	 * SimpleContentAndColumnProvider object, which will provide column manager
	 * information (a list of columns with: names, sort comparators, and methods to
	 * get display text from a row object)
	 * 
	 * @return
	 */
	protected abstract SimpleTabularReportContentProvider createContentProvider();

	protected abstract AbstractSimpleTabularReportTransformer<T> createTransformer();

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(manager -> fillContextMenu(manager));
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
		// manager.add(new Separator());
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

	protected void makeActions() {
		packColumnsAction = new PackGridTreeColumnsAction(viewer);
		copyTableAction = new CopyGridToHtmlClipboardAction(viewer.getGrid(), false, () -> setCopyPasteMode(true), () -> setCopyPasteMode(false));
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
		getViewSite().getActionBars().updateActionBars();
	}

	protected void setCopyPasteMode(boolean copyPasteMode) {
		this.pinnedMode = copyPasteMode;
		ViewerHelper.refresh(viewer, true);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	private void setShowColumns(final boolean showDeltaColumn, final int numberOfSchedules) {

	}

	private void addColumns() {
		for (final ColumnManager<T> cv : columnManagers) {
			final String name = cv.getName();
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setHeaderTooltip(cv.getTooltip());
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			viewerColumns.add(gvc);
			final GridColumn gc = gvc.getColumn();
			gc.setText(name);
			gc.pack();
			addSortSelectionListener(gc, cv);
			if (cv.isTree()) {
				// Enable the tree controls on this column
				gvc.getColumn().setTree(true);
			}
		}
	}

	private void clearColumns() {
		for (final GridViewerColumn gvc : viewerColumns) {
			gvc.getColumn().dispose();
		}
		viewerColumns.clear();
	}

	protected void refresh() {
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	public void doSelectionChanged(MPart part, Object selectionObject) {
		// For subclasses to override if they need to respond to selection changes

	}
}