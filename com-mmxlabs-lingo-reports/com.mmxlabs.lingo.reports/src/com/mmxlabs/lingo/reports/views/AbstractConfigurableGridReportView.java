/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.common.Equality;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnBlockManager;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.GridTableViewerColumnFactory;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.preferences.PreferenceConstants;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnInfoProvider;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnUpdater;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * A customisable report for schedule based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available
 * for the user to change the default settings.
 */
public abstract class AbstractConfigurableGridReportView extends ViewPart implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	// Memto keys
	protected static final String CONFIGURABLE_COLUMNS_ORDER = "CONFIGURABLE_COLUMNS_ORDER";
	protected static final String CONFIGURABLE_ROWS_ORDER = "CONFIGURABLE_ROWS_ORDER";
	protected static final String CONFIGURABLE_COLUMNS_REPORT_KEY = "CONFIGURABLE_COLUMNS_REPORT_KEY";
	private IMemento memento;

	private boolean customisableReport = true;

	protected Map<Object, ScenarioResult> elementToInstanceMap;
	protected Map<Object, LNGScenarioModel> elementToModelMap;

	public AbstractConfigurableGridReportView(final String helpContextID) {
		this.helpContextId = helpContextID;
	}

	protected String getColumnSettingsMementoKey() {
		return CONFIGURABLE_COLUMNS_REPORT_KEY;
	}

	protected final EObjectTableViewerSortingSupport sortingSupport = new EObjectTableViewerSortingSupport();

	private FilterField filterField;
	protected EObjectTableViewerFilterSupport filterSupport;
	private IPropertyChangeListener propertyChangeListener;

	private void setColumnsImmovable() {
		if (viewer != null) {
			for (final GridColumn column : viewer.getGrid().getColumns()) {
				column.setMoveable(false);
			}
		}
	}

	@Override
	public void init(final IViewSite site, IMemento memento) throws PartInitException {
		if (memento == null) {
			memento = XMLMemento.createWriteRoot("workbench");
		}
		this.memento = memento;

		super.init(site, memento);
	}

	@Override
	public void saveState(final IMemento memento) {
		super.saveState(memento);
		final IMemento configMemento = memento.createChild(getColumnSettingsMementoKey());
		getBlockManager().saveToMemento(CONFIGURABLE_COLUMNS_ORDER, configMemento);
		saveConfigState(configMemento);
	}

	protected void saveConfigState(final IMemento configMemento) {

	}

	@Override
	public final void createPartControl(final Composite parent) {
		initPartControl(parent);

		propertyChangeListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent event) {
				final String property = event.getProperty();
				if (PreferenceConstants.P_REPORT_DURATION_FORMAT.equals(property)) {
					ViewerHelper.refresh(viewer, false);
				}
			}
		};
		Activator.getPlugin().getPreferenceStore().addPropertyChangeListener(propertyChangeListener);
	}

	public void initPartControl(final Composite parent) {

		// Find the column definitions
		registerReportColumns();

		// Check ext point to see if we can enable the customise action (created within createPartControl)
		customisableReport = checkCustomisable();
		{
			final Composite container = new Composite(parent, SWT.NONE);
			// filterField = new FilterField(container);
			final GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = layout.marginWidth = 0;
			container.setLayout(layout);
			filterField = new FilterField(container);

			viewer = new GridTableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION) {

				@Override
				protected List<?> getSelectionFromWidget() {

					final List<?> list = super.getSelectionFromWidget();

					return adaptSelectionFromWidget(list);
				}
				//
				// @Override
				// protected Object[] getSortedChildren(final Object parent) {
				// // This is the filtered and sorted children.
				// // This may be smaller than the original set.
				// sortData.sortedChildren = super.getSortedChildren(parent);
				//
				// sortData.sortedIndices = new int[sortData.sortedChildren == null ? 0 : sortData.sortedChildren.length];
				// sortData.reverseSortedIndices = new int[sortData.sortedChildren == null ? 0 : sortData.sortedChildren.length];
				//
				// Arrays.fill(sortData.sortedIndices, -1);
				// Arrays.fill(sortData.reverseSortedIndices, -1);
				//
				// for (int i = 0; i < sortData.sortedChildren.length; ++i) {
				// final int rawIndex = table.getRows().indexOf(sortData.sortedChildren[i]);
				// sortData.sortedIndices[rawIndex] = i;
				// sortData.reverseSortedIndices[i] = rawIndex;
				// }
				// return sortData.sortedChildren;
				// }
			};
			GridViewerHelper.configureLookAndFeel(viewer);

			ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);

			viewer.setComparator(sortingSupport.createViewerComparer());

			this.filterSupport = new EObjectTableViewerFilterSupport(viewer, viewer.getGrid());
			viewer.addFilter(filterSupport.createViewerFilter());
			filterField.setFilterSupport(filterSupport);

			getBlockManager().setGrid(viewer.getGrid());
			getBlockManager().setColumnFactory(new GridTableViewerColumnFactory(viewer, sortingSupport, filterSupport));
			// filterField.setFilterSupport(viewer.getFilterSupport());

			viewer.getGrid().setLayoutData(new GridData(GridData.FILL_BOTH));

			if (handleSelections()) {
				viewer.setComparer(new IElementComparer() {
					@Override
					public int hashCode(final Object element) {
						return element.hashCode();
					}

					@Override
					public boolean equals(Object a, Object b) {

						if (!contents.contains(a) && equivalents.containsKey(a)) {
							a = equivalents.get(a);
						}
						if (!contents.contains(b) && equivalents.containsKey(b)) {
							b = equivalents.get(b);
						}
						return Equality.isEqual(a, b);
					}
				});
			}

			viewer.getGrid().setHeaderVisible(true);
			viewer.getGrid().setLinesVisible(true);

			// table = ScheduleReportFactory.eINSTANCE.createTable();
			table = ScheduleReportFactory.eINSTANCE.createTable();
			// Create default options
			table.setOptions(ScheduleReportFactory.eINSTANCE.createDiffOptions());

			table.eAdapters().add(new EContentAdapter() {
				@Override
				public void notifyChanged(final Notification notification) {
					super.notifyChanged(notification);
					if (notification.getFeature() == ScheduleReportPackage.Literals.DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS) {
						viewer.refresh();
					}
					if (notification.getFeature() == ScheduleReportPackage.Literals.TABLE__SELECTED_ELEMENTS) {
						if (table.getOptions().isFilterSelectedElements()) {
							viewer.refresh();
						}
					}
				}
			});

			for (final ColumnHandler handler : getBlockManager().getHandlersInOrder()) {
				final GridColumn column = handler.createColumn().getColumn();
				if (sortingSupport != null) {
					sortingSupport.addSortableColumn(viewer, handler.column, column);
				}
				column.setVisible(handler.block.getVisible());
				column.pack();
			}

			if (helpContextId != null) {
				PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), helpContextId);
			}

			makeActions();
			hookContextMenu();
			contributeToActionBars();

			getSite().setSelectionProvider(viewer);
			if (handleSelections()) {
				// Get e4 selection service!
				final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
				service.addPostSelectionListener(this);
			}
		}

		// Look at the extension points for the initial visibilities, rows and diff options
		setInitialState();

		// force the columns to be immovable except by using the config dialog
		setColumnsImmovable();

		// Restore state from memento if possible.
		if (memento != null) {
			final IMemento configMemento = memento.getChild(getColumnSettingsMementoKey());

			if (configMemento != null) {
				getBlockManager().initFromMemento(CONFIGURABLE_COLUMNS_ORDER, configMemento);
				initConfigMemento(configMemento);
			}
		}

	}

	protected abstract void registerReportColumns();

	protected void initConfigMemento(final IMemento configMemento) {

	}

	/**
	 * Check the view extension point to see if we can enable the customise dialog
	 * 
	 * @return
	 */
	protected boolean checkCustomisable() {

		return true;
	}

	/**
	 * Examine the view extension point to determine the default set of columns, order,row types and diff options.
	 */
	protected abstract void setInitialState();

	/**
	 * Fills the top-right pulldown menu, adding an option to configure the columns visible in this view.
	 */
	protected void fillLocalPullDown(final IMenuManager manager) {
		{
			manager.add(new GroupMarker("additions"));
		}
		final IWorkbench wb = PlatformUI.getWorkbench();
		final IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

		// Only create action if permitted.
		if (customisableReport) {
			final Action configureColumnsAction = new Action("Configure Contents") {
				@Override
				public void run() {
					final IColumnInfoProvider infoProvider = new ColumnConfigurationDialog.ColumnInfoAdapter() {

						@Override
						public int getColumnIndex(final Object columnObj) {
							return getBlockManager().getBlockIndex((ColumnBlock) columnObj);
						}

						@Override
						public boolean isColumnVisible(final Object columnObj) {
							return getBlockManager().getBlockVisible((ColumnBlock) columnObj);
						}

					};

					final IColumnUpdater updater = new ColumnConfigurationDialog.ColumnUpdaterAdapter() {

						@Override
						public void setColumnVisible(final Object columnObj, final boolean visible) {

							((ColumnBlock) columnObj).setUserVisible(visible);
							viewer.refresh();

						}

						@Override
						public void swapColumnPositions(final Object columnObj1, final Object columnObj2) {
							getBlockManager().swapBlockOrder((ColumnBlock) columnObj1, (ColumnBlock) columnObj2);
							viewer.refresh();
						}

						@Override
						public Object[] resetColumnStates() {
							// Hide everything
							for (final String blockId : getBlockManager().getBlockIDOrder()) {
								getBlockManager().getBlockByID(blockId).setUserVisible(false);
							}
							// Apply the initial state
							setInitialState();
							// Return!
							return getBlockManager().getBlocksInVisibleOrder().toArray();
						}

					};

					final Image nonVisibleIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/read_obj_disabled.gif").createImage();
					final Image visibleIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/read_obj.gif").createImage();

					final ColumnConfigurationDialog dialog = new ColumnConfigurationDialog(win.getShell()) {

						@Override
						protected IColumnInfoProvider getColumnInfoProvider() {
							return infoProvider;
						}

						@Override
						protected ColumnLabelProvider getLabelProvider() {
							return new ColumnLabelProvider() {
								@Override
								public String getText(final Object element) {
									final ColumnBlock block = (ColumnBlock) element;
									return block.blockName;
								}

								@Override
								public Image getImage(final Object element) {
									final ColumnBlock block = (ColumnBlock) element;
									if (block.isModeVisible()) {
										return visibleIcon;
									} else {
										return nonVisibleIcon;
									}
								}

								@Override
								public String getToolTipText(final Object element) {
									final ColumnBlock block = (ColumnBlock) element;
									return block.tooltip;
								}
							};
						}

						@Override
						protected IColumnUpdater getColumnUpdater() {
							return updater;
						}
					};
					dialog.setColumnsObjs(getBlockManager().getBlocksInVisibleOrder().toArray());

					// See if this report has any additional check items to add
					addDialogCheckBoxes(dialog);
					dialog.open();

					// Dialog has been closed, check the output
					postDialogOpen(dialog);
					nonVisibleIcon.dispose();
					visibleIcon.dispose();

					viewer.refresh();
				}

			};
			manager.appendToGroup("additions", configureColumnsAction);
		}

	}

	protected void addDialogCheckBoxes(final ColumnConfigurationDialog dialog) {

	}

	protected void postDialogOpen(final ColumnConfigurationDialog dialog) {

	}

	private final ColumnBlockManager blockManager = new ColumnBlockManager();

	/**
	 */
	protected GridTableViewer viewer;

	private Action packColumnsAction;
	private Action copyTableAction;

	private final String helpContextId;

	/**
	 * Finds the view index of the specified column handler, i.e. the index from left to right of the column in the grid's display.
	 * 
	 * @param handler
	 * @return
	 */
	public int getColumnGridIndex(final ColumnHandler handler) {
		final Grid grid = viewer.getGrid();
		final int[] columnOrder = grid.getColumnOrder();
		final int gridIndex = grid.indexOf(handler.column.getColumn());
		for (int i = 0; i < columnOrder.length; i++) {
			if (columnOrder[i] == gridIndex) {
				return i;
			}
		}

		return -1;
	}

	public void swapVisibleColumnOrder(final ColumnHandler a, final ColumnHandler b) {
		final int[] columnOrder = viewer.getGrid().getColumnOrder();
		final int aViewIndex = getColumnGridIndex(a);
		final int bViewIndex = getColumnGridIndex(b);
		final int swap = columnOrder[aViewIndex];
		columnOrder[aViewIndex] = columnOrder[bViewIndex];
		columnOrder[bViewIndex] = swap;
		viewer.getGrid().setColumnOrder(columnOrder);
	}

	/**
	 * Sets the column associated with handler <handler> to have a view index of viewIndex. N.B.: this is relative to other columns with the same visibility state; for instance, setting the view index
	 * to 2 (3rd element) on a visible column will
	 * 
	 * @param handler
	 * @param viewIndex
	 */
	public void setColumnViewIndex(final ColumnHandler handler, final int viewIndex) {
		final GridColumn hColumn = handler.column.getColumn();

		final int[] columnOrder = viewer.getGrid().getColumnOrder();
		final List<ColumnHandler> matchingColumns = new ArrayList<>();

		for (int i = 0; i < columnOrder.length; i++) {
			final GridColumn column = viewer.getGrid().getColumn(columnOrder[i]);
			if (column.getVisible() == hColumn.getVisible()) {
				matchingColumns.add(getBlockManager().findHandler(column));
			}
		}

		swapVisibleColumnOrder(handler, matchingColumns.get(viewIndex));
	}

	public int getColumnViewIndex(final ColumnHandler handler) {
		final Grid grid = viewer.getGrid();
		final GridColumn hColumn = handler.column.getColumn();

		int result = 0;
		final int[] columnOrder = grid.getColumnOrder();
		for (int i = 0; i < columnOrder.length; i++) {
			final int index = columnOrder[i];
			final GridColumn column = grid.getColumn(index);
			if (column == hColumn) {
				return result;
			} else if (column.getVisible() == hColumn.getVisible()) {
				result += 1;
			}
		}

		return -1;
	}

	public void swapColumnViewIndex(final ColumnHandler handler, final int viewIndex) {
		final int[] columnOrder = viewer.getGrid().getColumnOrder();
		final int oldViewIndex = getColumnGridIndex(handler);
		final int swappedGridIndex = columnOrder[viewIndex];
		columnOrder[viewIndex] = columnOrder[oldViewIndex];
		columnOrder[oldViewIndex] = swappedGridIndex;

		viewer.getGrid().setColumnOrder(columnOrder);
	}

	private final HashMap<Object, Object> equivalents = new HashMap<Object, Object>();
	private final HashSet<Object> contents = new HashSet<Object>();
	protected Table table;

	public void setInputEquivalents(final Object input, final Collection<? extends Object> objectEquivalents) {
		for (final Object o : objectEquivalents) {
			equivalents.put(o, input);
		}
		contents.add(input);
	}

	protected void clearInputEquivalents() {
		equivalents.clear();
		contents.clear();
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				AbstractConfigurableGridReportView.this.fillContextMenu(manager);
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

	private void fillContextMenu(final IMenuManager manager) {
		// Other plug-ins can contribute their actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	protected void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(new GroupMarker("filter"));
		// BE manager.add(new GroupMarker("sortmode")); //BE
		manager.add(new GroupMarker("pack"));
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));

		manager.appendToGroup("filter", filterField.getContribution());
		// BE manager.appendToGroup("sortmode", sortModeAction); //BE
		manager.appendToGroup("pack", packColumnsAction);
		manager.appendToGroup("copy", copyTableAction);
	}

	private void makeActions() {
		packColumnsAction = PackActionFactory.createPackColumnsAction(viewer);
		copyTableAction = new CopyGridToHtmlClipboardAction(viewer.getGrid(), false);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	//

	@Override
	public void selectionChanged(final MPart part, final Object selectionObject) {

		final IWorkbenchPart e3Part = SelectionHelper.getE3Part(part);
		if (e3Part != null) {
			if (e3Part == this) {
				return;
			}
			if (e3Part instanceof PropertySheet) {
				return;
			}
		}
		final ISelection selection = SelectionHelper.adaptSelection(selectionObject);
		viewer.setSelection(selection, true);
		ViewerHelper.refresh(viewer, true);
	}

	protected boolean handleSelections() {
		return false;
	}

	@Override
	public void dispose() {

		Activator.getPlugin().getPreferenceStore().removePropertyChangeListener(propertyChangeListener);

		if (handleSelections()) {
			final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
			service.removePostSelectionListener(this);
		}
		sortingSupport.clearColumnSortOrder();
		clearInputEquivalents();
		super.dispose();
	}

	/**
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		// if (SortData.class.isAssignableFrom(adapter)) {
		// return sortData;
		// }
		// if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
		// final PropertySheetPage propertySheetPage = new PropertySheetPage();
		//
		// propertySheetPage.setPropertySourceProvider(new ScheduledEventPropertySourceProvider());
		// return propertySheetPage;
		// }
		return super.getAdapter(adapter);
	}

	/**
	 * 
	 * Callback to convert the raw data coming out of the table into something usable externally. This is useful when the table data model is custom for the table rather from the real data model.
	 * 
	 */
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {
		return selection;
	}

	public ColumnBlockManager getBlockManager() {
		return blockManager;
	}

	public void mapInputs(final Map<Object, ScenarioResult> elementToInstanceMap, final Map<Object, LNGScenarioModel> elementToModelMap) {
		this.elementToInstanceMap = elementToInstanceMap;
		this.elementToModelMap = elementToModelMap;
	}

	public ScenarioResult getScenarioInstance(final Object key) {
		if (elementToInstanceMap != null) {
			return elementToInstanceMap.get(key);
		}
		return null;
	}

	public LNGScenarioModel getScenarioModel(final Object key) {
		if (elementToModelMap != null) {
			return elementToModelMap.get(key);
		}
		return null;
	}

}
