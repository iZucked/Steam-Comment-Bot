/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.mmxlabs.common.Equality;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.properties.ScheduledEventPropertySourceProvider;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.utils.PinDiffModeColumnManager;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Base class for views which show things from the EMF output model.
 * 
 * This shares a lot of function with EObjectTableViewer
 * 
 * @author hinton
 */
public abstract class EMFReportView extends ViewPart implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	private FilterField filterField;

	private boolean currentlyPinned = false;
	private int numberOfSchedules;
	protected PinDiffModeColumnManager pinDiffModeHelper = new PinDiffModeColumnManager(this);

	private final Map<String, List<EObject>> allObjectsByKey = new LinkedHashMap<>();
	private final Map<String, Integer> keyPresentInSchedulesCount = new LinkedHashMap<>();
	private final Map<Object, WeakReference<ScenarioResult>> elementMapping = new WeakHashMap<>();

	private final ColumnBlockManager blockManager = new ColumnBlockManager();

	private SelectedScenariosService selectedScenariosService;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			final Runnable r = new Runnable() {
				@Override
				public void run() {
					// Add Difference/Change columns when in Pin/Diff mode
					final boolean pinDiffMode = !others.isEmpty() && pinned != null;
					final int numberOfSchedules = others.size() + (pinned == null ? 0 : 1);
					for (final ColumnBlock handler : getBlockManager().getBlocksInVisibleOrder()) {
						if (handler != null) {
							handler.setViewState(numberOfSchedules > 1, pinDiffMode);
						}
					}

					final List<Object> rowElements = new LinkedList<>();
					final IScenarioInstanceElementCollector elementCollector = getElementCollector();
					elementCollector.beginCollecting(pinned != null);
					if (pinned != null) {
						final Collection<? extends Object> elements = elementCollector.collectElements(pinned, true);
						for (final Object e : elements) {
							elementMapping.put(e, new WeakReference<>(pinned));
						}
						rowElements.addAll(elements);
					}
					for (final ScenarioResult other : others) {
						final Collection<? extends Object> elements = elementCollector.collectElements(other, false);
						for (final Object e : elements) {
							elementMapping.put(e, new WeakReference<>(other));
						}
						rowElements.addAll(elements);
					}
					elementCollector.endCollecting();
					ViewerHelper.setInput(viewer, true, rowElements);
				}
			};

			RunnerHelper.exec(r, block);
		}
	};

	protected EMFReportView(final String helpContextId) {
		this.helpContextId = helpContextId;
	}

	protected final ICellRenderer containingScheduleFormatter = new BaseFormatter() {
		@Override
		public String render(final Object object) {
			if (object instanceof EObject) {
				final EObject eObject = (EObject) object;
				final ISelectedDataProvider selectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
				if (selectedDataProvider != null) {
					ScenarioResult instance = selectedDataProvider.getScenarioResult(eObject);
					if (instance != null) {
						return instance.getScenarioInstance().getName();
					}
					if (elementMapping.containsKey(eObject)) {
						final WeakReference<ScenarioResult> ref = elementMapping.get(eObject);
						if (ref != null) {
							instance = ref.get();
						}
						if (instance != null) {
							return instance.getScenarioInstance().getName();
						}
					}
				}
			}
			return null;
		}
	};

	/**
	 */
	protected EObjectTableViewer viewer;

	private Action packColumnsAction;
	private Action copyTableAction;
	private final String helpContextId;

	/**
	 */
	protected ITreeContentProvider getContentProvider() {
		return new ITreeContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {
			}

			@Override
			public Object[] getElements(final Object inputElement) {

				clearInputEquivalents();

				final Object[] result;

				if (numberOfSchedules > 1 && currentlyPinned) {
					final List<EObject> objects = new LinkedList<EObject>();
					for (final Map.Entry<String, List<EObject>> e : allObjectsByKey.entrySet()) {
						EObject ref = null;
						final LinkedHashSet<EObject> objectsToAdd = new LinkedHashSet<EObject>();

						// Find ref...
						for (final EObject ca : e.getValue()) {
							if (pinDiffModeHelper.pinnedObjectsContains(ca)) {
								ref = ca;
								break;
							}
						}

						if (ref == null) {
							// No ref found, so add all
							objectsToAdd.addAll(e.getValue());
						} else {

							int keyPresentCount = 0;
							if (keyPresentInSchedulesCount.containsKey(e.getKey())) {
								keyPresentCount = keyPresentInSchedulesCount.get(e.getKey()).intValue();
							}

							for (final EObject ca : e.getValue()) {
								if (keyPresentCount != numberOfSchedules) {
									// Different number of elements, so add all!
									// This means something has been removed/added
									objectsToAdd.addAll(e.getValue());
								}
								if (ca == ref) {
									continue;
								}
								if (isElementDifferent(ref, ca)) {
									// There is a data difference, so add
									objectsToAdd.addAll(e.getValue());
								}
							}
						}
						if (!pinDiffModeHelper.showPinnedData()) {
							objectsToAdd.remove(ref);
						}
						objects.addAll(objectsToAdd);

					}
					result = objects.toArray();

				} else {
					if (inputElement instanceof Collection<?>) {
						final Collection<?> collection = (Collection<?>) inputElement;
						return collection.toArray();
					} else {
						result = new Object[0];
					}
				}

				processInputs(result);

				return result;
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				return null;
			}

			@Override
			public Object getParent(final Object element) {
				return null;
			}

			@Override
			public boolean hasChildren(final Object element) {
				return false;
			}

		};
	}

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

	@Override
	public void createPartControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		filterField = new FilterField(container);
		final GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = 0;
		container.setLayout(layout);

		viewer = new EObjectTableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION) {
			// @Override
			// protected void inputChanged(final Object input, final Object oldInput) {
			// super.inputChanged(input, oldInput);
			//
			// final boolean inputEmpty = (input == null) || ((input instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) input).getCollectedElements().isEmpty());
			// final boolean oldInputEmpty = (oldInput == null)
			// || ((oldInput instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) oldInput).getCollectedElements().isEmpty());
			//
			// if (inputEmpty != oldInputEmpty) {
			// // Disabled because running this takes up 50% of the runtime when displaying a new schedule (!)
			// // if (packColumnsAction != null) {
			// // packColumnsAction.run();
			// // }
			// }
			// };

			@Override
			protected List<?> getSelectionFromWidget() {

				final List<?> list = super.getSelectionFromWidget();

				return adaptSelectionFromWidget(list);
			}

			/**
			 * Modify @link {AbstractTreeViewer#getTreePathFromItem(Item)} to adapt items before returning selection object.
			 */
			@Override
			protected TreePath getTreePathFromItem(Item item) {
				final LinkedList<Object> segments = new LinkedList<>();
				while (item != null) {
					final Object segment = item.getData();
					Assert.isNotNull(segment);
					segments.addFirst(segment);
					item = getParentItem(item);
				}
				final List<?> l = adaptSelectionFromWidget(segments);

				return new TreePath(l.toArray());
			}
		};

		getBlockManager().setColumnFactory(new EObjectTableViewerColumnFactory(viewer));
		getBlockManager().setGrid(viewer.getGrid());

		filterField.setFilterSupport(viewer.getFilterSupport());

		viewer.getGrid().setLayoutData(new GridData(GridData.FILL_BOTH));

		// this is very slow on refresh
		viewer.setDisplayValidationErrors(false);

		// BE
		// viewer.getSortingSupport().setCategoryFunction(this.rowCategoryFunction);
		// BE

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

		for (final ColumnHandler handler : getBlockManager().getHandlersInOrder()) {
			final GridColumn column = handler.createColumn().getColumn();
			column.setVisible(handler.block.getVisible());
			column.pack();
		}

		if (helpContextId != null) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), helpContextId);
		}

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		viewer.init(getContentProvider(), null);

		getSite().setSelectionProvider(viewer);
		if (handleSelections()) {

			final ESelectionService service = getSite().getService(ESelectionService.class);
			service.addPostSelectionListener(this);
		}

		selectedScenariosService = (SelectedScenariosService) getSite().getService(SelectedScenariosService.class);
		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	protected abstract IScenarioInstanceElementCollector getElementCollector();

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				EMFReportView.this.fillContextMenu(manager);
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

	protected void fillLocalPullDown(final IMenuManager manager) {
		manager.add(new GroupMarker("additions"));
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
		copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);

		// BE
		// sortModeAction = new Action("S", IAction.AS_CHECK_BOX) {
		// @Override
		// public void run() {
		// log.error("hello");
		// viewer.setInput(viewer.getInput());
		// viewer.refresh();
		// }
		// };
		// BE
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	@Override
	public void selectionChanged(final MPart part, final Object selectedObject) {
		{
			final IWorkbenchPart view = SelectionHelper.getE3Part(part);

			if (view == this) {
				return;
			}
			if (view instanceof PropertySheet) {
				return;
			}
		}

		// Convert selection
		final ISelection selection = SelectionHelper.adaptSelection(selectedObject);
		ViewerHelper.setSelection(viewer, true, selection, true);
	}

	protected boolean handleSelections() {
		return false;
	}

	@Override
	public void dispose() {

		final ESelectionService service = getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(this);

		selectedScenariosService.removeListener(selectedScenariosServiceListener);

		super.dispose();
	}

	/**
	 */
	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
			final PropertySheetPage propertySheetPage = new PropertySheetPage();

			propertySheetPage.setPropertySourceProvider(new ScheduledEventPropertySourceProvider());
			return (T) propertySheetPage;
		}
		return super.getAdapter(adapter);
	}

	/**
	 * Call from {@link IScenarioInstanceElementCollector#beginCollecting()} to reset pin mode data
	 * 
	 */
	public void clearPinModeData() {
		clearInputEquivalents();
		currentlyPinned = false;
		allObjectsByKey.clear();
		keyPresentInSchedulesCount.clear();
		numberOfSchedules = 0;
		pinDiffModeHelper.reset();
	}

	/**
	 * Call from {@link IScenarioInstanceElementCollector#collectElements(com.mmxlabs.models.mmxcore.MMXRootObject, boolean)} and pass in the list of collected objects rather than raw schedule.
	 * 
	 * @param objects
	 * @param isPinned
	 */
	public void collectPinModeElements(final Collection<? extends EObject> objects, final boolean isPinned) {
		currentlyPinned |= isPinned;
		++numberOfSchedules;

		final Set<String> elementKeys = new HashSet<>();
		for (final EObject ca : objects) {
			final List<EObject> l;
			final String key = getElementKey(ca);
			elementKeys.add(key);
			if (allObjectsByKey.containsKey(key)) {
				l = allObjectsByKey.get(key);
			} else {
				l = new LinkedList<EObject>();
				allObjectsByKey.put(key, l);
			}
			l.add(ca);

			if (isPinned) {
				pinDiffModeHelper.setPinnedObject(key, ca);
			} else {
				pinDiffModeHelper.addUnpinnedObject(key, ca);
			}
		}

		for (final String key : elementKeys) {
			int count = 0;
			if (keyPresentInSchedulesCount.containsKey(key)) {
				count = keyPresentInSchedulesCount.get(key).intValue();
			}
			keyPresentInSchedulesCount.put(key, ++count);
		}
	}

	/**
	 * Compare an element to the pinned object and return true if different.
	 * 
	 */
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return true;
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	public String getElementKey(final EObject element) {
		if (element instanceof NamedObject) {
			return ((NamedObject) element).getName();
		}
		return element.toString();
	}

	/**
	 * Process the array of elements before they are returned to e.g. map input equivalents
	 * 
	 * @param result
	 */
	protected void processInputs(final Object[] result) {

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
}
