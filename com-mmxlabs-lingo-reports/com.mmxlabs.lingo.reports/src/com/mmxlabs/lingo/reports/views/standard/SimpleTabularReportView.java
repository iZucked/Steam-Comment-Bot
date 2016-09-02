/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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

import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer.ColumnManager;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 */
public abstract class SimpleTabularReportView<T> extends ViewPart {

	private SelectedScenariosService selectedScenariosService;

	private final ArrayList<ColumnManager<T>> sortColumns = new ArrayList<ColumnManager<T>>();
	private final ArrayList<ColumnManager<T>> columnManagers = new ArrayList<ColumnManager<T>>();

	private boolean inverseSort = false;

	protected GridTreeViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private String helpContextId;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioInstance pinned, final Collection<ScenarioInstance> others, final boolean block) {

			final Runnable r = new Runnable() {
				@Override
				public void run() {
					AbstractSimpleTabularReportTransformer<T> transformer = createTransformer();

					columnManagers.clear();

					// pinnedData.clear();
					// rowData = (T[]) new Object[0];
					// if (newInput instanceof IScenarioViewerSynchronizerOutput) {
					// final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
					// for (final Object o : synchOutput.getCollectedElements()) {
					// if (o instanceof Schedule) {
					// rowData = createData((Schedule) o, synchOutput.getLNGScenarioModel(o), synchOutput.getLNGPortfolioModel(o)).toArray(rowData);
					// return;
					// }
					// }
					// }
					//
					final List<Object> rowElements = new LinkedList<>();
					int numberOfSchedules = 0;
					List<T> pinnedData = null;
					if (pinned != null) {
						LNGScenarioModel pinnedScenarioModel = selectedDataProvider.getScenarioModel(pinned);
						if (pinnedScenarioModel != null) {
							final Schedule schedule = ScenarioModelUtil.findSchedule(pinnedScenarioModel);
							if (schedule != null) {

								// pinnedData = createData((Schedule) o, synchOutput.getLNGScenarioModel(o), synchOutput.getLNGPortfolioModel(o)).toArray(rowData);

								pinnedData = transformer.createData(schedule, selectedDataProvider.getScenarioModel(schedule));
								rowElements.addAll(pinnedData);
								numberOfSchedules++;
							}
						}
					}
					for (final ScenarioInstance other : others) {
						LNGScenarioModel otherScenarioModel = selectedDataProvider.getScenarioModel(other);
						if (otherScenarioModel != null) {
							final Schedule schedule = ScenarioModelUtil.findSchedule(otherScenarioModel);
							if (schedule != null) {
								@Nullable
								LNGScenarioModel scenarioModel = selectedDataProvider.getScenarioModel(schedule);
								if (scenarioModel != null) {
									rowElements.addAll(transformer.createData(schedule, scenarioModel));
									numberOfSchedules++;
								}
							}
						}
					}

					columnManagers.addAll(transformer.getColumnManagers(selectedDataProvider));
					clearColumns();
					addColumns();
					setShowColumns(pinned != null, numberOfSchedules);

					viewer.getLabelProvider().dispose();
					viewer.setLabelProvider(new ViewLabelProvider());

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

	public class ViewLabelProvider extends CellLabelProvider implements ITableLabelProvider, IFontProvider, ITableColorProvider {

		public ViewLabelProvider() {
		}

		@Override
		public void dispose() {
			for (ColumnManager<T> manager : columnManagers) {
				manager.dispose();
			}

			super.dispose();
		}

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
		public Font getFont(final Object element) {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Color getBackground(final Object obj, final int index) {
			return columnManagers.get(index).getBackground((T) obj);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Color getForeground(Object obj, int index) {
			// TODO Auto-generated method stub
			return columnManagers.get(index).getForeground((T) obj);
		}
	}

	/**
	 * The constructor.
	 * 
	 * @param helpContextId
	 */
	public SimpleTabularReportView(String helpContextId) {
		this.helpContextId = helpContextId;
	}

	private void addSortSelectionListener(final GridColumn column, final ColumnManager<T> value) {
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

	final List<GridViewerColumn> viewerColumns = new ArrayList<GridViewerColumn>();

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		selectedScenariosService = (SelectedScenariosService) getSite().getService(SelectedScenariosService.class);

		viewer = new GridTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		viewer.setContentProvider(createContentProvider());
		viewer.setInput(getViewSite());

		// get a list of column managers from the content / column provider
		// List<ColumnManager<T>> columnManagers = contentProvider.getColumnManagers();

		/*
		 * // add corresponding columns to the report for (ColumnManager<T> cv: columnManagers) { String name = cv.getName(); GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE); GridColumn
		 * gc = gvc.getColumn(); gc.setText(name); gc.pack(); addSortSelectionListener(gc, cv); }
		 */

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);

		viewer.setComparator(new ViewerComparator() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				final int d = inverseSort ? -1 : 1;
				for (ColumnManager<T> cm : sortColumns) {
					int sort = cm.compare((T) e1, (T) e2);
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

		// viewerSynchronizer = ScenarioViewerSynchronizer.registerView(viewer, new ScheduleElementCollector() {
		// private boolean hasPin = false;
		// private int numberOfSchedules;
		//
		// @Override
		// public void beginCollecting(boolean pinDiffMode) {
		// hasPin = false;
		// numberOfSchedules = 0;
		// }
		//
		// @Override
		// public void endCollecting() {
		// setShowColumns(hasPin, numberOfSchedules);
		// }
		//
		// @Override
		// protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean pinned) {
		// hasPin = hasPin || pinned;
		// ++numberOfSchedules;
		// return Collections.singleton(schedule);
		// }
		// });

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	/**
	 * Must be implemented by descendant classes to produce a SimpleContentAndColumnProvider object, which will provide column manager information (a list of columns with: names, sort comparators, and
	 * methods to get display text from a row object)
	 * 
	 * @return
	 */
	abstract protected AbstractSimpleTabularReportContentProvider<T> createContentProvider();

	abstract protected AbstractSimpleTabularReportTransformer<T> createTransformer();

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				fillContextMenu(manager);
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
		packColumnsAction = new PackGridTreeColumnsAction(viewer);
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

	}

	private void addColumns() {
		for (ColumnManager<T> cv : columnManagers) {
			String name = cv.getName();
			GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			viewerColumns.add(gvc);
			GridColumn gc = gvc.getColumn();
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
		for (GridViewerColumn gvc : viewerColumns) {
			gvc.getColumn().dispose();
		}
		viewerColumns.clear();
	}
}